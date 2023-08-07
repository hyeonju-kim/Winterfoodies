package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.store.StoreDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;

import java.util.List;

public interface SearchService {
    // 상호명 검색
    List<StoreDto> searchStores(String keyword);

    // 지도로 근처 가게 검색
    List<StoreDto> searchStoresByAddressNo(String addressNo);
}