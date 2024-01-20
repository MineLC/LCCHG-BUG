package lc.chg.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import lc.chg.LCCHG;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LCConfig {
    public static FileConfiguration abconf;

    public static FileConfiguration bookconf;

    public static FileConfiguration config;

    public static FileConfiguration dsign;

    public static FileConfiguration kitconf;

    public static FileConfiguration worldconf;

    public LCConfig() {
        try {
            loadFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFiles() throws Exception {
        File configFile = new File(LCCHG.instance.getDataFolder(), "config.yml");
        File kitFile = new File(LCCHG.instance.getDataFolder(), "kit.yml");
        File deathSignFile = new File(LCCHG.instance.getDataFolder(), "deathsign.yml");
        File abilitiesFile = new File(LCCHG.instance.getDataFolder(), "abilities.yml");
        File bookFile = new File(LCCHG.instance.getDataFolder(), "book.yml");
        File worldFile = new File(LCCHG.instance.getDataFolder(), "world.yml");
        Integer creation = Integer.valueOf(0);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            LCCHG.copy(LCCHG.instance.getResource("config.yml"), configFile);
            creation = Integer.valueOf(creation.intValue() + 1);
        }
        if (!kitFile.exists()) {
            kitFile.getParentFile().mkdirs();
            LCCHG.copy(LCCHG.instance.getResource("kit.yml"), kitFile);
            creation = Integer.valueOf(creation.intValue() + 1);
        }
        if (!deathSignFile.exists()) {
            deathSignFile.getParentFile().mkdirs();
            LCCHG.copy(LCCHG.instance.getResource("deathsign.yml"), deathSignFile);
            creation = Integer.valueOf(creation.intValue() + 1);
        }
        if (!abilitiesFile.exists()) {
            abilitiesFile.getParentFile().mkdirs();
            LCCHG.copy(LCCHG.instance.getResource("abilities.yml"), abilitiesFile);
            creation = Integer.valueOf(creation.intValue() + 1);
        }
        if (!bookFile.exists()) {
            bookFile.getParentFile().mkdirs();
            LCCHG.copy(LCCHG.instance.getResource("book.yml"), bookFile);
            creation = Integer.valueOf(creation.intValue() + 1);
        }
        if (!worldFile.exists()) {
            worldFile.getParentFile().mkdirs();
            LCCHG.copy(LCCHG.instance.getResource("world.yml"), worldFile);
            creation = Integer.valueOf(creation.intValue() + 1);
        }
        abconf = (FileConfiguration)YamlConfiguration.loadConfiguration(
                new File(LCCHG.instance.getDataFolder(), "abilities.yml"));
        bookconf = (FileConfiguration)YamlConfiguration.loadConfiguration(
                new File(LCCHG.instance.getDataFolder(), "book.yml"));
        config = (FileConfiguration)YamlConfiguration.loadConfiguration(
                new File(LCCHG.instance.getDataFolder(), "config.yml"));
        dsign = (FileConfiguration)YamlConfiguration.loadConfiguration(
                new File(LCCHG.instance.getDataFolder(), "deathsign.yml"));
        kitconf = (FileConfiguration)YamlConfiguration.loadConfiguration(
                new File(LCCHG.instance.getDataFolder(), "kit.yml"));
        worldconf = (FileConfiguration) YamlConfiguration.loadConfiguration(
                new File(LCCHG.instance.getDataFolder(), "world.yml"));
    }
}
