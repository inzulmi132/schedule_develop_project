package com.sparta.scheduledevelop.filter;

import com.sparta.scheduledevelop.domain.user.entity.User;
import com.sparta.scheduledevelop.domain.user.entity.UserRoleEnum;
import com.sparta.scheduledevelop.domain.user.repository.UserRepository;
import com.sparta.scheduledevelop.exception.CustomErrorCode;
import com.sparta.scheduledevelop.exception.CustomException;
import com.sparta.scheduledevelop.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;


@Slf4j(topic = "로그인 및 JWT 생성")
@Component
@Order(1)
public class AuthFilter implements Filter {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    public AuthFilter(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String uri = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();

        if(StringUtils.hasText(uri) && (uri.endsWith("signup") || uri.endsWith("login"))) {
            chain.doFilter(request, response);
            return;
        }

        if(method.equals("GET")) {
            chain.doFilter(request, response);
            return;
        }

        Claims info = jwtUtil.getUserInfoFromRequest(httpServletRequest);
        User user = userRepository.findByEmail(info.getSubject())
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        String userRole = info.get(JwtUtil.AUTHORIZATION_KEY, String.class);
        if(userRole == null) {
            throw new CustomException(CustomErrorCode.USER_ROLE_NOT_FOUND);
        }
        UserRoleEnum role = UserRoleEnum.valueOf(userRole);

        request.setAttribute("user", user);
        request.setAttribute("role", role);
        chain.doFilter(request, response);
    }
}
