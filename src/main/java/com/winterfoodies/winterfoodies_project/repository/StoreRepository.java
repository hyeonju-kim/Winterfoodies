package com.winterfoodies.winterfoodies_project.repository;

import com.winterfoodies.winterfoodies_project.entity.Review;
import com.winterfoodies.winterfoodies_project.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    // 1. 가까운 가게 가져오기 . JPQL 사용
    // H2 데이터베이스에서 주어진 반경 내에 있는 가게를 조회. Haversine 공식을 사용하여 두 지점 간의 거리를 계산하고, 6371을 곱해 거리를 킬로미터 단위로 변환.
    // 1킬로미터 이내에 있는 모든 가게 가져옴
    @Query("SELECT s FROM Store s WHERE " +
            "6371 * acos(cos(radians(:latitude)) * cos(radians(s.storeDetail.latitude)) * cos(radians(s.storeDetail.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(s.storeDetail.latitude))) < :radius")
    List<Store> findNearbyStores(@Param("latitude") double latitude, @Param("longitude") double longitude, @Param("radius") double radius);


    // 2. 상품별로 가까운 가게 가져오기 (2중조인) -230830추가
    @Query("SELECT s FROM Store s JOIN s.storeDetail sd JOIN s.storeProducts sp WHERE " +
            "sp.product.id = :productId AND " + // productId에 해당하는 것만 가져오도록
            "6371 * acos(cos(radians(:latitude)) * cos(radians(sd.latitude)) * cos(radians(sd.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(sd.latitude))) < :radius")
    List<Store> findNearbyStoresByProductId(@Param("productId") Long productId, @Param("latitude") double latitude, @Param("longitude") double longitude, @Param("radius") double radius);


//    // 판매량 순으로 정렬하여 가게목록 가져오기 (신천3주문 - 소새울2주문 - 대야1주문)
//    @Query("SELECT s FROM Store s JOIN s.orders o GROUP BY s.id ORDER BY SUM(o.size) DESC")
//    List<Store> getStoresSortedByMenuSales();
//
//    // 상품별, 리뷰순으로 정렬하여 가게목록 가져오기 (신천3리뷰 - 소새울2리뷰 - 대야1리뷰)
//    @Query("SELECT s FROM Store s JOIN s.reviews r GROUP BY s.id ORDER BY SUM(r.size) DESC ")
//    List<Store> getStoreByReviews();

    // 3. 인기순(판매량 순)으로 정렬하여 가게목록 가져오기 - 쿼리 삭제하고 프론트에서 처리하기로 함 8/26
//    @Query("SELECT s FROM Store s JOIN s.orders o JOIN s.storeProducts sp WHERE "+
//            "sp.product.id = :productId " + // productId에 해당하는 것만 가져오도록
//            "GROUP BY s.id")
//    List<Store> getStoresSortedByMenuSalesByProductId(@Param("productId") Long productId);

    @Query("SELECT s FROM Store s JOIN s.storeDetail sd JOIN s.storeProducts sp WHERE " +
            "sp.product.id = :productId AND " + // productId에 해당하는 것만 가져오도록
            "6371 * acos(cos(radians(:latitude)) * cos(radians(sd.latitude)) * cos(radians(sd.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(sd.latitude))) < :radius " +
            "GROUP BY s.id")
    List<Store> getStoresSortedByMenuSalesByProductId(@Param("productId") Long productId, @Param("latitude") double latitude, @Param("longitude") double longitude, @Param("radius") double radius);


    // 4. 상품별, 리뷰순으로 정렬하여 가게목록 가져오기  - 쿼리 삭제하고 프론트에서 처리하기로 함 8/26
//    @Query("SELECT s FROM Store s JOIN s.reviews r JOIN s.storeProducts sp WHERE "+
//            "sp.product.id = :productId  " + // productId에 해당하는 것만 가져오도록
//            "GROUP BY s.id")
//    List<Store> getStoreByReviewsByProductId(@Param("productId") Long productId);

    @Query("SELECT s FROM Store s JOIN s.storeDetail sd JOIN s.storeProducts sp JOIN s.reviews r WHERE " +
            "sp.product.id = :productId AND " +
            "6371 * acos(cos(radians(:latitude)) * cos(radians(sd.latitude)) * cos(radians(sd.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(sd.latitude))) < :radius " +
            "GROUP BY s.id " +
            "ORDER BY COUNT(r.id) DESC")
    List<Store> getStoreByReviewsByProductId(@Param("productId") Long productId, @Param("latitude") double latitude, @Param("longitude") double longitude, @Param("radius") double radius);


    // 5. 별점순으로 가게목록 가져오기 - 230830 추가
//    @Query("SELECT s FROM Store s JOIN s.storeDetail sd JOIN s.storeProducts sp WHERE "+
//            "sp.product.id = :productId  " + // productId에 해당하는 것만 가져오도록
//            "ORDER BY sd.averageRating")
//    List<Store> getStoreByAverageRatingByProductId(@Param("productId") Long productId);


    @Query("SELECT s FROM Store s JOIN s.storeDetail sd JOIN s.storeProducts sp WHERE " +
            "sp.product.id = :productId AND " +
            "6371 * acos(cos(radians(:latitude)) * cos(radians(sd.latitude)) * cos(radians(sd.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(sd.latitude))) < :radius " +
            "ORDER BY sd.averageRating DESC")
    List<Store> getStoreByAverageRatingByProductId(@Param("productId") Long productId, @Param("latitude") double latitude, @Param("longitude") double longitude, @Param("radius") double radius);



    // ==================== 검색 ======================
    // 상호명 검색
    @Query("SELECT s FROM Store s WHERE s.storeDetail.name LIKE %:keyword%")
    List<Store> searchStores(@Param("keyword") String keyword);

    // 지도로 근처가게 검색 (addressNo가 같은 가게 검색)
    @Query("SELECT s FROM Store s WHERE s.storeDetail.addressNo = :addressNo")
    List<Store> searchStoresByAddressNo(@Param("addressNo") String addressNo);
}
