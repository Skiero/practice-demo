package com.hik.practicedemo.controller;

import com.hik.practicedemo.model.dto.DownloadDTO;
import com.hik.practicedemo.utils.MinioUtil;
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by wangJinChang on 2019/12/13 20:18
 * 文件上传下载测试接口
 */
@Slf4j
@Controller
@ResponseBody
public class IOStreamController {

    @Resource
    private MinioUtil minioTemplate;

    @Resource
    @Lazy
    private MinioClient minioClient;

    public String upload(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            minioTemplate.putObject("bucketName", fileName, file.getInputStream());
        } catch (Exception e) {
            return "上传失败";
        }
        return "上传成功";
    }

    public void download(String fileName, HttpServletResponse response, HttpServletRequest request) {
        String[] nameArray = StringUtils.split(fileName, "-");
        try (InputStream inputStream = minioTemplate.getObject(nameArray[0], nameArray[1])) {
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/force-download");
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            log.error("文件读取异常", e);
        }
    }

    //下载minio服务的文件
    @GetMapping("/show")
    public String showPdf(HttpServletResponse response) {
        try {
            InputStream fileInputStream = minioClient.getObject("test", "pub/test.jpg");
            response.setContentType("application/pdf");
            response.setCharacterEncoding("UTF-8");
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[1024];
            while ((fileInputStream.read(b, 0, b.length)) != -1) {
                out.write(b);
            }
            out.flush();
            fileInputStream.close();
            out.close();
            //下载
            /*IOUtils.copy(fileInputStream, response.getOutputStream());
            fileInputStream.close();*/
            return "下载完成";
        } catch (Exception e) {
            return "下载失败";
        }
    }

    //下载minio服务的文件
    @GetMapping("/download")
    public String download(HttpServletResponse response) throws IOException {
        InputStream fileInputStream = null;
        try {
            fileInputStream = minioClient.getObject("test", "pub/√行政需求提单接口人信息.xlsx");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("中国", "UTF-8") + ".xlsx;filename*=UTF-8''" + URLEncoder.encode("中国", "UTF-8") + ".xlsx");
            response.setContentType("application/force-download");
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(fileInputStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return "下载失败";
        } finally {
            assert fileInputStream != null;
            fileInputStream.close();
        }
        return "下载完成";
    }

    //获取minio文件的下载地址
    @GetMapping("/url")
    public String getUrl() {
        try {
            String url = minioClient.presignedGetObject("test", "pub/test.jpg");
            minioTemplate.getObjectURL("test", "pub/test.jpg", 5000);
            System.out.println(url);
            List<Bucket> allBuckets = minioTemplate.getAllBuckets();
            System.out.println(allBuckets);
            return url;
        } catch (Exception e) {
            return "获取失败";
        }
    }

    @RequestMapping("/uploads")
    public void recursion(Vector<File> vecFile, HttpServletResponse response) throws IOException {
        //根据路径生成一个文件
        String root = "http://127.0.0.1:9000/test/pub/";
        File file = new File(root);
        File[] subFile = file.listFiles();
        //遍历文件里面的所有文件
        for (int i = 0; i < subFile.length; i++) {
            /*如果是文件夹，则递归下去寻找文件夹里面的文件*/
            if (subFile[i].isDirectory()) {
                //recursion(subFile[i].getAbsolutePath(), vecFile);
            } else {
                //如果不是文件夹的话就直接添加到vector容器里面去
                //将遍历出来的文件进行压缩和下载：
                //String filename = subFile[i].getName();
                //vecFile.add(filename);
                vecFile.add(subFile[i]);
            }
        }
        //设置下载文件的名称
        String fileName = "temp1.zip";
        response.setContentType("text/html; charset=UTF-8"); // 设置编码字符
        response.setContentType("application/x-msdownload"); // 设置内容类型为下载类型
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);// 设置下载的文件名称
        OutputStream out = response.getOutputStream(); // 创建页面返回方式为输出流，会自动弹出下载框
        //创建压缩文件需要的空的zip包
        //String zipBasePath = request.getSession().getServletContext().getRealPath("/logs/2/");
        /* 输出basePath的路径，方便调试 */
        //System.out.println(zipBasePath);
        /* 创建压缩文件需要的空的zip包 ，这里是自动生成的，不用我们自己去生成 */
        //String zipFilePath = zipBasePath + "temp.zip";
        String zipFilePath = "temp.zip";
        System.out.println("create the empty zip file successfully??????");
        //根据临时的zip压缩包路径，创建zip文件
        java.io.File zip = new java.io.File(zipFilePath);
        if (!zip.exists()) {
            zip.createNewFile();
        }
        System.out.println("create the  zip file successfully");
        // 创建zip文件输出流
        FileOutputStream fos = new FileOutputStream(zip);
        ZipOutputStream zos = new ZipOutputStream(fos);
        System.out.println("create the empty zip stream successfully");
        //循环读取文件路径集合，获取每一个文件的路径（将文件一个一个进行压缩）
        for (java.io.File fp : vecFile) {
            zipFile(fp, zos); // 将每一个文件写入zip文件包内，即进行打包
        }
        System.out.println("get the path successfully");
        // fos.close();//如果这样关两次的话会报错，java.io.IOException: Stream closed
        zos.close();
        System.out.println("ok??");
        //将打包后的文件写到客户端,使用缓冲流输出
        InputStream fis = new BufferedInputStream(new FileInputStream(zipFilePath));
        byte[] buff = new byte[4096];
        int size = 0;
        while ((size = fis.read(buff)) != -1) {
            out.write(buff, 0, size);
        }
        System.out.println("package is packed successfully");
        //释放和关闭输入输出流
        out.flush();//清楚缓存
        out.close();
        fis.close();
    }

    // 文件压缩的方法
    public void zipFile(java.io.File inputFile, ZipOutputStream zipoutputStream) {
        try {
            if (inputFile.exists()) { // 判断文件是否存在
                if (inputFile.isFile()) { // 判断是否属于文件，还是文件夹

                    // 创建输入流读取文件
                    FileInputStream fis = new FileInputStream(inputFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);

                    // 将文件写入zip内，即将文件进行打包
                    ZipEntry ze = new ZipEntry(inputFile.getName()); // 获取文件名
                    zipoutputStream.putNextEntry(ze);

                    // 写入文件的方法，同上
                    byte[] b = new byte[1024];
                    long l = 0;
                    while (l < inputFile.length()) {
                        int j = bis.read(b, 0, 1024);
                        l += j;
                        zipoutputStream.write(b, 0, j);
                    }
                    // 关闭输入输出流
                    bis.close();
                    fis.close();
                } else { // 如果是文件夹，则使用穷举的方法获取文件，写入zip
                    try {
                        java.io.File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], zipoutputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/zip")
    public void zip(Vector<File> vecFile, HttpServletResponse response) throws IOException {
        //设置下载文件的名称
        String fileName = "temp1.zip";
        response.setContentType("text/html; charset=UTF-8"); // 设置编码字符
        response.setContentType("application/x-msdownload"); // 设置内容类型为下载类型
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);// 设置下载的文件名称
        OutputStream out = response.getOutputStream(); // 创建页面返回方式为输出流，会自动弹出下载框
        //创建压缩文件需要的空的zip包
        //String zipBasePath = request.getSession().getServletContext().getRealPath("/logs/2/");
        /* 输出basePath的路径，方便调试 */
        //System.out.println(zipBasePath);
        /* 创建压缩文件需要的空的zip包 ，这里是自动生成的，不用我们自己去生成 */
        //String zipFilePath = zipBasePath + "temp.zip";
        String zipFilePath = "temp.zip";
        System.out.println("create the empty zip file successfully??????");
        //根据临时的zip压缩包路径，创建zip文件
        java.io.File zip = new java.io.File(zipFilePath);
        if (!zip.exists()) {
            zip.createNewFile();
        }
        System.out.println("create the  zip file successfully");
        // 创建zip文件输出流
        FileOutputStream fos = new FileOutputStream(zip);
        ZipOutputStream zos = new ZipOutputStream(fos);
        System.out.println("create the empty zip stream successfully");
        //循环读取文件路径集合，获取每一个文件的路径（将文件一个一个进行压缩）
        //for (java.io.File fp : vecFile) {
        zipFileUtil("pub/√行政需求提单接口人信息.xlsx", "1.xlsx", zos); // 将每一个文件写入zip文件包内，即进行打包
        zipFileUtil("pub/test.jpg", "2.jpg", zos);
        //}
        System.out.println("get the path successfully");
        // fos.close();//如果这样关两次的话会报错，java.io.IOException: Stream closed
        zos.close();
        System.out.println("ok??");
        //将打包后的文件写到客户端,使用缓冲流输出
        InputStream fis = new BufferedInputStream(new FileInputStream(zipFilePath));
        byte[] buff = new byte[4096];
        int size = 0;
        while ((size = fis.read(buff)) != -1) {
            out.write(buff, 0, size);
        }
        System.out.println("package is packed successfully");
        //释放和关闭输入输出流
        out.flush();//清楚缓存
        out.close();
        fis.close();
    }

    @GetMapping("/testZip")
    public void getZipStream(HttpServletResponse response) throws Exception {

        List<DownloadDTO> downloadDTOList = new ArrayList<>();

        InputStream inputStream1 = minioClient.getObject("test", "pub/√行政需求提单接口人信息.xlsx");
        DownloadDTO dto1 = new DownloadDTO();
        dto1.setName("行政需求提单接口人信息.xlsx");
        dto1.setType(".xlsx");
        dto1.setBytes(IOUtils.toByteArray(inputStream1));
        downloadDTOList.add(dto1);

        InputStream inputStream2 = minioClient.getObject("test", "pub/test.jpg");
        DownloadDTO dto2 = new DownloadDTO();
        dto2.setName("test.jpg");
        dto2.setType(".jpg");
        dto2.setBytes(IOUtils.toByteArray(inputStream2));
        downloadDTOList.add(dto2);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            String name = "排班管理附件" + UUID.randomUUID().toString();
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8") + ".zip" + ";filename*=UTF-8''" + URLEncoder.encode(name, "UTF-8") + ".zip");
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding("UTF-8");
            String zipFilePath = name + ".zip";
            File zip = new File(zipFilePath);
            if (!zip.exists()) {
                boolean b = zip.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(zip);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (DownloadDTO downloadDTO : downloadDTOList) {
                ZipEntry zipEntry = new ZipEntry(downloadDTO.getName());
                zos.putNextEntry(zipEntry);
                zos.write(downloadDTO.getBytes());
            }
            zos.close();
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipFilePath));
            byte[] buff = new byte[1024 * 10];
            int size;
            while ((size = bis.read(buff)) != -1) {
                outputStream.write(buff, 0, size);
            }
            bis.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // IO流压缩文件
    private void zipFileUtil(String n1, String n2, ZipOutputStream zipoutputStream) {
        try {
            // 创建输入流读取文件
            InputStream fis = minioClient.getObject("test", n1);
            BufferedInputStream bis = new BufferedInputStream(fis);

            // 将文件写入zip内，即将文件进行打包
            ZipEntry ze = new ZipEntry(n2);
            zipoutputStream.putNextEntry(ze);

            // 写入文件的方法，同上
            byte[] b = new byte[1024];
            long l;
            while ((l = bis.read(b, 0, b.length)) != -1) {
                zipoutputStream.write(b, 0, (int) l);
            }
            // 关闭输入输出流
            bis.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
