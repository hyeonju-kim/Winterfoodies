package com.winterfoodies.winterfoodies_project.controller;

import com.winterfoodies.winterfoodies_project.ErrorBox;
import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.review.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserRequestDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
import com.winterfoodies.winterfoodies_project.entity.*;
import com.winterfoodies.winterfoodies_project.exception.RequestException;
import com.winterfoodies.winterfoodies_project.service.MainPageService;
import com.winterfoodies.winterfoodies_project.service.MypageService;
import com.winterfoodies.winterfoodies_project.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mypage")
@Slf4j
public class MyPageController {
    private final MypageService mypageService; //서비스 클래스를 직접 주입받지 말고, 서비스 인터페이스를 주입받자!
    // 마이페이지 메인 화면(목록 조회)
    @GetMapping // 테스트용
    @ApiOperation(value = "메인화면 조회")
    public ResponseEntity<String> getMyPageList() { // 반환값 List로 바꾸기
        return ResponseEntity.ok("굿~~");
    }

    // ***************** 1-1. 내정보 *****************
    // 마이페이지 내정보 조회
    @GetMapping("/info")
    @ApiOperation(value = "마이페이지 내정보 조회")
    public UserResponseDto getUser() {
        UserDto userDto = mypageService.retrieveUser();
        if (userDto != null) {
            UserResponseDto userResponseDto = userDto.convertToUserResponseDto();
            return userResponseDto;
        }else {
            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setMessage("존재하지 않는 이름입니다");
            return userResponseDto;
        }
    }

    // 230707
    // 비밀번호 변경
    @PutMapping("info/pw")
    @ApiOperation(value = "마이페이지 내정보 비밀번호 변경")
    public UserResponseDto changePw(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
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
        UserDto updatedUserDto = mypageService.changePw(userDto);
        updatedUserDto.setMessage("비밀번호 변경이 완료되었습니다.");
        return updatedUserDto.convertToUserResponseDto();
    }

//    @ExceptionHandler(RequestException.class)
//    public ErrorBox requestException(RequestException requestException) {
//        return requestException.getErrorBox();
//    }

    // ***************** 1-2. 찜 *****************
    // 찜한 가게 목록 조회
    @GetMapping("/likes")
    @ApiOperation(value = "찜한 가게목록 조회")
    public List<List<StoreResponseDto>> getFavoriteStoresByUserId() {
        return mypageService.getFavoriteStoresByUserId();
    }

    // ***************** 1-3. 리뷰관리 *****************
    // 내가 쓴 리뷰 목록 조회
    @GetMapping("/reviews")
    @ApiOperation(value = "작성한 리뷰 조회")
    public List<List<ReviewDto>> getMyReviews(){
        return mypageService.getReview();
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    @ApiOperation(value = "리뷰 삭제")
    public UserResponseDto delReview(@PathVariable("reviewId") Long reviewId) {
        UserDto userDto = mypageService.delReviewByUserId(reviewId);
        return userDto.convertToUserResponseDto();
    }

    // ***************** 1-4. 주문내역 *****************
    // 내가 주문한 주문목록 조회
    @GetMapping("/orderlist")
    @ApiOperation(value = "주문내역 조회")
    public List<List<OrderResponseDto>> getMyOrders() {
        return mypageService.getOrderByUserId();
    }

    // 리뷰 작성
    @PostMapping("/orderlist/reviews")
    @ApiOperation(value = "리뷰 작성")
    public ReviewDto reviewPost(@RequestBody ReviewDto reviewDto) { // 반환값 orderDto
        return mypageService.postReview(reviewDto);
    }

    // ***************** 1-5. 환경설정 및 공지사항 *****************
    @GetMapping("/config")
    @ApiOperation(value = "환경설정 및 공지사항")
    public Configuration config() {
        return mypageService.getConfig();
    }

}
