    package com.winterfoodies.winterfoodies_project.controller;

    import com.winterfoodies.winterfoodies_project.ErrorBox;
    import com.winterfoodies.winterfoodies_project.config.JwtUtil;
    import com.winterfoodies.winterfoodies_project.dto.auth.LoginRequestDto;
    import com.winterfoodies.winterfoodies_project.dto.auth.LoginSuccessResponseDto;
    import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
    import com.winterfoodies.winterfoodies_project.dto.user.UserRequestDto;
    import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
    import com.winterfoodies.winterfoodies_project.exception.RequestException;
    import com.winterfoodies.winterfoodies_project.service.UserDetailsServiceImpl;
    import com.winterfoodies.winterfoodies_project.service.UserServiceImpl;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.validation.BindingResult;
    import org.springframework.validation.ObjectError;
    import org.springframework.web.bind.annotation.*;

    import javax.naming.Binding;
    import javax.validation.Valid;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.List;

    @RequiredArgsConstructor
    @RestController
    @Slf4j
    @RequestMapping("/api")
    public class HomeController {

        private final UserDetailsServiceImpl userDetailsService;
        private final UserServiceImpl userService;
        private final JwtUtil jwtUtil;
        private final AuthenticationManager authenticationManager;

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
                errorBox.setMessage("[ 고객 id ] : 해당 없음" + " | [ 에러 유형 ] : "+ code + " | [ 에러 시간 ] : " + dateTime + " | [ 에러메시지 ] : "+ message);
                log.error(errorBox.getMessage());
                throw new RequestException(errorBox);
            }

            // [230726] 추가
            // 1. username, password로 인증시도
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
            } catch (BadCredentialsException e) {
                throw new BadCredentialsException("아이디, 비밀번호를 확인해주세요");
            }

            // 2. 인증 성공(회원저장소에 해당 이름이 있으면) 후 인증된 user의 정보를 갖고옴
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.getUsername());

            // 3. subject, claim 모두 UserDetails를 사용하므로 객체를 그대로 전달
            String token = jwtUtil.generateToken(userDetails);

            // 4. 생성된 토큰을 응답
            return ResponseEntity.ok(new LoginSuccessResponseDto(token));
        }

        @ExceptionHandler(BadCredentialsException.class)
        public String requestException(BadCredentialsException badCredentialsException) {
            return badCredentialsException.getMessage();
        }


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
                errorBox.setMessage("[ 고객 id ] : 해당 없음" + " | [ 에러 유형 ] : "+ code + " | [ 에러 시간 ] : " + dateTime + " | [ 에러메시지 ] : "+ message);
                log.error(errorBox.getMessage());
                throw new RequestException(errorBox);
            }
            UserDto userDto = userService.signUp(userRequestDto);
            UserResponseDto userResponseDto = userDto.convertToUserResponseDto();
            userResponseDto.setMessage("회원가입이 완료되었습니다.");
            return ResponseEntity.ok(userResponseDto);
        }

        @ExceptionHandler(RequestException.class)
        public ErrorBox requestException(RequestException requestException) {
            return requestException.getErrorBox();
        }
    }
