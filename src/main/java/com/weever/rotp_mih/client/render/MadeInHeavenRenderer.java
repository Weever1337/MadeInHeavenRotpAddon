package com.weever.rotp_mih.client.render;

import com.github.standobyte.jojo.client.render.entity.model.stand.StandEntityModel;
import com.github.standobyte.jojo.client.render.entity.model.stand.StandModelRegistry;
import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;
import com.weever.rotp_mih.MadeInHeavenAddon;
import com.weever.rotp_mih.entity.MadeInHeavenEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class MadeInHeavenRenderer extends StandEntityRenderer<MadeInHeavenEntity, StandEntityModel<MadeInHeavenEntity>> {

    public MadeInHeavenRenderer(EntityRendererManager renderManager) {
        super(renderManager,
                StandModelRegistry.registerModel(new ResourceLocation(MadeInHeavenAddon.MOD_ID, "madeinheaven"), MadeInHeavenModel::new),
                new ResourceLocation(MadeInHeavenAddon.MOD_ID, "textures/entity/stand/mih.png"), 0);
    }
}
