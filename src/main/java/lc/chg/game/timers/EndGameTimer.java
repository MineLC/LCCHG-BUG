package lc.chg.game.timers;

import lc.chg.LCCHG;
import lc.chg.game.BGFBattle;
import lc.chg.game.utils.BGChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;

public class EndGameTimer {
    private static Integer shed_id = null;

    public EndGameTimer() {
        shed_id = Integer.valueOf(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(LCCHG.instance, new Runnable() {
            public void run() {
                World w = Bukkit.getWorlds().get(0);
                w.setDifficulty(Difficulty.HARD);
                w.strikeLightning(LCCHG.spawn.clone().add(0.0D, 50.0D, 0.0D));
                BGChat.printInfoChat(ChatColor.RED + ""+ChatColor.BOLD + "¡Batalla Final!");
                BGChat.printInfoChat("&bTeletransportando al spawn.");
                LCCHG.log.info("Game phase: 4 - Final");
                BGFBattle.teleportGamers(LCCHG.getGamers());
            }
        },0L, 1200L));
    }

    public static void cancel() {
        if (shed_id != null) {
            Bukkit.getServer().getScheduler().cancelTask(shed_id.intValue());
            shed_id = null;
        }
    }
}
