package com.winterfoodies.winterfoodies_project.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@ConfigurationProperties(prefix = "location")
public class LocationConfig {
    private double latitude;
    private double longitude;

}

