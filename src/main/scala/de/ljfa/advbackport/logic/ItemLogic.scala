package de.ljfa.advbackport.logic

import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.Constants
import net.minecraft.item.ItemBlock
import net.minecraftforge.common.IPlantable
import net.minecraft.item.ItemDoor
import net.minecraft.item.ItemReed
import net.minecraft.item.ItemBed
import net.minecraft.item.ItemSkull
import net.minecraft.item.ItemSign
import net.minecraft.item.ItemRedstone
import net.minecraft.item.Item
import net.minecraft.init.Items

object ItemLogic {
    def canDestroy(tool: ItemStack, block: Block): Boolean = checkList("CanDestroy", tool, block)
    def canPlaceOn(stack: ItemStack, block: Block): Boolean = checkList("CanPlaceOn", stack, block)
    
    def checkList(listName: String, stack: ItemStack, block: Block): Boolean = {
        stack.getTagCompound match {
            case null => false
            case tag if tag.hasKey(listName, Constants.NBT.TAG_LIST) => {
                val nbtList = tag.getTagList(listName, Constants.NBT.TAG_STRING)
                val list = for(i <- 0 until nbtList.tagCount) yield nbtList getStringTagAt i
                
                list exists { Block.getBlockFromName(_) eq block }
            }
            case _ => false
        }
    }
    
    /**
     * This is a heuristic function that can give false negatives!
     * Only use this on the client side!
     */
    def isPlaceable(item: Item, meta: Int): Boolean = {
        item.isInstanceOf[ItemBlock] ||
        item.isInstanceOf[ItemReed] ||
        item.isInstanceOf[ItemBed] ||
        item.isInstanceOf[ItemRedstone] ||
        item.isInstanceOf[IPlantable] ||
        item.isInstanceOf[ItemDoor] ||
        item.isInstanceOf[ItemSign] ||
        item.isInstanceOf[ItemSkull] ||
        ((item eq Items.dye) && meta == 3) //Cocoa beans
    }
}
