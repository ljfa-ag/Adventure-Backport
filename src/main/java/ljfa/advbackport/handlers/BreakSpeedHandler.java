package ljfa.advbackport.handlers;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BreakSpeedHandler {
    @SubscribeEvent
    public void onBreakSpeed(BreakSpeed event) {
        ItemStack tool = event.entityPlayer.getCurrentEquippedItem();
        if(tool == null) {
            event.setCanceled(true);
            return;
        }
        NBTTagCompound tag = tool.getTagCompound();
        if(tag == null || !tag.hasKey("CanDestroy", 9)) {
            event.setCanceled(true);
            return;
        }
        NBTTagList canDestrList = tag.getTagList("CanDestroy", 8);
        for(int i = 0; i < canDestrList.tagCount(); i++) {
            String str = canDestrList.getStringTagAt(i);
            if(Block.getBlockFromName(str) == event.block)
                return;
        }
        event.setCanceled(true);
    }
}
