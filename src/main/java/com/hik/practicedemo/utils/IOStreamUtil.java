package com.hik.practicedemo.utils;

import com.hik.practicedemo.model.dto.DownloadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by wangJinChang on 2019/12/27 19:03
 * io流工具类
 */
@Slf4j
public class IOStreamUtil {

    /**
     * 使用zip的方式输出文件
     *
     * @param dataMap 数据集合，key是文件名，value是文件的字节byte[]数组
     * @param output  输出流
     */
    public static void zipOutputStream(Map<String, byte[]> dataMap, OutputStream output) {
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(output));
        Set<Map.Entry<String, byte[]>> dataEntries = dataMap.entrySet();
        for (Map.Entry<String, byte[]> data : dataEntries) {
            InputStream bufferIn = null;
            try {
                Assert.hasText(data.getKey(), "待写入的文件对象为NULL或空");
                Assert.notNull(data.getValue(), "要写入文件【" + data.getKey() + "】的字节数组对象为NULL");
                bufferIn = new BufferedInputStream(new ByteArrayInputStream(data.getValue()));
                byte[] bs = new byte[1024];                                     //容器1M
                Arrays.fill(bs, (byte) 0);
                zipOutputStream.putNextEntry(new ZipEntry(data.getKey()));      //创建压缩文件内的文件
                int len = -1;
                while ((len = bufferIn.read(bs)) > 0) {                         //写入文件内容
                    zipOutputStream.write(bs, 0, len);
                }

            } catch (IOException e) {
                log.error("IOStreamUtil.zipOutputStream error while ZipOutputStream write or ByteArrayInputStream close , message : {} , filename is {} ", e.getMessage(), data.getKey());
            } finally {
                try {
                    if (null != bufferIn) bufferIn.close();
                } catch (IOException e) {
                    log.error("IOStreamUtil.zipOutputStream error while BufferedInputStream close , message : {} ", e.getMessage());
                }
            }
        }
        try {
/*            output.flush();
            output.close();*/
            zipOutputStream.flush();
            zipOutputStream.close();
        } catch (IOException e) {
            log.error("IOStreamUtil.zipOutputStream error while OutputStream close , message : {} ", e.getMessage());
        }
    }

    public static void fileOutputStream(HttpServletResponse response, DownloadDTO downloadDTO) {
        String fileName = downloadDTO.getName();
        //文件名 -->  不包括文件格式
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        //文件格式 -->  包括 '.'
        String type = fileName.substring(fileName.lastIndexOf("."));
        //解决标题乱码问题
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8") + type + ";filename*=UTF-8''" + URLEncoder.encode(name, "UTF-8") + type);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/force-download");
        response.setCharacterEncoding("UTF-8");

        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(downloadDTO.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            log.error("IOStreamUtil.fileOutputStream error while OutputStream write or flush , message : {} ", e.getMessage());
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("IOStreamUtil.fileOutputStream error while OutputStream close , message : {} ", e.getMessage());
                }
            }
        }
    }
}
