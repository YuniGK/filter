package com.example.filter.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
//@Component
@WebFilter(urlPatterns = "/api/user/**")//특정 페이지를 지정할 수 있다.
public class GlobalFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //전처리
        /*
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;

        String url = httpServletRequest.getRequestURI();

        BufferedReader br = httpServletRequest.getReader();

        br.lines().forEach(line -> {
            log.info("url : {}, line : {}", url, line);
        });
        //br이 모든 내용을 읽어 프로그램 시작시 오류가 발생한다.
        */

        //아래와 같이 수정 시, 계속적으로 읽을 수 있어 br에 대하 오류가 발생하지 않는다.
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest)request);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper((HttpServletResponse)response);

        //해당 부분을 기점으로 전, 후처리가 결정된다.
        chain.doFilter(request, response);

        String url = httpServletRequest.getRequestURI();

        //후처리
        String reqContent = new String(httpServletRequest.getContentAsByteArray());

        log.info("requses url : {}, request body : {}", url, reqContent);

        String resContent = new String(httpServletResponse.getContentAsByteArray());
        int httpStatus = httpServletResponse.getStatus();

        //결과를 다시 넣어준다.
        httpServletResponse.copyBodyToResponse();

        log.info("response status : {}, responseBody : {}", httpStatus, resContent);

    }

}
