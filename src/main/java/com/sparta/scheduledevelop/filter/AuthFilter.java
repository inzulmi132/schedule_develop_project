package com.sparta.scheduledevelop.filter;

import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.entity.UserRoleEnum;
import com.sparta.scheduledevelop.jwt.JwtUtil;
import com.sparta.scheduledevelop.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        String mapping = httpServletRequest.getMethod();

        // 로그인이 필요한 경우 => POST, PUT, DELETE 매핑의 경우
        if(StringUtils.hasText(uri) && (mapping.equals("POST" ) || mapping.equals("PUT") || mapping.equals("DELETE"))) {
            String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);
            if(!StringUtils.hasText(tokenValue)) {
                ((HttpServletResponse) response).setStatus(400);
                return;
            }

            String token = jwtUtil.substringToken(tokenValue);
            String validateToken = jwtUtil.validateToken(token);
            if(!validateToken.isEmpty()) {
                if(validateToken.startsWith("Expired JWT token")) {
                    ((HttpServletResponse) response).setStatus(401);
                    return;
                }
                throw new IllegalArgumentException("Token Error");
            }

            Claims info = jwtUtil.getUserInfoFromToken(token);

            User user = userRepository.findByEmail(info.getSubject()).orElse(null);
            if(user == null) throw new NullPointerException("Not Found User");

            String userRole = info.get(JwtUtil.AUTHORIZATION_KEY, String.class);
            if(userRole == null) throw new NullPointerException("Not Found UserRole");
            UserRoleEnum role = UserRoleEnum.valueOf(userRole);

            request.setAttribute("user", user);
            request.setAttribute("role", role);
        }
        chain.doFilter(request, response);
    }
}
