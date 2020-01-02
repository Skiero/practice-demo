package com.hik.practicedemo.service.impl;

import com.google.common.collect.Lists;
import com.hik.practicedemo.conf.orika.UserMapper;
import com.hik.practicedemo.exception.BusinessException;
import com.hik.practicedemo.exception.CommonExceptionEnum;
import com.hik.practicedemo.model.dto.UserInfoDTO;
import com.hik.practicedemo.model.entity.UserEntity;
import com.hik.practicedemo.model.param.user.UserQuery;
import com.hik.practicedemo.model.vo.PageVO;
import com.hik.practicedemo.model.vo.UserVO;
import com.hik.practicedemo.primaryRepository.PrimaryUserRepository;
import com.hik.practicedemo.service.UserService;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by wangJinChang on 2019/12/26 12:05
 * 用户  --  实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private PrimaryUserRepository primaryUserRepository;

    @Resource
    private BCryptPasswordEncoder encoder;

    @Resource
    private MapperFacade mapperFacade;

    @Override
    public UserVO save(UserInfoDTO userInfoDTO) throws BusinessException {
        Assert.notNull(userInfoDTO, "用户信息不能为空");
        UserEntity userEntity = new UserEntity();
        UserEntity resultEntity;
        if (Objects.isNull(userInfoDTO.getId())) {
            //新增
            userEntity = UserMapper.constructUserEntity(userInfoDTO);
            userEntity.setPwd(encoder.encode(userInfoDTO.getPwd()));
            resultEntity = primaryUserRepository.save(userEntity);
        } else {
            //更新
            Optional<UserEntity> optional = primaryUserRepository.findById(userInfoDTO.getId());
            if (!optional.isPresent()) {
                throw new BusinessException(CommonExceptionEnum.RESOURCE_NOT_EXIST);
            }
            UserEntity modifyEntity = optional.get();
            BeanUtils.copyProperties(userInfoDTO, userEntity);
            userEntity.setUpdateTime(new Date());
            resultEntity = primaryUserRepository.save(modifyEntity);
        }
        return UserMapper.constructUserVO(resultEntity);
    }

    @Override
    public int remove(List<Integer> id) {
        return primaryUserRepository.deleteAllById(id);
    }

    @Override
    public Optional<UserVO> findById(Integer id) {
        Optional<UserEntity> optional = primaryUserRepository.findById(id);
        if (optional.isPresent()) {
            UserVO userVO = UserMapper.constructUserVO(optional.get());
            return Optional.of(userVO);
        }
        return Optional.empty();
    }

    @Override
    public PageVO<UserVO> fuzzyQuery(List<Integer> ids, String name, Integer pageNo, Integer pageSize) {
        Assert.notEmpty(ids, "用户id集合不能为空");
        Assert.notNull(name, "用户名不能为空");
        Sort sort = new Sort(Sort.Direction.DESC, "createTime").and(new Sort(Sort.Direction.ASC, "id"));
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<UserEntity> page = primaryUserRepository.fuzzyQuery(ids, "%" + name + "%", pageable);
        List<UserVO> userVOS = Lists.newArrayList();
        if (page.hasContent()) {
            userVOS = mapperFacade.mapAsList(page.getContent(), UserVO.class);
        }
        return new PageVO<>(page.getTotalPages(), page.getTotalElements(), pageNo, pageSize, userVOS);
    }

    @Override
    public PageVO<UserVO> fuzzyQuery(UserQuery query) {
        Assert.notNull(query, "用户信息条件不能为空");
        Sort sort = new Sort(Sort.Direction.DESC, "createTime").and(new Sort(Sort.Direction.ASC, "id"));
        Pageable pageable = PageRequest.of(query.getPageNo() - 1, query.getPageSize(), sort);
        Specification<UserEntity> specification = (Specification<UserEntity>) (root, criteriaQuery, criteriaBuilder) -> {
            //所有的断言
            List<Predicate> list = Lists.newArrayList();
            //根据 enterpriseId 查询
            if (!CollectionUtils.isEmpty(query.getIds())) {
                Predicate p = criteriaBuilder.in(root.get("ids")).value(query.getIds());
                list.add(p);
            }
            //根据 registerTime 时间之后查询
            if (Objects.nonNull(query.getRegisterStartTime())) {
                Predicate p = criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), query.getRegisterStartTime());
                list.add(p);
            }
            //根据 registerTime 时间之前查询
            if (Objects.nonNull(query.getRegisterEndTime())) {
                Predicate p = criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), query.getRegisterEndTime());
                list.add(p);
            }
            //根据 name 模糊查询
            if (StringUtils.isNotEmpty(query.getName())) {
                Predicate p = criteriaBuilder.like(root.get("name").as(String.class), "%" + query.getName() + "%");
                list.add(p);
            }
            //根据 tel 精确查询
            if (StringUtils.isNotEmpty(query.getTel())) {
                Predicate p = criteriaBuilder.equal(root.get("tel").as(String.class), query.getTel());
                list.add(p);
            }
            //添加断言
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
        Page<UserEntity> page = primaryUserRepository.findAll(specification, pageable);
        List<UserVO> userVOS = Lists.newArrayList();
        if (page.hasContent()) {
            userVOS = UserMapper.constructUserVOs(page.getContent());
        }
        return new PageVO<>(page.getTotalPages(), page.getTotalElements(), query.getPageNo(), query.getPageSize(), userVOS);
    }

    @Override
    public Optional<UserVO> signIn(String tel, String pwd) {
        Optional<UserEntity> optional = primaryUserRepository.findByTel(tel);
        if (optional.isPresent()) {
            UserEntity userEntity = optional.get();
            boolean b = encoder.matches(pwd, userEntity.getPwd());
            return b ? Optional.of(UserMapper.constructUserVO(userEntity)) : Optional.empty();
        }
        return Optional.empty();
    }
}
