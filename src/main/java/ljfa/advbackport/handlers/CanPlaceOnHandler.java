package ljfa.advbackport.handlers;

import ljfa.advbackport.Config;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemSign;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CanPlaceOnHandler {
    //Server side
    @SubscribeEvent
    public void onPlace(PlaceEvent event) {
        if(event.player.capabilities.isCreativeMode)
            return;
        //Bone meal is behaving derpy
        if(event.itemInHand.getItem() == Items.dye && event.itemInHand.getItemDamage() == 15)
            return;
        
        NBTTagList canPlaceList = getCanPlaceList(event.itemInHand);
        if(canPlaceList != null) {
            for(int i = 0; i < canPlaceList.tagCount(); i++) {
                String str = canPlaceList.getStringTagAt(i);
                if(Block.getBlockFromName(str) == event.placedAgainst)
                    return;
            }
        }
        event.setCanceled(true);
    }
    
    /* Client Side (unless affectInteraction is turned on)
     * Ugly implementation because BlockEvent.PlaceEvent is not being fired on the client side.
     */
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.entityPlayer.capabilities.isCreativeMode
                || (!event.world.isRemote && Config.affectInteraction))
            return;
        
        ItemStack stack = event.entityPlayer.getHeldItem();
        NBTTagList canPlaceList = getCanPlaceList(stack);
        if(canPlaceList != null) {
            Block block = event.world.getBlock(event.x, event.y, event.z);
            
            for(int i = 0; i < canPlaceList.tagCount(); i++) {
                String str = canPlaceList.getStringTagAt(i);
                if(Block.getBlockFromName(str) == block)
                    return;
            }
        }
        if(Config.affectInteraction || stack != null && isPlaceable(stack.getItem())) 
            event.setCanceled(true);
    }
    
    private NBTTagList getCanPlaceList(ItemStack stack) {
        if(stack == null)
            return null;
        NBTTagCompound tag = stack.getTagCompound();
        if(tag != null && tag.hasKey("CanPlaceOn", Constants.NBT.TAG_LIST))
            return tag.getTagList("CanPlaceOn", Constants.NBT.TAG_STRING);
        else
            return null;
    }
    
    /** This is a heuristic function which should only be used at the client side!
     * It only covers ItemBlocks and vanilla placeable items.
     */
    private boolean isPlaceable(Item item) {
        return item instanceof ItemBlock
            || item instanceof ItemReed
            || item instanceof ItemBed
            || item instanceof ItemRedstone
            || item instanceof IPlantable
            || item instanceof ItemSign
            || item instanceof ItemSkull;
    }
}
