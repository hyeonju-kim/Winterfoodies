package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.product.ProductResponseDto;
import com.winterfoodies.winterfoodies_project.dto.review.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreMainDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;

import java.util.List;
import java.util.Map;

public interface MainPageService {
    // 상품명 목록만 제공하는 API
    public List<ProductResponseDto> getProductList();

    // 메인 페이지 - 위치 정보 가져오기
    public StoreMainDto getNearbyStores(double latitude, double longitude) throws IllegalAccessException;

    // 메뉴별, 가까운순별 가게목록 - 가게명, 위치, 평점
    public StoreMainDto getNearbyStores2(Long productId, double latitude, double longitude);

    // 메뉴별, 인기순(판매순)별 가게목록
    public List<StoreResponseDto> getStoresSortedByMenuSales(Long productId, double latitude, double longitude);

    // 메뉴별, 리뷰순 가게목록
    public List<StoreResponseDto> getStoresSortedByReiviews(Long productId, double latitude, double longitude);

    // 별점 순 가게목록 - 230830 추가
    public List<StoreResponseDto> getStoreByAverageRating(Long productId, double latitude, double longitude);

    //  가게 상세 조회 (메뉴 및 인기간식 및 가게정보)
    StoreMainDto getStoreProducts(Long storeId);

//    // 가게 상세 조회 (가게정보)
//    StoreResponseDto getStoreDetails(Long storeId);

    // 가게 상세 조회(리뷰)
    StoreMainDto getStoreReviews(Long storeId);

    // 가게 찜하기
    UserResponseDto addFavoriteStore(Long storeId);

    // 가게 찜하기 취소
    UserResponseDto revokeFavoriteStore(Long storeId);
}
