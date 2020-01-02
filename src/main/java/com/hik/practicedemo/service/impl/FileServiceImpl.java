package com.hik.practicedemo.service.impl;

import com.hik.practicedemo.model.constant.CommonConstants;
import com.hik.practicedemo.model.dto.DownloadDTO;
import com.hik.practicedemo.model.entity.FileEntity;
import com.hik.practicedemo.model.vo.FileVO;
import com.hik.practicedemo.primaryRepository.FileRepository;
import com.hik.practicedemo.service.FileService;
import com.hik.practicedemo.utils.RandomUtil;
import io.minio.MinioClient;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * Created by wangJinChang on 2019/12/27 19:58
 * 文件  --  实现类
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileRepository fileRepository;

    @Resource
    private MinioClient minioClient;

    @Override
    public Optional<FileVO> uploadFile(MultipartFile file) {
        long fileSize = file.getSize();
        String originalFilename = file.getOriginalFilename();
        Assert.notNull(originalFilename, "文件名为NULL");
        String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "." + fileType;
        String contentType = file.getContentType();
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            minioClient.putObject(CommonConstants.BUCKET_NAME, CommonConstants.FILE_PREFIX + newFileName, inputStream, fileSize, null, null, contentType);
            String url = minioClient.getObjectUrl(CommonConstants.BUCKET_NAME, CommonConstants.FILE_PREFIX + newFileName);
            FileEntity fileEntity = new FileEntity(RandomUtil.uuid(), CommonConstants.BUCKET_NAME, originalFilename, fileType, newFileName, "admin", new Date(), new Date());
            FileEntity resultEntity = fileRepository.save(fileEntity);
            return Optional.of(new FileVO(resultEntity.getId(), resultEntity.getOriginalFileName(), url));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<DownloadDTO> downloadFile(String fileId) throws Exception {
        Optional<FileEntity> optional = fileRepository.findById(fileId);
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        FileEntity fileEntity = optional.get();
        minioClient.statObject(CommonConstants.BUCKET_NAME, CommonConstants.FILE_PREFIX + fileEntity.getMinionFileName());
        InputStream inputStream = minioClient.getObject(CommonConstants.BUCKET_NAME, CommonConstants.FILE_PREFIX + fileEntity.getMinionFileName());
        DownloadDTO downloadDTO = new DownloadDTO()
                .setName(fileEntity.getOriginalFileName())
                .setType(fileEntity.getFileType())
                .setBytes(IOUtils.toByteArray(inputStream));
        return Optional.of(downloadDTO);
    }
}
