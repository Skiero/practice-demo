package com.hik.practicedemo.primaryRepository;

import com.hik.practicedemo.model.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wangJinChang on 2019/12/27 19:56
 * 文件  --  持久层
 */
@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {

}
