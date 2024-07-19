package com.weever.rotp_mih.utils;

import com.weever.rotp_mih.GameplayUtil;
import net.minecraft.entity.player.PlayerEntity;

public class TimeData {
    private final PlayerEntity player;
    private final GameplayUtil.Values value;

    public TimeData(PlayerEntity player, GameplayUtil.Values value) {
        this.player = player;
        this.value = value;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public GameplayUtil.Values getValue() {
        return value;
    }
}