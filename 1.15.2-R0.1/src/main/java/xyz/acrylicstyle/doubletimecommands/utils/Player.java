package xyz.acrylicstyle.doubletimecommands.utils;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public class Player {
	public String username = null;
	public String uuid = null;

	public Player(String something, boolean uuid) {
		if (uuid) this.uuid = something;
	}

	public Player(UUID uuid) {
		this.uuid = uuid.toString();
	}

	public Player(String username) {
		this.username = username;
	}

	public Player setUUID(UUID uuid) {
		this.uuid = uuid.toString();
		return this;
	}

	public Player setUUID(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public Player setUsername(String username) {
		this.username = username;
		return this;
	}

	@Override
	public String toString() {
		try {
			return this.toUsername();
		} catch (IllegalArgumentException | IOException | ParseException e) {
			return null;
		}
	}

	public UUID toUUID() throws IllegalArgumentException, IOException, ParseException {
		if (this.uuid != null) return UUID.fromString(this.uuid);
		if (this.username == null) throw new IllegalArgumentException("Username must be set before call this method.");
		String url = "https://api.mojang.com/users/profiles/minecraft/" + this.username;
		String nameJson = IOUtils.toString(new URL(url).openStream());
		JSONObject nameValue = (JSONObject) JSONValue.parseWithException(nameJson);
		return UUID.fromString(nameValue.get("id").toString().replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
	}

	public String toStringUUID() throws IllegalArgumentException, IOException, ParseException {
		return this.toUUID().toString();
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public String toUsername() throws IllegalArgumentException, IOException, ParseException {
		if (this.username != null) return this.username;
		if (this.uuid == null) throw new IllegalArgumentException("UUID must be set before call this method.");
		UUID.fromString(this.uuid); // Try parse
		String url = "https://api.mojang.com/user/profiles/" + this.uuid.replaceAll("-", "") + "/names";
		String nameJson = IOUtils.toString(new URL(url).openStream());
		JSONArray nameValue = (JSONArray) JSONValue.parseWithException(nameJson);
		String playerSlot = nameValue.get(nameValue.size()-1).toString();
		JSONObject nameObject = (JSONObject) JSONValue.parseWithException(playerSlot);
		return nameObject.get("name").toString();
	}

	public OfflinePlayer getOfflinePlayer() throws IllegalArgumentException, IOException, ParseException {
		return Bukkit.getOfflinePlayer(this.toUUID());
	}

	public org.bukkit.entity.Player getPlayer() throws IllegalArgumentException, IOException, ParseException {
		return Bukkit.getPlayer(this.toUUID());
	}
}
