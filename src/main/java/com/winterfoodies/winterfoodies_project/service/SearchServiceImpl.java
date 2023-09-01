package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.store.StoreDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.entity.Store;
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

    // 상호명 검색
    @Override
    public List<StoreDto> searchStores(String keyword) {
        List<Store> storeList = storeRepository.searchStores(keyword);

        List<StoreDto> storeDtoList = new ArrayList<>();
        for (Store store : storeList) {
            StoreDto storeDto = new StoreDto();
            storeDto.setId(store.getId());
            System.out.println("storeId======" + store.getId());
            storeDto.setName(store.getStoreDetail().getName());
            storeDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
            storeDto.setAverageRating(store.getStoreDetail().getAverageRating());
            storeDto.setLatitude(store.getStoreDetail().getLatitude());
            storeDto.setLongitude(store.getStoreDetail().getLongitude());
            storeDtoList.add(storeDto);
        }
        return storeDtoList;
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



    // 근처 가게 불러오기
    @Override
    public List<StoreResponseDto> getNearbyStores(double latitude, double longitude) {
        double radius = 2.0; // 검색 반경 설정 (예: 2.0km)

        List<Store> nearbyStores = storeRepository.findNearbyStores(latitude, longitude, radius);
        List<StoreResponseDto> nearbyStoreDtoList = new ArrayList<>();

        for (Store store : nearbyStores) {
            StoreResponseDto storeResponseDto = new StoreResponseDto();
            storeResponseDto.setStoreId(store.getId());
            storeResponseDto.setName(store.getStoreDetail().getName());
            nearbyStoreDtoList.add(storeResponseDto);
            storeResponseDto.setLatitude(store.getStoreDetail().getLatitude());
            storeResponseDto.setLongitude(store.getStoreDetail().getLongitude());
        }

        return nearbyStoreDtoList;
    }
}
