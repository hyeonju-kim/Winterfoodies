package com.winterfoodies.winterfoodies_project.controller;


import com.winterfoodies.winterfoodies_project.dto.store.StoreDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreMainDto;
import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.repository.StoreRepository;
import com.winterfoodies.winterfoodies_project.service.SearchService;
import com.winterfoodies.winterfoodies_project.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;
    private final StoreRepository storeRepository;

    // ********** 3. 검색 **********
    // 상호명 검색
    @GetMapping
    @ApiOperation(value = "상호명 검색")
    public StoreMainDto search(@RequestParam("keyword") String keyword,
                               @RequestParam(required = false) Double latitude,
                               @RequestParam(required = false) Double longitude) {
       return searchService.searchStores(keyword, latitude, longitude);

    }

    // 지도 검색
    @GetMapping("/map")
    @ApiOperation(value = "지도로 검색 - 주변 가게 찾기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "latitude", value = "위도"),
            @ApiImplicitParam(name = "longitude", value = "경도"),
    })
    public List<StoreResponseDto> mainPage(@RequestParam(required = false) Double latitude,
                                           @RequestParam(required = false) Double longitude) {
        if (latitude != null && longitude != null) {
            // 위도와 경도가 전달된 경우
            return searchService.getNearbyStores(latitude, longitude);
        } else {
            // 위치 정보가 전달되지 않은 경우에 대한 처리
            return null;
        }
    }


}
