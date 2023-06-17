//package com.winterfoodies.winterfoodies_project.service;
//
//import com.winterfoodies.winterfoodies_project.entity.User;
//import com.winterfoodies.winterfoodies_project.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username).orElseThrow(
//                () -> {
//                    return new IllegalArgumentException("존재하지 않는 이름입니다.");
//                }
//        ));
//
//
//        return new UserDetailsImpl();
//    }
//}
