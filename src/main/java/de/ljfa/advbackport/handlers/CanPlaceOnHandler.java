package de.ljfa.advbackport.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.ljfa.advbackport.Config;
import de.ljfa.advbackport.logic.PlayerLogic;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

public class CanPlaceOnHandler {
    //Server side
    @SubscribeEvent
    public void onPlace(PlaceEvent event) {
        if(PlayerLogic.getGameType(event.player) != GameType.ADVENTURE)
            return;
        
        if(!PlayerLogic.canPlaceOn(event.player, event.itemInHand, event.placedAgainst))
            event.setCanceled(true);
    }
    
    //Client Side (unless affectInteraction is turned on)
    //Ugly implementation because BlockEvent.PlaceEvent is not being fired on the client side.
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
                || PlayerLogic.getGameType(event.entityPlayer) != GameType.ADVENTURE
                || (!event.world.isRemote && Config.affectInteraction))
            return;
        
        if(!PlayerLogic.canRightClickOn(event.entityPlayer, event.entityPlayer.getHeldItem(), event.x, event.y, event.z))
            event.setCanceled(true);
    }
}
