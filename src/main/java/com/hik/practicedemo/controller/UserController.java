package com.hik.practicedemo.controller;

import com.alibaba.fastjson.JSON;
import com.google.zxing.WriterException;
import com.hik.practicedemo.exception.BusinessException;
import com.hik.practicedemo.exception.CommonExceptionEnum;
import com.hik.practicedemo.model.constant.CommonConstants;
import com.hik.practicedemo.model.dto.UserInfoDTO;
import com.hik.practicedemo.model.param.user.UserQuery;
import com.hik.practicedemo.model.param.user.UserRegisterParam;
import com.hik.practicedemo.model.vo.PageVO;
import com.hik.practicedemo.model.vo.ResultVO;
import com.hik.practicedemo.model.vo.UserVO;
import com.hik.practicedemo.service.UserService;
import com.hik.practicedemo.utils.QRCodeUtil;
import com.hik.practicedemo.utils.RandomUtil;
import com.hik.practicedemo.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangJinChang on 2019/12/4 15:35
 * 用户控制层
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户控制层")
@Validated
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/otp")
    @ApiOperation(value = "获取二维码", notes = "获取二维码，通过二维码进行注册，二维码为4位数字，有效期为5分钟")
    @ApiResponse(code = 200, message = "获取二维码")
    public void getQRCode(HttpServletResponse response) {
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            // 存放在二维码中的内容
            int content = RandomUtil.randomLong(4);
            // 嵌入二维码的图片路径
            String imgPath = "G:/Others/Minio/image.jpeg";
            // 生成的二维码的路径及名称
            String destPath = "G:/Others/Minio/jam.jpeg";
            // 生成二维码
            BufferedImage encode = QRCodeUtil.encode(String.valueOf(content), imgPath, true);
            boolean b = ImageIO.write(encode, "JPG", Objects.requireNonNull(outputStream));
            if (b) {
                // 5分钟后二维码过期
                RedisUtil.set(CommonConstants.QRC_INFO_CACHE + content, String.valueOf(content), 5, TimeUnit.MINUTES);
                System.err.println("二维码验证信息是 ：" + content);
            }
        } catch (IOException | WriterException e) {
            log.error("获取二维码时IO流发生异常，错误信息是：{}", e.getMessage());
        }
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册", notes = "根据验证码、用户手机、邮箱、密码进行注册，返回用户视图模型")
    public ResultVO register(@RequestBody @Valid UserRegisterParam param) throws BusinessException {
        String redisValue = RedisUtil.get(CommonConstants.QRC_INFO_CACHE + param.getOtp());
        if (StringUtils.isEmpty(redisValue)) {
            return ResultVO.error(CommonExceptionEnum.OTP_EXPIRED.getCode(), CommonExceptionEnum.OTP_EXPIRED.getMsg());
        }
        if (!Objects.equals(redisValue, param.getOtp())) {
            return ResultVO.error(CommonExceptionEnum.OTP_VERIFIED_FAILED.getCode(), CommonExceptionEnum.OTP_VERIFIED_FAILED.getMsg());
        }
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(param, userInfoDTO);
        UserVO userVO = userService.save(userInfoDTO);
        return ResultVO.success(userVO);
    }

    @GetMapping("/remove")
    @ApiOperation(value = "删除信息", notes = "支持根据用户id批量删除，返回删除用户信息的数量")
    public ResultVO remove(@RequestParam("ids") @NotEmpty(message = "用户id集合不能为空") List<Integer> ids) {
        int rows = userService.remove(ids);
        return ResultVO.success(rows);
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "获取信息", notes = "根据用户id查询信息，返回用户信息视图模型")
    public ResultVO getUserInfo(@PathVariable("id") Integer id) {
        Optional<UserVO> optional = userService.findById(id);
        if (optional.isPresent()) {
            return ResultVO.success(optional.get());
        }
        return ResultVO.other(CommonExceptionEnum.RESOURCE_NOT_EXIST.getMsg());
    }

    @GetMapping("/info")
    @ApiOperation(value = "接口分页条件查询", notes = "根据Repository接口，分页条件查询用户信息，返回用户信息视图分页模型")
    public ResultVO<PageVO<UserVO>> getUserInfo(@RequestParam("ids") @NotEmpty(message = "用户id集合不能为空") List<Integer> ids,
                                                @RequestParam("name") @NotEmpty(message = "用户名不能为空") String name,
                                                @RequestParam("pageNo") @Min(value = 1, message = "当前页面必须大于0") Integer pageNo,
                                                @RequestParam("pageSize") @Range(min = 1, max = 100, message = "页面内容必须介于1~100") Integer pageSize) {
        PageVO<UserVO> userPageVO = userService.fuzzyQuery(ids, name, pageNo, pageSize);
        return ResultVO.success(userPageVO);
    }

    @PostMapping("/info")
    @ApiOperation(value = "断言分页条件查询", notes = "根据Specification接口，分页条件查询用户信息，返回用户信息视图分页模型")
    public ResultVO<PageVO<UserVO>> getUserInfo(@RequestBody @Valid UserQuery query) {
        PageVO<UserVO> userPageVO = userService.fuzzyQuery(query);
        return ResultVO.success(userPageVO);
    }

    @PostMapping("/signIn")
    @ApiOperation(value = "用户登录", notes = "使用手机号码和密码进行登录")
    public ResultVO<UserVO> signIn(@RequestParam("tel") @NotEmpty(message = "用户手机不能为空") String tel,
                                   @RequestParam("pwd") @NotEmpty(message = "用户密码不能为空") String pwd,
                                   HttpServletResponse response) {
        Optional<UserVO> optional = userService.signIn(tel, pwd);
        if (optional.isPresent()) {
            UserVO userVO = optional.get();
            String cookieValue = RandomUtil.uuid();
            Cookie cookie = new Cookie(CommonConstants.LOGIN_TOKEN, cookieValue);
            cookie.setHttpOnly(true);
            cookie.setPath(CommonConstants.BACK_SLASHES);
            cookie.setMaxAge(60 * 30);
            response.addCookie(cookie);
            RedisUtil.set(CommonConstants.LOGIN_TOKEN + cookieValue, JSON.toJSONString(userVO), 30, TimeUnit.MINUTES);
            return ResultVO.success(userVO);
        }
        return ResultVO.other("账户或密码错误");
    }

    @PostMapping("/signOut")
    @ApiOperation(value = "注销登录", notes = "注销，删除登录时种植的cookie")
    public ResultVO signOut(@CookieValue(value = "user_login_") String token,
                            HttpServletResponse response) {
        Cookie cookie = new Cookie(token, StringUtils.EMPTY);
        cookie.setMaxAge(0);
        cookie.setPath(CommonConstants.BACK_SLASHES);
        response.addCookie(cookie);
        return ResultVO.success();
    }
}
