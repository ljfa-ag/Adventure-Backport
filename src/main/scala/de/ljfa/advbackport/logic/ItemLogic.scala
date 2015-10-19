package de.ljfa.advbackport.logic

import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagList
import net.minecraftforge.common.util.Constants

object ItemLogic {
    def canDestroy(tool: ItemStack, block: Block): Boolean = checkList("CanDestroy", tool, block)
    def canPlaceOn(stack: ItemStack, block: Block): Boolean = checkList("CanPlaceOn", stack, block)
    
    def checkList(listName: String, stack: ItemStack, block: Block): Boolean = {
        stack.getTagCompound match {
            case null => false
            case tag if tag.hasKey(listName, Constants.NBT.TAG_LIST) => {
                val list = asStringList(tag.getTagList(listName, Constants.NBT.TAG_STRING))
                list exists { Block.getBlockFromName(_) eq block }
            }
            case _ => false
        }
    }
    
    def asStringList(nbtList: NBTTagList) = for(i <- 0 until nbtList.tagCount) yield nbtList getStringTagAt i
}
