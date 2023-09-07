package com.winterfoodies.winterfoodies_project.service;


import com.winterfoodies.winterfoodies_project.dto.auth.TempAuthInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;

    @EventListener(TempAuthInfo.class)
    @Async("threadPoolTaskExecutor")
    public void sendPasswordForgotMessage(TempAuthInfo tempAuthInfo){
        log.info("send Email");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@winterfoodies.com");
        message.setTo(tempAuthInfo.getEmail());
        message.setSubject("윈터 푸디스 임시 비밀번호 발송 안내");
        message.setText("임시비밀번호 : " + tempAuthInfo.getTempPassword());
        javaMailSender.send(message);
    }

}
