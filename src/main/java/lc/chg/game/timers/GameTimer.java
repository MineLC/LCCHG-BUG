package lc.chg.game.timers;

import lc.chg.LCCHG;
import lc.chg.game.BGFBattle;
import lc.chg.game.utils.BGChat;
import lc.core.utilidades.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GameTimer {
    private static Integer shed_id = null;

    public GameTimer() {
        shed_id = Integer.valueOf(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(LCCHG.instance, new Runnable() {
            public void run() {
                LCCHG.GAME_RUNNING_TIME = Integer.valueOf(LCCHG.GAME_RUNNING_TIME.intValue() + 1);
                LCCHG.checkwinner();
                if ((((LCCHG.GAME_RUNNING_TIME.intValue() % 5 != 0) ? 1 : 0) & ((LCCHG.GAME_RUNNING_TIME.intValue() % 10 != 0) ? 1 : 0)) != 0)
                    BGChat.printTipChat();
                if (LCCHG.GAME_RUNNING_TIME.intValue() == LCCHG.END_GAME_TIME.intValue() - 1) {
                    for (Player pl : LCCHG.getGamers())
                        pl.playSound(pl.getLocation(), Sound.AMBIENCE_CAVE, 1.0F, -1.0F);
                    BGChat.printInfoChat(ChatColor.RED + "[Batalla Final] " + ChatColor.GREEN + "Todos seran enviados a la cornucopia en 1 minuto!");
                }
                if (LCCHG.GAME_RUNNING_TIME == LCCHG.END_GAME_TIME && LCCHG.END_GAME.booleanValue()) {
                    LCCHG.END_GAME = Boolean.valueOf(false);
                    BGFBattle.createBattle();
                    for (Player p : LCCHG.getGamers()) {
                        for (PotionEffect pe : p.getActivePotionEffects())
                            p.removePotionEffect(pe.getType());
                        Util.sendTitle(p, 20, 60, 40, ChatColor.DARK_RED + "Batalla Final", ChatColor.WHITE + "El mejor equipado sobrevivira");
                        p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 9999, 0));
                    }
                }
                if (LCCHG.GAME_RUNNING_TIME.intValue() == LCCHG.MAX_GAME_RUNNING_TIME.intValue() - 1)
                    BGChat.printInfoChat(ChatColor.RED + "[Batalla final] " + ChatColor.GREEN + "1 minuto restante.");
                if (LCCHG.GAME_RUNNING_TIME.intValue() >= LCCHG.MAX_GAME_RUNNING_TIME.intValue())
                    for (Player p : LCCHG.getGamers()) {
                        for (PotionEffect pe : p.getActivePotionEffects())
                            p.removePotionEffect(pe.getType());
                        Util.sendTitle(p, 20, 60, 40, ChatColor.DARK_RED + "Eliminando Atributos", ChatColor.WHITE + "El mejor equipado sobrevivira");
                        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 9999, 1));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 9999, 1));
                    }
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
