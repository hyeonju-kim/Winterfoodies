package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.entity.User;
import com.winterfoodies.winterfoodies_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findByUsername(username);
        if (foundUser == null) {
            throw new UsernameNotFoundException("해당 계정이 존재하지 않습니다.");
        }
        User user = new User(username, foundUser.getEmail());

        return new UserDetailsImpl(user);
    }
}
