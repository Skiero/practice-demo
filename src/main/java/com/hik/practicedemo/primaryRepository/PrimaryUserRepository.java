package com.hik.practicedemo.primaryRepository;

import com.hik.practicedemo.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by wangJinChang on 2019/12/4 19:59
 * 用户  --  默认持久层
 */
@Repository
public interface PrimaryUserRepository extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity> {

    @Transactional
    @Modifying
    @Query("delete from UserEntity u where u.id in (:ids)")
    int deleteAllById(List<Integer> ids);

    @Query("select u from UserEntity  u where u.id in (:ids) or u.name like (:name)")
    Page<UserEntity> fuzzyQuery(List<Integer> ids, String name, Pageable pageable);

    @Query("select u from UserEntity u where u.tel = :tel")
    Optional<UserEntity> findByTel(String tel);
}
