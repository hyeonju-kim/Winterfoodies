    package com.winterfoodies.winterfoodies_project.controller;

    import com.winterfoodies.winterfoodies_project.ErrorBox;
    import com.winterfoodies.winterfoodies_project.config.JwtUtil;
    import com.winterfoodies.winterfoodies_project.dto.auth.LoginRequestDto;
    import com.winterfoodies.winterfoodies_project.dto.auth.LoginSuccessResponseDto;
    import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
    import com.winterfoodies.winterfoodies_project.dto.user.UserRequestDto;
    import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
    import com.winterfoodies.winterfoodies_project.entity.User;
    import com.winterfoodies.winterfoodies_project.exception.RequestException;
    import com.winterfoodies.winterfoodies_project.exception.UserException;
    import com.winterfoodies.winterfoodies_project.repository.UserRepository;
    import com.winterfoodies.winterfoodies_project.service.UserDetailsServiceImpl;
    import com.winterfoodies.winterfoodies_project.service.UserServiceImpl;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.validation.BindingResult;
    import org.springframework.validation.ObjectError;
    import org.springframework.web.bind.annotation.*;

    import javax.naming.Binding;
    import javax.servlet.http.HttpServletRequest;
    import javax.validation.Valid;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.List;
    import java.util.Optional;

    @RequiredArgsConstructor
    @RestController
    @Slf4j
    @RequestMapping("/api")
    public class HomeController {

        private final UserDetailsServiceImpl userDetailsService;
        private final UserServiceImpl userService;
        private final JwtUtil jwtUtil;
        private final AuthenticationManager authenticationManager;
        private final BCryptPasswordEncoder encoder;
        private final UserRepository userRepository;


        // 로그인
        @PostMapping("/login")
        public ResponseEntity<LoginSuccessResponseDto> loginTest(@Valid @RequestBody LoginRequestDto loginRequestDto, BindingResult bindingResult) {

            // [230726] 추가
            if (bindingResult.hasErrors()) {
                List<ObjectError> allErrors = bindingResult.getAllErrors();
                System.out.println("allErrors========== :  " + allErrors);
                String message = allErrors.get(0).getDefaultMessage();
                String code = allErrors.get(0).getCode();
                LocalDateTime now = LocalDateTime.now();
                String dateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                ErrorBox errorBox = new ErrorBox();
                errorBox.setCause(code);
                errorBox.setMessage("[ 고객 id ] : 해당 없음" + " | [ 에러 유형 ] : " + code + " | [ 에러 시간 ] : " + dateTime + " | [ 에러메시지 ] : " + message);
                log.error(errorBox.getMessage());
                throw new RequestException(errorBox);
            }

            String password = loginRequestDto.getPassword();
            String email = loginRequestDto.getEmail();
            Optional<User> retrievedUser = userRepository.findByEmail(email);
            retrievedUser.orElseThrow(() -> {
                return new UserException("가입되지 않은 이메일입니다.", HttpStatus.BAD_REQUEST, null);
            });

            // 조회한 비밀번호
            String foundPw = retrievedUser.get().getPassword();

            //비밀번호 같은지 여부 파악
            if (!encoder.matches(password, foundPw)) {
                throw new UserException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST, null);
            }

            // 1. username, password로 인증시도 -> 잘되다가 230810에 에러나서 일단 주석처리함....ㅜㅜ
//            try {
////                String encodedPassword = encoder.encode(loginRequestDto.getPassword()); //DD
////                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), encodedPassword));
//                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
//            } catch (BadCredentialsException e) {
//                System.out.println("비번=" + loginRequestDto.getPassword());
//                throw new BadCredentialsException("아이디, 비밀번호를 확인해주세요");
//            }


            // 2. 인증 성공(회원저장소에 해당 이름이 있으면) 후 인증된 user의 정보를 갖고옴
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.getUsername());

            // 3. subject, claim 모두 UserDetails를 사용하므로 객체를 그대로 전달
            String token = jwtUtil.generateToken(userDetails);

            // 4. 생성된 토큰을 응답
            return ResponseEntity.ok(new LoginSuccessResponseDto(token));
        }

//        @ExceptionHandler(BadCredentialsException.class)
//        public String requestException(BadCredentialsException badCredentialsException) {
//            return badCredentialsException.getMessage();
//        }


        // 회원가입
        @PostMapping("/signup")
        public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody UserRequestDto userRequestDto, BindingResult bindingResult) {
            if (bindingResult.hasErrors()) {
                List<ObjectError> allErrors = bindingResult.getAllErrors();
                System.out.println("allErrors========== :  " + allErrors);
                String message = allErrors.get(0).getDefaultMessage();
                String code = allErrors.get(0).getCode();
                LocalDateTime now = LocalDateTime.now();
                String dateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                ErrorBox errorBox = new ErrorBox();
                errorBox.setCause(code);
                errorBox.setMessage("[ 고객 id ] : 해당 없음" + " | [ 에러 유형 ] : " + code + " | [ 에러 시간 ] : " + dateTime + " | [ 에러메시지 ] : " + message);
                log.error(errorBox.getMessage());
                throw new RequestException(errorBox);
            }
            UserDto userDto = userService.signUp(userRequestDto);
            UserResponseDto userResponseDto = userDto.convertToUserResponseDto();
            userResponseDto.setMessage("회원가입이 완료되었습니다.");
            return ResponseEntity.ok(userResponseDto);
        }

//        @ExceptionHandler(RequestException.class)
//        public ErrorBox requestException(RequestException requestException) {
//            return requestException.getErrorBox();
//        }

        // 로그아웃
        @PostMapping("/logout")
        public ResponseEntity<UserResponseDto> logout(HttpServletRequest request) {
            String authorizationHeader = request.getHeader("Authorization");
            System.out.println(authorizationHeader);

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7); // "Bearer " 다음 부분을 추출
                System.out.println(token);

                // 토큰을 사용하여 JWT 무효화 처리 또는 관련 작업을 수행
                jwtUtil.setExpirationZero(token);
            }

            // Spring Security의 현재 인증 상태 비우기
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok(new UserResponseDto("로그아웃 되었습니다."));
        }
    }
