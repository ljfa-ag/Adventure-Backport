package ljfa.advbackport.handlers;

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
        if(tag != null && tag.hasKey("CanDestroy", 9)) {
            NBTTagList canDestrList = tag.getTagList("CanDestroy", 8);
            event.toolTip.add("Can break:");
            for(int i = 0; i < canDestrList.tagCount(); i++) {
                String str = canDestrList.getStringTagAt(i);
                event.toolTip.add(EnumChatFormatting.DARK_GRAY + str);
            }
        }
    }
}
