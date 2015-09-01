package ljfa.advbackport.handlers;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;
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
            if(tag.hasKey("CanDestroy", Constants.NBT.TAG_LIST)) {
                event.toolTip.add("");
                event.toolTip.add("Can break:");
                addBlockList(tag.getTagList("CanDestroy", Constants.NBT.TAG_STRING), event.toolTip);
            }
            if(tag.hasKey("CanPlaceOn", Constants.NBT.TAG_LIST)) {
                event.toolTip.add("");
                event.toolTip.add("Can be placed on:");
                addBlockList(tag.getTagList("CanPlaceOn", Constants.NBT.TAG_STRING), event.toolTip);
            }
        }
    }
    
    private void addBlockList(NBTTagList blockList, List<String> toolTip) {
        for(int i = 0; i < blockList.tagCount(); i++) {
            String str = blockList.getStringTagAt(i);
            Block block = Block.getBlockFromName(str);
            String name = (block != null) ? getName(block) : (EnumChatFormatting.ITALIC + "unknown: " + str);
            toolTip.add(EnumChatFormatting.DARK_GRAY + name);
        }
    }
    
    private String getName(Block block) {
        String unloc = block.getUnlocalizedName() + ".name";
        if(StatCollector.canTranslate(unloc))
            return StatCollector.translateToLocal(unloc);
        
        Item item = Item.getItemFromBlock(block);
        if(item != null)
            return item.getItemStackDisplayName(new ItemStack(item));
        
        return unloc;
    }
}
