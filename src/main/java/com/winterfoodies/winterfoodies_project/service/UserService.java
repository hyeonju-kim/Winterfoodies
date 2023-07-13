package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.cart.CartDto;
import com.winterfoodies.winterfoodies_project.dto.order.OrderRequestDto;
import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.review.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreMainDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.user.*;
import com.winterfoodies.winterfoodies_project.entity.*;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface UserService { // 인터페이스 메소드 명세를 만들고 가져다 쓰자!
    // 회원가입
    public void signUp(UserRequestDto userRequestDto);


    //마이페이지 내정보 조회
    public UserDto retrieveUser();

    //유저 비밀번호 변경
    public UserDto changePw(UserDto userDto);


    // 찜 한 가게 목록 조회
    public List<StoreResponseDto> getFavoriteStoresByUserId();

    // 주문한 가게 목록 조회
//    List<OrderProduct> getOrderProductByUserId(Long userId);

    // 리뷰 쓴 가게 목록 조회
    List<ReviewDto> getReview();

    // 주문한 가게 목록 조회
    List<OrderResponseDto> getOrderByUserId();

    // 리뷰 삭제
    public UserDto delReviewByUserId(Long reviewId);

    // 리뷰 작성
    ReviewDto postReview(ReviewDto reviewDto);

    // 환경설정
    Configuration getConfig();

    // ################메인페이지##################
    // 메인 페이지 - 위치 정보 가져오기
    public List<StoreResponseDto> getNearbyStores();

    // 메뉴별, 가까운순별 가게목록 - 가게명, 위치, 평점
    public List<StoreResponseDto> getNearbyStores2(Long productId);

    // 메뉴별, 인기순(판매순)별 가게목록
    public List<StoreResponseDto> getStoresSortedByMenuSales(Long productId);

    // 메뉴별, 리뷰순 가게목록
    public List<StoreResponseDto> getStoresSortedByReiviews(Long productId);


    // 상호명 검색
    List<StoreResponseDto> searchStores(String keyword);

    // 지도로 근처 가게 검색
    List<StoreResponseDto> searchStoresByAddressNo(String addressNo);


    //  가게 상세 조회 (메뉴 및 인기간식)
    public StoreMainDto getStoreProducts(Long storeId);

    // 가게 상세 조회 (가게정보)
    public StoreResponseDto getStoreDetails(Long storeId);

    // 가게 상세 조회(리뷰)
    public List<ReviewDto> getStoreReviews(Long storeId);

    // 가게 찜하기
    public UserResponseDto  addFavoriteStore(Long storeId);

    // 장바구니에 상품 추가
//    public UserResponseDto addProductToCart(Long cartId, Long productId, int quantity);
    public String addProductToCart(@RequestParam Long productId, @RequestParam Long quantity, HttpServletRequest request, HttpServletResponse response);


   // 장바구니 상품 목록 조회
    public List<CartDto> getCartProduct(HttpServletRequest request);

    // 장바구니 특정 상품 삭제
    public String removeProductFromCart(@RequestParam Long productId, HttpServletRequest request, HttpServletResponse response);

        // 장바구니 초기화
    public String clearCart(HttpServletResponse response);

    // 주문완료 페이지 조회
    public OrderResponseDto getOrderConfirmPage(OrderRequestDto orderRequestDto, HttpServletRequest request);

}


