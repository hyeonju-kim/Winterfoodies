package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
import com.winterfoodies.winterfoodies_project.entity.User;
import com.winterfoodies.winterfoodies_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserDto loginUser;

    private final UserRepository userRepository;

    @Override
    public UserDto retrieveUser() {
        Optional<User> user = userRepository.findByEmail(loginUser.getEmail());
        if(user.isPresent()){
            User foundUser = user.get();
            UserDto foundUserDto = new UserDto();
            foundUserDto.setEmail(foundUser.getEmail());
            foundUserDto.setName(foundUser.getName());
            return foundUserDto;
        }
        UserDto notFoundUserDto = new UserDto();
        notFoundUserDto.setMessage("해당 유저를 찾을 수 없습니다.");
        return notFoundUserDto;
    }

    @Override
    public UserDto changePw(UserDto userDto) {
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setPassword(userDto.getPassword());
            userRepository.save(foundUser);

            UserDto foundUserDto = new UserDto();
            foundUserDto.setMessage("변경완료!!");
            return foundUserDto;
        }
        UserDto notFoundUserDto = new UserDto();
        notFoundUserDto.setMessage("해당 유저를 찾을 수 없습니다.");
        return notFoundUserDto;
    }
}
