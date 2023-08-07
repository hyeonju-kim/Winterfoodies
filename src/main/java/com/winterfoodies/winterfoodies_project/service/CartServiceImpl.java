package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.ErrorBox;
import com.winterfoodies.winterfoodies_project.dto.cart.CartDto;
import com.winterfoodies.winterfoodies_project.dto.cartProduct.CartProductDto;
import com.winterfoodies.winterfoodies_project.dto.order.OrderRequestDto;
import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.product.ProductDto;
import com.winterfoodies.winterfoodies_project.entity.*;
import com.winterfoodies.winterfoodies_project.exception.RequestException;
import com.winterfoodies.winterfoodies_project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final StoreRepository storeRepository;

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

    // 1. 장바구니에 상품 추가 (쿠키와 DB에 장바구니 담기)
    @Override
    public ProductDto addProductToCart(ProductDto inProductDto, HttpServletRequest request, HttpServletResponse response) {

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
        Long id = inProductDto.getId();
        Long quantity = inProductDto.getQuantity();
        Long storeId = inProductDto.getStoreId();

        cartValueBuilder.append(id.toString());
        cartValueBuilder.append(":");
        cartValueBuilder.append(quantity.toString());

        String cartValue = cartValueBuilder.toString();

        Cookie cookie = new Cookie("Cart", cartValue);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키 수명 7일로 설정 (초 단위)
        response.addCookie(cookie);

        // 디비에 담기
        Optional<Store> optionalStore = storeRepository.findById(storeId);
        Store selectedStore = optionalStore.get();
        if (!cartProductRepository.findByUserId(getUserId()).isEmpty() && !cartProductRepository.findByUserId(getUserId()).get(0).getStore().getId().equals(storeId)) {
            throw new RequestException(new ErrorBox("장바구니에는 한 가게의 음식만 담을 수 있습니다."));
        }
        Cart cart = new Cart();
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.get();
        CartProduct cartProduct = new CartProduct(cart, product);
        cartProduct.setQuantity(quantity);
        cartProduct.setTotalPrice(product.getPrice() * quantity);
        cartProduct.setStore(storeRepository.findById(storeId).get());
        cartProduct.setUserId(getUserId());
        cartProductRepository.save(cartProduct);

        ProductDto outProductDto = new ProductDto(product);
        outProductDto.setMessage("장바구니에 추가되었습니다."); // [230726] TODO ProductDto하니까 이상한듯? CartDto로 바꾸던지 바꾸자

        return outProductDto;
    }

    // 2. 장바구니 상품 목록 조회 (쿠키에서 조회)
    @Override
    public List<CartProductDto> getCartProduct(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        List<CartProductDto> cartProductDtoList = new ArrayList<>();
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

                            CartProductDto cartProductDto = new CartProductDto();
                            cartProductDto.setProductId(productId);
                            cartProductDto.setQuantity(quantity);
                            cartProductDtoList.add(cartProductDto);
                        }
                    }
                    break;
                }
            }
        }
        return cartProductDtoList;
    }

    // 2-2 장바구니 상품 목록 조회 (DB에서)
    @Override
    public List<CartProductDto> getCartProductByDB() {
        Long userId = getUserId();
        List<CartProduct> cartProductList = cartProductRepository.findByUserId(userId);

        if (cartProductList.isEmpty()) {
            throw new RequestException(new ErrorBox("널이래"));
        }
        ArrayList<CartProductDto> cartProductDtoList = new ArrayList<>();

        for (CartProduct cartProduct : cartProductList) {
            CartProductDto cartProductDto = new CartProductDto(cartProduct);
            cartProductDtoList.add(cartProductDto);
        }
        return cartProductDtoList;
    }


    // 3. 장바구니 특정 상품 삭제 (쿠키와 DB에서 삭제)
    @Override
    public CartProductDto removeProductFromCart(Long productId, HttpServletRequest request, HttpServletResponse response) {
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
        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setMessage("장바구니에서 [" + productId +  "] 상품을 삭제했습니다.");
        return cartProductDto;
    }

    // 4. 장바구니 초기화 (쿠키와 DB 초기화)
    @Override
    public CartProductDto clearCart(HttpServletResponse response) {
        // 쿠키 초기화
        Cookie cookie = new Cookie("Cart", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        // 디비 초기화
        cartProductRepository.deleteAll();
        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setMessage("장바구니가 초기화 되었습니다.");
        return cartProductDto;
    }

    // 5. 주문하기 & 주문완료 페이지 조회 (쿠키에서 조회)
    @Override
    public OrderResponseDto getOrderConfirmPage(OrderRequestDto orderRequestDto, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        // 주문객체 만들기
        Optional<User> userOptional = userRepository.findById(getUserId());
        Order order = new Order();
        order.setUser(userOptional.get());
        order.setMessage(orderRequestDto.getMessage());
        order.setCreateAt(LocalDateTime.now());
        List<CartProduct> foundCartProductList = cartProductRepository.findByUserId(getUserId());

        order.setStore(foundCartProductList.get(0).getStore());

        Long totalPrice = 0L;
        for (CartProduct cartProduct : foundCartProductList) {
            totalPrice += cartProduct.getTotalPrice();
        }

        order.setTotalAmount(totalPrice);

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

        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(order.getId()); // jpql 로 바꾸기 ( )
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

    // 5-2. 주문하기 & 주문완료 페이지 조회 (DB에서 조회)
    @Override
    public OrderResponseDto getOrderConfirmPageByDB(OrderRequestDto orderRequestDto) {
        List<CartProduct> cartProductList = cartProductRepository.findByUserId(getUserId());
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        ArrayList<Map<String, Long>> prdAndQntMapList = new ArrayList<>();

        Long totalAmt = 0L;

        for (CartProduct cartProduct : cartProductList) {
            String prdName = cartProduct.getProduct().getName();
            Long prdQnt = cartProduct.getQuantity();
            Map<String, Long> map = new HashMap<>();
            map.put(prdName, prdQnt);
            prdAndQntMapList.add(map);
            totalAmt += cartProduct.getTotalPrice();
        }
        orderResponseDto.setProductAndQuantityList(prdAndQntMapList);
        orderResponseDto.setCustomerMessage(orderRequestDto.getMessage());
        orderResponseDto.setTotalAmount(totalAmt);

        return orderResponseDto;
    }
 }