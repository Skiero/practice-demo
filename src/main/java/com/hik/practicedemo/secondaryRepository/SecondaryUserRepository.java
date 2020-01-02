package com.hik.practicedemo.secondaryRepository;

import com.hik.practicedemo.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wangJinChang on 2019/12/4 16:05
 * 用户  --  备用持久层
 */
@Repository
public interface SecondaryUserRepository extends JpaRepository<UserEntity, Integer> {

}
