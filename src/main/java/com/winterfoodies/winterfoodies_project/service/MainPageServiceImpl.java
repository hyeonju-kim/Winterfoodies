package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.ErrorBox;
import com.winterfoodies.winterfoodies_project.dto.product.ProductDto;
import com.winterfoodies.winterfoodies_project.dto.product.ProductResponseDto;
import com.winterfoodies.winterfoodies_project.dto.review.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreMainDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
import com.winterfoodies.winterfoodies_project.entity.*;
import com.winterfoodies.winterfoodies_project.exception.RequestException;
import com.winterfoodies.winterfoodies_project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService{
    private final UserRepository userRepository;
    private final FavoriteStoreRepository favoriteStoreRepository;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final StoreProductRepository storeProductRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;


    // 0. 가게명 다 가져오기
    @Override
    public List<ProductResponseDto> getProductList() {
        List<Product> foundProduct = productRepository.findAll();
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();

        for (Product product : foundProduct) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setId(product.getId());
            productResponseDto.setProductName(product.getName());
            productResponseDtoList.add(productResponseDto);
        }


        return productResponseDtoList;
    }

    // jwt 토큰으로 현재 인증된 사용자의 Authentication 객체에서 이름 가져오기
    public String getUsernameFromAuthentication() {
        String username = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 인증된 사용자의 이름 가져오기
            username = authentication.getName();
        }
        return username;
    }

    // 인증된 사용자의 id 가져오기
    public Long getUserId() {
        User foundUser = userRepository.findByUsername(getUsernameFromAuthentication());
        return foundUser.getId();
    }


    // 1. 메인페이지 - 나와 가까운 가게 목록 보이기
    @Override
    public StoreMainDto getNearbyStores(double latitude, double longitude) {
        StoreMainDto storeMainDto = new StoreMainDto();

        // 1) 인기상품 (전체상품 중에 인기상품 랭킹)


        // 2) 상단의 상품목록 보이기
        List<Product> foundProduct = productRepository.findAll();
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();

        for (Product product : foundProduct) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setId(product.getId());
            productResponseDto.setProductName(product.getName());
            productResponseDtoList.add(productResponseDto);
        }

        // 3) 나와 가장 가까운 간식 top5
        double radius = 2.0; // 검색 반경 설정 (예: 2.0km)

        List<Store> nearbyStores = storeRepository.findNearbyStores(latitude, longitude, radius);
        List<StoreResponseDto> nearbyStoreDtoList = new ArrayList<>();

        for (Store store : nearbyStores) {
            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setStoreId(store.getId());
            storeResponseDto.setName(store.getStoreDetail().getName());
            storeResponseDto.setLatitude(store.getStoreDetail().getLatitude());
            storeResponseDto.setLongitude(store.getStoreDetail().getLongitude());
            storeResponseDto.setThumbNailImgUrl(store.getStoreDetail().getThumbnailImgUrl());
            nearbyStoreDtoList.add(storeResponseDto);
        }
        storeMainDto.setProductResponseDtoList(productResponseDtoList);
        storeMainDto.setStoreResponseDtoList(nearbyStoreDtoList);
        return storeMainDto;
    }

    // 2. 메뉴별, 가까운순별 가게목록 - 가게명, 위치, 평점
    @Override
    public StoreMainDto getNearbyStores2(Long productId, double latitude, double longitude) {

        StoreMainDto storeMainDto = new StoreMainDto();
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Product product = optionalProduct.get();
        storeMainDto.setProductName(product.getName());

        double radius = 2.0; // 검색 반경 설정 (2.0km)
        List<Store> nearbyStores = storeRepository.findNearbyStoresByProductId(productId, latitude, longitude, radius);
        ArrayList<StoreResponseDto> storeResponseDtoList = new ArrayList<>();

        for (Store store : nearbyStores) {
            List<StoreProduct> storeProducts = store.getStoreProducts();
            for (StoreProduct storeProduct : storeProducts) {
                if (Objects.equals(storeProduct.getProduct().getId(), productId)) {
                    StoreResponseDto storeResponseDto = new StoreResponseDto();
                    storeResponseDto.setStoreId(store.getId());
                    storeResponseDto.setName(store.getStoreDetail().getName());
                    storeResponseDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
                    storeResponseDto.setAverageRating(store.getStoreDetail().getAverageRating());
                    storeResponseDto.setLatitude(store.getStoreDetail().getLatitude());
                    storeResponseDto.setLongitude(store.getStoreDetail().getLongitude());
                    storeResponseDto.setThumbNailImgUrl(store.getStoreDetail().getThumbnailImgUrl());


                    storeResponseDtoList.add(storeResponseDto);
                }
            }
        }
        storeMainDto.setStoreResponseDtoList(storeResponseDtoList);
        return storeMainDto;
    }

    // 3. 메뉴별, 인기순(판매순)별 가게목록
    @Override
    public List<StoreResponseDto> getStoresSortedByMenuSales(Long productId, double latitude, double longitude) {
        double radius = 5.0; // 검색 반경 설정

        List<Store> storesSortedByMenuSales = storeRepository.getStoresSortedByMenuSalesByProductId(productId, latitude, longitude, radius);
        List<StoreResponseDto> storeBySalesStoreList = new ArrayList<>();
        for (Store store : storesSortedByMenuSales) {
            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setStoreId(store.getId());
            storeResponseDto.setName(store.getStoreDetail().getName());
            storeResponseDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
            storeResponseDto.setAverageRating(store.getStoreDetail().getAverageRating());
            Long countOrders = orderRepository.countByStoreId(store.getId());
            storeResponseDto.setOrders(countOrders);
            storeResponseDto.setLatitude(store.getStoreDetail().getLatitude());
            storeResponseDto.setLongitude(store.getStoreDetail().getLongitude());

            storeBySalesStoreList.add(storeResponseDto);
        }
        return storeBySalesStoreList;
    }

    // 4. 메뉴별, 리뷰순 가게목록
    @Override
    public List<StoreResponseDto> getStoresSortedByReiviews(Long productId, double latitude, double longitude) {
        double radius = 5.0; // 검색 반경 설정
        List<Store> storeByReviews = storeRepository.getStoreByReviewsByProductId(productId, latitude, longitude, radius);
        List<StoreResponseDto> storeByReiviewsStoreList = new ArrayList<>();

        for (Store store : storeByReviews) {
            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setStoreId(store.getId());
            storeResponseDto.setName(store.getStoreDetail().getName());
            storeResponseDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
            storeResponseDto.setAverageRating(store.getStoreDetail().getAverageRating());
            Long countReviews = reviewRepository.countByStoreId(store.getId());
            storeResponseDto.setCountReviews(countReviews);
            storeResponseDto.setLatitude(store.getStoreDetail().getLatitude());
            storeResponseDto.setLongitude(store.getStoreDetail().getLongitude());

            storeByReiviewsStoreList.add(storeResponseDto);
        }
        return storeByReiviewsStoreList;
    }

    // 5. 메뉴별, 별점순 가게목록 - 230830추가
    @Override
    public List<StoreResponseDto> getStoreByAverageRating(Long productId, double latitude, double longitude) {
        double radius = 5.0; // 검색 반경 설정
        List<Store> storeByAverageRating = storeRepository.getStoreByAverageRatingByProductId(productId, latitude, longitude, radius);
        List<StoreResponseDto> storeByAverageRatingList = new ArrayList<>();

        for (Store store : storeByAverageRating) {
            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setStoreId(store.getId());
            storeResponseDto.setName(store.getStoreDetail().getName());
            storeResponseDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
            storeResponseDto.setAverageRating(store.getStoreDetail().getAverageRating());
            storeResponseDto.setLatitude(store.getStoreDetail().getLatitude());
            storeResponseDto.setLongitude(store.getStoreDetail().getLongitude());

            storeByAverageRatingList.add(storeResponseDto);
        }
        return storeByAverageRatingList;
    }



    //  6. 가게 상세 조회 (메뉴 및 인기간식)
    @Override
    public StoreMainDto getStoreProducts(Long storeId) {
        StoreMainDto storeMainDto = new StoreMainDto();
        List<StoreProduct> storeProducts = storeProductRepository.findByStoreId(storeId);
        Store foundStore = storeRepository.getStoreById(storeId);
        storeMainDto.setThumbNailImgUrl(foundStore.getStoreDetail().getThumbnailImgUrl());

        // 1) 가게 메뉴 담는 리스트 만들기
        List<StoreProduct> storeProductList = storeProductRepository.findByStoreId(storeId);
        List<ProductResponseDto> storeProductDtoList = new ArrayList<>();


        for (StoreProduct storeProduct : storeProductList) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setId(storeProduct.getId());
            productResponseDto.setProductName(storeProduct.getProduct().getName());
            productResponseDto.setPrice(storeProduct.getProduct().getPrice());
            productResponseDto.setQuantity(1L);

            storeProductDtoList.add(productResponseDto);
        }

        // 2) 인기 메뉴 리스트 만들기

        List<ProductResponseDto> popularProductsDtoList = new ArrayList<>();

        for (StoreProduct storeProduct : storeProducts) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setProductName(storeProduct.getProduct().getName());
            productResponseDto.setId(storeProduct.getProduct().getId());

            popularProductsDtoList.add(productResponseDto);
        }

        storeMainDto.setReviewCnt(reviewRepository.countByStoreId(storeId));
        storeMainDto.setStoreName(foundStore.getStoreDetail().getName());
        storeMainDto.setAverageRating(foundStore.getStoreDetail().getAverageRating());
        List<FavoriteStore> favoriteStoreList = favoriteStoreRepository.findByUserId(getUserId());
        for (FavoriteStore favoriteStore : favoriteStoreList) {
            if (Objects.equals(favoriteStore.getStoreId(), storeId)) {
                storeMainDto.setLike("Y");
            }
        }

        // 3) 가게정보 만들기 추가 -230831
        Optional<Store> optionalStore = storeRepository.findById(storeId);
        Store store = optionalStore.get();
        StoreResponseDto storeResponseDto = new StoreResponseDto();
        storeResponseDto.setName(store.getStoreDetail().getName());
        storeResponseDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
        storeResponseDto.setDetailAddress(store.getStoreDetail().getDetailAddress());
        storeResponseDto.setOpenTime(store.getStoreDetail().getOpenTime());
        storeResponseDto.setCloseTime(store.getStoreDetail().getCloseTime());
        storeResponseDto.setLatitude(store.getStoreDetail().getLatitude());
        storeResponseDto.setLongitude(store.getStoreDetail().getLongitude());
        storeResponseDto.setInfo(store.getStoreDetail().getInfo());
        storeResponseDto.setEstimatedCookingTime(store.getStoreDetail().getEstimatedCookingTime());
        storeResponseDto.setStatus(store.getStoreDetail().getStatus());
        storeResponseDto.setOpenDate(store.getStoreDetail().getOpenDate());
        storeMainDto.setProductResponseDtoList(storeProductDtoList);
        storeMainDto.setPopularProductsDtoList(popularProductsDtoList);
        storeMainDto.setStoreResponseDto(storeResponseDto);

        return storeMainDto;
    }

