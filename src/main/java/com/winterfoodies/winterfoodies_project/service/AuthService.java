package com.winterfoodies.winterfoodies_project.service;


import com.winterfoodies.winterfoodies_project.dto.auth.TempAuthInfo;
import com.winterfoodies.winterfoodies_project.entity.User;
import com.winterfoodies.winterfoodies_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String SPECIAL_CHAR = "!@#$%^&*()-_=+\\|[]{};:'\",.<>?/";

    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + SPECIAL_CHAR;

    private static SecureRandom random = new SecureRandom();

    public static String instancePasswordGenerator() {
        int passwordLength = random.nextInt(9) + 8; // 8에서 16 사이의 랜덤 길이

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(PASSWORD_ALLOW_BASE.length());
            char randomChar = PASSWORD_ALLOW_BASE.charAt(randomIndex);
            password.append(randomChar);
        }

        return password.toString();
    }



//    // 임시 비밀번호 생성 메서드
//    private String instancePasswordGenerator(){
//        Random random = new Random(122929L);
//        int k = 1;
//        for(int i = 0; i < 7; i++) {
//            int no = random.nextInt(10);
//            k = k * 10 + no;
//        }
//
//        return String.valueOf(k);
//    }

    @Transactional
    public boolean saveTempAuthInfo(String email){
        //임시 비밀번호 생성
        String tempPassword = instancePasswordGenerator();

        //임시 유저정보 생성
        TempAuthInfo tempAuthInfo = new TempAuthInfo();
        tempAuthInfo.setEmail(email);
        tempAuthInfo.setTempPassword(tempPassword);

        //임시 유저비밀번호로 setting
        User user = userRepository.findByUsername(email);
        user.setPassword(passwordEncoder.encode(tempPassword));

        //email 발송 이벤트 퍼블리싱(비동기)
        eventPublisher.publishEvent(tempAuthInfo);
        return true;
    }


}
