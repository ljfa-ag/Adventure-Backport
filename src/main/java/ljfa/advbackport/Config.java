package ljfa.advbackport;

import static ljfa.advbackport.AdventureBackport.logger;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ljfa.advbackport.exception.InvalidConfigValueException;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class Config {
    public static Configuration conf;

    public static final String CAT_GENERAL = "general";
    
    public static boolean activateCanDestroy;
    public static boolean activateCanPlaceOn;
    public static boolean affectInteraction;
    public static Set<Block> alwaysBreakable;
    public static Set<Item> alwaysPlaceable;

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
    }
    
    public static void createSets() {
        String[] alwaysBreakableArray = conf.get(CAT_GENERAL, "Always breakable", new String[0], "List of blocks that can always be broken").getStringList();
        alwaysBreakable = new HashSet<Block>();
        for(String name: alwaysBreakableArray) {
            Block block = (Block)Block.blockRegistry.getObject(name);
            if(block == Blocks.air || block == null)
                throw new InvalidConfigValueException("Invalid always breakable list entry: " + name);
            else {
                alwaysBreakable.add(block);
                logger.debug("Block always breakable: {}", name);
            }
        }
        //----------------
        String[] alwaysPlaceableArray = conf.get(CAT_GENERAL, "Always placeable", new String[0], "List of items that can be placed anywhere").getStringList();
        alwaysPlaceable = new HashSet<Item>();
        for(String name: alwaysPlaceableArray) {
            Item item = (Item)Item.itemRegistry.getObject(name);
            if(item == null)
                throw new InvalidConfigValueException("Invalid always placeable list entry: " + name);
            else {
                alwaysPlaceable.add(item);
                logger.debug("Item always placeable: {}", name);
            }
        }
        //----------------
        if(conf.hasChanged())
            conf.save();
    }

    /** Reloads the config values upon change */
    public static class ChangeHandler {
        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.modID.equals(Reference.MODID)) {
                loadValues();
                createSets();
            }
        }
    }
}