//    // 7. 가게 상세 조회 (가게정보)
//    @Override
//    public StoreResponseDto getStoreDetails(Long storeId) {
//        Optional<Store> optionalStore = storeRepository.findById(storeId);
//        Store store = optionalStore.get();
//        StoreResponseDto storeResponseDto = new StoreResponseDto();
//        storeResponseDto.setName(store.getStoreDetail().getName());
//        storeResponseDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
//        storeResponseDto.setDetailAddress(store.getStoreDetail().getDetailAddress());
//        storeResponseDto.setOpenTime(store.getStoreDetail().getOpenTime());
//        storeResponseDto.setCloseTime(store.getStoreDetail().getCloseTime());
//        storeResponseDto.setLatitude(store.getStoreDetail().getLatitude());
//        storeResponseDto.setLongitude(store.getStoreDetail().getLongitude());
//        storeResponseDto.setInfo(store.getStoreDetail().getInfo());
//
//        return storeResponseDto;
//    }

    // 8. 가게 상세 조회(리뷰)
    @Override
    public List<ReviewDto> getStoreReviews(Long storeId) {

        List<Review> reviewList = reviewRepository.findByStoreId(storeId);
        List<ReviewDto> reviewDtoList = new ArrayList<>();

        for (Review review : reviewList) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setId(review.getId());
            reviewDto.setUserId(review.getUserId());
            reviewDto.setRating(review.getRating());
//            reviewDto.setPhoto(review.getPhoto());
            reviewDto.setContent(review.getContent());
            reviewDto.setTimestamp(review.getCreatedAt()); // getTimeStamp라고 해서 안나왔었음
            reviewDtoList.add(reviewDto);
        }
        return reviewDtoList;

    }

    // 9. 가게 찜하기
    @Override
    public UserResponseDto addFavoriteStore(Long storeId) {
        FavoriteStore favoriteStore = new FavoriteStore();
        favoriteStore.setUserId(getUserId());
        favoriteStore.setStoreId(storeId);
        favoriteStoreRepository.save(favoriteStore);

        UserResponseDto userDto = new UserResponseDto();
        userDto.setMessage("찜하기 등록!");
        return userDto;
    }

    // 10. 가게 찜하기 취소
    @Override
    public UserResponseDto revokeFavoriteStore(Long storeId) {

        // 사용자가 찜한 가게인지 확인
        Optional<FavoriteStore> favoriteStoreOptional = favoriteStoreRepository.findByUserIdAndStoreId(getUserId(), storeId);

        if (favoriteStoreOptional.isPresent()) {
            // 사용자가 찜한 가게인 경우 삭제
            favoriteStoreRepository.delete(favoriteStoreOptional.get());

            UserResponseDto userDto = new UserResponseDto();
            userDto.setMessage("찜하기 취소");
            return userDto;
        } else {
            throw new RequestException(new ErrorBox("자신이 찜한 가게만 취소할 수 있습니다."));
        }
    }

}
