package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.review.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
import com.winterfoodies.winterfoodies_project.entity.*;
import com.winterfoodies.winterfoodies_project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MypageService{
    private final UserRepository userRepository;
    private final FavoriteStoreRepository favoriteStoreRepository;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final BCryptPasswordEncoder encoder;
    private final OrderRepository orderRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;

    // jwt 토큰으로 현재 인증된 사용자의 Authentication 객체에서 이름 가져오기
    public String getUsernameFromAuthentication() {
        String username = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 인증된 사용자의 이름 가져오기
            username = authentication.getName();
            System.out.println("인증객체에서 꺼낸 유저네임 ==== "+username);
        }
        return username;
    }

    // 인증된 사용자의 id 가져오기
    public Long getUserId() {
        User foundUser = userRepository.findByUsername(getUsernameFromAuthentication());
        return foundUser.getId();
    }


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
            String encodedPassword = encoder.encode(inUserDto.getPassword());// 230726 추가
            foundUser.setPassword(encodedPassword);
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
    public List<List<StoreResponseDto>> getFavoriteStoresByUserId() {
        List<FavoriteStore> foundFavoriteStore = favoriteStoreRepository.findByUserId(getUserId());
        List<StoreResponseDto> storeResponseDtoList = new ArrayList<>();
        List<List<StoreResponseDto>> outerStoreResponseDtoList = new ArrayList<>();

        for (FavoriteStore favoriteStore : foundFavoriteStore) {
            Long foundStoreId = favoriteStore.getStoreId();
            Long id = favoriteStore.getId();
            Optional<Store> foundStore = storeRepository.findById(foundStoreId);

            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setId(id);
            storeResponseDto.setName(foundStore.get().getStoreDetail().getName());
            storeResponseDto.setBasicAddress(foundStore.get().getStoreDetail().getBasicAddress());
            storeResponseDto.setAverageRating(foundStore.get().getStoreDetail().getAverageRating());
            storeResponseDto.setLatitude(foundStore.get().getStoreDetail().getLatitude());
            storeResponseDto.setLongitude(foundStore.get().getStoreDetail().getLongitude());

            storeResponseDtoList.add(storeResponseDto);
        }
        outerStoreResponseDtoList.add(storeResponseDtoList);
        return outerStoreResponseDtoList;
    }

    // 리뷰 쓴 가게 목록 조회
    @Override
    public List<List<ReviewDto>> getReview() {
        List<Review> foundReview = reviewRepository.findByUserId(getUserId());
        List<ReviewDto> reviewDtoList = new ArrayList<>();
        List<List<ReviewDto>> outerReviewDtoList = new ArrayList<>();
        for (Review review : foundReview) {
            ReviewDto reviewDto2 = new ReviewDto();
            reviewDto2.setId(review.getId());
            reviewDto2.setStoreName(review.getStoreName());
            reviewDto2.setRating(review.getRating());
            reviewDto2.setContent(review.getContent());
            reviewDtoList.add(reviewDto2);
        }
        outerReviewDtoList.add(reviewDtoList);
        return outerReviewDtoList;
    }


    // 주문한 가게 목록 조회
    @Override
    public List<List<OrderResponseDto>> getOrderByUserId() {
        List<Order> foundOrderList = orderRepository.findByUserId(getUserId());
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();
        List<List<OrderResponseDto>> outerOrderResponseDtoList = new ArrayList<>();
        List<CartProduct> cartProductList = cartProductRepository.findByUserId(getUserId());

        for (Order order : foundOrderList) {
            OrderResponseDto orderResponseDto = new OrderResponseDto();
            orderResponseDto.setOrderId(order.getId());
            orderResponseDto.setOrderDate(order.getCreateAt());
            orderResponseDto.setStoreName(order.getStore().getStoreDetail().getName());
            orderResponseDto.setTotalAmount(order.getTotalAmount());

            List<OrderProduct> foundOrderProductsList = order.getOrderProducts();
            List<Map<String, Object>> tempProductsList = new ArrayList<>();


            for (OrderProduct foundOrderProduct : foundOrderProductsList) {
                Map<String, Object> productInfoMap = new HashMap<>(); // 가격 정보를 포함하는 Map으로 변경
                Product product = productRepository.findById(foundOrderProduct.getProduct().getId()).get();
                productInfoMap.put("name", foundOrderProduct.getProduct().getName());
                productInfoMap.put("quantity", foundOrderProduct.getQuantity());
                productInfoMap.put("price", foundOrderProduct.getQuantity() * product.getPrice()); // 가격 정보 추가

                tempProductsList.add(productInfoMap);
            }

            orderResponseDto.setProductAndQuantityList(tempProductsList);

            orderResponseDtoList.add(orderResponseDto);
        }
        outerOrderResponseDtoList.add(orderResponseDtoList);
        return outerOrderResponseDtoList;
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
        review.setUserId(getUserId());
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

}
