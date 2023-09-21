package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.review.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
import com.winterfoodies.winterfoodies_project.entity.Configuration;

import java.util.List;

public interface MypageService {
    //마이페이지 내정보 조회
    public UserDto retrieveUser();

    //유저 비밀번호 변경
    public UserDto changePw(UserDto userDto);

    // 찜 한 가게 목록 조회
    public List<StoreResponseDto> getFavoriteStoresByUserId();

    // 주문한 가게 목록 조회
//    List<OrderProduct> getOrderProductByUserId(double userId);

    // 리뷰 쓴 가게 목록 조회
    List<ReviewDto> getReview();

    // 주문한 가게 목록 조회
    List<List<OrderResponseDto>> getOrderByUserId();

    // 리뷰 삭제
    public UserDto delReviewByUserId(double reviewId);

    // 리뷰 작성
    List<ReviewDto> postReview(ReviewDto reviewDto);

    // 환경설정
    Configuration getConfig();
}
