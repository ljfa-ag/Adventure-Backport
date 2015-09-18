package de.ljfa.advbackport.asm

import de.ljfa.advbackport.Config
import de.ljfa.advbackport.logic.ItemLogic
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

class AdventureHooks

object AdventureHooks {
    def isToolAdventureModeExempt(player: EntityPlayer, x: Int, y: Int, z: Int): Boolean = {
        if(player.capabilities.allowEdit)
            return true
        
        val block = player.worldObj.getBlock(x, y, z)
        if(Config.alwaysBreakable contains block)
            return true
        
        val tool = player.getCurrentEquippedItem
        if(tool == null)
            return false;
        
        val cache = tcache.get
        if((tool ne cache.tool) || (block ne cache.block)) {
            cache.tool = tool
            cache.block = block
            cache.canDestroy = ItemLogic.canDestroy(tool, block)
        }
        cache.canDestroy
    }
    
    private case class Cache(var tool: ItemStack, var block: Block, var canDestroy: Boolean)
    
    private object tcache extends ThreadLocal[Cache] {
        override def initialValue = Cache(null, null, false)
    }
}
