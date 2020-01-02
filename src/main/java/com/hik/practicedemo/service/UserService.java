package com.hik.practicedemo.service;

import com.hik.practicedemo.exception.BusinessException;
import com.hik.practicedemo.model.dto.UserInfoDTO;
import com.hik.practicedemo.model.param.user.UserQuery;
import com.hik.practicedemo.model.vo.PageVO;
import com.hik.practicedemo.model.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by wangJinChang on 2019/12/4 15:35
 * 用户  --  接口
 */
@Service
public interface UserService {

    UserVO save(UserInfoDTO dto) throws BusinessException;

    int remove(List<Integer> id);

    Optional<UserVO> findById(Integer id);

    PageVO<UserVO> fuzzyQuery(List<Integer> ids, String name, Integer pageNo, Integer pageSize);

    PageVO<UserVO> fuzzyQuery(UserQuery query);

    Optional<UserVO> signIn(String tel, String pwd);
}
