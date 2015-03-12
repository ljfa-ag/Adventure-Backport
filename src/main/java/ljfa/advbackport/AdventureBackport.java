package ljfa.advbackport;

import ljfa.advbackport.handlers.CanDestroyHandler;
import ljfa.advbackport.handlers.CanPlaceOnHandler;
import ljfa.advbackport.handlers.TooltipHandler;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Reference.MODID, name = Reference.MODNAME, version = Reference.VERSION)
public class AdventureBackport {
    @Mod.Instance(Reference.MODID)
    public static AdventureBackport instance;
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.loadConfig(event.getSuggestedConfigurationFile());
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if(Config.activateCanDestroy)
            MinecraftForge.EVENT_BUS.register(new CanDestroyHandler());
        if(Config.activateCanPlaceOn)
            MinecraftForge.EVENT_BUS.register(new CanPlaceOnHandler());
        
        if(event.getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new TooltipHandler());
        }
    }
    
    /*@Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        
    }*/
}
