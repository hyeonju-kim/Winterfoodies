package com.winterfoodies.winterfoodies_project.controller;

import com.winterfoodies.winterfoodies_project.dto.store.StoreRequestDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.user.LocationDto;
import com.winterfoodies.winterfoodies_project.entity.FavoriteStore;
import com.winterfoodies.winterfoodies_project.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MainPageController {

    private final UserService userService; //서비스 클래스를 직접 주입받지 말고, 서비스 인터페이스를 주입받자

    // 1. 메인페이지 (나와 가장 가까운 가게 - 가게명만)
    @GetMapping("/main")
    @ApiOperation(value = "메인페이지(나와 가장 가까운 가게 - 가게명만)")
    public List<StoreResponseDto> mainPage() {
        return userService.getNearbyStores();
    }

    // 2. 메뉴별, 가까운순별 가게목록 - 가게명, 위치, 평점
    @GetMapping("/main/{productId}/near")
    @ApiOperation(value = "메뉴별, 가까운순별 가게목록")
    public List<StoreResponseDto> nearbyStoreList(@PathVariable("productId") Long productId) {
        return userService.getNearbyStores2(productId);
    }

    // 3. 메뉴별, 인기순(판매순)별 가게목록
    @GetMapping("/main/{productId}/popular")
    @ApiOperation(value = "메뉴별, 인기순별 가게목록")
    public List<StoreResponseDto> popularList(@PathVariable("productId") Long productId) {
        return userService.getStoresSortedByMenuSales(productId);
    }

    // 4. 메뉴별, 리뷰순별 가게목록
    @GetMapping("/main/{productId}/reviewstore")
    @ApiOperation(value = "메뉴별, 리뷰순별 가게목록")
    public List<StoreResponseDto> reviewList(@PathVariable("productId") Long productId) {
        return userService.getStoresSortedByReiviews(productId);
    }

    // 5. 가게 상세 조회 (메뉴)
    @GetMapping("/main/{storeId}")
    @ApiOperation(value = "가게 상세 조회(메뉴)")
    public void storeDetail() {
        return;
    }

    // 6. 가게 상세 조회 (가게정보)
    @GetMapping("/main/{storeId}/info")
    @ApiOperation(value = "가게 상세 조회(가게정보)")
    public void storeDetailInfo() {
        return;
    }

    // 7. 가게 상세 조회(리뷰)
    @GetMapping("/main/{storeId}/review")
    @ApiOperation(value = "가게 상세 조회(리뷰)")
    public void storeDetailReview() {
        return;
    }

    // 8. 가게 찜하기
    @PostMapping("/main/{storeId}/like")
    @ApiOperation(value = "가게 찜하기")
    public FavoriteStore addFavoriteStore(@RequestParam("userId") Long userId, @PathVariable("storeId") Long storeId) {
        return userService.addFavoriteStore(userId, storeId);
    }

}
