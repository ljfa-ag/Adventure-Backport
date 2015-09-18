package de.ljfa.advbackport.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.ljfa.advbackport.Config;
import de.ljfa.advbackport.logic.ItemLogic;
import de.ljfa.advbackport.logic.PlayerLogic;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemSign;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

public class CanPlaceOnHandler {
    //Server side
    @SubscribeEvent
    public void onPlace(PlaceEvent event) {
        if(PlayerLogic.getGameType(event.player) != GameType.ADVENTURE)
            return;
        
        if(event.itemInHand == null) {
            event.setCanceled(true);
            return;
        }
        
        //Bone meal is behaving derpy
        if((event.itemInHand.getItem() == Items.dye && event.itemInHand.getItemDamage() == 15)
                || Config.alwaysPlaceable.contains(event.itemInHand.getItem()))
            return;
        
        if(!ItemLogic.canPlaceOn(event.itemInHand, event.placedAgainst))
            event.setCanceled(true);
    }
    
    /* Client Side (unless affectInteraction is turned on)
     * Ugly implementation because BlockEvent.PlaceEvent is not being fired on the client side.
     */
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
                || PlayerLogic.getGameType(event.entityPlayer) != GameType.ADVENTURE
                || (!event.world.isRemote && Config.affectInteraction))
            return;
        
        ItemStack stack = event.entityPlayer.getHeldItem();
        if(stack == null || Config.alwaysPlaceable.contains(stack.getItem())
                || ItemLogic.canPlaceOn(stack, event.world.getBlock(event.x, event.y, event.z)))
            return;
        
        if(Config.affectInteraction || isPlaceable(stack.getItem())) 
            event.setCanceled(true);
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
