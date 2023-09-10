    package com.winterfoodies.winterfoodies_project.controller;

    import com.sun.xml.bind.v2.TODO;
    import com.winterfoodies.winterfoodies_project.ErrorBox;
    import com.winterfoodies.winterfoodies_project.config.JwtUtil;
    import com.winterfoodies.winterfoodies_project.dto.auth.LoginRequestDto;
    import com.winterfoodies.winterfoodies_project.dto.auth.LoginSuccessResponseDto;
    import com.winterfoodies.winterfoodies_project.dto.user.TokenRequestDto;
    import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
    import com.winterfoodies.winterfoodies_project.dto.user.UserRequestDto;
    import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
    import com.winterfoodies.winterfoodies_project.entity.User;
    import com.winterfoodies.winterfoodies_project.exception.RequestException;
    import com.winterfoodies.winterfoodies_project.exception.UserException;
    import com.winterfoodies.winterfoodies_project.repository.UserRepository;
    import com.winterfoodies.winterfoodies_project.service.AuthService;
    import com.winterfoodies.winterfoodies_project.service.UserDetailsServiceImpl;
    import com.winterfoodies.winterfoodies_project.service.UserServiceImpl;
    import io.netty.buffer.ByteBuf;
    import io.netty.buffer.Unpooled;
    import io.netty.handler.codec.base64.Base64;
    import io.netty.util.CharsetUtil;
    import io.swagger.annotations.*;
    import io.swagger.v3.oas.annotations.media.ExampleObject;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.jasypt.encryption.StringEncryptor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.data.redis.core.RedisTemplate;
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
    import java.nio.charset.StandardCharsets;
    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.Date;
    import java.util.List;
    import java.util.Map;
    import java.util.Optional;
    import java.util.concurrent.TimeUnit;

    import static io.netty.handler.codec.base64.Base64.*;
    import static java.util.Base64.getDecoder;


    @CrossOrigin
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
        private final RedisTemplate redisTemplate;
        private final AuthService authService;



        // 로그인 - 기존방식
        @PostMapping("/loginTest")
        @ApiOperation(value = "로그인")
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
            String username = loginRequestDto.getUsername();
            User retrievedUser = userRepository.findByUsername(username);

            log.info("retrievedUser = {}", retrievedUser);

            if (retrievedUser == null) {
                throw new UserException("가입되지 않은 이메일입니다.", HttpStatus.BAD_REQUEST, null);
            }
            String nickname = retrievedUser.getNickname();
            String phoneNumber = retrievedUser.getPhoneNumber();


            // 조회한 비밀번호
            String foundPw = retrievedUser.getPassword();
            log.info("foundPw = {}", foundPw);

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
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails); // 230814 추가


            // 230814 추가 - redis에 refresh 토큰 저장하기!
            // RT:나나(key) / 23jijiofj2io3hi32hiongiodsninioda(value) 형태로

            Date refreshTokenExpiration = jwtUtil.getExpirationDate(refreshToken);
            long remainingTimeInMillis = refreshTokenExpiration.getTime() - System.currentTimeMillis();// 만료까지 남은시간

            redisTemplate.opsForValue().set("RT:" + username, refreshToken, remainingTimeInMillis, TimeUnit.MILLISECONDS);
            System.out.println("key==========RT:" + username);
            System.out.println("refreshToken===========" + refreshToken);


            // 4. 생성된 토큰을 응답
            LoginSuccessResponseDto loginSuccessResponseDto = new LoginSuccessResponseDto(accessToken);
