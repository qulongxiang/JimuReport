package com.jeecg.modules.jmreport.config;

import lombok.val;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义积木报表鉴权(如果不进行自定义，则所有请求不做权限控制)
 * 1.自定义获取登录token
 * 2.自定义获取登录用户
 */
@Component
public class JimuReportTokenService implements JmReportTokenServiceI {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    /**
     * 通过请求获取Token
     * @param request
     * @return
     */
    @Override
    public String getToken(HttpServletRequest request) {
        System.out.println("---------call---------getToken-----------------------");
        String token= TokenUtils.getTokenByRequest(request);
        System.out.println("getToken返回结果----token:"+token+"-----");
        return token;
    }

    /**
     * 通过Token获取登录人用户名
     * @param token
     * @return
     */
    @Override
    public String getUsername(String token) {
        String username = TokenUtils.getUsername(token);
        System.out.println("getUsername 返回：  用户名："+username);
        return username;
    }

    /**
     * Token校验
     * @param token
     * @return
     */
    @Override
    public Boolean verifyToken(String token) {
        System.out.println("---------verify-----Token---------------");
        Boolean reBool=false; //认证结果
        Map paramMap=new HashMap<>();
        paramMap.put("token",token);
        List list=namedParameterJdbcTemplate.queryForList("select jn.id from jimu_user_token jn where jn.token=:token ",paramMap);
        if(list.size()>0){
            reBool= TokenUtils.verifyToken(token);//认证通过 并且能解密
        }
        System.out.println("verifyToken认证返回结果："+reBool);
        return reBool;
    }

    /**
     *  自定义请求头
     * @return
     */
    @Override
    public HttpHeaders customApiHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("custom-header1", "Please set a custom value 1");
        header.add("token", "token value 2");
        return header;
    }
}