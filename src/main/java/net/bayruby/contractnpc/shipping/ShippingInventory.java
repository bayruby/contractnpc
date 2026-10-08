package net.bayruby.contractnpc.shipping;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class ShippingInventory {

    private final SimpleContainer inventory = new SimpleContainer(27);

    public SimpleContainer getInventory() {
        return inventory;
    }

    public CompoundTag save(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);

            if (!stack.isEmpty()) {
                tag.put(
                        "slot_" + i,
                        stack.save(provider)
                );
            }
        }

        return tag;
    }

    public void load(
            CompoundTag tag,
            HolderLookup.Provider provider
    ) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {

            String key = "slot_" + i;

            if (tag.contains(key)) {
                inventory.setItem(
                        i,
                        ItemStack.parseOptional(
                                provider,
                                tag.getCompound(key)
                        )
                );
            }
        }
    }
}
