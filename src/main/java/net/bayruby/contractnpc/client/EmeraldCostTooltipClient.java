package net.bayruby.contractnpc.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class EmeraldCostTooltipClient
        implements ClientTooltipComponent {

    private final int amount;

    public EmeraldCostTooltipClient(
            EmeraldCostTooltip tooltip
    ) {
        this.amount = tooltip.amount();
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public int getWidth(Font font) {
        return 16
                + 4
                + font.width(String.valueOf(amount));
    }

    @Override
    public void renderImage(
            Font font,
            int x,
            int y,
            GuiGraphics guiGraphics
    ) {
        ItemStack emerald =
                new ItemStack(Items.EMERALD);

        guiGraphics.renderFakeItem(
                emerald,
                x,
                y
        );

        guiGraphics.drawString(
                font,
                String.valueOf(amount),
                x + 20,
                y + 4,
                0xFFFFFFFF,
                true
        );
    }
}