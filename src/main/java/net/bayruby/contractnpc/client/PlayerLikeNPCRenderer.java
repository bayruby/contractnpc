package net.bayruby.contractnpc.client;

import net.bayruby.contractnpc.entity.PlayerLikeNPC;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PlayerLikeNPCRenderer extends HumanoidMobRenderer<PlayerLikeNPC, HumanoidModel<PlayerLikeNPC>> {
    public PlayerLikeNPCRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(PlayerLikeNPC entity) {
        return new ResourceLocation("minecraft", "textures/entity/player/slim/alex.png");
    }
}
