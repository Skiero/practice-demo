package com.hik.practicedemo.conf.orika;

import com.hik.practicedemo.model.dto.UserInfoDTO;
import com.hik.practicedemo.model.entity.UserEntity;
import com.hik.practicedemo.model.vo.UserVO;
import com.hik.practicedemo.utils.RandomUtil;
import com.hik.practicedemo.utils.SpringBeanContextUtil;
import com.hik.practicedemo.utils.StringUtil;
import ma.glasnost.orika.MapperFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * Created by wangJinChang on 2019/12/27 13:59
 * 用户属性映射配置类
 */
public class UserMapper {

    public static UserEntity constructUserEntity(UserInfoDTO userInfoDTO) {
        Assert.notNull(userInfoDTO, "the object argument can not be null");
        MapperFactory mapperFactory = SpringBeanContextUtil.getBean(MapperFactory.class);
        mapperFactory.classMap(UserEntity.class, UserInfoDTO.class)
                .field("id", "id")
                .byDefault()
                .register();
        UserEntity userEntity = mapperFactory.getMapperFacade().map(userInfoDTO, UserEntity.class);
        userEntity.setId(RandomUtil.randomLong(4));
        userEntity.setName(StringUtils.EMPTY);
        userEntity.setCreateTime(new Date());
        userEntity.setUpdateTime(new Date());
        return userEntity;
    }

    public static UserVO constructUserVO(UserEntity userEntity) {
        Assert.notNull(userEntity, "the object argument can not be null");
        MapperFactory mapperFactory = SpringBeanContextUtil.getBean(MapperFactory.class);
        mapperFactory.classMap(UserVO.class, UserEntity.class)
                .field("tel", "tel")
                .byDefault()
                .register();
        UserVO userVO = mapperFactory.getMapperFacade().map(userEntity, UserVO.class);
        userVO.setTel(StringUtil.encryptPhoneNumber(userVO.getTel()));
        userVO.setEmail(StringUtil.encryptEmail(userVO.getEmail()));
        return userVO;
    }

    public static List<UserVO> constructUserVOs(List<UserEntity> userEntities) {
        Assert.notEmpty(userEntities, "the object argument can not be empty");
        MapperFactory mapperFactory = SpringBeanContextUtil.getBean(MapperFactory.class);
        mapperFactory.classMap(UserVO.class, UserEntity.class)
                .field("tel", "tel")
                .byDefault()
                .register();
        List<UserVO> userVOS = mapperFactory.getMapperFacade().mapAsList(userEntities, UserVO.class);
        for (UserVO userVO : userVOS) {
            userVO.setTel(StringUtil.encryptPhoneNumber(userVO.getTel()));
            userVO.setEmail(StringUtil.encryptEmail(userVO.getEmail()));
        }
        return userVOS;
    }
}