//            UserResponseDto userResponseDto = new UserResponseDto(username, nickname, phoneNumber);
//            loginSuccessResponseDto.setUserResponseDto(userResponseDto);
            return ResponseEntity.ok(loginSuccessResponseDto);
        }

        // 클라에서 인코딩 후 백에서 디코딩하는 방식의 로그인 (보안 강화) - 230910 추가
        @PostMapping("/login")
        public ResponseEntity<LoginSuccessResponseDto> loginBasic(HttpServletRequest request) {
            // "Authorization" 헤더 값을 가져옵니다.
            String authorizationHeader = request.getHeader("Authorization");

            String username = null;
            String password;

            if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
                // "Basic " 이후의 문자열을 추출합니다.
                String base64Credentials = authorizationHeader.substring(6);

                // Base64로 인코딩된 문자열을 디코딩합니다.
                byte[] decodedBytes = getDecoder().decode(base64Credentials);


                // 디코딩된 바이트 배열을 문자열로 변환합니다.
                String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

                // 사용자 이름과 비밀번호를 ":"로 분리하여 배열에 담아 반환합니다.
                String[] credentials = decodedString.split(":");
                username = credentials[0];
                password = credentials[1];

                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
                User retrievedUser = userRepository.findByUsername(username);

                log.info("retrievedUser = {}", retrievedUser);

                if (retrievedUser == null) {
                    throw new UserException("가입되지 않은 이메일입니다.", HttpStatus.BAD_REQUEST, null);
                }

                // 조회한 비밀번호
                String foundPw = retrievedUser.getPassword();
                log.info("foundPw = {}", foundPw);

                //비밀번호 같은지 여부 파악
                if (!encoder.matches(password, foundPw)) {
                    throw new UserException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST, null);
                }


            }
            // 2. 인증 성공(회원저장소에 해당 이름이 있으면) 후 인증된 user의 정보를 갖고옴
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);


            // 3. subject, claim 모두 UserDetails를 사용하므로 객체를 그대로 전달
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails); // 230814 추가


            Date refreshTokenExpiration = jwtUtil.getExpirationDate(refreshToken);
            long remainingTimeInMillis = refreshTokenExpiration.getTime() - System.currentTimeMillis();// 만료까지 남은시간

            redisTemplate.opsForValue().set("RT:" + username, refreshToken, remainingTimeInMillis, TimeUnit.MILLISECONDS);
            System.out.println("key==========RT:" + username);
            System.out.println("refreshToken===========" + refreshToken);


            // 4. 생성된 토큰을 응답
            LoginSuccessResponseDto loginSuccessResponseDto = new LoginSuccessResponseDto(accessToken);
            return ResponseEntity.ok(loginSuccessResponseDto);

        }



        // 로그인 후, 유저네임/닉네임/휴대폰번호 조회
        @GetMapping("/me")
        @ApiOperation(value = "로그인 후 개인정보 조회")
        public UserResponseDto personalInfo() {
            return userService.personalInfo();
        }


        // 회원가입
        @PostMapping("/signup")
        @ApiOperation(value = "회원가입")
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
//            userResponseDto.setMessage("회원가입이 완료되었습니다.");
            return ResponseEntity.ok(userResponseDto);
        }

        // 유저네임(이메일) 중복확인 - 230901 추가
        @GetMapping("/check-username/{username}")
        @ApiOperation(value = "유저네임(이메일) 중복확인")
        @ApiImplicitParam(name = "username", value = "유저네임(이메일)")
        public ResponseEntity<UserResponseDto> checkUsername(@PathVariable String username) {
            boolean isUsernameUnique = userService.isUsernameUnique(username);
            if (isUsernameUnique) {
                return ResponseEntity.ok(new UserResponseDto("이미 사용 중인 이메일 주소입니다.", "error"));
            }else {
                return ResponseEntity.ok(new UserResponseDto("사용 가능한 이메일 주소 입니다.", "success"));
            }
        }

        // 비밀번호 찾기 - 230907 추가
        @PostMapping("/forgot-password")
        public ResponseEntity<UserResponseDto> sendForgotPasswordEmail(@RequestBody UserRequestDto userRequestDto){
            // 임시 유저 정보 생성하고 메일 발송
            boolean authInfo = authService.saveTempAuthInfo(userRequestDto.getUsername());
            return ResponseEntity.ok(new UserResponseDto("메일을 발송 했습니다.", "success"));
        }

//
//        // 닉네임 중복확인 - 230901 추가
//        @GetMapping("/check-nickname/{nickname}")
//        @ApiOperation(value = "닉네임 중복확인")
//        @ApiImplicitParam(name = "nickname", value = "닉네임")
//        public ResponseEntity<Boolean> checkNickname(@PathVariable String nickname) {
//            return ResponseEntity.ok(userService.isNicknameUnique(nickname));
//        }

//        @ExceptionHandler(RequestException.class)
//        public ErrorBox requestException(RequestException requestException) {
//            return requestException.getErrorBox();
//        }

        // 로그아웃
        // 230814 추가 - Redis에서 로그인할때 저장했던 refresh토큰 삭제하고, access토큰을 다시 저장하기 (value값을 logout으로)
        @PostMapping("/logout")
        @ApiOperation(value = "로그아웃")
        public ResponseEntity<UserResponseDto> logout(@RequestBody TokenRequestDto tokenRequestDto) {

            //로그아웃 하고싶은 토큰이 유효한 지 먼저 검증하기
            String accessToken = tokenRequestDto.getAccessToken();
            if (jwtUtil.isTokenExpired(accessToken)) {
                throw new UserException("로그아웃 : 유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST, null);
            }


            // access토큰에서 username을 가져온다
            String usernameFromToken = jwtUtil.getUsernameFromToken(accessToken);
            System.out.println("토큰에서가져온이름==================" + usernameFromToken);

            String refreshToken = (String) redisTemplate.opsForValue().get("RT:" + usernameFromToken);

            // redis에서 해당 username으로 저장된 refresh토큰이 있는지 여부 확인 후에 있을 경우 삭제하기
            if (redisTemplate.opsForValue().get("RT:" + usernameFromToken) != null){
                // refresh token 삭제
                redisTemplate.delete("RT:" + usernameFromToken);
            }

            // 해당 access token 유효시간을 가지고 와서 블랙리스트에 저장하기
            Date accessTokenExpiration = jwtUtil.getExpirationDate(tokenRequestDto.getAccessToken());
            long remainingTimeInMillis = accessTokenExpiration.getTime() - System.currentTimeMillis();// 만료까지 남은시간
            System.out.println("accessTokenExpiration====" + accessTokenExpiration);
            System.out.println("remainingTimeInMillis====" + remainingTimeInMillis);

            redisTemplate.opsForValue().set(tokenRequestDto.getAccessToken(), "logout", remainingTimeInMillis, TimeUnit.MILLISECONDS);

            return ResponseEntity.ok(new UserResponseDto("로그아웃 되었습니다."));
        }
    }
