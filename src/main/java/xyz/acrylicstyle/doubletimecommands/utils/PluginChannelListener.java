package xyz.acrylicstyle.doubletimecommands.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;

import java.io.*;
import util.Collection;
import xyz.acrylicstyle.tomeito_core.utils.Log;

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
                Log.debug("Received message!");
                Log.debug("Channel: " + channel);
                Log.debug("Subchannel: " + subchannel);
                Log.debug("Input: " + input);
                synchronized (Lock.LOCK) {
                    Lock.LOCK.notifyAll();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized Object get(org.bukkit.entity.Player p, String channel, String what, Object defaultv) {
        sendToBungeeCord(p, channel, what);
        try {
            synchronized (Lock.LOCK) {
                Lock.LOCK.wait(1000);
            }
        } catch (InterruptedException ignored){}
        return obj.get(p) != null ? obj.get(p) : defaultv;
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
        p.sendPluginMessage(DoubleTimeCommands.getPlugin(DoubleTimeCommands.class), "dtc:rank", b.toByteArray());
    }
}

class Lock {
    final static Object LOCK = new Object();
}
