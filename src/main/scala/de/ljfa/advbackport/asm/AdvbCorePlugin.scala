package de.ljfa.advbackport.asm

import java.io.File

import com.google.common.eventbus.EventBus

import cpw.mods.fml.common.DummyModContainer
import cpw.mods.fml.common.LoadController
import cpw.mods.fml.common.ModMetadata
import cpw.mods.fml.relauncher.IFMLLoadingPlugin
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions
import de.ljfa.advbackport.Config
import de.ljfa.advbackport.Reference

@Name("Adventure Backport Core")
@MCVersion("1.7.10")
@SortingIndex(1100)
@TransformerExclusions(Array("de.ljfa.advbackport"))
class AdvbCorePlugin extends DummyModContainer({
    val meta = new ModMetadata
    meta.modId = "adventure_backport_core"
    meta.name = "Adventure Backport Core"
    meta.version = Reference.VERSION
    meta.authorList.add("ljfa")
    meta.url = "http://minecraft.curseforge.com/mc-mods/228312-adventure-backport"
    meta.description = "The core mod of Adventure Backport"
    meta.parent = Reference.MODID
    meta
}) with IFMLLoadingPlugin {
    Config.loadConfig(new File("config", "adventure_backport.cfg"))
    
    override def registerBus(bus: EventBus, controller: LoadController) = true
    
    override def getASMTransformerClass = {
        if(Config.activateCanDestroy)
            Array(classOf[AdvbTransformer].getName)
        else
            Array()
    }
    
    override def getAccessTransformerClass = null
    
    override def getModContainerClass = getClass.getName
    
    override def getSetupClass = null
    
    override def injectData(data: java.util.Map[String, Object]) {}
}
