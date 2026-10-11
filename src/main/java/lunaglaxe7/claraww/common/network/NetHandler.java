package lunaglaxe7.claraww.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import lunaglaxe7.claraww.common.network.packet.PacketCapSelectedToServer;
import lunaglaxe7.claraww.common.network.packet.PacketForgeStartWorking;
import lunaglaxe7.claraww.common.network.packet.PacketUTypeSelected;

public class NetHandler {

    public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel("claraww");

    public static void init() {
        int index = 0;
        instance.registerMessage(
                PacketCapSelectedToServer.class,
                PacketCapSelectedToServer.class,
                index++,
                Side.SERVER);
        instance.registerMessage(PacketForgeStartWorking.class, PacketForgeStartWorking.class, index++, Side.SERVER);
        instance.registerMessage(PacketUTypeSelected.class, PacketUTypeSelected.class, index++, Side.SERVER);
    }
}
