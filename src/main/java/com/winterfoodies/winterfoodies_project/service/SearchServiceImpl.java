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

            storeDtoList.add(storeDto);
        }
        return storeDtoList;
    }

    // 지도로 근처 가게 검색 (addressNo가 같은 가게 반환)
    @Override
    public List<StoreDto> searchStoresByAddressNo(String addressNo) {
        List<Store> storeList = storeRepository.searchStoresByAddressNo(addressNo);

        List<StoreDto> storeDtoList = new ArrayList<>();
        for (Store store : storeList) {
            StoreDto storeDto = new StoreDto();
            storeDto.setId(store.getId());
            storeDto.setName(store.getStoreDetail().getName());
            storeDto.setBasicAddress(store.getStoreDetail().getBasicAddress());
            storeDto.setAverageRating(store.getStoreDetail().getAverageRating());

            storeDtoList.add(storeDto);
        }
        return storeDtoList;
    }
}
