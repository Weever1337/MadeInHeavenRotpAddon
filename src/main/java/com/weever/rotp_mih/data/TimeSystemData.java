package com.weever.rotp_mih.data;

import com.weever.rotp_mih.utils.TimeUtil;

import java.util.UUID;

public class TimeSystemData {
    private final UUID owner;
    private final TimeUtil.Values value;

    public TimeSystemData(UUID player, TimeUtil.Values value) {
        this.owner = player;
        this.value = value;
    }

    public UUID getOwner() {
        return owner;
    }

    public TimeUtil.Values getValue() {
        return value;
    }
}