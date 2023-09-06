package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.store.StoreDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreMainDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.entity.Store;
import com.winterfoodies.winterfoodies_project.repository.ReviewRepository;
import com.winterfoodies.winterfoodies_project.repository.StoreRepository;
import com.winterfoodies.winterfoodies_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;

    // 상호명 검색
    @Override
    public StoreMainDto searchStores(String keyword, double latitude, double longitude) {
        double radius = 5.0; // 검색 반경 설정 (예: 5.0km)
        StoreMainDto storeMainDto = new StoreMainDto();
        List<Store> storeList = storeRepository.searchStores(keyword, latitude, longitude, radius);
        storeMainDto.setSearchCnt((long) storeList.size());

        List<StoreResponseDto> storeDtoList = new ArrayList<>();
        for (Store store : storeList) {
            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setStoreId(store.getId());
            System.out.println("storeId======" + store.getId());
            storeResponseDto.setName(store.getStoreDetail().getName());
            storeResponseDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
            storeResponseDto.setAverageRating(store.getStoreDetail().getAverageRating());
            storeResponseDto.setLatitude(store.getStoreDetail().getLatitude());
            storeResponseDto.setLongitude(store.getStoreDetail().getLongitude());
            storeResponseDto.setThumbNailImgUrl(store.getStoreDetail().getThumbnailImgUrl());
            storeDtoList.add(storeResponseDto);
        }
        storeMainDto.setStoreResponseDtoList(storeDtoList);
        return storeMainDto;
    }


//    // 지도로 근처 가게 검색 (addressNo가 같은 가게 반환)
//    @Override
//    public List<StoreDto> searchStoresByAddressNo(String addressNo) {
//        List<Store> storeList = storeRepository.searchStoresByAddressNo(addressNo);
//
//        List<StoreDto> storeDtoList = new ArrayList<>();
//        for (Store store : storeList) {
//            StoreDto storeDto = new StoreDto();
//            storeDto.setId(store.getId());
//            storeDto.setName(store.getStoreDetail().getName());
//            storeDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
//            storeDto.setAverageRating(store.getStoreDetail().getAverageRating());
//            storeDto.setLatitude(store.getStoreDetail().getLatitude());
//            storeDto.setLongitude(store.getStoreDetail().getLongitude());
//
//            storeDtoList.add(storeDto);
//        }
//        return storeDtoList;
//    }



    // 지도로 근처 가게 불러오기
    @Override
    public List<StoreResponseDto> getNearbyStores(double latitude, double longitude) {
        double radius = 5.0;

        List<Store> nearbyStores = storeRepository.findNearbyStores(latitude, longitude, radius);
        List<StoreResponseDto> nearbyStoreDtoList = new ArrayList<>();

        for (Store store : nearbyStores) {
            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setStoreId(store.getId());
            storeResponseDto.setName(store.getStoreDetail().getName());
            storeResponseDto.setMapIcon(store.getStoreDetail().getMapIcon());
            storeResponseDto.setAverageRating(store.getStoreDetail().getAverageRating());
            Long reviewCnt = reviewRepository.countByStoreId(store.getId());
            storeResponseDto.setCountReviews(reviewCnt);
            nearbyStoreDtoList.add(storeResponseDto);
            storeResponseDto.setLatitude(store.getStoreDetail().getLatitude());
            storeResponseDto.setLongitude(store.getStoreDetail().getLongitude());
        }

        return nearbyStoreDtoList;
    }
}
