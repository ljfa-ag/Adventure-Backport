package de.ljfa.advbackport.asm

import de.ljfa.advbackport.Config
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.Constants
import de.ljfa.advbackport.AdventureBackport.logger
import net.minecraft.nbt.NBTTagCompound

class AdventureHooks

object AdventureHooks {
    def isToolAdventureModeExempt(player: EntityPlayer, x: Int, y: Int, z: Int): Boolean = {
        if(player.capabilities.allowEdit)
            return true
        
        val block = player.worldObj.getBlock(x, y, z)
        if(Config.alwaysBreakable contains block)
            return true
        
        val tool = player.getCurrentEquippedItem
        val cache = tcache.get
        if((tool ne cache.tool) || (block ne cache.block)) {
            cache.tool = tool
            cache.block = block
            cache.canDestroy = computeCanDestroy(tool, block)
        }
        cache.canDestroy
    }
    
    private def computeCanDestroy(tool: ItemStack, block: Block): Boolean = {
        tool != null && (tool.getTagCompound match {
            case null => false
            case tag if tag.hasKey("CanDestroy", Constants.NBT.TAG_LIST) => {
                val nbtList = tag.getTagList("CanDestroy", Constants.NBT.TAG_STRING)
                val canDestrList = for(i <- 0 until nbtList.tagCount) yield nbtList getStringTagAt i
            
                canDestrList exists(Block.getBlockFromName(_) == block)
            }
            case _ => false
        })
    }
    
    private case class Cache(var tool: ItemStack, var block: Block, var canDestroy: Boolean)
    
    private object tcache extends ThreadLocal[Cache] {
        override def initialValue = Cache(null, null, false)
    }
}