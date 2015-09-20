package de.ljfa.advbackport.logic

import java.lang.reflect.Field
import cpw.mods.fml.relauncher.ReflectionHelper
import cpw.mods.fml.relauncher.SideOnly
import de.ljfa.advbackport.Config
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.PlayerControllerMP
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.world.WorldSettings.GameType
import cpw.mods.fml.relauncher.Side

object PlayerLogic {
    def getGameType(player: EntityPlayer): GameType = {
        player match {
            case player: EntityPlayerMP => player.theItemInWorldManager.getGameType
            case _ => gameTypeField.get(Minecraft.getMinecraft.playerController).asInstanceOf[GameType]
        }
    }
    
    def canDestroy(player: EntityPlayer, block: Block): Boolean = {
        if(Config.alwaysBreakable contains block)
            true
        else {
            val tool = player.getCurrentEquippedItem
            tool != null && ItemLogic.canDestroy(tool, block)
        }
    }
    
    def canPlaceOn(player: EntityPlayer, stack: ItemStack, against: Block): Boolean = {
        if(stack == null)
            false
        else if((stack.getItem eq Items.dye) && (stack.getItemDamage == 15))
            true //Bone-mealing a plant is considered as placing blocks,
                 //which causes weird effects so we're whitelisting that
        else if(Config.alwaysPlaceable contains stack.getItem)
            true
        else
            ItemLogic.canPlaceOn(stack, against)
    }
    
    /**
     * Checks whether the player is allowed to right-click on the block with the stack in their hand.
     * However, this function can give false negatives unless Config.affectInteraction is turned on!
     * So in this case is must only be used on the client side!
     */
    def canRightClickOn(player: EntityPlayer, stack: ItemStack, x: Int, y: Int, z: Int): Boolean = {
        if(stack == null)
            !Config.affectInteraction
        else if(Config.alwaysPlaceable contains stack.getItem)
            true
        else if(!Config.affectInteraction && !ItemLogic.isPlaceable(stack.getItem, stack.getItemDamage))
            true
        else
            ItemLogic.canPlaceOn(stack, player.worldObj.getBlock(x, y, z))
    }
    
    @SideOnly(Side.CLIENT)
    private lazy val gameTypeField: Field = ReflectionHelper.findField(classOf[PlayerControllerMP], "field_78779_k", "currentGameType")
}
