package xyz.acrylicstyle.doubletimecommands.utils;

import org.bukkit.plugin.messaging.PluginMessageListener;
import util.CollectionStrictSync;
import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;
import xyz.acrylicstyle.tomeito_core.utils.Log;

import java.io.*;
import java.util.UUID;

public class PluginChannelListener implements PluginMessageListener {
    private static CollectionStrictSync<UUID, Callback<String>> callbacks = new CollectionStrictSync<>();

    @Override
    public synchronized void onPluginMessageReceived(String channel, org.bukkit.entity.Player player, byte[] message) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subchannel = in.readUTF();
            if (subchannel.equals("rank")) {
                String input = in.readUTF();
                //obj.put(player.getUniqueId(), input);
                Log.debug("Received message!");
                Log.debug("Channel: " + channel);
                Log.debug("Subchannel: " + subchannel);
                Log.debug("Input: " + input);
                Log.debug("Player: " + player.getUniqueId());
                callbacks.get(player.getUniqueId()).done(input, null);
            }
        } catch (IOException e) {
            callbacks.get(player.getUniqueId()).done(null, e);
        } finally {
            callbacks.remove(player.getUniqueId());
        }
    }

    synchronized void get(org.bukkit.entity.Player p, String channel, String what, Callback<String> callback) {
        sendToBungeeCord(p, channel, what);
        callbacks.put(p.getUniqueId(), callback);
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
