package de.ljfa.advbackport.asm

import org.apache.logging.log4j.LogManager

import net.minecraft.launchwrapper.IClassTransformer

class AdvbTransformer extends IClassTransformer {
    private val logger = LogManager.getLogger("Adventure Backport Core")
    
    override def transform(name: String, transformedName: String, bytes: Array[Byte]): Array[Byte] = {
        transformedName match {
            case "net.minecraft.entity.player.EntityPlayer" => transformEntityPlayer(bytes, false)
            case "yz" => transformEntityPlayer(bytes, true)
            case _ => bytes
        }
    }
    
    private def transformEntityPlayer(bytes: Array[Byte], obfuscated: Boolean): Array[Byte] = {
        logger.info(s"Transforming EntityPlayer, obfuscated: $obfuscated")
        ???
    }
}
