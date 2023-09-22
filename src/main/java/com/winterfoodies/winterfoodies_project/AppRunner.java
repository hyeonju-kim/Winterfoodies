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
    private final StoreDetailRepository storeDetailRepository;
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
//        //사장 - 홍길동
//        User ceo = new User();
//        ceo.setUsername("ghd123@naver.com");
//        ceo.setPassword(encoder.encode("ghd123!@#"));
//        ceo.setNickname("홍길동");

        //가게 생성 1 - 신천붕어빵
        Store store = new Store();
        store.setStatus(StoreStatus.OPEN);

        //가게 생성 2 - 대야붕어빵
        Store store2 = new Store();
        store2.setStatus(StoreStatus.OPEN);

        //가게 생성 3 - 소새울호떡집
        Store store3 = new Store();
        store3.setStatus(StoreStatus.OPEN);



        //상품 생성
        // 1번 가게 상품리스트 생성 (붕어빵, 어묵, 군밤)
        List<Product> productsList1 = new ArrayList<>();
        Product product = new Product();
        product.setName("붕어빵");
        product.setPrice(1500L);
        productsList1.add(product);

        Product product2 = new Product();
        product2.setName("어묵");
        product2.setPrice(3000L);
        productsList1.add(product2);

        Product product3 = new Product();
        product3.setName("군밤");
        product3.setPrice(4000L);
        productsList1.add(product3);

        // 2번 가게 상품리스트 생성 (붕어빵, 호떡)
        List<Product> productsList2 = new ArrayList<>();
        Product product4 = new Product();
        product4.setName("붕어빵");
        product4.setPrice(1000L);
        productsList2.add(product4);

        Product product5 = new Product();
        product5.setName("호떡");
        product5.setPrice(2000L);
        productsList2.add(product5);

        // 3번 가게 상품리스트 생성 (계란빵, 다코야키, 호두과자, 국화빵)
        List<Product> productsList3 = new ArrayList<>();
        Product product6 = new Product();
        product6.setName("계란빵");
        product6.setPrice(1500L);
        productsList3.add(product6);

        Product product7 = new Product();
        product7.setName("다코야키");
        product7.setPrice(1500L);
        productsList3.add(product7);

        Product product8 = new Product();
        product8.setName("호두과자");
        product8.setPrice(1500L);
        productsList3.add(product8);

        Product product9 = new Product();
        product9.setName("국화빵");
        product9.setPrice(1500L);
        productsList3.add(product9);

        productRepository.save(product);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
        productRepository.save(product5);
        productRepository.save(product6);
        productRepository.save(product7);
        productRepository.save(product8);
        productRepository.save(product9);


        //가게 디테일 생성 1 - 신천붕어빵 (붕어빵, 어묵, 군밤)
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
        storeDetail.setEstimatedCookingTime("20분~30분");
        storeDetail.setAverageRating(4l);
        storeDetail.setLatitude(37.381798);
        storeDetail.setLongitude(126.800944);
        storeDetail.setStatus("영업중");
        storeDetail.setOpenDate("매일");
        storeDetail.setThumbnailImgUrl("photo1");
        storeDetail.setMapIcon("붕어빵");
        storeDetail.setProductsList(productsList1); // 위에서 만든 상품리스트1 (붕어빵, 어묵, 군밤)

        // 아래처럼 product에도 storeDetail 을 저장해줘야 정상저장됨!!!!!!
        for (Product p : productsList1) {
            p.setStoreDetail(storeDetail);
            productRepository.save(p); // 각각의 Product 엔티티를 저장합니다.
        }
        storeDetailRepository.save(storeDetail);

        //가게 디테일 생성 2 - 대야붕어빵 (붕어빵, 호떡)
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
        storeDetail2.setEstimatedCookingTime("10분~15분");
        storeDetail2.setAverageRating(5l);
        storeDetail2.setLatitude(37.375499);
        storeDetail2.setLongitude(126.785488);
        storeDetail2.setStatus("영업중");
        storeDetail2.setOpenDate("주 5일");
        storeDetail2.setThumbnailImgUrl("photo2");
        storeDetail2.setMapIcon("붕어빵");
        storeDetail2.setProductsList(productsList2); // 위에서 만든 상품리스트2 (붕어빵, 호떡)
        for (Product p : productsList2) {
            p.setStoreDetail(storeDetail2);
            productRepository.save(p); // 각각의 Product 엔티티를 저장합니다.
        }
        storeDetailRepository.save(storeDetail2);

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
        storeDetail3.setEstimatedCookingTime("20분~25분");
        storeDetail3.setAverageRating(5l);
        storeDetail3.setLatitude(37.467441);
        storeDetail3.setLongitude(126.793399);
        storeDetail3.setStatus("영업종료");
        storeDetail3.setOpenDate("주말");
        storeDetail3.setThumbnailImgUrl("photo3");
        storeDetail3.setMapIcon("호떡");
        storeDetail3.setProductsList(productsList3); // 3번 가게 상품리스트 생성 (호떡, 계란빵, 다코야키, 호두과자, 국화빵)

        for (Product p : productsList3) {
            p.setStoreDetail(storeDetail3);
            productRepository.save(p); // 각각의 Product 엔티티를 저장합니다.
        }
        storeDetailRepository.save(storeDetail3);

        //가게에게 가게 디테일 인젝션 1
        store.setStoreDetail(storeDetail);
        storeRepository.save(store);

        //가게에게 가게 디테일 인젝션 2
        store2.setStoreDetail(storeDetail2);
        storeRepository.save(store2);

        //가게에게 가게 디테일 인젝션 3
        store3.setStoreDetail(storeDetail3);
        storeRepository.save(store3);

