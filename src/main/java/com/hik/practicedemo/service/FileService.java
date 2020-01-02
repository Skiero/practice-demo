package com.hik.practicedemo.service;

import com.hik.practicedemo.model.dto.DownloadDTO;
import com.hik.practicedemo.model.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Created by wangJinChang on 2019/12/27 19:57
 * 文件  --  接口
 */
public interface FileService {

    Optional<FileVO> uploadFile(MultipartFile file);

    Optional<DownloadDTO> downloadFile(String fileId) throws Exception;
}
