package xyz.acrylicstyle.doubletimecommands.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;

import java.io.*;
import util.Collection;

public class PluginChannelListener implements PluginMessageListener {
    private static Collection<Player, String> obj = new Collection<>();

    @Override
    public synchronized void onPluginMessageReceived(String channel, org.bukkit.entity.Player player, byte[] message) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subchannel = in.readUTF();
            if (subchannel.equals("rank")) {
                String input = in.readUTF();
                obj.put(player, input);
                notifyAll();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized Object get(org.bukkit.entity.Player p, String channel, String what) {
        sendToBungeeCord(p, channel, what);
        try {
            wait();
        } catch (InterruptedException ignored){}
        return obj.get(p);
    }

    private void sendToBungeeCord(org.bukkit.entity.Player p, String channel, String sub){
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF(channel);
            out.writeUTF(sub);
        } catch (IOException e) { // impossible?
            e.printStackTrace();
        }
        p.sendPluginMessage(DoubleTimeCommands.getPlugin(DoubleTimeCommands.class), "BungeeCord", b.toByteArray());
    }
}