//        //홍길동사장님에게 가게 인젝션 1
//        ceo.setStore(store);
//        userRepository.save(ceo);




        //가게에 상품 생성 후 연결
        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setStore(store); // 신천붕어빵
        storeProduct.setProduct(product); // 붕어빵
        storeProduct.setProduct(product2); // 델리만쥬
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
        customer.setNickname("헨리");
        userRepository.save(customer);

        User customer2 = new User();
        customer2.setPassword(encoder.encode("thfk123!@#"));
        customer2.setUsername("thfk123@naver.com");
        customer2.setNickname("김소라");
        userRepository.save(customer2);

        User customer3 = new User();
        customer3.setPassword(encoder.encode("qkd123!@#"));
        customer3.setUsername("qkd123@naver.com");
        customer3.setNickname("방성훈");
        userRepository.save(customer3);

        //헨리에게 가게 인젝션 2
        customer.setStore(store2);
        userRepository.save(customer);

        //김소라에게 가게 인젝션 3
        customer2.setStore(store3);
        userRepository.save(customer2);

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



        // 주문 - 신천붕어빵 3회주문 ,  소새울호떡 2회주문,  대야붕어빵 1회주문

        //주문 1 - 헨리가 신천붕어빵에서 붕어빵 3개 주문
        Order order = new Order();
        order.setUser(customer); //헨리
        order.setStore(store); // 신천붕어빵
        order.setCreateAt(LocalDateTime.now());
        order.setProcessYn("N");
        order.setTotalAmount(20000L);
        order.setMessage("빨리 만들어 주세요~!");


        // 주문 1 - 주문상품
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrder(order); // 신천붕어빵
        orderProduct.setProduct(product);//붕어빵
        orderProduct.setQuantity(3L);
//        orderProduct.setVisitTime(LocalDateTime.now().plus(Duration.ofHours(1)));

        orderProductRepository.save(orderProduct);

        List<OrderProduct> orderProductList = new ArrayList<>();
        orderProductList.add(orderProduct);

        order.setOrderProducts(orderProductList);
        orderRepository.save(order);

        // 주문 2 - 김소라가 대야붕어빵에서 붕어빵10개, 호떡5개 주문
        Order order2 = new Order();
        order2.setUser(customer2); // 김소라
        order2.setStore(store2); // 대야붕어빵
        order2.setCreateAt(LocalDateTime.now());
        order2.setProcessYn("N");
        order2.setTotalAmount(10000L);
        order2.setMessage("바짝 익혀주세요~~");   /////////////// 주문 2 하는중 //////////////////////

        // 주문 2 - 주문상품
        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setOrder(order2);
        orderProduct2.setProduct(product4);  //붕어빵
        orderProduct2.setQuantity(10L);
