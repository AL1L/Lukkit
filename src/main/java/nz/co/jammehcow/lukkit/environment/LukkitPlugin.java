package nz.co.jammehcow.lukkit.environment;

import com.avaje.ebean.EbeanServer;
import com.sun.org.apache.xerces.internal.impl.io.UTF8Reader;
import nz.co.jammehcow.lukkit.Main;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * The type Lukkit plugin.
 *
 * @author jammehcow
 */
public class LukkitPlugin implements Plugin {
    private String name;
    private LukkitPluginFile pluginFile;
    private LuaValue pluginMain;
    private LuaFunction loadCB;
    private LuaFunction enableCB;
    private LuaFunction disableCB;
    private File pluginConfig;
    private LukkitPluginLoader pluginLoader;
    private LukkitConfigurationFile config;
    private PluginDescriptionFile descriptor;
    private Globals globals;
    private File dataFolder;
    private boolean enabled = false;

    private HashMap<String, LuaFunction> commands = new HashMap<>();

    /**
     * Instantiates a new Lukkit plugin.
     *
     * @param loader the loader
     * @param file   the file
     */
    public LukkitPlugin(LukkitPluginLoader loader, LukkitPluginFile file) {
        try {
            this.descriptor = new PluginDescriptionFile(this.pluginFile.getPluginYML());
        } catch (InvalidDescriptionException e) { e.printStackTrace(); }

        this.pluginFile = file;
        try {
            this.pluginMain = LuaEnvironment.globals.load(new UTF8Reader(this.pluginFile.getResource(this.descriptor.getMain())), this.descriptor.getMain());
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        this.dataFolder = new File(Main.instance.getDataFolder().getAbsolutePath() + File.separator + this.name); // TODO: use a plugin.yml name to create datafolder
        this.pluginLoader = loader;
        this.globals = LuaEnvironment.globals;

        this.pluginMain.call();
    }

    @Override
    public File getDataFolder() {
        return this.dataFolder;
    }

    @Override
    public PluginDescriptionFile getDescription() {
        return this.descriptor;
    }

    @Override
    public FileConfiguration getConfig() {
        return this.config;
    }

    @Override
    public InputStream getResource(String s) {
        // TODO
        return null;
    }

    @Override
    public void saveConfig() {

    }

    @Override
    public void saveDefaultConfig() {
        try {
            Files.copy(this.pluginFile.getDefaultConfig(), new File(this.dataFolder.getAbsolutePath() + File.separator + "config.yml").toPath());
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
        File resourceOutput = new File(this.dataFolder.getAbsolutePath() + File.separator + "");

        if (!resourceOutput.exists() || replace) {
            // TODO
        } else {
            getLogger().warning("You tried to access a resource that doesn't exist");
        }
    }

    @Override
    public void reloadConfig() {
        this.config = new LukkitConfigurationFile(this.pluginConfig);
    }

    @Override
    public PluginLoader getPluginLoader() {
        return this.pluginLoader;
    }

    @Override
    public Server getServer() {
        return Main.instance.getServer();
    }

    @Override
    public boolean isEnabled() {
        return LukkitPluginLoader.loadedPlugins.contains(this) && this.enabled;
    }

    @Override
    public void onEnable() {
        if (this.enableCB != null) this.enableCB.call();
        this.enabled = true;
    }

    @Override
    public void onDisable() {
        if (this.disableCB != null) this.disableCB.call();
        this.enabled = false;
    }

    @Override
    public void onLoad() {
        if (this.loadCB != null) this.loadCB.call();
    }

    @Override
    public boolean isNaggable() {
        // TODO: lookup
        return false;
    }

    @Override
    public void setNaggable(boolean b) {
        // TODO: lookup
    }

    @Override
    public EbeanServer getDatabase() {
        // Will be replaced with HSQLDB
        return null;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        // TODO: lookup
        return null;
    }

    @Override
    public Logger getLogger() {
        // TODO: create plugin-individual logger
        return Main.logger;
    }

    @Override
    public String getName() { return this.getDescription().getName(); }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (commands.containsKey(command.getName())) {

        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] strings) {
        // TODO
        return null;
    }
}