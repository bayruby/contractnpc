package net.bayruby.contractnpc.client;

import net.bayruby.contractnpc.entity.PlayerLikeNPC;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PlayerLikeNPCRenderer extends MobRenderer<PlayerLikeNPC, PlayerModel<PlayerLikeNPC>> {
    private static final ResourceLocation TEXTURE = 
        ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/player/slim/alex.png");

    public PlayerLikeNPCRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(PlayerLikeNPC entity) {
        return TEXTURE;
    }
}
