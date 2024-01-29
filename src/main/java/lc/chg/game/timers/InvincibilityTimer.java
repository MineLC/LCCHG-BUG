package lc.chg.game.timers;

import lc.chg.LCCHG;
import lc.chg.game.GameState;
import lc.chg.game.utils.BGChat;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class InvincibilityTimer {
    private static Integer shed_id = null;

    public InvincibilityTimer() {
        shed_id = Integer.valueOf(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)LCCHG.instance, new Runnable() {
            public void run() {
                if (LCCHG.FINAL_COUNTDOWN.intValue() > 0) {
                    if ((((LCCHG.FINAL_COUNTDOWN.intValue() >= 10) ? 1 : 0) & (
                            (LCCHG.FINAL_COUNTDOWN.intValue() % 10 == 0) ? 1 : 0)) != 0) {
                        BGChat.printTimeChat("La invencibilidad termina en " +
                                LCCHG.TIME(LCCHG.FINAL_COUNTDOWN) +
                                ".");
                        for (Player pl : LCCHG.getGamers())
                            pl.playSound(pl.getLocation(), Sound.NOTE_PLING, 2.0F, 2.0F);
                    }
                    LCCHG.FINAL_COUNTDOWN = Integer.valueOf(LCCHG.FINAL_COUNTDOWN.intValue() - 1);
                } else {
                    BGChat.printTimeChat("&cLa invencibilidad ha terminado.");
                    LCCHG.log.info("Game phase: 3 - Fighting");
                    for (Player pl : LCCHG.getGamers())
                        pl.playSound(pl.getLocation(), Sound.ANVIL_LAND, 1.0F, 2.0F);
                    BGChat.printTipChat();
                    LCCHG.spawn.getWorld().setAutoSave(true);
                    LCCHG.GAMESTATE = GameState.GAME;
                    cancel();
                    new GameTimer();
                    return;
                }
            }
        },20L, 20L));
    }

    public static void cancel() {
        if (shed_id != null) {
            Bukkit.getServer().getScheduler().cancelTask(shed_id.intValue());
            shed_id = null;
        }
    }
}
