package com.winterfoodies.winterfoodies_project.controller;

import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
import com.winterfoodies.winterfoodies_project.entity.Store;
import com.winterfoodies.winterfoodies_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    // ********** 1. 마이페이지 **********
    // 마이페이지 메인 화면(목록 조회)
    @GetMapping("/mypage")
    public void getMyPageList() { // 반환값 List로 바꾸기
        return;
    }

    // ***** 1-1. 내정보 *****
    // 마이페이지 내정보 조회
    @GetMapping("/mypage/info")
    public UserDto getUser() {
        return userService.retrieveUser();
    }

    // 비밀번호 변경
    @PutMapping("/mypage/info/pw")
    public UserDto changePw(@RequestBody UserDto userDto) {
        return userService.changePw(userDto);
    }

    // ***** 1-2. 찜 *****
    // 찜한 가게 목록 조회
    @GetMapping("/like")
    public List<Store> like() {
        return null;
    }

    // ***** 1-3. 리뷰관리 *****
    // 내가 쓴 리뷰 목록 조회
    @GetMapping("/review")
    public void myReview() { // TODO 반환값 List<Review> 로 수정
        return;
    }

    // 리뷰상세
    @GetMapping("/review/detail")
    public void myReviewDetail() { //반환값 ReviewDto
        return;
    }

    // ***** 1-4. 주문내역 *****
    // 내가 주문한 주문목록 조회
    @GetMapping("/order")
    public void order() { // 반환값 orderDto
        return;
    }

    // 주문 상세
    @GetMapping("/order/detail")
    public void orderDetail() { // 반환값 orderDto
        return;
    }

    // ********** 2. 메인페이지 **********
    // 메뉴별, 가까운순별 가게목록
    @GetMapping("/{id}/short")
    public void shortList() {
        return;
    }

    // 메뉴별, 인기순별 가게목록
    @GetMapping("/{id}/popular")
    public void popularList() {
        return;
    }

    // 메뉴별, 리뷰순별 가게목록
    @GetMapping("/{id}/review")
    public void reviewList() {
        return;
    }

    // 가게 상세 조회 (메뉴)
    @GetMapping("/store/detail/{storeId}")
    public void storeDetail() {
        return;
    }

    // 가게 상세 조회 (가게정보)
    @GetMapping("/store/detail/{storeId}/info")
    public void storeDetailInfo() {
        return;
    }

    // 가게 상세 조회(리뷰)
    @GetMapping("/store/detail/{storeId}/review")
    public void storeDetailReview() {
        return;
    }

    // 가게 상세 조회 (리뷰 - 상세)
    @GetMapping("/store/detail/{storeId}/review/detail")
    public void storeDetailReviewDetail() {
        return;
    }

    // ********** 3. 검색 **********
    // 상호명 검색
    @GetMapping("/search")
    public void search() {
        return;
    }

    // 지도 검색
    @GetMapping("/map")
    public void searchMap() {
        return;
    }

    // ********** 4. 장바구니 **********
    // 장바구니 페이지
    @GetMapping("/basket")
    public void basket() {
        return;
    }

    // 주문완료 페이지
    @GetMapping("/orderComplete")
    public void orderComplete() {
        return;
    }
}
