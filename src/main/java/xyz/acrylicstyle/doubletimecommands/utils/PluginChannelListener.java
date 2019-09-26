package xyz.acrylicstyle.doubletimecommands.utils;

import org.bukkit.plugin.messaging.PluginMessageListener;
import util.CollectionStrictSync;
import xyz.acrylicstyle.doubletimecommands.DoubleTimeCommands;
import xyz.acrylicstyle.tomeito_core.utils.Log;

import java.io.*;

public class PluginChannelListener implements PluginMessageListener {
    private static CollectionStrictSync<String, Callback<String>> callbacks = new CollectionStrictSync<>();

    @Override
    public synchronized void onPluginMessageReceived(String tag, org.bukkit.entity.Player player, byte[] message) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            if (tag.equalsIgnoreCase("dtc:rank")) {
                String subchannel = in.readUTF();
                String input = in.readUTF(); // message
                Log.debug("Received message!");
                Log.debug("Tag: " + tag);
                Log.debug("Subchannel: " + subchannel);
                Log.debug("Input: " + input);
                Log.debug("Player: " + player.getUniqueId());
                callbacks.get(subchannel).done(input, null);
                callbacks.remove(subchannel);
            }
        } catch (IOException e) {
            callbacks.get(player.getUniqueId().toString()).done(null, e);
            callbacks.remove(player.getUniqueId().toString());
        }
    }

    synchronized void get(org.bukkit.entity.Player p, String subchannel, String message, Callback<String> callback) {
        sendToBungeeCord(p, subchannel, message);
        callbacks.put(p.getUniqueId().toString(), callback);
    }

    private void sendToBungeeCord(org.bukkit.entity.Player p, String subchannel, String message) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF(subchannel);
            out.writeUTF(message);
        } catch (IOException e) { // impossible?
            e.printStackTrace();
        }
        p.sendPluginMessage(DoubleTimeCommands.getPlugin(DoubleTimeCommands.class), "dtc:rank", b.toByteArray());
    }
}
