package com.winterfoodies.winterfoodies_project;


import com.winterfoodies.winterfoodies_project.entity.*;
import com.winterfoodies.winterfoodies_project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
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

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        testScenario();
    }

    public void testScenario(){
        //사장 - 홍길동
        User ceo = new User();
        ceo.setEmail("asdf@naver.com");
        ceo.setPassword("asdf123!");
        ceo.setName("홍길동");

        //가게 생성 1
        Store store = new Store();
        store.setStatus(StoreStatus.OPEN);

        //가게 생성 2
        Store store2 = new Store();
        store2.setStatus(StoreStatus.OPEN);

        //가게 디테일 생성 1
        StoreDetail storeDetail = new StoreDetail();
        storeDetail.setName("길동이네 다꼬야끼 가게");
        storeDetail.setBasicAddress("부천역 1번 출구");
        storeDetail.setDetailAddress("부천로 11길");
        storeDetail.setAddressNo("14774");
        storeDetail.setOfficialCodeNo("1111111111");
        storeDetail.setRoadCodeNo("222222222");
        storeDetail.setInfo("존맛탱인 저희가게 많이 놀러와주세요~!");
        storeDetail.setAverageRating(4l);

        //가게 디테일 생성 2
        StoreDetail storeDetail2 = new StoreDetail();
        storeDetail2.setName("미미네 붕어빵");
        storeDetail2.setBasicAddress("신도림역 1번 출구");
        storeDetail2.setDetailAddress("신도림길 10번길 22");
        storeDetail2.setAddressNo("45612");
        storeDetail2.setOfficialCodeNo("222223333");
        storeDetail2.setRoadCodeNo("222222222");
        storeDetail2.setInfo("국산 재료만 사용합니다. 많은 관심 부탁드려요~!");
        storeDetail2.setAverageRating(5l);

        //가게에게 가게 디테일 인젝션 1
        store.setStoreDetail(storeDetail);
        storeRepository.save(store);

        //가게에게 가게 디테일 인젝션 2
        store2.setStoreDetail(storeDetail2);
        storeRepository.save(store2);

        //사장님에게 가게 인젝션 1
        ceo.setStore(store);
        userRepository.save(ceo);

        //사장님에게 가게 인젝션 1
        ceo.setStore(store2);
        userRepository.save(ceo);


        //상품 생성
        Product product = new Product();
        product.setName("붕어빵");
        product.setPrice(1500L);

        Product product2 = new Product();
        product2.setName("델리만쥬");
        product2.setPrice(3000L);

        productRepository.save(product);
        productRepository.save(product2);

        //가게에 상품 생성 후 연결
        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setStore(store);
        storeProduct.setProduct(product);
        storeProductRepository.save(storeProduct);

        StoreProduct storeProduct1 = new StoreProduct();
        storeProduct1.setStore(store);
        storeProduct1.setProduct(product2);
        storeProductRepository.save(storeProduct1);

        //사용자 - 류현수, 김소라, 방성훈
        User customer = new User();
        customer.setPassword("33");
        customer.setEmail("aa@kbanknow.com");
        customer.setName("류현수");
        userRepository.save(customer);

        User customer2 = new User();
        customer2.setPassword("100825asa!");
        customer2.setEmail("bb@kbanknow.com");
        customer2.setName("김소라");
        userRepository.save(customer2);

        User customer3 = new User();
        customer3.setPassword("100825asa111!");
        customer3.setEmail("bang@kbanknow.com");
        customer3.setName("방성훈");
        userRepository.save(customer3);

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
        review.setStoreName("미미붕어빵");
        review.setContent("붕어빵 너무 맛있어요!!");
//        review.setTimestamp(LocalDateTime.now());
        reviewRepository.save(review);

        // 사용자 리뷰작성 2
        Review review2 = new Review();
        review2.setUserId(customer.getId());
        review2.setRating(4L);
        review2.setStoreName("서울붕어빵");
        review2.setContent("매일 먹어요 최고!!");
//        review2.setTimestamp(LocalDateTime.now());
        reviewRepository.save(review2);

        //주문
        Order order = new Order();
        order.setUser(customer);
        order.setStore(ceo.getStore());
        order.setCreateAt(LocalDateTime.now());
        order.setProcessYn("N");
        order.setTotalAmount(20000L);
        List<OrderProduct> orderProductList = new ArrayList<>();
        orderProductList.add(new OrderProduct(new Product("호떡", 2L)));
        orderProductList.add(new OrderProduct(new Product("어묵", 4L)));
        order.setOrderProducts(orderProductList);
        orderRepository.save(order);

        Order order2 = new Order();
        order2.setUser(customer2); // 김소라
        order2.setStore(ceo.getStore());
        order2.setCreateAt(LocalDateTime.now());
        order2.setProcessYn("N");
        order2.setTotalAmount(10000L);
        List<OrderProduct> orderProductList2 = new ArrayList<>();
        orderProductList2.add(new OrderProduct(new Product("다코야끼", 10L)));
        orderProductList2.add(new OrderProduct(new Product("호빵", 4L)));
        order2.setOrderProducts(orderProductList2);
        orderRepository.save(order2);

        Order order3 = new Order();
        order3.setUser(customer2); // 김소라
        order3.setStore(ceo.getStore());
        order3.setCreateAt(LocalDateTime.now());
        order3.setProcessYn("Y");
        order3.setTotalAmount(8000L);
        List<OrderProduct> orderProductList3 = new ArrayList<>();
        orderProductList3.add(new OrderProduct(new Product("붕어빵",20L)));
        orderProductList3.add(new OrderProduct(new Product("어묵", 5L)));
        order3.setOrderProducts(orderProductList3);
        orderRepository.save(order3);

        //주문상세
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrder(order);
        orderProduct.setProduct(product);
        orderProduct.setClientMessage("빨리 만들어주세요");
        orderProduct.setQuantity(3L);
        orderProduct.setVisitTime(LocalDateTime.now().plus(Duration.ofHours(1)));
        orderProductRepository.save(orderProduct);


        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setOrder(order);
        orderProduct2.setProduct(product2);
        orderProduct2.setClientMessage("덜 익혀주세요");
        orderProduct2.setQuantity(2L);
        orderProduct2.setVisitTime(LocalDateTime.now().plus(Duration.ofHours(1)));
        orderProductRepository.save(orderProduct2);

        OrderProduct orderProduct3 = new OrderProduct();
        orderProduct3.setOrder(order2);
        orderProduct3.setProduct(product);
        orderProduct3.setClientMessage("리뷰이벤트 감자튀김이요");
        orderProduct3.setQuantity(10L);
        orderProduct3.setVisitTime(LocalDateTime.now().plus(Duration.ofHours(1)));
        orderProductRepository.save(orderProduct3);

    }
}