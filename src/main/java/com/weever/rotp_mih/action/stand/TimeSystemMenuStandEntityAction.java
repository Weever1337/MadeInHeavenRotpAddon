package com.weever.rotp_mih.action.stand;

import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.weever.rotp_mih.client.ui.time_system.TimeSystemMenu;
import com.weever.rotp_mih.init.InitStands;
import com.weever.rotp_mih.utils.GameplayUtil;
import com.weever.rotp_mih.utils.TimeUtil;
import net.minecraft.world.World;

public class TimeSystemMenuStandEntityAction extends StandEntityAction {
    public TimeSystemMenuStandEntityAction(Builder builder) {
        super(builder);
    }

//    @Override
//    protected Action<IStandPower> replaceAction(IStandPower power, ActionTarget target) {
//        if (GameplayUtil.getUniverseResetPlayer() == power.getUser()) {
//            return InitStands.MIH_UNIVERSE_RESET.get();
//        }
//        return super.replaceAction(power, target);
//    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (!world.isClientSide()) { return; }
        TimeSystemMenu.openWindowOnClick();
        System.out.println(TimeUtil.getGlobalValue().getOwner());
        System.out.println(TimeUtil.getGlobalValue().getValue());
    }

    @Override
    public StandAction[] getExtraUnlockable() {
        return new StandAction[] {
            InitStands.MIH_UNIVERSE_RESET.get(),
            InitStands.MIH_TIME_SYSTEM_ACCELERATION.get(),
            InitStands.MIH_TIME_SYSTEM_CLEAR.get()
        };
    }
}