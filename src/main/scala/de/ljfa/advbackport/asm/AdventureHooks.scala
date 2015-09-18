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
        tool != null && ItemLogic.canDestroy(tool, block)
    }
}
