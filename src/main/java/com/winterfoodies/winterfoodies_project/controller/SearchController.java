package com.winterfoodies.winterfoodies_project.controller;


import com.winterfoodies.winterfoodies_project.dto.store.StoreResponseDto;
import com.winterfoodies.winterfoodies_project.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {
    private final UserService userService;


    // ********** 3. 검색 **********
    // 상호명 검색
    @GetMapping("/search")
    @ApiOperation(value = "상호명 검색")
    public List<StoreResponseDto> search(@RequestParam("keyword") String keyword) {
        return userService.searchStores(keyword);
    }

    // 지도 검색
    @GetMapping("/search/map")
    @ApiOperation(value = "지도로 검색")
    public List<StoreResponseDto> searchMap(@RequestParam("addressNo") String addressNo) {
        return userService.searchStoresByAddressNo(addressNo);
    }


}