//        orderProduct2.setVisitTime(LocalDateTime.now().plus(Duration.ofHours(1)));
        orderProductRepository.save(orderProduct2);

        OrderProduct orderProduct3 = new OrderProduct();
        orderProduct3.setOrder(order2);
        orderProduct3.setProduct(product5); //호떡
        orderProduct3.setQuantity(5L);
        orderProductRepository.save(orderProduct3);

        List<OrderProduct> orderProductList2 = new ArrayList<>();
        orderProductList2.add(orderProduct2);
        orderProductList2.add(orderProduct3);

        order2.setOrderProducts(orderProductList2);
        orderRepository.save(order2);

        // 주문 3 - 방성훈이 신천붕어빵에서 붕어빵 30개 주문
        Order order3 = new Order();
        order3.setUser(customer3); // 방성훈
        order3.setStore(store);  //신천붕어빵
        order3.setCreateAt(LocalDateTime.now());
        order3.setProcessYn("Y");
        order3.setTotalAmount(8000L);
        order3.setMessage("리뷰이벤트 감자튀김이요");

        OrderProduct orderProduct4 = new OrderProduct();
        orderProduct4.setOrder(order3);
        orderProduct4.setProduct(product); // 붕어빵
        orderProduct4.setQuantity(30L);

        orderProductRepository.save(orderProduct4);

        List<OrderProduct> orderProductList3 = new ArrayList<>();
        orderProductList3.add(orderProduct4);
        order3.setOrderProducts(orderProductList3);
        orderRepository.save(order3);

        // 주문 4 - 신천붕어빵에서 김소라가 붕어빵15개, 어묵10개, 군밤50개 주문
        Order order4 = new Order();
        order4.setUser(customer2); // 김소라
        order4.setStore(store);  //신천붕어빵
        order4.setCreateAt(LocalDateTime.now());
        order4.setProcessYn("Y");
        order4.setTotalAmount(8000L);
        order4.setMessage("빨리 만들어주세요");

        OrderProduct orderProduct5 = new OrderProduct();
        orderProduct5.setOrder(order4); // 신천붕어빵
        orderProduct5.setProduct(product); // 붕어빵
        orderProduct5.setQuantity(15L);
        orderProductRepository.save(orderProduct5);

        OrderProduct orderProduct6 = new OrderProduct();
        orderProduct6.setOrder(order4); // 신천붕어빵
        orderProduct6.setProduct(product2); // 어묵
        orderProduct6.setQuantity(10L);
        orderProductRepository.save(orderProduct6);

        OrderProduct orderProduct7 = new OrderProduct();
        orderProduct7.setOrder(order4); // 신천붕어빵
        orderProduct7.setProduct(product3); // 군밤
        orderProduct7.setQuantity(50L);
        orderProductRepository.save(orderProduct7);

        List<OrderProduct> orderProductList4 = new ArrayList<>();
        orderProductList4.add(orderProduct5);
        orderProductList4.add(orderProduct6);
        orderProductList4.add(orderProduct7);

        order4.setOrderProducts(orderProductList4);
        orderRepository.save(order4);

        // 주문 5 - 헨리가 소새울호떡집에서 호떡10개, 계란빵5개, 다코야키50개 주문
        Order order5 = new Order();
        order5.setUser(customer); // 헨리
        order5.setStore(store3);  //소새울호떡집
        order5.setCreateAt(LocalDateTime.now());
        order5.setProcessYn("Y");
        order5.setTotalAmount(30000L);
        order5.setMessage("덜 익혀주세용!!!");

        OrderProduct orderProduct8 = new OrderProduct();
        orderProduct8.setOrder(order5);
        orderProduct8.setProduct(product6); // 호떡
        orderProduct8.setQuantity(10L);
        orderProductRepository.save(orderProduct8);

        OrderProduct orderProduct9 = new OrderProduct();
        orderProduct9.setOrder(order5);
        orderProduct9.setProduct(product7); // 계란빵
        orderProduct9.setQuantity(5L);
        orderProductRepository.save(orderProduct9);

        OrderProduct orderProduct10 = new OrderProduct();
        orderProduct10.setOrder(order5);
        orderProduct10.setProduct(product8); // 다코야키
        orderProduct10.setQuantity(50L);
        orderProductRepository.save(orderProduct10);

        List<OrderProduct> orderProductList5 = new ArrayList<>();
        orderProductList5.add(orderProduct8);
        orderProductList5.add(orderProduct9);
        orderProductList5.add(orderProduct10);

        order5.setOrderProducts(orderProductList5);
        orderRepository.save(order5);


        // 주문 6 - 헨리가 신천붕어빵에서 어묵10개 주문
        Order order6 = new Order();
        order6.setUser(customer); // 헨리
        order6.setStore(store);  // 신천붕어빵
        order6.setCreateAt(LocalDateTime.now());
        order6.setProcessYn("Y");
        order6.setTotalAmount(8000L);
        order6.setMessage("리뷰이벤트 감자튀김이용 감사합니다~!!");

        OrderProduct orderProduct11 = new OrderProduct();
        orderProduct11.setOrder(order6);
        orderProduct11.setProduct(product2); // 어묵
        orderProduct11.setQuantity(10L);
        orderProductRepository.save(orderProduct11);

        List<OrderProduct> orderProductList6 = new ArrayList<>();
        orderProductList6.add(orderProduct11);

        order6.setOrderProducts(orderProductList6);
        orderRepository.save(order6);



        // 장바구니 생성
        Cart cart = new Cart();
        Cart cart2 = new Cart();
        cartRepository.save(cart);
        cartRepository.save(cart2);



        // 사용자 리뷰작성 1
        Review review = new Review();
        review.setUserId(customer.getId());
        review.setRating(5L);
        review.setStore(store);
        review.setStoreName("신천붕어빵");
        review.setContent("붕어빵 너무 맛있어요!!");
        review.setOrder(order);
        review.setImages("리뷰사진1_붕어빵");
