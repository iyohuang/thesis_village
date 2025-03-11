package com.thesis.village.utils;

/**
 * @author yh
 */

import com.thesis.village.model.BusinessException;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public final class JavaMailUntil {
    private JavaMailUntil() {}

    public static Session createSession(String from, String authCode) throws Exception {
        //	账号信息
        String username = from;//	邮箱发送账号
        String password = AuthCodeCrypto.decrypt(authCode);

        //	创建一个配置文件，并保存
        Properties props = new Properties();

        //	SMTP服务器连接信息
        //  126——smtp.126.com
        //  163——smtp.163.com
        //  qqsmtp.qq.com"
        props.put("mail.smtp.host", "smtp.qq.com");//	SMTP主机名

        //  126——25
        //  163——645 
        //  qq——465
        props.put("mail.smtp.port", "465");//	主机端口号
        props.put("mail.smtp.auth", "true");//	是否需要用户认证
        props.put("mail.smtp.starttls.enale", "true");//	启用TlS加密
        props.put("mail.smtp.ssl.enable", true); // SSL true
        Session session = Session.getInstance(props,new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // TODO Auto-generated method stub
                return new PasswordAuthentication(username,password);
            }
        });

        //  控制台打印调试信息
        session.setDebug(true);
        return session;

    }
}
