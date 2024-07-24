package com.weever.rotp_mih.utils;

import com.weever.rotp_mih.GameplayUtil;

import java.util.UUID;

public class TimeData {
    private final UUID player;
    private final GameplayUtil.Values value;

    public TimeData(UUID player, GameplayUtil.Values value) {
        this.player = player;
        this.value = value;
    }

    public UUID getPlayer() {
        return player;
    }

    public GameplayUtil.Values getValue() {
        return value;
    }
}