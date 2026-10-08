package net.bayruby.contractnpc.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ShippingMenu extends AbstractContainerMenu {

    private final Container container;

    // Client constructor
    public ShippingMenu(
            int containerId,
            Inventory playerInventory
    ) {
        this(
                containerId,
                playerInventory,
                new SimpleContainer(27)
        );
    }

    // Server constructor
    public ShippingMenu(
            int containerId,
            Inventory playerInventory,
            Container container
    ) {
        super(
                ModMenus.SHIPPING_MENU.get(),
                containerId
        );

        this.container = container;

        // Shipping inventory: 3 rows × 9 columns
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(
                        new Slot(
                                container,
                                col + row * 9,
                                8 + col * 18,
                                18 + row * 18
                        )
                );
            }
        }

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(
                        new Slot(
                                playerInventory,
                                col + row * 9 + 9,
                                8 + col * 18,
                                84 + row * 18
                        )
                );
            }
        }

        // Hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(
                    new Slot(
                            playerInventory,
                            col,
                            8 + col * 18,
                            142
                    )
            );
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;

        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();

            result = stack.copy();

            if (index < 27) {
                // Shipping inventory -> player inventory
                if (!this.moveItemStackTo(stack, 27, 63, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Player inventory -> shipping inventory
                if (!this.moveItemStackTo(stack, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return result;
    }

    public Container getContainer() {
        return this.container;
    }
}