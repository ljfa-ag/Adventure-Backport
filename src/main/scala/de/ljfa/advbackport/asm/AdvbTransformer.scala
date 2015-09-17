package de.ljfa.advbackport.asm

import scala.collection.JavaConverters.asScalaBufferConverter

import org.apache.logging.log4j.LogManager
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.VarInsnNode

import net.minecraft.launchwrapper.IClassTransformer

class AdvbTransformer extends IClassTransformer {
    private val logger = LogManager.getLogger("Adventure Backport Core")
    
    override def transform(name: String, transformedName: String, bytes: Array[Byte]): Array[Byte] = {
        name match {
            case "net.minecraft.entity.player.EntityPlayer" => transformEntityPlayer(bytes, false)
            case "yz" => transformEntityPlayer(bytes, true)
            case _ => bytes
        }
    }
    
    private def transformEntityPlayer(bytes: Array[Byte], obfuscated: Boolean): Array[Byte] = {
        logger.info(s"Transforming EntityPlayer, obfuscated: $obfuscated")
        //Read class
        val classNode = new ClassNode
        val classReader = new ClassReader(bytes)
        classReader.accept(classNode, 0)
        
        classNode.methods.asScala find { m =>
            m.name == (if(obfuscated) "func_82246_f" else "isCurrentToolAdventureModeExempt") && m.desc == "(III)Z"
        } match {
            case Some(meth: MethodNode) => {
                logger.info(s"Found target method ${meth.name}${meth.desc}")
                //Add call to hook method to the beginning of the method
                //and make it return immediatly after
                val toInject = new InsnList
                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0)) //this
                toInject.add(new VarInsnNode(Opcodes.ILOAD, 1)) //x
                toInject.add(new VarInsnNode(Opcodes.ILOAD, 2)) //y
                toInject.add(new VarInsnNode(Opcodes.ILOAD, 3)) //z
                toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(classOf[AdventureHooks]),
                        "isToolAdventureModeExempt", "(Lnet/minecraft/entity/player/EntityPlayer;III)Z", false))
                toInject.add(new InsnNode(Opcodes.IRETURN))
                
                meth.instructions.insert(toInject)
                logger.info(s"Successfully injected into ${meth.name}${meth.desc}")
            }
            case None => throw new RuntimeException("Could not find target method isCurrentToolAdventureModeExempt")
        }
        
        //Write class
        val classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES)
        classNode.accept(classWriter)
        classWriter.toByteArray
    }
}
