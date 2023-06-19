package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.user.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
import com.winterfoodies.winterfoodies_project.entity.*;
import com.winterfoodies.winterfoodies_project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserDto loginUser; //ScenarioConfig에서 등록한 bean을 주입받아서 사용하기
    private final UserRepository userRepository;
    private final FavoriteStoreRepository favoriteStoreRepository;
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;


    @Override
    public UserDto retrieveUser() {
        Optional<User> user = userRepository.findByEmail(loginUser.getEmail());
        if(user.isPresent()){  // respository에서 가져온건 꼭 분기처리 해야한다!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            User foundUser = user.get();
            UserDto foundUserDto = new UserDto();
            foundUserDto.setEmail(foundUser.getEmail());
            foundUserDto.setName(foundUser.getName());
            return foundUserDto;
        }
        UserDto notFoundUserDto = new UserDto();
        notFoundUserDto.setMessage("해당 유저를 찾을 수 없습니다.");
        return notFoundUserDto;
    }

    @Override
    public UserDto changePw(UserDto userDto) {
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setPassword(userDto.getPassword());
            userRepository.save(foundUser);

            UserDto foundUserDto = new UserDto();
            foundUserDto.setMessage("변경완료!!");
            return foundUserDto;
        }
        UserDto notFoundUserDto = new UserDto();
        notFoundUserDto.setMessage("해당 유저를 찾을 수 없습니다.");
        return notFoundUserDto;
    }

    // 가게 찜하기
    @Override
    public FavoriteStore addFavoriteStore(Long userId, Long storeId) {
        FavoriteStore favoriteStore = new FavoriteStore();
        favoriteStore.setUserId(userId);
        favoriteStore.setStoreId(storeId);
        return favoriteStoreRepository.save(favoriteStore);
    }

    // 찜한 가게 목록 조회
    @Override
    public List<StoreResponseDto> getFavoriteStoresByUserId(Long userId) {
        List<FavoriteStore> foundFavoriteStore = favoriteStoreRepository.findByUserId(userId);
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
    public List<ReviewDto> getReview(Long userId) {
        List<Review> foundReview = reviewRepository.findByUserId(userId);
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
    public List<OrderResponseDto> getOrderByUserId(Long userId) {
        List<Order> foundOrderList = orderRepository.findByUserId(userId);
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();

        for (Order order : foundOrderList) {
            OrderResponseDto orderResponseDto = new OrderResponseDto();
            orderResponseDto.setStoreName(order.getStore().getStoreDetail().getName());

            List<OrderProduct> foundOrderProducts = order.getOrderProducts();
            List<String> tempProducts = new ArrayList<>();
            for (OrderProduct foundOrderProduct : foundOrderProducts) {
                tempProducts.add(foundOrderProduct.getProduct().getName());
            }
            orderResponseDto.setOrderMenu(tempProducts);
            orderResponseDto.setTotalAmount(order.getTotalAmount());
            orderResponseDto.setOrderDate(order.getCreateAt().toString());

            orderResponseDtoList.add(orderResponseDto);
        }
        return  orderResponseDtoList;

    }

    // 리뷰 삭제
    @Override
    public UserDto delReviewByUserId(Long reviewId) {
        UserDto userDto = new UserDto();
        reviewRepository.deleteById(reviewId);
        userDto.setMessage("삭제완료!!");
        return userDto;
    }

    // 리뷰 등록
    @Override
    public ReviewDto postReview(ReviewDto reviewDto) {
        ReviewDto reviewDto1 = new ReviewDto();
        Review review = new Review();

        review.setUserId(reviewDto.getUserId());
        review.setRating(reviewDto.getRating());
        review.setPhoto(reviewDto.getPhoto());
        review.setContent(reviewDto.getContent());
        review.setStoreName(reviewDto.getStoreName());

        reviewRepository.save(review);

        reviewDto1.setMessage("리뷰가 등록되었습니다");
        return reviewDto1;
    }

    @Override
    public Configuration getConfig() {
        Configuration configuration = new Configuration(); // TODO DTO로 변경예정
        configuration.setConfig("환경설정 샘플1");
        configuration.setAnnounce("공지사항 샘플2");

        return configuration;
    }
}
