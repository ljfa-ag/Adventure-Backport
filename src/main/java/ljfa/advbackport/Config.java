package ljfa.advbackport;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Config {
    public static Configuration conf;

    public static final String CAT_GENERAL = "general";
    
    public static boolean activateCanDestroy;
    public static boolean activateCanPlaceOn;
    public static boolean affectInteraction;

    public static void loadConfig(File file) {
        if(conf == null)
            conf = new Configuration(file);

        conf.load();
        loadValues();

        FMLCommonHandler.instance().bus().register(new ChangeHandler());
    }

    public static void loadValues() {
        activateCanDestroy = conf.get(CAT_GENERAL, "Activate CanDestroy", true, "Blocks cannot be broken unless the tool used has an appropriate CanDestroy tag").setRequiresMcRestart(true).getBoolean();
        activateCanPlaceOn = conf.get(CAT_GENERAL, "Activate CanPlaceOn", true, "Blocks cannot be placed unless they have an appropriate CanPlaceOn tag").setRequiresMcRestart(true).getBoolean();
        affectInteraction = conf.get(CAT_GENERAL, "CanPlaceOn also affects interaction", false, "When activated, the player can also not interact with blocks (e.g. pushing a button) unless they use an item with appropriate CanPlaceOn").getBoolean();
        //----------------
        if(conf.hasChanged())
            conf.save();
    }

    /** Reloads the config values upon change */
    public static class ChangeHandler {
        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.modID.equals(Reference.MODID))
                loadValues();
        }
    }
}
