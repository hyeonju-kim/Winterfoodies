package com.winterfoodies.winterfoodies_project.controller;

import com.winterfoodies.winterfoodies_project.dto.store.StoreMainDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.review.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
import com.winterfoodies.winterfoodies_project.service.MainPageService;
import com.winterfoodies.winterfoodies_project.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/main")
public class MainPageController {
    private final MainPageService mainPageService; //서비스 클래스를 직접 주입받지 말고, 서비스 인터페이스를 주입받자

    // 1. 메인페이지 (나와 가장 가까운 가게 - 가게명만)
    @GetMapping
    @ApiOperation(value = "메인페이지(나와 가장 가까운 가게 - 가게명만)")
    public List<StoreResponseDto> mainPage() {
        return mainPageService.getNearbyStores();
    }

    // 2. 메뉴별, 가까운순별 가게목록 - 가게명, 위치, 평점
    @GetMapping("/{productId}/near")
    @ApiOperation(value = "메뉴별, 가까운순별 가게목록")
    public List<StoreResponseDto> nearbyStoreList(@PathVariable("productId") Long productId) {
        return mainPageService.getNearbyStores2(productId);
    }

    // 3. 메뉴별, 인기순(판매순)별 가게목록
    @GetMapping("/{productId}/popular")
    @ApiOperation(value = "메뉴별, 인기순별 가게목록")
    public List<StoreResponseDto> popularList(@PathVariable("productId") Long productId) {
        return mainPageService.getStoresSortedByMenuSales(productId);
    }

    // 4. 메뉴별, 리뷰순별 가게목록
    @GetMapping("/{productId}/reviewstore")
    @ApiOperation(value = "메뉴별, 리뷰순별 가게목록")
    public List<StoreResponseDto> reviewList(@PathVariable("productId") Long productId) {
        return mainPageService.getStoresSortedByReiviews(productId);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////


    // 5. 가게 상세 조회 (메뉴 및 인기메뉴)
    @GetMapping("/{storeId}")
    @ApiOperation(value = "가게 상세 조회(메뉴)")
    public StoreMainDto storeDetail(@PathVariable Long storeId) {
        return mainPageService.getStoreProducts(storeId);

    }

    // 6. 가게 상세 조회 (가게정보)
    @GetMapping("/{storeId}/info")
    @ApiOperation(value = "가게 상세 조회(가게정보)")
    public StoreResponseDto storeDetailInfo(@PathVariable Long storeId) {
        return mainPageService.getStoreDetails(storeId);
    }

    // 7. 가게 상세 조회(리뷰)
    @GetMapping("/{storeId}/review")
    @ApiOperation(value = "가게 상세 조회(리뷰)")
    public List<ReviewDto> storeDetailReview(@PathVariable Long storeId) {
        return mainPageService.getStoreReviews(storeId);
    }

    // 8. 가게 찜하기
    @PostMapping("/{storeId}/like")
    @ApiOperation(value = "가게 찜하기")
    public UserResponseDto addFavoriteStore(@PathVariable("storeId") Long storeId) {
        return mainPageService.addFavoriteStore(storeId);
    }

}
