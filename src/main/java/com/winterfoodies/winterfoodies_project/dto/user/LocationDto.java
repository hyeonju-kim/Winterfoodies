package com.winterfoodies.winterfoodies_project.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@RequiredArgsConstructor
public class LocationDto {
    private double latitude;
    private double longitude;
}
