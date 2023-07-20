package com.winterfoodies.winterfoodies_project.controller;

import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.review.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserRequestDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
import com.winterfoodies.winterfoodies_project.entity.*;
import com.winterfoodies.winterfoodies_project.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MyPageController {
    private final UserService userService; //서비스 클래스를 직접 주입받지 말고, 서비스 인터페이스를 주입받자!

    // ################################################## 1. 마이페이지 ##################################################
    // 마이페이지 메인 화면(목록 조회)
    @GetMapping("/mypage") // 테스트용
    @ApiOperation(value = "메인화면 조회")
    public ResponseEntity<String> getMyPageList() { // 반환값 List로 바꾸기
        return ResponseEntity.ok("굿~~");
    }

    // ***************** 1-1. 내정보 *****************
    // 마이페이지 내정보 조회
    @GetMapping("/mypage/info")
    @ApiOperation(value = "마이페이지 내정보 조회")
    public UserResponseDto getUser() {
        UserDto userDto = userService.retrieveUser();
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
    @PutMapping("/mypage/info/pw")
    @ApiOperation(value = "마이페이지 내정보 비밀번호 변경")
    public UserResponseDto changePw(@RequestBody UserRequestDto userRequestDto) {
        UserDto userDto = new UserDto(userRequestDto);
        UserDto updatedUserDto = userService.changePw(userDto);
        return updatedUserDto.convertToUserResponseDto();
    }

    // ***************** 1-2. 찜 *****************
    // 찜한 가게 목록 조회
    @GetMapping("/mypage/like")
    @ApiOperation(value = "찜한 가게목록 조회")
    public List<StoreResponseDto> getFavoriteStoresByUserId() {
        return userService.getFavoriteStoresByUserId();
    }

    // ***************** 1-3. 리뷰관리 *****************
    // 내가 쓴 리뷰 목록 조회
    @GetMapping("/mypage/review")
    @ApiOperation(value = "작성한 리뷰 조회")
    public List<ReviewDto> getMyReviews(){
        return userService.getReview();
    }

    // 리뷰 삭제
    @DeleteMapping("/mypage/review/{reviewId}")
    @ApiOperation(value = "리뷰 삭제")
    public UserResponseDto delReview(@PathVariable("reviewId") Long reviewId) {
        UserDto userDto = userService.delReviewByUserId(reviewId);
        return userDto.convertToUserResponseDto();
    }

    // ***************** 1-4. 주문내역 *****************
    // 내가 주문한 주문목록 조회
    @GetMapping("/mypage/orderlist")
    @ApiOperation(value = "주문내역 조회")
    public List<OrderResponseDto> getMyOrders() {
        return userService.getOrderByUserId();
    }

    // 리뷰 작성
    @PostMapping("/mypage/orderlist/reviewpost")
    @ApiOperation(value = "리뷰 작성")
    public ReviewDto reviewPost(@RequestBody ReviewDto reviewDto) { // 반환값 orderDto
        return userService.postReview(reviewDto);
    }

    // ***************** 1-5. 환경설정 및 공지사항 *****************
    @GetMapping("/mypage/config")
    @ApiOperation(value = "환경설정 및 공지사항")
    public Configuration config() {
        return userService.getConfig();
    }

    ///////////////////////////////////////////////////////////////////////////

    // ********** 4. 장바구니 **********
    // 장바구니 페이지
    @GetMapping("/basket")
    @ApiOperation(value = "장바구니 페이지")
    public void basket() {
        return;
    }

    // 주문완료 페이지
    @GetMapping("/orderconfirm")
    @ApiOperation(value = "주문 완료")
    public void orderConfirm() {
        return;
    }
}
