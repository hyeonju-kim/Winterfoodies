package com.winterfoodies.winterfoodies_project.dto.dashboard;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DashBoard {
    //누적 주문 수
    private Long accumulatedOrderCount;

    //상단 카드배너 (4개)
    private List<TopInfoCard> topInfoCards = new ArrayList<>();

    //알림 메세지
    private List<Message> alertMessages = new ArrayList<>();

}
