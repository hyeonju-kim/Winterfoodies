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
    private final StoreDetailRepository storeDetailRepository;
    private final StoreProductRepository storeProductRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;


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
//    public Long getUserId() {
//        User foundUser = userRepository.findByUsername(getUsernameFromAuthentication());
//        if (foundUser != null) {
//            return foundUser.getId();
//        } else {
//            // 사용자를 찾지 못하거나 인증되지 않은 경우에 대한 처리
//            throw new RequestException(new ErrorBox("사용자를 찾을 수 없거나 인증되지 않았습니다."));
//        }
//    }

    public Long getUserId() {
        User foundUser = userRepository.findByUsername(getUsernameFromAuthentication());
        return foundUser.getId();
    }



    // 1. 메인페이지 - 나와 가까운 가게 목록 보이기
    @Override
    public StoreMainDto getNearbyStores(double latitude, double longitude) {
        StoreMainDto storeMainDto = new StoreMainDto();

        // 1) 인기상품 (지금까지 판매된 상품중에 가장 많이 팔린 인기상품 랭킹) - 나중에 일주일 간격, 하루 간격으로 수정 예정
        ArrayList<ProductResponseDto> productList = new ArrayList<>();

        List<Product> top5PopularProducts = orderProductRepository.findTop5PopularProducts();
        for (Product top5PopularProduct : top5PopularProducts) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setProductName(top5PopularProduct.getName());
            productList.add(productResponseDto);
        }

        // 2) 상단의 상품목록 보이기//
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

        storeMainDto.setPopularProductsDtoList(productList);
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

        Store foundStore = storeRepository.getStoreById(storeId);
        Optional<StoreDetail> optionalStoreDetail = storeDetailRepository.findById(storeId);
        StoreDetail storeDetail = optionalStoreDetail.get();

        storeMainDto.setThumbNailImgUrl(foundStore.getStoreDetail().getThumbnailImgUrl());

        // 1) 가게 메뉴 담는 리스트 만들기
        List<Product> productsList = storeDetail.getProductsList();
        System.out.println("==========" + productsList);

        List<ProductResponseDto> storeProductDtoList = new ArrayList<>();

        for (Product product : productsList) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setProductName(product.getName());
            productResponseDto.setId(product.getId());
            productResponseDto.setPrice(product.getPrice());
            storeProductDtoList.add(productResponseDto);
        }

        // 2) 인기 메뉴 리스트 만들기  (판매량 순으로 내림차순)

        List<Product> popularProductsByStoreIdList = orderProductRepository.findTop5PopularProductsByStoreId(storeId);

        List<ProductResponseDto> popularProductsDtoList = new ArrayList<>();

        for (Product product : popularProductsByStoreIdList) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setProductName(product.getName());
            productResponseDto.setId(product.getId());

            popularProductsDtoList.add(productResponseDto);
        }

        storeMainDto.setReviewCnt(reviewRepository.countByStoreId(storeId));
        storeMainDto.setStoreName(foundStore.getStoreDetail().getName());
        storeMainDto.setAverageRating(foundStore.getStoreDetail().getAverageRating());

        // 토큰이 있으면 가게 좋아요표시(채워진하트) 나타나지 않고, 토큰이 있으면(로그인하면) 좋아요표시 나타나도록! - 230909추가

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication == " + authentication);
        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.getCredentials());
        System.out.println(authentication.getDetails());

        // 사용자가 로그인한 경우에는 찜여부를 보여주고, 로그인하지 않은 경우에는 항상 찜하지 않은 상태 - 안되네 진짜 미치겠네 ㅡㅡ;;;;;;;;;
        if (authentication.getPrincipal() instanceof User) {
            // 사용자가 로그인한 경우
            String username = authentication.getName();
            System.out.println("username == ? " + username);
            User byUsername = userRepository.findByUsername(username);
            List<FavoriteStore> favoriteStoreList = favoriteStoreRepository.findByUserId(getUserId());
            for (FavoriteStore favoriteStore : favoriteStoreList) {
                if (Objects.equals(favoriteStore.getStoreId(), storeId)) {
                    storeMainDto.setLike("Y");
                }
            }
        } else {
            // 사용자가 로그인하지 않은 경우
            storeMainDto.setLike("N");
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

        storeMainDto.setPopularProductsDtoList(popularProductsDtoList); // 인기메뉴
        storeMainDto.setProductResponseDtoList(storeProductDtoList); // 메뉴
        storeMainDto.setStoreResponseDto(storeResponseDto); // 가게정보

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
    public StoreMainDto getStoreReviews(Long storeId) {
        StoreMainDto storeMainDto = new StoreMainDto();

        List<Review> reviewList = reviewRepository.findByStoreId(storeId);
        Optional<Store> optionalStore = storeRepository.findById(storeId);
        Store store = optionalStore.get();
        storeMainDto.setStoreName(store.getStoreDetail().getName());
        storeMainDto.setThumbNailImgUrl(store.getStoreDetail().getThumbnailImgUrl());

        Long reviewCnt = reviewRepository.countByStoreId(storeId);
        storeMainDto.setReviewCnt(reviewCnt);

        List<ReviewDto> reviewDtoList = new ArrayList<>();

        for (Review review : reviewList) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setId(review.getId());
            Long userId = review.getUserId();
            reviewDto.setUserId(userId);
            Optional<User> userOptional = userRepository.findById(userId);
            User user = userOptional.get();

            // 주문한 음식 리스트로 나타내기
            List<OrderProduct> orderProducts = review.getOrder().getOrderProducts();
            ArrayList<String> orderedProducts = new ArrayList<>();

            for (OrderProduct orderProduct : orderProducts) {
                Long productId = orderProduct.getProduct().getId();
                Optional<Product> optionalProduct = productRepository.findById(productId);
                Product product = optionalProduct.get();
                orderedProducts.add(product.getName());
            }
            reviewDto.setNickname(user.getNickname());
            reviewDto.setRating(review.getRating());
//            reviewDto.setPhoto(review.getPhoto());
            reviewDto.setImages(review.getImages());
            reviewDto.setContent(review.getContent());
            reviewDto.setReviewTime(review.getCreatedAt()); // getTimeStamp라고 해서 안나왔었음
            reviewDto.setOrderedProducts(orderedProducts);

            reviewDtoList.add(reviewDto);
        }
        storeMainDto.setReviewDtoList(reviewDtoList);
        return storeMainDto;

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
