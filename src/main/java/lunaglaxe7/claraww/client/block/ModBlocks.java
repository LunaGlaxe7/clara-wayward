package lunaglaxe7.claraww.client.block;

import cpw.mods.fml.common.registry.GameRegistry;
import lunaglaxe7.claraww.items.ItemForge;

public class ModBlocks {

    public static BlockForge forge;

    public static void registerBlocks() {
        forge = new BlockForge();
        GameRegistry.registerBlock(forge, ItemForge.class, "block_wand_forge");
    }
}
