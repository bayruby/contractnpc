package net.bayruby.contractnpc.client;

import net.bayruby.contractnpc.entity.HumanoidMob;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HumanoidMobRenderer
        extends MobRenderer<
        HumanoidMob,
        HumanoidModel<HumanoidMob>
        > {

    /*
     * Minecraft's Steve texture.
     */
    private static final ResourceLocation TEXTURE =
            ResourceLocation.withDefaultNamespace(
                    "textures/entity/player/wide/steve.png"
            );


    public HumanoidMobRenderer(
            EntityRendererProvider.Context context
    ) {

        super(
                context,

                new HumanoidModel<>(
                        context.bakeLayer(
                                ModelLayers.PLAYER
                        )
                ),

                0.5F
        );
    }


    /*
     * Tell Minecraft which texture to use.
     */
    @Override
    public ResourceLocation getTextureLocation(
            HumanoidMob entity
    ) {

        return TEXTURE;
    }
}