package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.cart.CartDto;
import com.winterfoodies.winterfoodies_project.dto.order.OrderRequestDto;
import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.product.ProductDto;
import com.winterfoodies.winterfoodies_project.dto.product.ProductResponseDto;
import com.winterfoodies.winterfoodies_project.dto.review.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreMainDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.user.*;
import com.winterfoodies.winterfoodies_project.entity.*;
import com.winterfoodies.winterfoodies_project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    //    private final UserDto loginUser; //ScenarioConfig에서 등록한 bean을 주입받아서 사용하기 -> jwt토큰 적용 후에는 필요x
    private final UserRepository userRepository;
    private final FavoriteStoreRepository favoriteStoreRepository;
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final StoreRepository storeRepository;
    private final StoreProductRepository storeProductRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;
    private final BCryptPasswordEncoder encoder;

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

    // ################################################################ 회원가입/로그인 ##############################################################
    // 회원가입
    @Override
    public UserDto signUp(UserRequestDto userRequestDto) {
        String encodedPassword = encoder.encode(userRequestDto.getPassword()); // 230726 추가
        userRequestDto.setPassword(encodedPassword);
        User user = new User(userRequestDto);
        userRepository.save(user);
        return new UserDto(user);
    }

    // ################################################################ 마이페이지 ##############################################################
    // 마이페이지 내정보
    @Override
    public UserDto retrieveUser() {
        User foundUser = userRepository.findByUsername(getUsernameFromAuthentication());
        if (foundUser != null) {  // respository에서 가져온건 꼭 분기처리 해야한다!!!!
            return new UserDto(foundUser);
        } else {

            UserDto notFoundUserDto = new UserDto();
            notFoundUserDto.setMessage("해당 유저를 찾을 수 없습니다.");
            return notFoundUserDto;
        }
    }

    // 마이페이지 비번 변경
    @Override
    public UserDto changePw(UserDto inUserDto) {
        User foundUser = userRepository.findByUsername(getUsernameFromAuthentication());
        if (foundUser != null) {
            foundUser.setPassword(inUserDto.getPassword());
            userRepository.save(foundUser);

            // Entity -> UserDto
            UserDto outUserDto = new UserDto(foundUser);
            outUserDto.setMessage("변경완료!!");
            return outUserDto;
        }

        UserDto notFoundOutUserDto = new UserDto();
        notFoundOutUserDto.setMessage("해당 유저를 찾을 수 없습니다.");
        return notFoundOutUserDto;
    }


    // 찜한 가게 목록 조회
    @Override
    public List<StoreResponseDto> getFavoriteStoresByUserId() {
        List<FavoriteStore> foundFavoriteStore = favoriteStoreRepository.findByUserId(getUserId());
        List<StoreResponseDto> storeResponseDtoList = new ArrayList<>();

        for (FavoriteStore favoriteStore : foundFavoriteStore) {
            Long foundStoreId = favoriteStore.getStoreId();
            Optional<Store> foundStore = storeRepository.findById(foundStoreId);

            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setName(foundStore.get().getStoreDetail().getName());
            storeResponseDto.setBasicAddress(foundStore.get().getStoreDetail().getBasicAddress());
            storeResponseDto.setAvergeRating(foundStore.get().getStoreDetail().getAverageRating());

            storeResponseDtoList.add(storeResponseDto);
        }
        return storeResponseDtoList;
    }

    // 리뷰 쓴 가게 목록 조회
    @Override
    public List<ReviewDto> getReview() {
        List<Review> foundReview = reviewRepository.findByUserId(getUserId());
        List<ReviewDto> reviewDtoList = new ArrayList<>();
        for (Review review : foundReview) {
            ReviewDto reviewDto2 = new ReviewDto();
            reviewDto2.setStoreName(review.getStoreName());
            reviewDto2.setRating(review.getRating());
            reviewDto2.setContent(review.getContent());
            reviewDtoList.add(reviewDto2);
        }
        return reviewDtoList;
    }


    // 주문한 가게 목록 조회
    @Override
    public List<OrderResponseDto> getOrderByUserId() {
//        List<Order> foundOrderList = orderRepository.findByUserId(getUserId());
//        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();
//
//        for (Order order : foundOrderList) {
//            OrderResponseDto orderResponseDto = new OrderResponseDto();
//            orderResponseDto.setStoreName(order.getStore().getStoreDetail().getName());
//
//            List<OrderProduct> foundOrderProducts = order.getOrderProducts();
//            List<String> tempProducts = new ArrayList<>();
//            for (OrderProduct foundOrderProduct : foundOrderProducts) {
//                tempProducts.add(foundOrderProduct.getProduct().getName());
//            }
//            orderResponseDto.setOrderMenu(tempProducts);
//            orderResponseDto.setTotalAmount(order.getTotalAmount());
//            orderResponseDto.setOrderDate(order.getCreateAt().toString());
//
//            orderResponseDtoList.add(orderResponseDto);
//        }
//        return orderResponseDtoList;
        return null;

    }

    // 리뷰 삭제
    @Override
    public UserDto delReviewByUserId(Long reviewId) {
        reviewRepository.deleteById(reviewId);
        UserDto userDto = new UserDto();
        userDto.setMessage("삭제완료!!");
        return userDto;
    }

    // 리뷰 등록
    @Override
    public ReviewDto postReview(ReviewDto inReviewDto) {
        Review review = new Review(inReviewDto);
        reviewRepository.save(review);

        ReviewDto reviewDto = new ReviewDto(review);
        reviewDto.setMessage("리뷰가 등록되었습니다");
        return reviewDto;
    }

    // 환경설정
    @Override
    public Configuration getConfig() {
        Configuration configuration = new Configuration(); // TODO DTO로 변경예정
        configuration.setConfig("환경설정 샘플1");
        configuration.setAnnounce("공지사항 샘플2");

        return configuration;
    }

    // ################################################################ 메인페이지 ##############################################################
    // 메인페이지 - 나와 가까운 가게 목록 보이기
    @Override
    public List<StoreResponseDto> getNearbyStores() {
        double radius = 2.0; // 검색 반경 설정 (예: 2.0km)

        User foundUser = userRepository.findByUsername(getUsernameFromAuthentication());

        List<Store> nearbyStores = storeRepository.findNearbyStores(foundUser.getLatitude(), foundUser.getLongitude(), radius);
        List<StoreResponseDto> nearbyStoreDtoList = new ArrayList<>();

        for (Store store : nearbyStores) {
            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setName(store.getStoreDetail().getName());
            nearbyStoreDtoList.add(storeResponseDto);
        }

        return nearbyStoreDtoList;
    }

    // 메뉴별, 가까운순별 가게목록 - 가게명, 위치, 평점
    @Override
    public List<StoreResponseDto> getNearbyStores2(Long productId) {
        double radius = 2.0; // 검색 반경 설정 (예: 2.0km)

        User foundUser = userRepository.findByUsername(getUsernameFromAuthentication());

        List<Store> nearbyStores = storeRepository.findNearbyStores(foundUser.getLatitude(), foundUser.getLongitude(), radius);
        List<StoreResponseDto> nearbyStoreDtoList = new ArrayList<>();

        for (Store store : nearbyStores) {
            List<StoreProduct> storeProducts = store.getStoreProducts();
            for (StoreProduct storeProduct : storeProducts) {
                if (Objects.equals(storeProduct.getProduct().getId(), productId)) {
                    StoreResponseDto storeResponseDto = new StoreResponseDto();
                    storeResponseDto.setName(store.getStoreDetail().getName());
                    storeResponseDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
                    storeResponseDto.setAvergeRating(store.getStoreDetail().getAverageRating());
                    nearbyStoreDtoList.add(storeResponseDto);
                }
            }
        }
        return nearbyStoreDtoList;
    }

    // 메뉴별, 인기순(판매순)별 가게목록
    @Override
    public List<StoreResponseDto> getStoresSortedByMenuSales(Long productId) {

        List<Store> storesSortedByMenuSales = storeRepository.getStoresSortedByMenuSales();
        List<StoreResponseDto> storeBySalesStoreList = new ArrayList<>();
        for (Store store : storesSortedByMenuSales) {
            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setName(store.getStoreDetail().getName());
            storeResponseDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
            storeResponseDto.setAvergeRating(store.getStoreDetail().getAverageRating());

            storeBySalesStoreList.add(storeResponseDto);
        }
        return storeBySalesStoreList;
    }

    // 메뉴별, 리뷰순 가게목록
    @Override
    public List<StoreResponseDto> getStoresSortedByReiviews(Long productId) {
        List<Store> storeByReviews = storeRepository.getStoreByReviews(); // TODO productId넣기!
        List<StoreResponseDto> storeByReiviewsStoreList = new ArrayList<>();

        for (Store store : storeByReviews) {
            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setName(store.getStoreDetail().getName());
            storeResponseDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
            storeResponseDto.setAvergeRating(store.getStoreDetail().getAverageRating());

            storeByReiviewsStoreList.add(storeResponseDto);
        }
        return storeByReiviewsStoreList;
    }

    // 상호명 검색
    @Override
    public List<StoreResponseDto> searchStores(String keyword) {
        List<Store> storeList = storeRepository.searchStores(keyword);

        List<StoreResponseDto> storeResponseDtoList = new ArrayList<>();
        for (Store store : storeList) {
            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setName(store.getStoreDetail().getName());
            storeResponseDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
            storeResponseDto.setAvergeRating(store.getStoreDetail().getAverageRating());

            storeResponseDtoList.add(storeResponseDto);
        }
        return storeResponseDtoList;
    }

    // 지도로 근처 가게 검색 (addressNo가 같은 가게 반환)
    @Override
    public List<StoreResponseDto> searchStoresByAddressNo(String addressNo) {
        List<Store> storeList = storeRepository.searchStoresByAddressNo(addressNo);

        List<StoreResponseDto> storeResponseDtoList = new ArrayList<>();
        for (Store store : storeList) {
            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setName(store.getStoreDetail().getName());
            storeResponseDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
            storeResponseDto.setAvergeRating(store.getStoreDetail().getAverageRating());

            storeResponseDtoList.add(storeResponseDto);
        }
        return storeResponseDtoList;
    }

    //  가게 상세 조회 (메뉴 및 인기간식)
    @Override
    public StoreMainDto getStoreProducts(Long storeId) {
        StoreMainDto storeMainDto = new StoreMainDto();

        // 가게 메뉴 담는 리스트 만들기
        List<StoreProduct> storeProductList = storeProductRepository.findByStoreId(storeId);
        List<ProductResponseDto> storeProductDtoList = new ArrayList<>();

        for (StoreProduct storeProduct : storeProductList) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setProductName(storeProduct.getProduct().getName());
            productResponseDto.setPrice(storeProduct.getProduct().getPrice());
            productResponseDto.setQuantity(1L);

            storeProductDtoList.add(productResponseDto);
        }

        // 인기 메뉴 리스트 만들기
        List<StoreProduct> storeProducts = storeProductRepository.findByStoreId(storeId);
        List<ProductResponseDto> popularProductsDtoList = new ArrayList<>();

        for (StoreProduct storeProduct : storeProducts) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setProductName(storeProduct.getProduct().getName());

            popularProductsDtoList.add(productResponseDto);
        }

        storeMainDto.setProductResponseDtoList(storeProductDtoList);
        storeMainDto.setPopularProductsDtoList(popularProductsDtoList);

        return storeMainDto;
    }

    // 가게 상세 조회 (가게정보)
    @Override
    public StoreResponseDto getStoreDetails(Long storeId) {
        Optional<Store> optionalStore = storeRepository.findById(storeId);
        Store store = optionalStore.get();
        StoreResponseDto storeResponseDto = new StoreResponseDto();
        storeResponseDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
        storeResponseDto.setDetailAddress(store.getStoreDetail().getDetailAddress());
        storeResponseDto.setOpenTime(store.getStoreDetail().getOpenTime());
        storeResponseDto.setCloseTime(store.getStoreDetail().getCloseTime());
        storeResponseDto.setInfo(store.getStoreDetail().getInfo());

        return storeResponseDto;
    }

    // 가게 상세 조회(리뷰)
    @Override
    public List<ReviewDto> getStoreReviews(Long storeId) {

        List<Review> reviewList = reviewRepository.findByStoreId(storeId);
        List<ReviewDto> reviewDtoList = new ArrayList<>();

        for (Review review : reviewList) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setRating(review.getRating());
            reviewDto.setPhoto(review.getPhoto());
            reviewDto.setContent(review.getContent());
            reviewDto.setTimestamp(review.getCreatedAt()); // getTimeStamp라고 해서 안나왔었음
            reviewDtoList.add(reviewDto);
        }
        return reviewDtoList;

    }

    // 가게 찜하기
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

}