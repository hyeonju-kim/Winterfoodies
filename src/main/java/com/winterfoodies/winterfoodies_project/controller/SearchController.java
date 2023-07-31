package com.winterfoodies.winterfoodies_project.controller;


import com.winterfoodies.winterfoodies_project.dto.store.StoreDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.service.SearchService;
import com.winterfoodies.winterfoodies_project.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    // ********** 3. 검색 **********
    // 상호명 검색
    @GetMapping("/{keyword}")
    @ApiOperation(value = "상호명 검색")
    public List<StoreResponseDto> search(@PathVariable("keyword") String keyword) {
        List<StoreDto> storeDtoList = searchService.searchStores(keyword);
        ArrayList<StoreResponseDto> storeResponseDtoList = new ArrayList<>();
        for (StoreDto storeDto : storeDtoList) {
            StoreResponseDto storeResponseDto = storeDto.convertToStoreResponseDto();
            storeResponseDtoList.add(storeResponseDto);
        }
        return storeResponseDtoList;
    }

    // 지도 검색
    @GetMapping("/map/{addressNo}")
    @ApiOperation(value = "지도로 검색")
    public List<StoreResponseDto> searchMap(@PathVariable("addressNo") String addressNo) {
        List<StoreDto> storeDtoList = searchService.searchStoresByAddressNo(addressNo);
        System.out.println(storeDtoList);
        ArrayList<StoreResponseDto> storeResponseDtoList = new ArrayList<>();
        for (StoreDto storeDto : storeDtoList) {
            StoreResponseDto storeResponseDto = storeDto.convertToStoreResponseDto();
            System.out.println(storeResponseDto);
            storeResponseDtoList.add(storeResponseDto);
        }
        System.out.println(storeResponseDtoList);
        return storeResponseDtoList;
    }


}
