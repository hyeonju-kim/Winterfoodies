package com.winterfoodies.winterfoodies_project.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 매핑에 참여하지만, 실제로 테이블로 매핑되지는 않음.
@EntityListeners(AuditingEntityListener.class) //엔티티의 생성일, 수정일을 자동으로 관리
public class Timestamped {
    @CreatedDate
    @Column(updatable = false)
    @ApiModelProperty(value = "작성 시간" , hidden = true)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @ApiModelProperty(value = "수정 시간" , hidden = true)
    protected LocalDateTime modifiedAt;
}
