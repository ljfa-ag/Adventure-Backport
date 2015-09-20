package de.ljfa.advbackport.asm

import de.ljfa.advbackport.Config
import de.ljfa.advbackport.logic.ItemLogic
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import de.ljfa.advbackport.logic.PlayerLogic

class AdventureHooks

object AdventureHooks {
    def isToolAdventureModeExempt(player: EntityPlayer, x: Int, y: Int, z: Int): Boolean = {
        if(player.capabilities.allowEdit)
            true
        else
            PlayerLogic.canDestroy(player, player.worldObj.getBlock(x, y, z))
    }
}
