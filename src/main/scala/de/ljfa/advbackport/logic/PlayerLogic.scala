package de.ljfa.advbackport.logic

import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.multiplayer.PlayerControllerMP
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.world.WorldSettings.GameType
import cpw.mods.fml.relauncher.Side
import java.lang.reflect.Field
import cpw.mods.fml.relauncher.ReflectionHelper
import net.minecraft.block.Block
import de.ljfa.advbackport.Config

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
    
    @SideOnly(Side.CLIENT)
    private lazy val gameTypeField: Field = ReflectionHelper.findField(classOf[PlayerControllerMP], "field_78779_k", "currentGameType")
}
