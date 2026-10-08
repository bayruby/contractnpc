package net.bayruby.contractnpc.shipping;

import net.bayruby.contractnpc.ContractNpc;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(
                    net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
                    ContractNpc.MODID
            );

    public static final DeferredHolder<
            AttachmentType<?>,
            AttachmentType<ShippingInventory>
            > SHIPPING_INVENTORY =
            ATTACHMENTS.register(
                    "shipping_inventory",
                    () -> AttachmentType.builder(
                            ShippingInventory::new
                    ).build()
            );
}