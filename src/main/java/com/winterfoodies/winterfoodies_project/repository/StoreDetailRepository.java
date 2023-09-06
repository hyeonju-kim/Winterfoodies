package com.winterfoodies.winterfoodies_project.repository;

import com.winterfoodies.winterfoodies_project.entity.StoreDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreDetailRepository extends JpaRepository<StoreDetail, Long> {
    // 추가적인 메서드가 필요한 경우 여기에 정의할 수 있습니다.
}
