package ljfa.advbackport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import ljfa.advbackport.handlers.CanDestroyHandler;
import ljfa.advbackport.handlers.CanPlaceOnHandler;
import ljfa.advbackport.handlers.TooltipHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Reference.MODID, name = Reference.MODNAME, version = Reference.VERSION)
public class AdventureBackport {
    @Mod.Instance(Reference.MODID)
    public static AdventureBackport instance;
    
    public static final Logger logger = LogManager.getLogger(Reference.MODNAME);
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.loadConfig(event.getSuggestedConfigurationFile());
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Config.createSets();
        if(Config.activateCanDestroy)
            MinecraftForge.EVENT_BUS.register(new CanDestroyHandler());
        if(Config.activateCanPlaceOn)
            MinecraftForge.EVENT_BUS.register(new CanPlaceOnHandler());
        
        if(event.getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new TooltipHandler());
        }
        
        addVersionChecker();
    }
    
    /*@Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        
    }*/
    
    public void addVersionChecker() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("curseProjectName", "228312-adventure-backport");
        tag.setString("curseFilenameParser", "adventure_backport-[].jar");
        FMLInterModComms.sendRuntimeMessage(Reference.MODID, "VersionChecker", "addCurseCheck", tag);
    }
}
