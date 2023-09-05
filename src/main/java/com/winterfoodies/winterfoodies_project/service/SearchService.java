package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.store.StoreDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreMainDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;

import java.util.List;

public interface SearchService {
    // 상호명 검색 (5km내)
    StoreMainDto searchStores(String keyword, double latitude, double longitude);

    // 지도로 근처 가게 검색 (5km내)
//    List<StoreDto> searchStoresByAddressNo(String addressNo);

    List<StoreResponseDto> getNearbyStores(double latitude, double longitude);

}

