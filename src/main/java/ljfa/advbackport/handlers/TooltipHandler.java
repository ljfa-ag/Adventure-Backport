package ljfa.advbackport.handlers;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TooltipHandler {
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        NBTTagCompound tag = event.itemStack.getTagCompound();
        if(tag != null) {
            if(tag.hasKey("CanDestroy", 9)) {
                event.toolTip.add("");
                event.toolTip.add("Can break:");
                addBlockList(tag.getTagList("CanDestroy", 8), event.toolTip);
            }
            if(tag.hasKey("CanPlaceOn", 9)) {
                event.toolTip.add("");
                event.toolTip.add("Can be placed on:");
                addBlockList(tag.getTagList("CanPlaceOn", 8), event.toolTip);
            }
        }
    }
    
    private void addBlockList(NBTTagList blockList, List<String> toolTip) {
        for(int i = 0; i < blockList.tagCount(); i++) {
            String str = blockList.getStringTagAt(i);
            Block block = Block.getBlockFromName(str);
            String name = block != null ? block.getLocalizedName() : str;
            toolTip.add(EnumChatFormatting.DARK_GRAY + name);
        }
    }
}
