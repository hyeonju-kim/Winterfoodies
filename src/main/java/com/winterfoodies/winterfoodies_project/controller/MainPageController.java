package com.winterfoodies.winterfoodies_project.controller;

import com.winterfoodies.winterfoodies_project.dto.product.ProductResponseDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreMainDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.review.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
import com.winterfoodies.winterfoodies_project.entity.Store;
import com.winterfoodies.winterfoodies_project.service.MainPageService;
import com.winterfoodies.winterfoodies_project.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/main")
public class MainPageController {
    private final MainPageService mainPageService; //서비스 클래스를 직접 주입받지 말고, 서비스 인터페이스를 주입받자

    // 상품명 목록만 제공하는 API
    @GetMapping("/productList")
    @ApiOperation(value = "상품명 모두 보여주기  -  토큰 XXXXXXXXXXXXXXX")
    public List<ProductResponseDto> storeList() {
        return mainPageService.getProductList();
    }

    // 1. 메인페이지 (나와 가장 가까운 가게 - 가게명만) - 로그인 하면 바로 보이는 화면 (2km 이내)
    @GetMapping
    @ApiOperation(value = "메인페이지(나와 가장 가까운 가게 - 가게명만) - 로그인 하면 바로 보이는 화면")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "latitude", value = "위도"),
            @ApiImplicitParam(name = "longitude", value = "경도"),
    })
    public StoreMainDto mainPage(@RequestParam(required = false) Double latitude,
                                                                          @RequestParam(required = false) Double longitude) {

       return mainPageService.getNearbyStores(latitude, longitude);
    }

    // 2. 메뉴별, 가까운순별 가게목록 - 가게명, 위치, 평점 (2km 이내)
    @GetMapping("/{productId}/near")
    @ApiOperation(value = "메뉴별, 가까운순별 가게목록")
    @ApiImplicitParam(name = "productId", value = "상품 아이디")
    public StoreMainDto nearbyStoreList(@PathVariable("productId") Long productId,
                                                  @RequestParam(required = false) Double latitude,
                                                  @RequestParam(required = false) Double longitude) {
        return mainPageService.getNearbyStores2(productId, latitude, longitude);
    }

    // 3. 메뉴별, 인기순(판매순)별 가게목록 (5km 이내)
    @GetMapping("/{productId}/popular")
    @ApiOperation(value = "메뉴별, 인기순(판매량순)별 가게목록")
    @ApiImplicitParam(name = "productId", value = "상품 아이디")
    public List<StoreResponseDto> popularList(@PathVariable("productId") Long productId,
                                              @RequestParam(required = false) Double latitude,
                                              @RequestParam(required = false) Double longitude) {
        return mainPageService.getStoresSortedByMenuSales(productId, latitude, longitude);
    }

    // 4. 메뉴별, 리뷰순별 가게목록 (5km 이내)
    @GetMapping("/{productId}/reviewstore")
    @ApiOperation(value = "메뉴별, 리뷰순별 가게목록")
    @ApiImplicitParam(name = "productId", value = "상품 아이디")
    public List<StoreResponseDto> reviewList(@PathVariable("productId") Long productId,
                                             @RequestParam(required = false) Double latitude,
                                             @RequestParam(required = false) Double longitude) {
        return mainPageService.getStoresSortedByReiviews(productId, latitude, longitude);
    }

    // 5. 메뉴별, 별점순 가게목록 (5km 이내)
    @GetMapping("/{productId}/averageRating")
    @ApiOperation(value = "메뉴별, 평점별 가게목록")
    @ApiImplicitParam(name = "productId", value = "상품 아이디")
    public List<StoreResponseDto> ratingList(@PathVariable("productId") Long productId,
                                             @RequestParam(required = false) Double latitude,
                                             @RequestParam(required = false) Double longitude) {
        return mainPageService.getStoreByAverageRating(productId, latitude, longitude);
    }


///////////////////////////////////////////////////////////////////////////////////////////////////


    // 6. 가게 상세 조회 (메뉴 및 인기메뉴, 가게정보)
    @GetMapping("/{storeId}")
    @ApiOperation(value = "가게 상세 조회(메뉴, 인기메뉴, 가게정보)")
    @ApiImplicitParam(name = "storeId", value = "가게 아이디")
    public StoreMainDto storeDetail(@PathVariable Long storeId) {
        return mainPageService.getStoreProducts(storeId);
    }

//    // 7. 가게 상세 조회 (가게정보)
//    @GetMapping("/{storeId}/info")
//    @ApiOperation(value = "가게 상세 조회(가게정보)")
//    @ApiImplicitParam(name = "storeId", value = "가게 아이디")
//    public StoreResponseDto storeDetailInfo(@PathVariable Long storeId) {
//        return mainPageService.getStoreDetails(storeId);
//    }

    // 8. 가게 상세 조회(리뷰)
    @GetMapping("/{storeId}/review")
    @ApiOperation(value = "가게 상세 조회(리뷰)  -  토큰 XXXXXXXXXXXXXXX")
    @ApiImplicitParam(name = "storeId", value = "가게 아이디")
    public List<ReviewDto> storeDetailReview(@PathVariable Long storeId) {
        return mainPageService.getStoreReviews(storeId);
    }

    //9. 가게 찜하기
    @PostMapping("/{storeId}/like")
    @ApiOperation(value = "가게 찜하기")
    @ApiImplicitParam(name = "storeId", value = "가게 아이디")
    public UserResponseDto addFavoriteStore(@PathVariable("storeId") Long storeId) {
        return mainPageService.addFavoriteStore(storeId);
    }

    // 10. 가게 찜하기 취소
    @PutMapping("/{storeId}/like")
    @ApiOperation(value = "가게 찜하기 취소")
    @ApiImplicitParam(name = "storeId", value = "가게 아이디")
    public UserResponseDto revokeFavoriteStore(@PathVariable("storeId") Long storeId) {
        return mainPageService.revokeFavoriteStore(storeId);
    }

}
