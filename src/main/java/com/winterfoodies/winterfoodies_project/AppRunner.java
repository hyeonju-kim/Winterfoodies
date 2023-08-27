package com.winterfoodies.winterfoodies_project;


import com.winterfoodies.winterfoodies_project.entity.*;
import com.winterfoodies.winterfoodies_project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final StoreProductRepository storeProductRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final FavoriteStoreRepository favoriteStoreRepository;
    private final ReviewRepository reviewRepository;
    private final CartRepository cartRepository;
    private final BCryptPasswordEncoder encoder;

    private final JdbcTemplate jdbcTemplate;


    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        testScenario();
    }

    public void testScenario(){
        //사장 - 홍길동
        User ceo = new User();
        ceo.setUsername("ghd123@naver.com");
        ceo.setPassword(encoder.encode("ghd123!@#"));
        ceo.setName("홍길동");

        //가게 생성 1 - 신천붕어빵
        Store store = new Store();
        store.setStatus(StoreStatus.OPEN);

        //가게 생성 2 - 대야붕어빵
        Store store2 = new Store();
        store2.setStatus(StoreStatus.OPEN);

        //가게 생성 3 - 소새울호떡집
        Store store3 = new Store();
        store3.setStatus(StoreStatus.OPEN);

        //가게 디테일 생성 1 - 신천붕어빵 (붕어빵, 델리만쥬, 다코야키)
        StoreDetail storeDetail = new StoreDetail();
        storeDetail.setName("신천붕어빵");
        storeDetail.setBasicAddress("신천역 1번 출구");
        storeDetail.setDetailAddress("신천로 11길");
        storeDetail.setAddressNo("14774");
        storeDetail.setOfficialCodeNo("1111111111");
        storeDetail.setRoadCodeNo("222222222");
        storeDetail.setInfo("존맛탱인 저희가게 많이 놀러와주세요~!");
        storeDetail.setOpenTime(LocalTime.of(8,0));
        storeDetail.setCloseTime(LocalTime.of(20,0));
        storeDetail.setAverageRating(4l);
        storeDetail.setLatitude(37.381798);
        storeDetail.setLongitude(126.800944);

        //가게 디테일 생성 2 - 대야붕어빵 (붕어빵, 다코야끼)
        StoreDetail storeDetail2 = new StoreDetail();
        storeDetail2.setName("대야붕어빵");
        storeDetail2.setBasicAddress("대야역 1번 출구");
        storeDetail2.setDetailAddress("대야길 10번길 22");
        storeDetail2.setAddressNo("45612");
        storeDetail2.setOfficialCodeNo("222223333");
        storeDetail2.setRoadCodeNo("222222222");
        storeDetail2.setInfo("국산 재료만 사용합니다. 많은 관심 부탁드려요~!");
        storeDetail2.setOpenTime(LocalTime.of(11,0));
        storeDetail2.setCloseTime(LocalTime.of(22,0));
        storeDetail2.setAverageRating(5l);
        storeDetail2.setLatitude(37.375499);
        storeDetail2.setLongitude(126.785488);

        //가게 디테일 생성 3 - 소새울호떡집 (호떡)
        StoreDetail storeDetail3 = new StoreDetail();
        storeDetail3.setName("소새울호떡집");
        storeDetail3.setBasicAddress("소새울역 1번 출구");
        storeDetail3.setDetailAddress("소새울길 10번길 22");
        storeDetail3.setAddressNo("45612");
        storeDetail3.setOfficialCodeNo("222223333");
        storeDetail3.setRoadCodeNo("222222222");
        storeDetail3.setInfo("신규오픈 했어요 50% 할인 행사 합니다 ^^~!");
        storeDetail3.setOpenTime(LocalTime.of(13,0));
        storeDetail3.setCloseTime(LocalTime.of(20,0));
        storeDetail3.setAverageRating(5l);
        storeDetail3.setLatitude(37.467441);
        storeDetail3.setLongitude(126.793399);

        //가게에게 가게 디테일 인젝션 1
        store.setStoreDetail(storeDetail);
        storeRepository.save(store);

        //가게에게 가게 디테일 인젝션 2
        store2.setStoreDetail(storeDetail2);
        storeRepository.save(store2);

        //가게에게 가게 디테일 인젝션 3
        store3.setStoreDetail(storeDetail3);
        storeRepository.save(store3);

        //홍길동사장님에게 가게 인젝션 1
        ceo.setStore(store);
        userRepository.save(ceo);



        //상품 생성
        Product product = new Product();
        product.setName("붕어빵");
        product.setPrice(1500L);

        Product product2 = new Product();
        product2.setName("어묵");
        product2.setPrice(3000L);

        Product product3 = new Product();
        product3.setName("군밤");
        product3.setPrice(4000L);

        Product product4 = new Product();
        product4.setName("호떡");
        product4.setPrice(2000L);

        Product product5 = new Product();
        product5.setName("계란빵");
        product5.setPrice(1500L);

        Product product6 = new Product();
        product6.setName("군고구마");
        product6.setPrice(1500L);

        Product product7 = new Product();
        product7.setName("다코야키");
        product7.setPrice(1500L);

        Product product8 = new Product();
        product8.setName("호두과자");
        product8.setPrice(1500L);

        Product product9 = new Product();
        product9.setName("국화빵");
        product9.setPrice(1500L);

        productRepository.save(product);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
        productRepository.save(product5);
        productRepository.save(product6);
        productRepository.save(product7);
        productRepository.save(product8);
        productRepository.save(product9);

        //가게에 상품 생성 후 연결
        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setStore(store); // 신천붕어빵
        storeProduct.setProduct(product); // 붕어빵
        storeProductRepository.save(storeProduct);

        StoreProduct storeProduct2 = new StoreProduct();
        storeProduct2.setStore(store); // 신천붕어빵
        storeProduct2.setProduct(product2); // 델리만쥬
        storeProductRepository.save(storeProduct2);

        StoreProduct storeProduct3 = new StoreProduct();
        storeProduct3.setStore(store); // 신천붕어빵
        storeProduct3.setProduct(product3); // 다코야끼
        storeProductRepository.save(storeProduct3);

        StoreProduct storeProduct4 = new StoreProduct();
        storeProduct4.setStore(store2); // 대야붕어빵
        storeProduct4.setProduct(product); // 붕어빵
        storeProductRepository.save(storeProduct4);

        StoreProduct storeProduct5 = new StoreProduct();
        storeProduct5.setStore(store2); // 대야붕어빵
        storeProduct5.setProduct(product3); // 다코야끼
        storeProductRepository.save(storeProduct5);

        StoreProduct storeProduct6 = new StoreProduct();
        storeProduct6.setStore(store3); // 소새울호떡집
        storeProduct6.setProduct(product4); // 호떡
        storeProductRepository.save(storeProduct6);

        //사용자 - 헨리, 김소라, 방성훈
        User customer = new User();
        customer.setUsername("gpsfl123@naver.com");
        customer.setPassword(encoder.encode("gpsfl123!@#"));
        customer.setName("헨리");
        userRepository.save(customer);

        User customer2 = new User();
        customer2.setPassword(encoder.encode("thfk123!@#"));
        customer2.setUsername("thfk123@naver.com");
        customer2.setName("김소라");
        userRepository.save(customer2);

        User customer3 = new User();
        customer3.setPassword(encoder.encode("qkd123!@#"));
        customer3.setUsername("qkd123@naver.com");
        customer3.setName("방성훈");
        userRepository.save(customer3);

        //헨리에게 가게 인젝션 2
        customer.setStore(store2);
        userRepository.save(ceo);

        //김소라에게 가게 인젝션 3
        customer2.setStore(store3);
        userRepository.save(ceo);

        // 사용자 찜하기 1
        FavoriteStore favoriteStore = new FavoriteStore();
        favoriteStore.setUserId(customer.getId());
        favoriteStore.setStoreId(1L);
        favoriteStoreRepository.save(favoriteStore);

        // 사용자 찜하기 2
        FavoriteStore favoriteStore2 = new FavoriteStore();
        favoriteStore2.setUserId(customer.getId());
        favoriteStore2.setStoreId(2L);
        favoriteStoreRepository.save(favoriteStore2);


        // 사용자 리뷰작성 1
        Review review = new Review();
        review.setUserId(customer.getId());
        review.setRating(5L);
        review.setStore(store);
        review.setStoreName("신천붕어빵");
        review.setContent("붕어빵 너무 맛있어요!!");
//        review.setTimestamp(LocalDateTime.now());
        reviewRepository.save(review);

        // 사용자 리뷰작성 2
        Review review2 = new Review();
        review2.setUserId(customer.getId());
        review2.setRating(4L);
        review.setStore(store2);
        review2.setStoreName("대야붕어빵");
        review2.setContent("매일 먹어요 최고!!");
//        review2.setTimestamp(LocalDateTime.now());
        reviewRepository.save(review2);

        // 사용자 리뷰작성 3
        Review review3 = new Review();
        review3.setUserId(customer.getId());
        review3.setRating(5L);
        review3.setStore(store);
        review3.setStoreName("신천붕어빵");
        review3.setContent("붕어빵 너무 맛있어요!!");
        reviewRepository.save(review3);

        // 사용자 리뷰작성 4
        Review review4 = new Review();
        review4.setUserId(customer.getId());
        review4.setRating(5L);
        review4.setStore(store);
        review4.setStoreName("신천붕어빵");
        review4.setContent("맛있어요!!");
        reviewRepository.save(review4);

        // 사용자 리뷰작성 5
        Review review5 = new Review();
        review5.setUserId(customer.getId());
        review5.setRating(5L);
        review5.setStore(store3);
        review5.setStoreName("소새울호떡집");
        review5.setContent("넘맛있어요!!");
        reviewRepository.save(review5);

        // 사용자 리뷰작성 6
        Review review6 = new Review();
        review6.setUserId(customer.getId());
        review6.setRating(5L);
        review6.setStore(store3);
        review6.setStoreName("소새울호떡집");
        review6.setContent("완전맛있어요!!");
        reviewRepository.save(review6);

        //주문 - 신천붕어빵 3회주문 ,  소새울호떡 2회주문,  대야붕어빵 1회주문
        Order order = new Order();
        order.setUser(customer); //헨리
        order.setStore(store); // 신천붕어빵
        order.setCreateAt(LocalDateTime.now());
        order.setProcessYn("N");
        order.setTotalAmount(20000L);
        List<OrderProduct> orderProductList = new ArrayList<>();
//        orderProductList.add(new OrderProduct(new Product("호떡", 2L)));
//        orderProductList.add(new OrderProduct(new Product("어묵", 4L)));
        order.setOrderProducts(orderProductList);
        orderRepository.save(order);

        Order order2 = new Order();
        order2.setUser(customer2); // 김소라
        order2.setStore(store2); // 대야붕어빵
        order2.setCreateAt(LocalDateTime.now());
        order2.setProcessYn("N");
        order2.setTotalAmount(10000L);
        List<OrderProduct> orderProductList2 = new ArrayList<>();
//        orderProductList2.add(new OrderProduct(new Product("다코야끼", 10L)));
//        orderProductList2.add(new OrderProduct(new Product("호빵", 4L)));
        order2.setOrderProducts(orderProductList2);
        orderRepository.save(order2);

        Order order3 = new Order();
        order3.setUser(customer3); // 방성훈
        order3.setStore(store);  //신천붕어빵
        order3.setCreateAt(LocalDateTime.now());
        order3.setProcessYn("Y");
        order3.setTotalAmount(8000L);
        List<OrderProduct> orderProductList3 = new ArrayList<>();
//        orderProductList3.add(new OrderProduct(new Product("붕어빵",20L)));
//        orderProductList3.add(new OrderProduct(new Product("어묵", 5L)));
        order3.setOrderProducts(orderProductList3);
        orderRepository.save(order3);

        Order order4 = new Order();
        order4.setUser(customer2); // 김소라
        order4.setStore(store);  //신천붕어빵
        order4.setCreateAt(LocalDateTime.now());
        order4.setProcessYn("Y");
        order4.setTotalAmount(8000L);
        List<OrderProduct> orderProductList4 = new ArrayList<>();
//        orderProductList4.add(new OrderProduct(new Product("붕어빵",20L)));
//        orderProductList4.add(new OrderProduct(new Product("어묵", 5L)));
        order4.setOrderProducts(orderProductList4);
        orderRepository.save(order4);

        Order order5 = new Order();
        order5.setUser(customer2); // 김소라
        order5.setStore(store3);  //소새울호떡집
        order5.setCreateAt(LocalDateTime.now());
        order5.setProcessYn("Y");
        order5.setTotalAmount(8000L);
        List<OrderProduct> orderProductList5 = new ArrayList<>();
//        orderProductList5.add(new OrderProduct(new Product("붕어빵",20L)));
//        orderProductList5.add(new OrderProduct(new Product("어묵", 5L)));
        order5.setOrderProducts(orderProductList5);
        orderRepository.save(order5);

        Order order6 = new Order();
        order6.setUser(customer2); // 김소라
        order6.setStore(store3);  //소새울호떡집
        order6.setCreateAt(LocalDateTime.now());
        order6.setProcessYn("Y");
        order6.setTotalAmount(8000L);
        List<OrderProduct> orderProductList6 = new ArrayList<>();
//        orderProductList6.add(new OrderProduct(new Product("붕어빵",20L)));
//        orderProductList6.add(new OrderProduct(new Product("어묵", 5L)));
        order6.setOrderProducts(orderProductList6);
        orderRepository.save(order6);

        //주문상품
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrder(order4); // 신천붕어빵
        orderProduct.setProduct(product); // 붕어빵
        orderProduct.setClientMessage("빨리 만들어주세요");
        orderProduct.setQuantity(3L);
        orderProduct.setVisitTime(LocalDateTime.now().plus(Duration.ofHours(1)));
        orderProductRepository.save(orderProduct);


        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setOrder(order5); // 소새울호떡집
        orderProduct2.setProduct(product2); // 델리만쥬
        orderProduct2.setClientMessage("덜 익혀주세요");
        orderProduct2.setQuantity(2L);
        orderProduct2.setVisitTime(LocalDateTime.now().plus(Duration.ofHours(1)));
        orderProductRepository.save(orderProduct2);

        OrderProduct orderProduct3 = new OrderProduct();
        orderProduct3.setOrder(order6); // 소새울호떡집
        orderProduct3.setProduct(product); // 붕어빵
        orderProduct3.setClientMessage("리뷰이벤트 감자튀김이요");
        orderProduct3.setQuantity(10L);
        orderProduct3.setVisitTime(LocalDateTime.now().plus(Duration.ofHours(1)));
        orderProductRepository.save(orderProduct3);

        OrderProduct orderProduct4 = new OrderProduct();
        orderProduct4.setOrder(order); // 신천붕어빵
        orderProduct4.setProduct(product); // 붕어빵
        orderProduct4.setClientMessage("빨리 만들어주세요");
        orderProduct4.setQuantity(3L);
        orderProduct4.setVisitTime(LocalDateTime.now().plus(Duration.ofHours(1)));
        orderProductRepository.save(orderProduct4);


        OrderProduct orderProduct5 = new OrderProduct();
        orderProduct5.setOrder(order); // 신천붕어빵
        orderProduct5.setProduct(product2); // 델리만쥬
        orderProduct5.setClientMessage("덜 익혀주세요");
        orderProduct5.setQuantity(2L);
        orderProduct5.setVisitTime(LocalDateTime.now().plus(Duration.ofHours(1)));
        orderProductRepository.save(orderProduct5);

        OrderProduct orderProduct6 = new OrderProduct();
        orderProduct6.setOrder(order2); // 대야붕어빵
        orderProduct6.setProduct(product); // 붕어빵
        orderProduct6.setClientMessage("리뷰이벤트 감자튀김이요");
        orderProduct6.setQuantity(10L);
        orderProduct6.setVisitTime(LocalDateTime.now().plus(Duration.ofHours(1)));
        orderProductRepository.save(orderProduct6);

        // 장바구니 생성
        Cart cart = new Cart();
        Cart cart2 = new Cart();
        cartRepository.save(cart);
        cartRepository.save(cart2);



    }
}