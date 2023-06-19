package com.winterfoodies.winterfoodies_project.controller;

import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.user.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
import com.winterfoodies.winterfoodies_project.entity.*;
import com.winterfoodies.winterfoodies_project.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService; //서비스 클래스를 직접 주입받지 말고, 서비스 인터페이스를 주입받자!

    // ********** 1. 마이페이지 **********
    // 마이페이지 메인 화면(목록 조회)
    @GetMapping("/mypage")
    @ApiOperation(value = "메인화면 조회")
    public void getMyPageList() { // 반환값 List로 바꾸기
        return;
    }

    // ***** 1-1. 내정보 *****
    // 마이페이지 내정보 조회
    @GetMapping("/mypage/info")
    @ApiOperation(value = "마이페이지 내정보 조회")
    public UserDto getUser() {
        return userService.retrieveUser();
    }

    // 비밀번호 변경
    @PutMapping("/mypage/info/pw")
    @ApiOperation(value = "마아페이지 내정보 비밀번호 변경")
    public UserDto changePw(@RequestBody UserDto userDto) {
        return userService.changePw(userDto);
    }

    // ***** 1-2. 찜 *****
    // 찜한 가게 목록 조회
    @GetMapping("/mypage/like")
    @ApiOperation(value = "찜한 가게목록 조회")
    public List<StoreResponseDto> getFavoriteStoresByUserId(@RequestParam("userId") Long userId) {
        return userService.getFavoriteStoresByUserId(userId);
    }

    // ***** 1-3. 리뷰관리 *****
    // 내가 쓴 리뷰 목록 조회
    @GetMapping("/mypage/review")
    @ApiOperation(value = "작성한 리뷰 조회")
    public List<ReviewDto> getMyReviews(@RequestParam("userId") Long userId){
        return userService.getReview(userId);
    }

    // 리뷰 삭제 추가
    @DeleteMapping("/mypage/review/{reviewId}")
    @ApiOperation(value = "리뷰 삭제")
    public UserDto delReview(@PathVariable("reviewId") Long reviewId) {
        return userService.delReviewByUserId(reviewId);
    }

    // ***** 1-4. 주문내역 *****
    // 내가 주문한 주문목록 조회
    @GetMapping("/mypage/orderlist")
    @ApiOperation(value = "주문내역 조회")
    public List<OrderResponseDto> getMyOrders(@RequestParam("userId") Long userId) {
        return userService.getOrderByUserId(userId);
    }

    // 리뷰 작성
    @PostMapping("/mypage/orderlist/reviewpost")
    @ApiOperation(value = "리뷰 작성")
    public ReviewDto reviewPost(@RequestBody ReviewDto reviewDto) { // 반환값 orderDto
        return userService.postReview(reviewDto);
    }

    // ***** 1-5. 환경설정 및 공지사항 *****
    @GetMapping("/mypage/config")
    @ApiOperation(value = "환경설정 및 공지사항")
    public Configuration config() {
        return userService.getConfig();
    }



    // ********** 2. 메인페이지 **********
    // 메인페이지 (나와 가장 가까운 간식 , 인기 간식 랭킹)
    @GetMapping("/main")
    @ApiOperation(value = "메인페이지(나와 가장 가까운 간식, 인기 간식 랭킹)")
    public void mainPage() {
        return;
    }

    // 메뉴별, 가까운순별 가게목록
    @GetMapping("/main/{menuId}")
    @ApiOperation(value = "메뉴별, 가까운순별 가게목록")
    public void shortList() {
        return;
    }

    // 메뉴별, 인기순별 가게목록
    @GetMapping("/main/{menuId}/popular")
    @ApiOperation(value = "메뉴별, 인기순별 가게목록")
    public void popularList() {
        return;
    }

    // 메뉴별, 리뷰순별 가게목록
    @GetMapping("/main/{menuId}/review")
    @ApiOperation(value = "메뉴별, 리뷰순별 가게목록")
    public void reviewList() {
        return;
    }

    // 가게 상세 조회 (메뉴)
    @GetMapping("/main/{storeId}")
    @ApiOperation(value = "가게 상세 조회(메뉴)")
    public void storeDetail() {
        return;
    }

    // 가게 상세 조회 (가게정보)
    @GetMapping("/main/{storeId}/info")
    @ApiOperation(value = "가게 상세 조회(가게정보)")
    public void storeDetailInfo() {
        return;
    }

    // 가게 상세 조회(리뷰)
    @GetMapping("/main/{storeId}/review")
    @ApiOperation(value = "가게 상세 조회(리뷰)")
    public void storeDetailReview() {
        return;
    }

    // 가게 찜하기
    @PostMapping("/main/{storeId}/like")
    @ApiOperation(value = "가게 찜하기")
    public FavoriteStore addFavoriteStore(@RequestParam("userId") Long userId, @PathVariable("storeId") Long storeId) {
        return userService.addFavoriteStore(userId, storeId);
    }


    // ********** 3. 검색 **********
    // 상호명 검색
    @GetMapping("/search")
    @ApiOperation(value = "상호명 검색")
    public void search() {
        return;
    }

    // 지도 검색
    @GetMapping("/search/map")
    @ApiOperation(value = "지도로 검색")
    public void searchMap() {
        return;
    }

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
