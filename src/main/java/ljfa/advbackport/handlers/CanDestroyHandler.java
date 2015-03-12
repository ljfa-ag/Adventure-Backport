package ljfa.advbackport.handlers;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CanDestroyHandler {
    @SubscribeEvent
    public void onBreakSpeed(BreakSpeed event) {
        ItemStack tool = event.entityPlayer.getCurrentEquippedItem();
        if(tool == null) {
            event.setCanceled(true);
            return;
        }
        
        Cache cache = event.entityPlayer.worldObj.isRemote ? clientCache : serverCache;
        if(tool == cache.tool && event.block == cache.block) {
            if(!cache.canDestroy)
                event.setCanceled(true);
            return;
        }
        else {
            cache.tool = tool;
            cache.block = event.block;
            
            NBTTagCompound tag = tool.getTagCompound();
            if(tag == null || !tag.hasKey("CanDestroy", Constants.NBT.TAG_LIST)) {
                event.setCanceled(true);
                cache.canDestroy = false;
                return;
            }
            NBTTagList canDestrList = tag.getTagList("CanDestroy", Constants.NBT.TAG_STRING);
            for(int i = 0; i < canDestrList.tagCount(); i++) {
                String str = canDestrList.getStringTagAt(i);
                if(Block.getBlockFromName(str) == event.block) {
                    cache.canDestroy = true;
                    return;
                }
            }
            cache.canDestroy = false;
            event.setCanceled(true);
        }
    }
    
    private static class Cache {
        public ItemStack tool = null;
        public Block block = null;
        public boolean canDestroy = false;
    }
    
    private Cache serverCache = new Cache(), clientCache = new Cache();
}