//        review.setTimestamp(LocalDateTime.now());
        reviewRepository.save(review);

        // 사용자 리뷰작성 2
        Review review2 = new Review();
        review2.setUserId(customer.getId());
        review2.setRating(4L);
        review.setStore(store2);
        review2.setStoreName("대야붕어빵");
        review2.setContent("매일 먹어요 최고!!");
        review2.setOrder(order2);
        review2.setImages("리뷰사진2_붕어빵");
//        review2.setTimestamp(LocalDateTime.now());
        reviewRepository.save(review2);

        // 사용자 리뷰작성 3
        Review review3 = new Review();
        review3.setUserId(customer.getId());
        review3.setRating(5L);
        review3.setStore(store);
        review3.setStoreName("신천붕어빵");
        review3.setContent("붕어빵 너무 맛있어요!!");
        review3.setOrder(order3);
        review3.setImages("리뷰사진3_붕어빵");
        reviewRepository.save(review3);

        // 사용자 리뷰작성 4
        Review review4 = new Review();
        review4.setUserId(customer.getId());
        review4.setRating(5L);
        review4.setStore(store);
        review4.setStoreName("신천붕어빵");
        review4.setContent("맛있어요!!");
        review4.setOrder(order4);
        review4.setImages("리뷰사진4_붕어빵");
        reviewRepository.save(review4);

        // 사용자 리뷰작성 5
        Review review5 = new Review();
        review5.setUserId(customer.getId());
        review5.setRating(5L);
        review5.setStore(store3);
        review5.setStoreName("소새울호떡집");
        review5.setContent("넘맛있어요!!");
        review5.setOrder(order5);
        review5.setImages("리뷰사진5_호떡");
        reviewRepository.save(review5);

        // 사용자 리뷰작성 6
        Review review6 = new Review();
        review6.setUserId(customer.getId());
        review6.setRating(5L);
        review6.setStore(store3);
        review6.setStoreName("소새울호떡집");
        review6.setContent("완전맛있어요!!");
        review6.setOrder(order6);
        review6.setImages("리뷰사진6_호떡");
        reviewRepository.save(review6);


    }
}