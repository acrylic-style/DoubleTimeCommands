package xyz.acrylicstyle.doubletimecommands.providers;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigProvider extends YamlConfiguration {
	public final File file;
	public final String path;

	public ConfigProvider(String path) throws FileNotFoundException, IOException, InvalidConfigurationException {
		this.path = path;
		this.file = new File(this.path);
		if (!this.file.exists()) this.save(this.path);
		this.load(this.file);
	}

	public void save() throws IOException {
		this.save(this.file);
	}

	public void setThenSave(String path, Object value) throws IOException {
		this.set(path, value);
		this.save();
	}

	public static void setThenSave(String path, Object value, File file) throws IOException, InvalidConfigurationException {
		YamlConfiguration config = new YamlConfiguration();
		if (!file.exists()) config.save(file);
		config.load(file);
		config.set(path, value);
		config.save(file);
	}

	public static Boolean getBoolean(String path, Boolean def, String pluginName) throws FileNotFoundException, IOException, InvalidConfigurationException {
		return getBoolean(path, def, new File("./plugins/" + pluginName + "/config.yml"));
	}

	public static Boolean getBoolean(String path, Boolean def, File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
		YamlConfiguration config = new YamlConfiguration();
		config.load(file);
		return config.getBoolean(path, def);
	}

	public static String getString(String path, String def, String pluginName) throws FileNotFoundException, IOException, InvalidConfigurationException {
		return getString(path, def, new File("./plugins/" + pluginName + "/config.yml"));
	}

	public static String getString(String path, String def, File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
		YamlConfiguration config = new YamlConfiguration();
		config.load(file);
		return config.getString(path, def);
	}

	public static void setThenSave(String path, Object value, String pluginName) throws IOException, InvalidConfigurationException {
		setThenSave(path, value, new File("./plugins/" + pluginName + "/config.yml"));
	}
}