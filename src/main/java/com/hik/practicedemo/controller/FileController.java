package com.hik.practicedemo.controller;

import com.google.common.collect.Maps;
import com.hik.practicedemo.exception.CommonExceptionEnum;
import com.hik.practicedemo.model.dto.DownloadDTO;
import com.hik.practicedemo.model.vo.FileVO;
import com.hik.practicedemo.model.vo.ResultVO;
import com.hik.practicedemo.service.FileService;
import com.hik.practicedemo.utils.IOStreamUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Optional;

/**
 * Created by wangJinChang on 2019/12/13 14:43
 * 文件控制层
 */
@RestController
@RequestMapping("/file")
@Api(tags = "文件控制层")
@Slf4j
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件", notes = "上传文件至minion服务器")
    public ResultVO<FileVO> upload(@RequestParam("file") @NotNull(message = "上传文件不能为空") MultipartFile multipartFile) {
        Optional<FileVO> optional = fileService.uploadFile(multipartFile);
        return optional.map(ResultVO::success).orElseGet(() -> ResultVO.error(CommonExceptionEnum.OPERATION_FAILED.getCode(), CommonExceptionEnum.OPERATION_FAILED.getMsg()));
    }

    @GetMapping("/zip")
    @ApiOperation(value = "下载zip", notes = "根据文件id下载zip")
    @ApiResponse(code = 200, message = "zip下载文件")
    public void testZip(@RequestParam("filename") String filename, HttpServletResponse response) {
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8") + ".zip" + ";filename*=UTF-8''" + URLEncoder.encode(filename, "UTF-8") + ".zip");
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding("UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            Map<String, byte[]> dataMap = Maps.newHashMap();
            dataMap.put("1.txt", "我只是".getBytes());
            dataMap.put("2.txt", "生成字节码".getBytes());
            dataMap.put("3.txt", "压缩后被下载".getBytes());
            IOStreamUtil.zipOutputStream(dataMap, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/download/{id}")
    @ApiOperation(value = "下载文件", notes = "根据文件id下载文件")
    @ApiResponse(code = 200, message = "下载单个文件")
    public void download(@PathVariable("id") String id,
                         HttpServletResponse response) {
        Optional<DownloadDTO> optional = Optional.empty();
        try {
            optional = fileService.downloadFile(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!optional.isPresent()) {
            log.error("FileController.Download failed because file does not exist , file id is {}", id);
            return;
        }
        IOStreamUtil.fileOutputStream(response, optional.get());
    }
}
