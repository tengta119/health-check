package com.example.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.common.Constants;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.DoctorService;
import com.example.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Component
public class TokenUtils {
    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private DoctorService doctorService;

    private static AdminService staticadminService;
    private static UserService staticuserService;
    private static DoctorService staticdoctorService;

    @PostConstruct
    public void init() {
        staticadminService = adminService;
        staticuserService = userService;
        staticdoctorService = doctorService;
    }

    public static  String createToken(String data, String sign) {
        // audience是存储数据的一个媒介， 存储用户ID和用户的角色
        return JWT
                .create()
                .withAudience(data)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .sign(Algorithm.HMAC256(sign));
    }

    //获取当前登录的用户
    public static Account getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader(Constants.TOKEN);
            String audience = JWT.decode(token).getAudience().get(0);
            Integer userId = Integer.valueOf(audience.split("-")[0]);
            String role = audience.split("-")[1];

            if (RoleEnum.ADMIN.name().equals(role)) {
                return staticadminService.selectById(userId);
            } else if (RoleEnum.USER.name().equals(role)) {
                return staticuserService.selectById(userId);
            } else if (RoleEnum.DOCTOR.name().equals(role)) {
                return staticdoctorService.selectById(userId);
            }
        } catch (Exception e) {
            log.error("获取当前登录用户失败：", e);
        }
        return null;
    }
}
