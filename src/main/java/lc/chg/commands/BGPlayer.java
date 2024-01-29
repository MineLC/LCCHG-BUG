package lc.chg.commands;

import lc.chg.configuration.Translation;
import lc.chg.game.BGKit;
import lc.chg.game.GameState;
import lc.chg.game.utils.BGChat;
import lc.chg.game.utils.BGTeam;
import lc.core.entidades.Jugador;
import lc.core.entidades.minijuegos.CHGRank;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import lc.chg.LCCHG;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BGPlayer implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        final Player p = (Player)sender;
        Jugador jug = Jugador.getJugador(p);
        if (cmd.getName().equalsIgnoreCase("help")) {
            BGChat.printHelpChat(p);
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("rank")) {
            CHGRank rango = jug.getChgInfo().getRank();
            if (!LCCHG.Fame.containsKey(p))
                LCCHG.Fame.put(p, Integer.valueOf(0));
            int kills = jug.getChgInfo().getFama();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&m--------------------------"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fRango: &a" + rango));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fFama: &a" + kills));
            int faltante = 0;
            if (rango == CHGRank.NUEVO) {
                faltante = 100 - kills;
            } else if (rango == CHGRank.APRENDIZ) {
                faltante = 500 - kills;
            } else if (rango == CHGRank.HÉROE) {
                faltante = 1000 - kills;
            } else if (rango == CHGRank.FEROZ) {
                faltante = 2000 - kills;
            } else if (rango == CHGRank.PODEROSO) {
                faltante = 3000 - kills;
            } else if (rango == CHGRank.MORTAL) {
                faltante = 4000 - kills;
            } else if (rango == CHGRank.TERRORÍFICO) {
                faltante = 5000 - kills;
            } else if (rango == CHGRank.CONQUISTADOR) {
                faltante = 6000 - kills;
            } else if(rango == CHGRank.RENOMBRADO) {
                faltante = 7000 - kills;
            } else if (rango == CHGRank.ILUSTRE) {
                faltante = 8000 - kills;
            } else if (rango == CHGRank.EMINENTE) {
                faltante = 9000 - kills;
            } else if (rango == CHGRank.REY) {
                faltante = 10000 - kills;
            } else if (rango == CHGRank.EMPERADOR) {
                faltante = 15000 - kills;
            } else if (rango == CHGRank.LEGENDARIO) {
                faltante = 20000 - kills;
            }
            if (rango != CHGRank.MÍTICO) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fRankUP: &a" + faltante + " de Fama"));
            } else {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fRankUP: &eRango Máximo"));
            }
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&m--------------------------"));
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("gamemaker")) {
            if (p.hasPermission("chg.gamemaker")) {
                if (LCCHG.isSpectator(p).booleanValue()) {
                    LCCHG.remSpectator(p);
                    BGChat.printPlayerChat(p, ChatColor.GREEN + "¡Ya no eres Espectador!");
                    p.setGameMode(GameMode.CREATIVE);
                    return true;
                }
                LCCHG.addSpectator(p);
                p.setGameMode(GameMode.SPECTATOR);
                BGChat.printPlayerChat(p, ChatColor.RED + "&c¡Ahora eres Espectador!");
                return true;
            }
            BGChat.printPlayerChat(p, ChatColor.RED + Translation.NO_PERMISSION.t());
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("vanish")) {
            if (p.hasPermission("chg.vanish")) {
                if (!LCCHG.isSpectator(p).booleanValue())
                    LCCHG.spectators.add(p);
                if (LCCHG.gamers.contains(p))
                    LCCHG.gamers.remove(p);
                if (p.getGameMode() == GameMode.SURVIVAL) {
                    p.setGameMode(GameMode.CREATIVE);
                    for (Player Online : Bukkit.getOnlinePlayers())
                        Online.hidePlayer(p);
                    BGChat.printPlayerChat(p, ChatColor.GREEN + "¡Ahora eres invisible!");
                } else {
                    p.setGameMode(GameMode.SURVIVAL);
                    p.setAllowFlight(true);
                    p.setFlying(true);
                    for (Player Online : Bukkit.getOnlinePlayers())
                        Online.showPlayer(p);
                    BGChat.printPlayerChat(p, ChatColor.GREEN + "¡Ahora eres visible!");
                }
                return true;
            }
            BGChat.printPlayerChat(p, ChatColor.RED + Translation.NO_PERMISSION.t());
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("kitinfo")) {
            if (args.length != 1)
                return false;
            BGChat.printKitInfo(p, args[0]);
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("kit")) {
            if (LCCHG.GAMESTATE != GameState.PREGAME) {
                BGChat.printPlayerChat(p, ChatColor.RED + Translation.GAME_BEGUN.t());
                return true;
            }
            if (args.length != 1) {
                BGChat.printKitChat(p);
                return true;
            }
            BGKit.setKit(p, args[0]);
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (LCCHG.GAMESTATE != GameState.PREGAME && !LCCHG.isSpectator(p).booleanValue()) {
                BGChat.printPlayerChat(p, Translation.GAME_BEGUN.t());
                return true;
            }
            p.teleport(LCCHG.getSpawn());
            BGChat.printPlayerChat(p, ChatColor.GREEN + Translation.TELEPORTED_SPAWN.t());
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("desbug")) {
            if (LCCHG.GAMESTATE != GameState.INVINCIBILITY) {
                BGChat.printPlayerChat(p, Translation.GAME_BEGUN.t());
                return true;
            }
            p.teleport(p);
            BGChat.printPlayerChat(p, ChatColor.GOLD + "¡Desbugeado!");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("hack"))
            if (LCCHG.isSpectator(p).booleanValue()) {
                try {
                    p.setGameMode(GameMode.SURVIVAL);
                    p.setAllowFlight(true);
                    p.setFlying(true);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 40, 1));
                    p.sendMessage(ChatColor.GREEN + "Ahora eres visible durante un momento..");
                    if (p.hasPermission("chg.hack")) {
                        Bukkit.getScheduler().runTaskLater((Plugin)LCCHG.instance, new Runnable() {
                            public void run() {
                                p.setGameMode(GameMode.SPECTATOR);
                                p.setAllowFlight(true);
                                p.setFlying(true);
                                p.setFlySpeed(0.2F);
                                p.sendMessage(ChatColor.YELLOW + "Ahora vuelves a ser invisible.");
                            }
                        },  4L);
                    } else {
                        Bukkit.getScheduler().runTaskLater((Plugin)LCCHG.instance, () -> {
                            p.setGameMode(GameMode.SPECTATOR);
                            p.setAllowFlight(true);
                            p.setFlying(true);
                            p.setFlySpeed(0.2F);
                            p.sendMessage(ChatColor.YELLOW + "Ahora vuelves a ser invisible.");
                        },  2L);
                    }
                } catch (Exception ex) {
                    p.setGameMode(GameMode.SPECTATOR);
                    ex.printStackTrace();
                }
            } else {
                p.sendMessage(ChatColor.RED + "Comando solo para espectadores.");
            }
        if (cmd.getName().equalsIgnoreCase("teleport"))
            if (LCCHG.isSpectator(p).booleanValue()) {
                if (args.length > 2)
                    return false;
                if (args.length == 0) {
                    BGChat.printPlayerChat(p, ChatColor.YELLOW + Translation.TELEPORT_FUNC_CMDS.t());
                    return true;
                }
                if (args.length == 1) {
                    if (Bukkit.getServer().getPlayer(args[0]) == null) {
                        BGChat.printPlayerChat(p, ChatColor.RED + Translation.PLAYER_NOT_ONLINE.t());
                        return true;
                    }
                    Player target = Bukkit.getServer().getPlayer(args[0]);
                    BGChat.printPlayerChat(p, ChatColor.GREEN + Translation.TELEPORT_FUNC_TELEPORTED_PLAYER.t().replace("<player>", target.getName()));
                    p.teleport((Entity)target);
                    return true;
                }
                int x = 0;
                int z = 0;
                try {
                    x = Integer.parseInt(args[0]);
                    z = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    BGChat.printPlayerChat(p, ChatColor.RED + Translation.TELEPORT_FUNC_COORDS_NOT_VALID.t());
                    return true;
                }
                Location loc = new Location(Bukkit.getServer().getWorlds().get(0), x, ((World)Bukkit.getServer().getWorlds().get(0)).getHighestBlockYAt(x, z) + 1.5D, z);
                BGChat.printPlayerChat(p, ChatColor.GREEN + Translation.TELEPORT_FUNC_TELEPORTED_COORDS.t().replace("<x>", (new StringBuilder(String.valueOf(x))).toString()).replace("<z>", (new StringBuilder(String.valueOf(z))).toString()));
                p.teleport(loc);
                return true;
            } else {
                BGChat.printPlayerChat(p, ChatColor.RED + Translation.NO_PERMISSION.t());
                return true;
            }
        return true;
    }
}
