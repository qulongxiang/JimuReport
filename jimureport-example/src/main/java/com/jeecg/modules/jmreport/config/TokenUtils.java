package com.jeecg.modules.jmreport.config;

import com.aliyun.oss.common.utils.DateUtil;
import com.aliyun.oss.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class TokenUtils {
    private static String privateKey="elink";
    private static String privateSplit="@@@!!!";

    public static String getTokenByRequest(HttpServletRequest request) {
        String token=request.getParameter("token");
        if(StringUtils.isNullOrEmpty(token)){
            token=request.getHeaders("token").nextElement();
        }
        return token;
    }

    /**
     * 验证token是否能正确解密
     * @param token
     * @return
     */
    public static Boolean verifyToken(String token) {
        try {
            String tokenStr = AESUtil.decrypt(token, privateKey);
            System.out.println("解密后的值："+tokenStr);
            //验证是否有分隔符
            if(tokenStr.indexOf(privateSplit)>-1){
                //认证通过
                return true;
            }
        }catch (Exception ex){
            System.out.println("解密异常：");
            ex.printStackTrace();
        }
        return false;
    }
    /**
     * 获取用户名
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        try {
            String tokenStr = AESUtil.decrypt(token, privateKey);
            System.out.println("解密后的值："+tokenStr);
            //第一个参数是用户名
            return tokenStr.split(privateSplit)[0];
        }catch (Exception ex){
            System.out.println("解密异常：");
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 根据用户名生成token
     * @param username
     * @return
     */
    public static String generateToken(String username) {
        String accessToken = "";
        StringBuilder tokenStr = new StringBuilder();
        tokenStr.append(username).append(privateSplit)
                .append(DateUtil.formatAlternativeIso8601Date(new Date()));
        System.out.println(DateUtil.formatAlternativeIso8601Date(new Date()));
        accessToken = AESUtil.encrypt(tokenStr.toString(), privateKey);
        System.out.println(username+"="+accessToken);
        return accessToken;
    }

    public static void main(String[] args) {
        generateToken("neimeng");
        generateToken("scj");
        generateToken("ypj");
    }

}
