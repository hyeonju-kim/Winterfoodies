package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.user.*;
import com.winterfoodies.winterfoodies_project.entity.*;
import com.winterfoodies.winterfoodies_project.exception.UserException;
import com.winterfoodies.winterfoodies_project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    //    private final UserDto loginUser; //ScenarioConfig에서 등록한 bean을 주입받아서 사용하기 -> jwt토큰 적용 후에는 필요x
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;



    // jwt 토큰으로 현재 인증된 사용자의 Authentication 객체에서 이름 가져오기
    public String getUsernameFromAuthentication() {
        String username = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 인증된 사용자의 이름 가져오기
            username = authentication.getName();
        }
        return username;
    }

    // 인증된 사용자의 id 가져오기
    public Long getUserId() {
        User foundUser = userRepository.findByUsername(getUsernameFromAuthentication());
        return foundUser.getId();
    }

    // ################################################################ 회원가입/로그인 ##############################################################
    // 회원가입
    @Override
    public UserDto  signUp(UserRequestDto userRequestDto) {
        User foundUserByUsername = userRepository.findByUsername(userRequestDto.getUsername());
        if (foundUserByUsername != null) {
            throw new UserException("해당 이메일이 이미 존재합니다.", HttpStatus.BAD_REQUEST, null);
        }
//        User foundUserByNickname = userRepository.findByNickname(userRequestDto.getNickname());
//        if (foundUserByNickname != null) {
//            throw new UserException("해당 닉네임이 이미 존재합니다.", HttpStatus.BAD_REQUEST, null);
//        }

        if (!userRequestDto.getPassword().equals(userRequestDto.getConfirmPassword())) {
            throw new UserException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST, null);
        }

        String encodedPassword = encoder.encode(userRequestDto.getPassword()); // 230726 추가
        userRequestDto.setPassword(encodedPassword);

        User user = new User(userRequestDto);
        userRepository.save(user);
        return new UserDto(user);
    }



    // 로그인 후, 유저네임/닉네임/휴대폰번호 조회
    @Override
    public UserResponseDto personalInfo() {
        User foundUser = userRepository.findByUsername(getUsernameFromAuthentication());
        String username = foundUser.getUsername();
        String nickname = foundUser.getNickname();
        String phoneNumber = foundUser.getPhoneNumber();
        return new UserResponseDto(username, nickname, phoneNumber);
    }


    // 계정 중복확인 - 230901 추가
    @Override
    public boolean isUsernameUnique(String username) {
        return userRepository.existsByUsername(username);// 존재하면 true
    }
//
//    // 닉네임 중복확인 - 230901 추가
//    @Override
//    public boolean isNicknameUnique(String nickname) {
//        return userRepository.existsByNickname(nickname); // 존재하면 true
//    }



}