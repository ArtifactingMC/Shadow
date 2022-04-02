/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.text.Text;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.exception.CommandException;
import net.shadow.client.helper.util.Utils;

import java.util.Objects;

public class Rename extends Command {

    public Rename() {
        super("Rename", "Renames an item (requires creative)", "rename", "rn", "name");
    }

    @Override
    public String[] getSuggestions(String fullCommand, String[] args) {
        if (args.length == 1) {
            return new String[]{"(new item name)"};
        }
        return super.getSuggestions(fullCommand, args);
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1);

        if (Objects.requireNonNull(ShadowMain.client.player).getInventory().getMainHandStack().isEmpty()) {
            error("You're not holding anything");
            return;
        }
        ItemStack iStack = ShadowMain.client.player.getInventory().getMainHandStack();
        iStack.setCustomName(Text.of("§r" + String.join(" ", args).replaceAll("&", "§")));
        if (!ShadowMain.client.interactionManager.hasCreativeInventory()) {
            warn("You dont have creative mode; the item will only be renamed client side");
        } else {
            ShadowMain.client.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(Utils.Inventory.slotIndexToId(ShadowMain.client.player.getInventory().selectedSlot), iStack));
        }
    }
}
