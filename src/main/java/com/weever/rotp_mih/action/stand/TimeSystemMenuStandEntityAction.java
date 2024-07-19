package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.GameplayUtil;
import com.weever.rotp_mih.client.ui.time_system.TimeSystemMenu;
import com.weever.rotp_mih.init.InitStands;
import net.minecraft.world.World;

public class TimeSystemMenuStandEntityAction extends CustomStandEntityAction {
    public TimeSystemMenuStandEntityAction(Builder builder) {
        super(builder);
    }

    @Override
    protected Action<IStandPower> replaceAction(IStandPower power, ActionTarget target) {
        if (GameplayUtil.getUniverseResetPlayer() == power.getUser()) {
            return InitStands.MIH_UNIVERSE_RESET.get();
        }
        return super.replaceAction(power, target);
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (world.isClientSide()) { return; }
        TimeSystemMenu.openWindowOnClick();
    }

    @Override
    public StandAction[] getExtraUnlockable() {
        return new StandAction[] {
            InitStands.MIH_UNIVERSE_RESET.get(),
            InitStands.MIH_TIME_SYSTEM_ACCELERATION.get(),
            InitStands.MIH_TIME_SYSTEM_CLEAR.get(),
            InitStands.MIH_TIME_SYSTEM_SLOW.get()
        };
    }
}