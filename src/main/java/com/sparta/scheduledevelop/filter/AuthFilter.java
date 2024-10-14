package com.sparta.scheduledevelop.filter;

import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.jwt.JwtUtil;
import com.sparta.scheduledevelop.repository.UserRepository;
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

        if(!StringUtils.hasText(uri)) return;
        // 로그인이 필요 없는 경우.
        if(uri.endsWith("login") || uri.endsWith("signup") || uri.endsWith("write") || uri.endsWith("edit") || uri.endsWith("delete")) {
            chain.doFilter(request, response);
        } else {
            String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);
            if(!StringUtils.hasText(tokenValue)) throw new IllegalArgumentException("Not Found Token");

            String token = jwtUtil.substringToken(tokenValue);
            if(!jwtUtil.validateToken(token)) throw new IllegalArgumentException("Token Error");

            Claims info = jwtUtil.getUserInfoFromToken(token);
            User user = userRepository.findByEmail(info.getSubject())
                    .orElseThrow(() -> new NullPointerException("Not Found User")
                    );

            request.setAttribute("user", user);
            chain.doFilter(request, response);
        }
    }
}
