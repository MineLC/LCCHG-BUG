package lc.chg.game.timers;

import lc.chg.LCCHG;
import lc.chg.configuration.LCConfig;
import lc.chg.game.litesteners.habilidades.BGHabilidadesListener;
import lc.chg.game.utils.BGChat;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class BGCooldown {
    public BGCooldown() {
        LCConfig.abconf = (FileConfiguration) YamlConfiguration.loadConfiguration(new File(LCCHG.instance.getDataFolder(), "abilities.yml"));
    }

    public static void monkCooldown(final Player player) {
        TimerTask action = new TimerTask() {
            public void run() {
                BGHabilidadesListener.cooldown.remove(player);
            }
        };
        Timer timer = new Timer();
        timer.schedule(action, (LCConfig.abconf.getInt("AB.12.Cooldown") * 1000));
    }

    public static void thiefCooldown(final Player player) {
        TimerTask action = new TimerTask() {
            public void run() {
                BGHabilidadesListener.cooldown.remove(player);
            }
        };
        Timer timer = new Timer();
        timer.schedule(action, (LCConfig.abconf.getInt("AB.15.Cooldown") * 1000));
    }

    public static void ghostCooldown(final Player player) {
        TimerTask action = new TimerTask() {
            public void run() {
                BGHabilidadesListener.cooldown.remove(player);
            }
        };
        Timer timer = new Timer();
        timer.schedule(action, (LCConfig.abconf.getInt("AB.16.Cooldown") * 1000));
    }

    public static void viperCooldown(final Player player) {
        TimerTask action = new TimerTask() {
            public void run() {
                BGHabilidadesListener.cooldown.remove(player);
            }
        };
        Timer timer = new Timer();
        timer.schedule(action, (LCConfig.abconf.getInt("AB.19.Duration") * 1000));
    }

    public static void orcoCooldown(final Player player) {
        TimerTask action = new TimerTask() {
            public void run() {
                BGHabilidadesListener.cooldown.remove(player);
            }
        };
        Timer timer = new Timer();
        timer.schedule(action, (LCConfig.abconf.getInt("AB.35.Duration") * 1000));
    }

    public static void trollCooldown(final Player player) {
        TimerTask action = new TimerTask() {
            public void run() {
                BGHabilidadesListener.cooldown.remove(player);
            }
        };
        Timer timer = new Timer();
        timer.schedule(action, (LCConfig.abconf.getInt("AB.36.Duration") * 1000));
    }

    public static void thorCooldown(final Player player) {
        TimerTask action = new TimerTask() {
            public void run() {
                BGHabilidadesListener.cooldown.remove(player);
            }
        };
        Timer timer = new Timer();
        timer.schedule(action, (LCConfig.abconf.getInt("AB.11.Cooldown") * 1000));
    }

    public static void flashCooldown(final Player player) {
        TimerTask action = new TimerTask() {
            public void run() {
                BGHabilidadesListener.cooldown.remove(player);
                player.sendMessage(ChatColor.GREEN + "Ahora puedes volver a teletransportarte!");
            }
        };
        Timer timer = new Timer();
        timer.schedule(action, (LCConfig.abconf.getInt("AB.31.Cooldown") * 1000));
    }

    public static void timeCooldown(final Player player) {
        TimerTask action = new TimerTask() {
            public void run() {
                BGHabilidadesListener.cooldown.remove(player);
            }
        };
        Timer timer = new Timer();
        timer.schedule(action, (LCConfig.abconf.getInt("AB.22.Cooldown") * 1000));
    }

    public static void freezeCooldown(final Player player) {
        TimerTask action = new TimerTask() {
            public void run() {
                BGChat.printPlayerChat(player, LCConfig.abconf.getString("AB.22.unfrozen"));
                LCCHG.frezee.remove(player);
            }
        };
        Timer timer = new Timer();
        timer.schedule(action, (LCConfig.abconf.getInt("AB.22.Duration") * 1000));
    }
}
