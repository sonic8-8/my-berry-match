package com.gongcha.berrymatch.springSecurity.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.gongcha.berrymatch.exception.BusinessException;
import com.gongcha.berrymatch.CommonResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            setErrorResponse(response, e);
        }
    }

    private void setErrorResponse(HttpServletResponse response, BusinessException e) throws IOException {
        CommonResponse commonResponse = new CommonResponse(e.getStatus(), e.getStatus().value(), e.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(e.getStatus().value());
        response.setContentType("application/json; charset=UTF-8");

        response.getWriter().write(
                objectMapper.writeValueAsString(commonResponse)
        );
    }
}
