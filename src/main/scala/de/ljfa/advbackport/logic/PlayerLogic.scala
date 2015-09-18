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

object PlayerLogic {
    def getGameType(player: EntityPlayer): GameType = {
        player match {
            case player: EntityPlayerMP => player.theItemInWorldManager.getGameType
            case _ => gameTypeField.get(Minecraft.getMinecraft.playerController).asInstanceOf[GameType]
        }
    }
    
    @SideOnly(Side.CLIENT)
    lazy val gameTypeField: Field = {
        val f = classOf[PlayerControllerMP].getDeclaredField("currentGameType")
        f.setAccessible(true)
        f
    }
}
