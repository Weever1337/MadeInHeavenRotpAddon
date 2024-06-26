
package com.weever.rotp_mih.client.render.entity.renderer.stand;

import com.weever.rotp_mih.RotpMadeInHeavenAddon;
import com.weever.rotp_mih.client.render.entity.model.stand.MihModel;
import com.weever.rotp_mih.entity.stand.stands.MihEntity;
import com.github.standobyte.jojo.client.render.entity.model.stand.StandEntityModel;
import com.github.standobyte.jojo.client.render.entity.model.stand.StandModelRegistry;
import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class MihRenderer extends StandEntityRenderer<MihEntity, StandEntityModel<MihEntity>> {

    public MihRenderer(EntityRendererManager renderManager) {
        super(renderManager,
                StandModelRegistry.registerModel(new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "madeinheaven"), MihModel::new),
                new ResourceLocation(RotpMadeInHeavenAddon.MOD_ID, "textures/entity/stand/mih.png"), 0);
    }
}
