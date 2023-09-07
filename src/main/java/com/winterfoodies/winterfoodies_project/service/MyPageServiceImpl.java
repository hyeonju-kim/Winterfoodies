package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.product.ProductDto;
import com.winterfoodies.winterfoodies_project.dto.review.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
import com.winterfoodies.winterfoodies_project.entity.*;
import com.winterfoodies.winterfoodies_project.exception.RequestException;
import com.winterfoodies.winterfoodies_project.exception.UserException;
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
    public List<StoreResponseDto> getFavoriteStoresByUserId() {
        List<FavoriteStore> foundFavoriteStore = favoriteStoreRepository.findByUserId(getUserId());
        List<StoreResponseDto> storeResponseDtoList = new ArrayList<>();

        for (FavoriteStore favoriteStore : foundFavoriteStore) {
            Long foundStoreId = favoriteStore.getStoreId();
            Long id = favoriteStore.getId();
            Optional<Store> foundStore = storeRepository.findById(foundStoreId);

            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setStoreId(foundStoreId);
            storeResponseDto.setName(foundStore.get().getStoreDetail().getName());
            storeResponseDto.setBasicAddress(foundStore.get().getStoreDetail().getBasicAddress());
            storeResponseDto.setAverageRating(foundStore.get().getStoreDetail().getAverageRating());
            storeResponseDto.setLatitude(foundStore.get().getStoreDetail().getLatitude());
            storeResponseDto.setLongitude(foundStore.get().getStoreDetail().getLongitude());
            storeResponseDto.setThumbNailImgUrl(foundStore.get().getStoreDetail().getThumbnailImgUrl());

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
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setId(review.getId());
            reviewDto.setUserId(getUserId());
            reviewDto.setStoreName(review.getStore().getStoreDetail().getName());
            reviewDto.setRating(review.getRating());
            reviewDto.setContent(review.getContent());
            reviewDto.setReviewTime(review.getCreatedAt());
            reviewDto.setImages(review.getImages());
            reviewDto.setOrderTime(review.getOrder().getCreateAt());

            List<OrderProduct> orderProducts = review.getOrder().getOrderProducts();

            List<String> orderedProducts = new ArrayList<>(); // 주문한 음식 리스트

            for (OrderProduct orderProduct : orderProducts) {
                Optional<Product> optionalProduct = productRepository.findById(orderProduct.getProduct().getId());
                Product product = optionalProduct.get();
                orderedProducts.add(product.getName());

            }
            reviewDto.setOrderedProducts(orderedProducts);
            reviewDtoList.add(reviewDto);
        }
        return reviewDtoList;
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
            orderResponseDto.setAverageRating(order.getStore().getStoreDetail().getAverageRating());
            orderResponseDto.setTotalAmount(order.getTotalAmount());

            List<OrderProduct> foundOrderProductsList = order.getOrderProducts();
            List<ProductDto> prdAndQntMapList = new ArrayList<>();


            for (OrderProduct foundOrderProduct : foundOrderProductsList) {
                Product product = productRepository.findById(foundOrderProduct.getProduct().getId()).get();
                ProductDto productDto = new ProductDto();
                productDto.setProductName(product.getName());
                productDto.setQuantity(foundOrderProduct.getQuantity());
                productDto.setSubTotalAmount(foundOrderProduct.getSubTotalAmount());

                prdAndQntMapList.add(productDto);
            }

            orderResponseDto.setProductAndQuantityList(prdAndQntMapList);

            orderResponseDtoList.add(orderResponseDto);
        }
        outerOrderResponseDtoList.add(orderResponseDtoList);
        return outerOrderResponseDtoList;
    }

    // 리뷰 삭제
    @Override
    public UserDto delReviewByUserId(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        UserDto userDto = new UserDto();
        userDto.setMessage("삭제완료!!");
        return userDto;
    }

    // 리뷰 등록
    @Override
    public List<ReviewDto> postReview(ReviewDto inReviewDto) {
        Store store = storeRepository.getStoreById(inReviewDto.getStoreId());
        Optional<Order> optionalOrder = orderRepository.findById(inReviewDto.getOrderId());
        Order order = optionalOrder.get();

        Review review = new Review(inReviewDto);
        review.setUserId(getUserId());
        review.setOrder(order);
        review.setStore(store);

        reviewRepository.save(review);

        List<Review> foundReview = reviewRepository.findByUserId(getUserId());
        List<ReviewDto> reviewDtoList = new ArrayList<>();
        for (Review rv : foundReview) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setId(rv.getId());
            reviewDto.setUserId(getUserId());
            reviewDto.setStoreName(rv.getStore().getStoreDetail().getName());
            reviewDto.setRating(rv.getRating());
            reviewDto.setContent(rv.getContent());
            reviewDto.setReviewTime(rv.getCreatedAt());
            reviewDto.setImages(rv.getImages());
            reviewDto.setOrderTime(rv.getOrder().getCreateAt());

            List<OrderProduct> orderProducts = rv.getOrder().getOrderProducts();

            List<String> orderedProducts = new ArrayList<>(); // 주문한 음식 리스트

            for (OrderProduct orderProduct : orderProducts) {
                Optional<Product> optionalProduct = productRepository.findById(orderProduct.getProduct().getId());
                Product product = optionalProduct.get();
                orderedProducts.add(product.getName());
            }
            reviewDto.setOrderedProducts(orderedProducts);
            reviewDtoList.add(reviewDto);
        }
        return reviewDtoList;
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
