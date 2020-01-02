package com.hik.practicedemo.controller;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.hik.practicedemo.aop.AccessLimit;
import com.hik.practicedemo.aop.CheckLogin;
import com.hik.practicedemo.aop.Login;
import com.hik.practicedemo.model.constant.CommonConstants;
import com.hik.practicedemo.model.vo.ResultVO;
import com.hik.practicedemo.utils.QRCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangJinChang on 2019/12/17 21:28
 * 二维码测试接口
 */
@RestController
@RequestMapping("/qr")
@Api(tags = "二维码控制层")
public class QrCodeController {

    @Resource
    private HttpServletRequest request;

    private Integer id;

    /**
     * 生成二维码
     *
     * @param response HTTP相应
     * @throws IOException     io异常
     * @throws WriterException io异常
     */
    @GetMapping("/encode")
    @AccessLimit(seconds = 1, timeUnit = TimeUnit.MINUTES, maxCount = 10, needLogin = false)
    @ApiOperation(value = "获取二维码", notes = "获取二维码，二维码为4位数字")
    @Login
    public void getQr(@RequestAttribute("user_login_") String id,
                      HttpServletResponse response) throws IOException, WriterException, NotFoundException {
        Integer userId = getUserId();
        System.out.println("通过域对象获取当前登录用户的id ==> " + userId);
        System.out.println("通过注解来获取当前登录用户的id ==> " + id);
        ServletOutputStream outputStream = response.getOutputStream();
        // 存放在二维码中的内容
        String text = "这是一个测试信息，哈哈哈哈";
        // 嵌入二维码的图片路径
        String imgPath = "G:/Others/Minio/backdrop.jpg";
        // 生成的二维码的路径及名称
        String destPath = "G:/Others/Minio/example.jpg";
        // 生成二维码
        QRCodeUtil.encode(text, imgPath, destPath, true);
        BufferedImage encode = QRCodeUtil.encode(text, imgPath, true);
        ImageIO.write(encode, "JPG", outputStream);

        // 解析二维码
        String str = QRCodeUtil.decode(destPath);
        // 打印出解析出的内容
        System.out.println(str);
    }

    /**
     * 解析二维码
     *
     * @return 二维码的解析信息
     * @throws NotFoundException file异常
     * @throws IOException       io异常
     */
    @GetMapping("/decode")
    @ApiOperation(value = "解析二维码", notes = "解析二维码中的信息")
    @Login
    public Object show() throws NotFoundException, IOException {
        Integer userId = getUserId();
        System.out.println("通过域对象获取当前登录用户的id ==> " + userId);
        // 解析二维码
        String destPath = "G:/Others/Minio/jam.jpeg";
        String str = QRCodeUtil.decode(destPath);
        // 打印出解析出的内容
        System.out.println(str);
        return str;
    }

    @PostMapping("/testLoginByRequestAttribute")
    @ApiOperation(value = "登录校验注解的测试接口", notes = "登录校验注解的测试接口（使用拦截器实现登录校验，响应数据使用response write实现）")
    @Login
    public ResultVO testLoginByRequestAttribute(@RequestAttribute("user_login_") String userId) {
        return ResultVO.success(userId);
    }

    @PostMapping("/testLoginByAOP")
    @ApiOperation(value = "登录校验注解的测试接口", notes = "登录校验注解的测试接口（使用切面编程实现登录校验，响应数据使用return实现）")
    @CheckLogin(fileName = "id")
    public ResultVO testLoginByAOP() {
        System.out.println(id);
        return ResultVO.success(id);
    }

    @GetMapping("/testAccessLimit")
    @ApiOperation(value = "测试注解实现接口防刷功能", notes = "通过注解实现接口防刷功能（使用拦截器实现接口防刷，响应数据使用抛异常实现）")
    @AccessLimit(maxCount = 10, seconds = 20)
    public ResultVO testAccessLimit() {
        return ResultVO.success();
    }

    /**
     * 通过request域对象获取登录用户的id
     *
     * @return 登录用户id
     */
    private Integer getUserId() {
        Object attribute = request.getAttribute(CommonConstants.LOGIN_TOKEN);
        return (Integer) attribute;
    }
}
