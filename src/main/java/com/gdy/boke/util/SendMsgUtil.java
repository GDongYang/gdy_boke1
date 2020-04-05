package com.gdy.boke.util;

import com.gdy.boke.constant.RedisKeyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Configuration
public class SendMsgUtil {


    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Autowired
    private RedisTemplate redisTemplate;

    public boolean sendEmailCode(String toEmail){
        //生成验证码
        String verificationCode = generateNumber();
        SimpleMailMessage message = new SimpleMailMessage();
        //验证码缓存
        redisTemplate.opsForValue().set(RedisKeyConstant.REDIS_KEY_EMAIL+toEmail,verificationCode,60, TimeUnit.SECONDS);
        message.setFrom(fromMail);
        message.setTo(toEmail);
        message.setSubject("验证码");
        message.setText("【东洋科技有限公司】您的验证码是："+verificationCode+";有效时间1分钟。");
        try {
            //验证码发送
            javaMailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private static String generateNumber(){
        String result = "";
        Random random = new Random();
        for(int i = 0;i<6;i++){
            result = result+String.valueOf(random.nextInt(9));
        }
        return result;
    }

}
