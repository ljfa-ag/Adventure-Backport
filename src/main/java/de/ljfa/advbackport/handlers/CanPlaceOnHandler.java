package de.ljfa.advbackport.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.ljfa.advbackport.Config;
import de.ljfa.advbackport.logic.PlayerLogic;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

public class CanPlaceOnHandler {
    //Server side (unfortunately there is no satisfying solution for the client side)
    @SubscribeEvent
    public void onPlace(PlaceEvent event) {
        if(PlayerLogic.getGameType(event.player) != GameType.ADVENTURE)
            return;
        
        if(!PlayerLogic.canPlaceOn(event.player, event.itemInHand, event.placedAgainst))
            event.setCanceled(true);
    }
    
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
                || PlayerLogic.getGameType(event.entityPlayer) != GameType.ADVENTURE)
            return;
        
        if(Config.affectInteraction 
                && !PlayerLogic.canRightClickOn(event.entityPlayer, event.entityPlayer.getHeldItem(), event.x, event.y, event.z))
            event.setCanceled(true);
    }
}
