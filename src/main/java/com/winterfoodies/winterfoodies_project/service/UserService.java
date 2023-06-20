package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.user.LocationDto;
import com.winterfoodies.winterfoodies_project.dto.user.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
import com.winterfoodies.winterfoodies_project.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService { // 인터페이스 메소드 명세를 만들고 가져다 쓰자!
    //마이페이지 내정보 조회
    public UserDto retrieveUser();

    //유저 비밀번호 변경
    public UserDto changePw(UserDto userDto);

    // 가게 찜하기
    public FavoriteStore  addFavoriteStore(Long userId, Long storeId);

    // 찜 한 가게 목록 조회
    public List<StoreResponseDto> getFavoriteStoresByUserId(Long userId);

    // 주문한 가게 목록 조회
//    List<OrderProduct> getOrderProductByUserId(Long userId);

    // 리뷰 쓴 가게 목록 조회
    List<ReviewDto> getReview(Long userId);

    // 주문한 가게 목록 조회
    List<OrderResponseDto> getOrderByUserId(Long userId);

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

}
