package lc.chg.game.timers;

import lc.chg.LCCHG;
import lc.chg.game.utils.BGChat;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PreGameTimer {
    private static Integer shed_id = null;

    public static boolean started = false;

    public PreGameTimer() {
        started = true;
        shed_id = Integer.valueOf(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(LCCHG.instance, new Runnable() {
            public void run() {
                if (LCCHG.COUNTDOWN.intValue() > 0) {
                    if (LCCHG.COUNTDOWN >= 30 & LCCHG.COUNTDOWN % 10 == 0) {
                        BGChat.printTimeChat("El juego comienza en "
                                + LCCHG.TIME(LCCHG.COUNTDOWN) + ".");
                        for (Player pl : LCCHG.getGamers()) {
                            if (LCCHG.getGamers().size() >= Bukkit.getServer().getMaxPlayers()){
                                LCCHG.COUNTDOWN = 26;
                                pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 1, 2);
                            }
                        }
                    } else if (LCCHG.COUNTDOWN <= 25 && LCCHG.COUNTDOWN > 10 & LCCHG.COUNTDOWN % 5 == 0) {
                        BGChat.printTimeChat("El juego comienza en "
                                + LCCHG.TIME(LCCHG.COUNTDOWN) + ".");
                    }
                    else if (LCCHG.COUNTDOWN <= 10 && LCCHG.COUNTDOWN > 3) {
                        BGChat.printTimeChat("El juego comienza en "
                                + LCCHG.TIME(LCCHG.COUNTDOWN) + ".");
                    } else if (LCCHG.COUNTDOWN <= 3) {
                        BGChat.printTimeChat("El juego comienza en "
                                + LCCHG.TIME(LCCHG.COUNTDOWN) + ".");
                        for (Player pl : LCCHG.getGamers()) {
                            pl.playSound(pl.getLocation(), Sound.NOTE_PLING, 1, -1);
                        }
                    }

                    LCCHG.COUNTDOWN--;
                } else if (LCCHG.getGamers().size() < LCCHG.MINIMUM_PLAYERS.intValue()) {
                    BGChat.printTimeChat("&cEsperando mas jugadores..");
                    LCCHG.COUNTDOWN = LCCHG.COUNTDOWN_SECONDS;
                } else {
                    LCCHG.startgame();
                }
            }
        },  0L, 20L));
    }

    public static void cancel() {
        if (shed_id != null) {
            Bukkit.getServer().getScheduler().cancelTask(shed_id.intValue());
            shed_id = null;
        }
    }
}
