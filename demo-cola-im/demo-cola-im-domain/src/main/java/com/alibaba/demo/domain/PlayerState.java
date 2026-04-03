package com.alibaba.demo.domain;

import lombok.Data;

@Data
public class PlayerState {

    private final Long playerId;
    private int x;
    private int y;

    public PlayerState(Long playerId) {
        this.playerId = playerId;
        this.x = 0;
        this.y = 0;
    }
}
