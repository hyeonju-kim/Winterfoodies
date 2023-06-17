package com.winterfoodies.winterfoodies_project;


import com.winterfoodies.winterfoodies_project.entity.*;
import com.winterfoodies.winterfoodies_project.repository.ProductRepository;
import com.winterfoodies.winterfoodies_project.repository.StoreRepository;
import com.winterfoodies.winterfoodies_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final StoreProductRepository storeProductRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        testScenario();
    }

    public void testScenario(){
        //사장 - 오주영
        User ceo = new User();
        ceo.setEmail("blessdutch@naver.com");
        ceo.setPassword("100825asa!");
        ceo.setName("김현주");

        //가게 생성
        Store store = new Store();
        store.setStatus(StoreStatus.OPEN);

        //가게 디테일 생성
        StoreDetail storeDetail = new StoreDetail();
        storeDetail.setName("현주네 다꼬야끼 가게");
        storeDetail.setBasicAddress("경기도 부천시 소사로 78번길");
        storeDetail.setDetailAddress("두산아파트 101동 1504호");
        storeDetail.setAddressNo("14774");
        storeDetail.setOfficialCodeNo("1111111111");
        storeDetail.setRoadCodeNo("222222222");
        storeDetail.setInfo("존맛탱인 저희가게 많이 놀러와주세요~!");

        //가게에게 가게 디테일 인젝션
        store.setStoreDetail(storeDetail);
        storeRepository.save(store);

        //사장님에게 가게 인젝션
        ceo.setStore(store);
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

        //사용자 - 장준혁
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


        //주문
        Order order = new Order();
        order.setUser(customer);
        order.setStore(ceo.getStore());
        order.setCreateAt(LocalDateTime.now());
        order.setProcessYn("N");
        orderRepository.save(order);

        Order order2 = new Order();
        order2.setUser(customer2);
        order2.setStore(ceo.getStore());
        order2.setCreateAt(LocalDateTime.now());
        order2.setProcessYn("N");
        orderRepository.save(order2);

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
        orderProduct3.setClientMessage("이집이 최고에요");
        orderProduct3.setQuantity(10L);
        orderProduct3.setVisitTime(LocalDateTime.now().plus(Duration.ofHours(1)));
        orderProductRepository.save(orderProduct3);

    }
}