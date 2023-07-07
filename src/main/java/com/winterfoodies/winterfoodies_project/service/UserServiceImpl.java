package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.config.LocationConfig;
import com.winterfoodies.winterfoodies_project.dto.order.OrderRequestDto;
import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.product.ProductResponseDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreMainDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreRequestDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.dto.user.*;
import com.winterfoodies.winterfoodies_project.entity.*;
import com.winterfoodies.winterfoodies_project.repository.*;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
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
    public void signUp(UserRequestDto userRequestDto) {
        User user = new User(userRequestDto);
        userRepository.save(user);
    }

    // ################################################################ 마이페이지 ##############################################################
    // 마이페이지 내정보
    @Override
    public UserDto retrieveUser() {
        User foundUser = userRepository.findByUsername(getUsernameFromAuthentication());
        if (foundUser != null) {  // respository에서 가져온건 꼭 분기처리 해야한다!!!!
            UserDto foundUserDto = new UserDto();
            foundUserDto.setEmail(foundUser.getEmail());
            foundUserDto.setUsername(foundUser.getUsername());
            return foundUserDto;
        }
        UserDto notFoundUserDto = new UserDto();
        notFoundUserDto.setMessage("해당 유저를 찾을 수 없습니다.");
        return notFoundUserDto;
    }

    // 마이페이지 비번 변경
    @Override
    public UserDto changePw(UserDto inUserDto) {
        User foundUser = userRepository.findByUsername(getUsernameFromAuthentication());
        if (foundUser != null) {
            foundUser.setPassword(inUserDto.getPassword());
            userRepository.save(foundUser);

            UserDto foundUserDto = new UserDto();
            foundUserDto.setMessage("변경완료!!");
            return foundUserDto;
        }

        UserDto notFoundUserDto = new UserDto();
        notFoundUserDto.setMessage("해당 유저를 찾을 수 없습니다.");

        // Entity -> UserDto
        UserDto outUserDto = new UserDto(foundUser);
        return outUserDto;
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

        review.setUserId(getUserId());
        review.setRating(reviewDto.getRating());
        review.setPhoto(reviewDto.getPhoto());
        review.setContent(reviewDto.getContent());
        review.setStoreName(reviewDto.getStoreName());

        reviewRepository.save(review);

        reviewDto1.setMessage("리뷰가 등록되었습니다");
        return reviewDto1;
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

    // ################################################################ 장바구니 ##############################################################

    // 장바구니에 상품 추가
//    @Override
//    public UserResponseDto addProductToCart(Long cartId, Long productId, int quantity) {
//        Optional<Cart> optionalCart = cartRepository.findById(cartId);
//        if (optionalCart.isPresent()) {
//
//            Cart cart = optionalCart.get();
//            Optional<Product> optionalProduct = productRepository.findById(productId);
//            Product product = optionalProduct.get();
//            CartProduct cartProduct = new CartProduct(cart, product, quantity);
//            cartProductRepository.save(cartProduct);
//
//            UserResponseDto userResponseDto = new UserResponseDto();
//            userResponseDto.setMessage("장바구니에 상품을 담았습니다.");
//
//            return userResponseDto;
//        }
//        UserResponseDto notFoundUserDto = new UserResponseDto();
//        notFoundUserDto.setMessage("장바구니를 찾을 수 없습니다.");
//        return notFoundUserDto;
//    }

    // 1. 장바구니에 상품 추가 (쿠키와 DB에 장바구니 담기)
    @Override
    public String addProductToCart(@RequestParam Long productId,@RequestParam Long quantity, HttpServletRequest request, HttpServletResponse response) {

        // 쿠키에 담기
        Cookie[] cookies = request.getCookies();
        StringBuilder cartValueBuilder = new StringBuilder();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Cart")) {
                    // 기존 장바구니 값을 StringBuilder에 추가
                    cartValueBuilder.append(cookie.getValue());
                    cartValueBuilder.append("|");
                    break;
                }
            }
        }
        // 새로운 상품과 수량을 StringBuilder에 추가
        cartValueBuilder.append(productId.toString());
        cartValueBuilder.append(":");
        cartValueBuilder.append(quantity.toString());

        String cartValue = cartValueBuilder.toString();

        Cookie cookie = new Cookie("Cart", cartValue);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키 수명 7일로 설정 (초 단위)
        response.addCookie(cookie);

        // 디비에 담기
        Cart cart = new Cart();
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Product product = optionalProduct.get();
        CartProduct cartProduct = new CartProduct(cart, product);
        cartProduct.setQuantity(quantity);
        cartProduct.setTotalPrice(product.getPrice() * quantity);
        cartProductRepository.save(cartProduct);

        return "장바구니에 추가되었습니다.";
    }

    // 2. 장바구니 상품 목록 조회 (쿠키에서 조회)
    @Override
    public List<CartDto> getCartProduct(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        List<CartDto> cartDtoList = new ArrayList<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Cart")) {
                    String cartProduct = cookie.getValue();
                    String[] products = cartProduct.split("\\|");

                    for (String product : products) {
                        String[] productAndQuantity = product.split(":");

                        Long productId = Long.valueOf(productAndQuantity[0]);
                        Long quantity = Long.valueOf(productAndQuantity[1]);

                        Optional<Product> optionalProduct = productRepository.findById(productId);

                        if (optionalProduct.isPresent()) {
                            Product foundProduct = optionalProduct.get();
                            String productName = foundProduct.getName();

                            CartDto cartDto = new CartDto();
                            cartDto.setName(productName);
                            cartDto.setQuantity(quantity);
                            cartDtoList.add(cartDto);
                        }
                    }
                    break;
                }
            }
        }
        return cartDtoList;
    }

    // 3. 장바구니 특정 상품 삭제 (쿠키와 DB에서 삭제)
    @Override
    public String removeProductFromCart(Long productId, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        // 쿠키에서 삭제
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Cart")) {
                    String cartProduct = cookie.getValue();
                    String[] product = cartProduct.split("\\|");

                    StringBuilder updatedCartValue = new StringBuilder();

                    // 입력한 상품 id가 현재의 상품과 같지 않으면 새로운 Cart에 추가하기 (입력한 상품 id만 빼고 추가하는 것. 결론적으로는 입력한 상품 id가 삭제되는 꼴 !)
                    for (String prd : product) {
                        String[] prdAndQnt = prd.split(":");
                        Long cartProductId = Long.valueOf(prdAndQnt[0]);

                        if (!cartProductId.equals(productId)) {
                            updatedCartValue.append("|").append(prd);
                        }
                    }

                    if (updatedCartValue.length() > 0) {
                        cookie.setValue(updatedCartValue.substring(1)); // 첫 번째 '|' 문자 제외
                    } else {
                        cookie.setMaxAge(0); // 장바구니가 비어있으면 쿠키를 삭제
                    }

                    response.addCookie(cookie);
                }
            }
        }
        // 디비에서 삭제
        cartProductRepository.deleteByProductId(productId);
        return "장바구니에서 상품을 삭제했습니다.";
    }

    // 4. 장바구니 초기화 (쿠키와 DB 초기화)
    @Override
    public String clearCart(HttpServletResponse response) {
        // 쿠키 초기화
        Cookie cookie = new Cookie("Cart", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        // 디비 초기화
        cartProductRepository.deleteAll();
        return "모두 삭제 되었습니다.";
    }

    // 5. 주문등록 & 주문완료 페이지 조회 (쿠키에서 조회)
    @Override
    public OrderResponseDto getOrderConfirmPage(OrderRequestDto orderRequestDto, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        // 주문객체 만들기
        Optional<User> userOptional = userRepository.findById(getUserId());
        Order order = new Order();
        order.setUser(userOptional.get());
        order.setMessage(orderRequestDto.getMessage());
        order.setCreateAt(LocalDateTime.now());

        orderRepository.save(order);

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Cart")) {
                    String[] products = cookie.getValue().split("\\|");
                    for (String product : products) {
                        String[] productAndQuantity = product.split(":");
                        String productId = productAndQuantity[0];
                        String quantity = productAndQuantity[1];

                        // 상품객체 만들기
                        Optional<Product> optionalProduct = productRepository.findById(Long.valueOf(productId));
                        Product foundProduct = optionalProduct.get();

                        // 주문상품객체에 상품객체, 주문객체 인젝션
                        OrderProduct orderProduct = new OrderProduct();
                        orderProduct.setProduct(foundProduct);
                        orderProduct.setOrder(order);
                        orderProduct.setQuantity(Long.valueOf(quantity));
                        orderProductRepository.save(orderProduct);
                    }
                    break;
                }
            }
        }

        OrderResponseDto orderResponseDto = new OrderResponseDto();

        List<Map<String, Long>> prdAndQntList = new ArrayList<>();

        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(order.getId());
        for (OrderProduct orderProduct : orderProducts) {
            Map<String, Long> prdAndQnt = new HashMap<>();
            prdAndQnt.put(orderProduct.getProduct().getName(), orderProduct.getQuantity());
            prdAndQntList.add(prdAndQnt);
        }

        orderResponseDto.setProductAndQuantityList(prdAndQntList);
        orderResponseDto.setOrderDate(LocalDateTime.now());
        orderResponseDto.setCustomerMessage(orderRequestDto.getMessage());

        return orderResponseDto;

    }
}
