package lc.chg.game.litesteners;

import lc.chg.LCCHG;
import lc.chg.configuration.LCConfig;
import lc.chg.configuration.Translation;
import lc.chg.game.BGKit;
import lc.chg.game.GameState;
import lc.chg.game.litesteners.habilidades.Habilidades;
import lc.chg.game.placeholderapi.LCPAPI;
import lc.chg.game.timers.PreGameTimer;
import lc.chg.game.utils.BGChat;
import lc.chg.game.utils.BGSign;
import lc.chg.game.utils.BGTeam;
import lc.chg.game.utils.Tagged;
import lc.core.entidades.Jugador;
import lc.core.entidades.database.CHGInfoQuery;
import lc.core.entidades.database.Database;
import lc.core.entidades.database.LCoinsQuery;
import lc.core.entidades.database.VipPointsQuery;
import lc.core.entidades.minijuegos.CHGRank;
import lc.core.utilidades.IconMenu;
import lc.core.utilidades.ItemUtils;
import lc.core.utilidades.Util;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;

public class BGGameListener implements Listener {
    private static ItemStack stats_item = (ItemStack)new ItemUtils(Material.PAPER, Short.valueOf((short)0), Integer.valueOf(1), ChatColor.GREEN + "TOP Jugadores", ChatColor.GRAY + "Click derecho para ver el top de jugadores");

    private static ItemStack kit_item = (ItemStack)new ItemUtils(Material.BOW, Short.valueOf((short)0), Integer.valueOf(1), ChatColor.GREEN + "Selector De Kit", ChatColor.GRAY + "Click derecho para abrir el menu de kits");

    private static ItemStack book_item;

    private static ItemStack vippoints_item = (ItemStack)new ItemUtils(Material.GOLD_INGOT, Short.valueOf((short)0), Integer.valueOf(1), ChatColor.GREEN + "Menú de Vip Points", ChatColor.GRAY + "Click derecho para abrir el menu de vip points");

    private void giveStatsItem(Player player){
        player.getInventory().setItem(4,
                new ItemUtils(player.getName(), 1, ChatColor.GREEN + "Tus Estadísticas", ChatColor.GRAY + "Click derecho para ver tus estadísticas"));

    }
    private static IconMenu invStats_CHG = null;

    private static IconMenu invStats = null;

    private static IconMenu invVipPoints = null;

    private static IconMenu invStats_CHG_kills = null;

    private static IconMenu invStats_CHG_deaths = null;

    private static IconMenu invStats_CHG_part_ganadas = null;

    private static IconMenu invStats_CHG_part_jugadas = null;

    private static IconMenu invStats_CHG_lvl = null;

    static {
        book_item = new ItemStack(Material.WRITTEN_BOOK);
        List<String> pages = LCConfig.bookconf.getStringList("content");
        List<String> content = new ArrayList<>();
        List<String> page = new ArrayList<>();
        for (String line : pages) {
            line = line.replace("<server_title>", LCCHG.SERVER_TITLE);
            line = line.replace("<space>", ChatColor.RESET + "\n");
            line = ChatColor.translateAlternateColorCodes('&', line);
            if (!line.contains("<newpage>")) {
                page.add(String.valueOf(line) + "\n");
                continue;
            }
            String str1 = "";
            for (String l : page)
                str1 = String.valueOf(str1) + l;
            content.add(str1);
            page.clear();
        }
        String pagestr = "";
        for (String l : page)
            pagestr = String.valueOf(pagestr) + l;
        content.add(pagestr);
        page.clear();
        BookMeta im = (BookMeta)book_item.getItemMeta();
        im.setPages(content);
        im.setAuthor(LCConfig.bookconf.getString("author"));
        im.setTitle(LCConfig.bookconf.getString("title"));
        book_item.setItemMeta((ItemMeta)im);
    }



    private static IconMenu getInvStats_CHG_kills() {
        if (invStats_CHG_kills == null) {
            invStats_CHG_kills = new IconMenu("TOP Asesinatos - CHG", 45, e -> {
                e.setWillClose(false);
                e.setWillDestroy(false);
                if (e.getPosition() == 31)
                    BGGameListener.getInvStats_CHG().open(e.getPlayer());
            }, LCCHG.instance);
            LinkedHashMap<String, Integer> top = Database.getTop(18, "kills", "CHGInfo");
            int slot = 0;
            for (Map.Entry<String, Integer> es : top.entrySet())
                invStats_CHG_kills.setOption(slot++, (ItemStack)new ItemUtils(es.getKey(), Integer.valueOf(1), ChatColor.GOLD + ""+ChatColor.BOLD + "#" + slot + ChatColor.DARK_GRAY + " - " + ChatColor.RED + (String)es.getKey(), ChatColor.GRAY + ""+es.getValue() + " asesinatos"));
            invStats_CHG_kills.setOption(31, new ItemStack(Material.MAP), ChatColor.GRAY +""+ ChatColor.BOLD + "Regresar", new String[0]);
        }
        return invStats_CHG_kills;
    }

    private static IconMenu getInvStats_CHG_partidas_ganadas() {
        if (invStats_CHG_part_ganadas == null) {
            invStats_CHG_part_ganadas = new IconMenu("TOP Partidas Ganadas - CHG", 45, e -> {
                e.setWillClose(false);
                e.setWillDestroy(false);
                if (e.getPosition() == 31)
                    BGGameListener.getInvStats_CHG().open(e.getPlayer());
            },  (Plugin)LCCHG.instance);
            LinkedHashMap<String, Integer> top = Database.getTop(18, "ganadas", "CHGInfo");
            int slot = 0;
            for (Map.Entry<String, Integer> es : top.entrySet())
                invStats_CHG_part_ganadas.setOption(slot++, (ItemStack)new ItemUtils(es.getKey(), Integer.valueOf(1), ChatColor.GOLD + ""+ChatColor.BOLD + "#" + slot + ChatColor.DARK_GRAY + " - " + ChatColor.RED + (String)es.getKey(), ChatColor.GRAY + ""+es.getValue() + " partidas ganadas"));
            invStats_CHG_part_ganadas.setOption(31, new ItemStack(Material.MAP), ChatColor.GRAY +""+ ChatColor.BOLD + "Regresar", new String[0]);
        }
        return invStats_CHG_part_ganadas;
    }

    private static IconMenu getInvStats_CHG_partidas_jugadas() {
        if (invStats_CHG_part_jugadas == null) {
            invStats_CHG_part_jugadas = new IconMenu("TOP Partidas Jugadas - CHG", 45, new IconMenu.OptionClickEventHandler() {
                public void onOptionClick(IconMenu.OptionClickEvent e) {
                    e.setWillClose(false);
                    e.setWillDestroy(false);
                    if (e.getPosition() == 31)
                        BGGameListener.getInvStats_CHG().open(e.getPlayer());
                }
            },  (Plugin)LCCHG.instance);
            LinkedHashMap<String, Integer> top = Database.getTop(18, "jugadas", "CHGInfo");
            int slot = 0;
            for (Map.Entry<String, Integer> es : top.entrySet())
                invStats_CHG_part_jugadas.setOption(slot++, (ItemStack)new ItemUtils(es.getKey(), Integer.valueOf(1), ChatColor.GOLD +""+ ChatColor.BOLD + "#" + slot +""+ ChatColor.DARK_GRAY + " - " + ChatColor.RED + (String)es.getKey(), ChatColor.GRAY + ""+es.getValue() + " partidas jugadas"));
            invStats_CHG_part_jugadas.setOption(31, new ItemStack(Material.MAP), ChatColor.GRAY+ ""+ ChatColor.BOLD + "Regresar", new String[0]);
        }
        return invStats_CHG_part_jugadas;
    }

    private static IconMenu getInvStats_CHG_lvl() {
        if (invStats_CHG_lvl == null) {
            invStats_CHG_lvl = new IconMenu("TOP Nivel - CHG", 45, e -> {
                e.setWillClose(false);
                e.setWillDestroy(false);
                if (e.getPosition() == 31)
                    BGGameListener.getInvStats_CHG().open(e.getPlayer());
            },  (Plugin)LCCHG.instance);
            LinkedHashMap<String, Integer> top = Database.getTop(18, "rango", "CHGInfo");
            int slot = 0;
            for (Map.Entry<String, Integer> es : top.entrySet())
                invStats_CHG_lvl.setOption(slot++, (ItemStack)new ItemUtils(es.getKey(), Integer.valueOf(1), ChatColor.GOLD +""+ ChatColor.BOLD + "#" + slot + ChatColor.DARK_GRAY + " - " + ChatColor.RED + (String)es.getKey(), ChatColor.GRAY + "Nivel: " + es.getValue() + " "));
            invStats_CHG_lvl.setOption(31, new ItemStack(Material.MAP), ChatColor.GRAY + ""+ChatColor.BOLD + "Regresar", new String[0]);
        }
        return invStats_CHG_lvl;
    }

    private static IconMenu getInvStats_CHG() {
        if (invStats_CHG == null) {
            invStats_CHG = new IconMenu("TOP Jugadores - CHG", 45, new IconMenu.OptionClickEventHandler() {
                public void onOptionClick(IconMenu.OptionClickEvent e) {
                    e.setWillClose(false);
                    e.setWillDestroy(false);
                    switch (e.getPosition()) {
                        case 19:
                            BGGameListener.getInvStats_CHG_kills().open(e.getPlayer());
                            break;
                        case 21:
                            BGGameListener.getInvStats_CHG_partidas_ganadas().open(e.getPlayer());
                            break;
                        case 23:
                            BGGameListener.getInvStats_CHG_partidas_jugadas().open(e.getPlayer());
                            break;
                    }
                }
            },  (Plugin)LCCHG.instance);
            invStats_CHG.setOption(19, new ItemStack(Material.SIGN), ChatColor.GREEN + ""+ChatColor.BOLD + "Asesinatos   ", new String[] { ChatColor.GRAY + "Click para mostrar a los usuarios con", ChatColor.GRAY + "mas asesinatos" });
            invStats_CHG.setOption(21, new ItemStack(Material.SIGN), ChatColor.GREEN + ""+ChatColor.BOLD + "Partidas Ganadas", new String[] { ChatColor.GRAY + "Click para mostrar a los usuarios con", ChatColor.GRAY + "mas partidas ganadas" });
            invStats_CHG.setOption(23, new ItemStack(Material.SIGN), ChatColor.GREEN + ""+ChatColor.BOLD + "Partidas Jugadas", new String[] { ChatColor.GRAY + "Click para mostrar a los usuarios con", ChatColor.GRAY + "mas partidas jugadas" });
            invStats_CHG.setOption(25, new ItemStack(Material.SIGN), ChatColor.GREEN + ""+ChatColor.BOLD + "Muertes  ", new String[] { ChatColor.GRAY + "Click para mostrar a los usuarios con", ChatColor.GRAY + "mas muertes" });
        }
        return invStats_CHG;
    }

    private static IconMenu getInv_VipPoints(Player p) {
        VipPointsQuery.load_PlayerVipPoints(Jugador.getJugador(p));
            if (invVipPoints == null) {
                invVipPoints = new IconMenu("Menú de Vip Points", 27, (IconMenu.OptionClickEventHandler) e -> {
                    e.setWillClose(false);
                    e.setWillDestroy(false);
                    switch (e.getPosition()) {
                        case 9:
                            BGChat.printPlayerChat(e.getPlayer(), "&eCompra o canjea(/lobby) el rango &b&lVIP");
                            BGChat.printPlayerChat(e.getPlayer(), "&bhttps://tienda.mine.lc");
                            break;
                        case 11:
                            BGChat.printPlayerChat(e.getPlayer(), "&eCompra o canjea(/lobby) el rango &a&lSVIP");
                            BGChat.printPlayerChat(e.getPlayer(), "&ahttps://tienda.mine.lc");
                            break;
                        case 15:
                            BGChat.printPlayerChat(e.getPlayer(), "&eCompra o canjea(/lobby) el rango &6&lELITE");
                            BGChat.printPlayerChat(e.getPlayer(), "&6https://tienda.mine.lc");
                            break;
                        case 17:
                            BGChat.printPlayerChat(e.getPlayer(), "&eCompra o canjea(/lobby) el rango &c&lRUBY");
                            BGChat.printPlayerChat(e.getPlayer(), "&chttps://tienda.mine.lc");
                            break;
                    }
                }, (Plugin) LCCHG.instance);
                invVipPoints.setOption(9, new ItemStack(Material.IRON_INGOT), ChatColor.AQUA + "Rango " + ChatColor.BOLD + "VIP", new String[]{ChatColor.GRAY + "Desde: " + ChatColor.YELLOW + "75" + ChatColor.YELLOW + " VIP-Points", ChatColor.GRAY + "/lobby para reclamarlos!"});
                invVipPoints.setOption(11, new ItemStack(Material.GOLD_INGOT), ChatColor.GREEN + "Rango " + ChatColor.BOLD + "SVIP", new String[]{ChatColor.GRAY + "Desde: " + ChatColor.YELLOW + "150" + ChatColor.YELLOW + " VIP-Points", ChatColor.GRAY + "/lobby para reclamarlos!"});
                ItemUtils itemUtils = new ItemUtils(p.getName(), Integer.valueOf(1), "", "");
                invVipPoints.setOption(13, (ItemStack) itemUtils, ChatColor.GOLD + "Vip Points", new String[]{String.valueOf(ChatColor.YELLOW + "" + PlaceholderAPI.setPlaceholders(p, "%lceco_vippoints%"))});
                invVipPoints.setOption(15, new ItemStack(Material.DIAMOND), ChatColor.GOLD + "Rango " + ChatColor.BOLD + "ELITE", new String[]{ChatColor.GRAY + "Desde: " + ChatColor.YELLOW + "225" + ChatColor.YELLOW + " VIP-Points", ChatColor.GRAY + "/lobby para reclamarlos!"});
                invVipPoints.setOption(17, new ItemStack(Material.EMERALD), ChatColor.RED + "Rango " + ChatColor.BOLD + "RUBY", new String[]{ChatColor.GRAY + "Desde: " + ChatColor.YELLOW + "300" + ChatColor.YELLOW + " VIP-Points", ChatColor.GRAY + "/lobby para reclamarlos!"});
            }
        return invVipPoints;
    }

    private static IconMenu getInv_Stats(Player p) {
        p.sendMessage(ChatColor.YELLOW+"Cargando estadísticas...");
        Jugador j = Jugador.getJugador(p);
        VipPointsQuery.load_PlayerVipPoints(j);
        CHGInfoQuery.load_PlayerCHGInfo(j);
        LCoinsQuery.load_PlayerCoins(j);
        if (invStats == null) {
            invStats = new IconMenu("Tus Estadadísticas", 45, e -> {
                e.setWillClose(false);
                e.setWillDestroy(false);
            }, (Plugin) LCCHG.instance);
            invStats.setOption(10, new ItemStack(Material.DIAMOND_SWORD), ChatColor.GOLD + "" + ChatColor.BOLD + "Asesinatos", ChatColor.GRAY + "" + Jugador.getJugador(p).getChgInfo().getKills());
            invStats.setOption(12, new ItemStack(Material.SKULL_ITEM), ChatColor.GOLD + "" + ChatColor.BOLD + "Muertes", new String[]{ChatColor.GRAY + "" + (Jugador.getJugador(p).getChgInfo().getPlayeds() - Jugador.getJugador(p).getChgInfo().getWins())});
            invStats.setOption(14, new ItemStack(Material.DIAMOND_AXE), ChatColor.GOLD + "" + ChatColor.BOLD + "KDR", new String[]{ChatColor.GRAY + "" + LCPAPI.kdr(Jugador.getJugador(p).getChgInfo().getKills(), Jugador.getJugador(p).getChgInfo().getPlayeds() - Jugador.getJugador(p).getChgInfo().getWins())});
            invStats.setOption(16, new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1), ChatColor.GOLD + "" + ChatColor.BOLD + "Victorias", new String[]{ChatColor.GRAY + "" + Jugador.getJugador(p).getChgInfo().getWins()});
            invStats.setOption(28, new ItemStack(Material.PAPER), ChatColor.GOLD + "" + ChatColor.BOLD + "Jugadas", new String[]{ChatColor.GRAY + "" + Jugador.getJugador(p).getChgInfo().getPlayeds()});
            invStats.setOption(30, new ItemStack(Material.GOLD_INGOT), ChatColor.GOLD + "" + ChatColor.BOLD + "LCoins", new String[]{ChatColor.GRAY + "" + PlaceholderAPI.setPlaceholders(p, "%lceco_lcoins%")});
            invStats.setOption(32, new ItemStack(Material.DIAMOND), ChatColor.GOLD + "" + ChatColor.BOLD + "Vip Points", new String[]{ChatColor.GRAY + PlaceholderAPI.setPlaceholders(p, "%lceco_vippoints%")});
            invStats.setOption(34, new ItemStack(Material.NETHER_STAR), ChatColor.GOLD + "" + ChatColor.BOLD + "Nivel", new String[]{ChatColor.GRAY + StringUtils.capitalize(Jugador.getJugador(p).getChgInfo().getRank().name())});
        }
        return invStats;
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onPlayerChat(AsyncPlayerChatEvent e) {
    if (LCCHG.GAMESTATE == GameState.PREGAME)
        return;
    if (LCCHG.isSpectator(e.getPlayer()).booleanValue())
        e.setFormat(ChatColor.translateAlternateColorCodes('&', "&7&lEspectador " + e.getFormat()));
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onPlayerInteract(PlayerInteractEvent event) {
    Player p = event.getPlayer();
    Action a = event.getAction();
    if ((a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) &&
            LCCHG.GAMESTATE == GameState.PREGAME)
        if (p.getItemInHand().getType() == Material.BOW) {
            BGChat.printKitChat(p);
        } else if (p.getItemInHand().getType() == Material.PAPER) {
            getInvStats_CHG().open(p);

        }else if (p.getItemInHand().getType() == Material.GOLD_INGOT) {
            getInv_VipPoints(p).open(p);
        } else if (p.getItemInHand().getType() == Material.SKULL_ITEM) {
            getInv_Stats(p).open(p);
        }
    if (LCCHG.isSpectator(p).booleanValue()) {
        event.setCancelled(true);
        return;
    }
    if (LCCHG.GAMESTATE == GameState.PREGAME) {
        event.setCancelled(true);
        return;
    }
    if (p.getItemInHand().getType() == Material.COMPASS) {
        Player cplayer = null;
        double cdistance = 1000.0D;
        for (Player gamers : LCCHG.getGamers()) {
            if (p == gamers)
                continue;
            if (BGTeam.isInTeam(p, gamers.getName()))
                continue;
            double distance = gamers.getLocation().distance(p.getLocation());
            if (distance < cdistance) {
                cplayer = gamers;
                cdistance = distance;
            }
        }
        if (cplayer != null) {
            DecimalFormat df = new DecimalFormat("##.#");
            p.sendMessage(ChatColor.GOLD + "La brapunta al jugador " + ChatColor.YELLOW + cplayer.getName() + ChatColor.GREEN + " (" + df.format(cdistance) + " bloques)!");
            p.setCompassTarget(cplayer.getLocation());
        } else {
            p.sendMessage(ChatColor.GRAY + "La brapunta al spawn!");
            p.setCompassTarget(LCCHG.getSpawn());
        }
    }
}

@EventHandler
public void onPing(ServerListPingEvent e) {
    if (LCCHG.GAMESTATE != GameState.PREGAME) {
        e.setMotd(ChatColor.GOLD + "Estado: " + ChatColor.AQUA + "Progreso" + "==" + ChatColor.GOLD + "Mapa: " + ChatColor.AQUA + WordUtils.capitalize(LCCHG.mapa));
    } else {
        e.setMotd(ChatColor.GOLD + "Estado: " + ChatColor.AQUA + "Esperando" + "==" + ChatColor.GOLD + "Mapa: " + ChatColor.AQUA + WordUtils.capitalize(LCCHG.mapa));
    }
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onEntityShootArrow(EntityShootBowEvent event) {
    if (event.getEntity() instanceof Player &&
            LCCHG.isSpectator((Player)event.getEntity()).booleanValue()) {
        event.setCancelled(true);
        return;
    }
    if (event.getEntity() instanceof Player && LCCHG.GAMESTATE == GameState.PREGAME) {
        event.getBow().setDurability((short)0);
        event.setCancelled(true);
    }
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onPlayerDropItem(PlayerDropItemEvent event) {
    if (LCCHG.isSpectator(event.getPlayer()).booleanValue()) {
        event.setCancelled(true);
        return;
    }
    if (LCCHG.GAMESTATE == GameState.PREGAME)
        event.setCancelled(true);
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onEntityExplode(EntityExplodeEvent event) {
    if (LCCHG.GAMESTATE != GameState.GAME) {
        event.setCancelled(true);
        return;
    }
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onPlayerPickupItem(PlayerPickupItemEvent event) {
    if (LCCHG.isSpectator(event.getPlayer()).booleanValue()) {
        event.setCancelled(true);
        return;
    }
    if (LCCHG.GAMESTATE == GameState.PREGAME)
        event.setCancelled(true);
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onPlayerKick(PlayerKickEvent event) {
    event.setLeaveMessage(null);
}

@EventHandler
public void onLogin(AsyncPlayerPreLoginEvent e) {
    Jugador jug = Jugador.getJugador(e.getName());
    Player p = jug.getBukkitInstance();
    try {
        CHGInfoQuery.load_PlayerCHGInfo_ASYNC(jug);
        LCoinsQuery.load_PlayerCoins_ASYNC(jug);
        VipPointsQuery.load_PlayerVipPoints_ASYNC(jug);
    } catch (Exception a) {
        a.printStackTrace();
        p.kickPlayer("&cError al cargar tus datos Ingresa Nuevamente!");
    }
}

@EventHandler
public void onLogin(PlayerLoginEvent e) {
    Jugador jug = Jugador.getJugador(e.getPlayer());
    if (e.getResult() == PlayerLoginEvent.Result.KICK_FULL)
        if (LCCHG.GAMESTATE == GameState.PREGAME) {
            if (e.getPlayer().hasPermission("chg.bypassfull"))
                e.allow();
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "El juego esta lleno!");
        } else {
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, ChatColor.RED + "El juego esta en progreso!");
        }
}

@EventHandler
public void onPickUp(PlayerPickupItemEvent e) {
    Material mat = e.getItem().getItemStack().getType();
    if (mat == Material.DIAMOND_BLOCK || mat == Material.GOLD_ORE || mat == Material.IRON_ORE) {
        e.setCancelled(true);
        e.getItem().remove();
    }
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onPlayerJoin(PlayerJoinEvent event) {
    Player p = event.getPlayer();
    Jugador jug = Jugador.getJugador(p);
    event.setJoinMessage(null);
    p.getInventory().clear();
    p.updateInventory();
    jug.setBukkitInstance(p);
    p.setGameMode(GameMode.SURVIVAL);
    p.setMaxHealth(24.0D);
    p.setHealth(24.0D);
    p.setAllowFlight(true);
    LCCHG.kills.put(p.getName(), Integer.valueOf(0));
    if (LCCHG.GAMESTATE == GameState.PREGAME) {
        LCCHG.gamers.add(p);
        p.getInventory().addItem(new ItemStack[] { kit_item });
        p.getInventory().addItem(new ItemStack[] { book_item });
        giveStatsItem(p);
        p.getInventory().setItem(8, BGGameListener.stats_item);
        p.getInventory().setItem(7, vippoints_item);
    } else {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f" + event.getPlayer().getName() + " &aesta espectando"));
        LCCHG.addSpectator(p);
    }
    String command = "minecraft:gamerule sendCommandFeedback false";
    Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command);
    Util.sendTitle(p, 0, 60, 0, "&6&lCHG", "&awww.mine.lc");
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onBlockBreak(BlockBreakEvent event) {
    if (LCCHG.GAMESTATE == GameState.PREGAME) {
        event.setCancelled(true);
        return;
    }
}

@EventHandler(priority = EventPriority.HIGHEST)
public void FoodCe(FoodLevelChangeEvent e) {
    if (LCCHG.GAMESTATE != GameState.GAME)
        e.setCancelled(true);
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onBlockPlace(BlockPlaceEvent event) {
    if (LCCHG.GAMESTATE == GameState.PREGAME) {
        event.setCancelled(true);
        return;
    }
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onPlayerQuit(PlayerQuitEvent event) {
    Player p = event.getPlayer();
    event.setQuitMessage(null);
    Jugador jug = Jugador.getJugador(p);
    if (LCCHG.gamers.contains(p))
        LCCHG.gamers.remove(p);
    if (LCCHG.isSpectator(p).booleanValue()) {
        LCCHG.remSpectator(p);
    } else if (LCCHG.GAMESTATE == GameState.GAME) {
        long ttime = Tagged.getTime(jug).longValue();
        Jugador killer = Tagged.getKiller(jug);
        if (System.currentTimeMillis() - ttime < 10000L && killer != jug) {
            if (killer != null) {
                killer.getChgInfo().setKills(killer.getChgInfo().getKills()+1);
                LCCHG.kills.put(killer.getBukkitInstance().getName(), Integer.valueOf(((Integer)LCCHG.kills.get(killer.getBukkitInstance().getName())).intValue() + 1));
                sendGameMessage(ChatColor.GRAY + jug.getBukkitInstance().getName() + ChatColor.YELLOW + " se desconecto pero fue asesinado por " +
                        ChatColor.GRAY + killer.getBukkitInstance().getName() + ChatColor.YELLOW + "!");

                CHGInfoQuery.saveCHGInfo(killer);
                Location loc = p.getLocation();
                byte b;
                int i;
                ItemStack[] arrayOfItemStack;
                for (i = (arrayOfItemStack = p.getInventory().getArmorContents()).length, b = 0; b < i; ) {
                    ItemStack is = arrayOfItemStack[b];
                    try {
                        if (is != null)
                            loc.getWorld().dropItem(loc, is);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    b++;
                }
                for (i = (arrayOfItemStack = p.getInventory().getContents()).length, b = 0; b < i; ) {
                    ItemStack is = arrayOfItemStack[b];
                    try {
                        if (is != null)
                            loc.getWorld().dropItem(loc, is);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    b++;
                }
            }
        } else {
            sendGameMessage(ChatColor.GRAY + jug.getBukkitInstance().getName() + ChatColor.YELLOW + " ha muerto.");
        }
        LCCHG.checkwinner();
    }
    Tagged.removeTagged(jug);
    if (LCCHG.TEAMS.containsKey("kills" + p.getName()))
        ((Team)LCCHG.TEAMS.remove("kills" + p.getName())).unregister();
    if (LCCHG.TEAMS.containsKey("deaths" + p.getName()))
        ((Team)LCCHG.TEAMS.remove("deaths" + p.getName())).unregister();
    if (LCCHG.TEAMS.containsKey("kdr" + p.getName()))
        ((Team)LCCHG.TEAMS.remove("kdr" + p.getName())).unregister();
    if (LCCHG.TEAMS.containsKey("lvl" + p.getName()))
        ((Team)LCCHG.TEAMS.remove("lvl" + p.getName())).unregister();
    p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
    p.getScoreboard().clearSlot(DisplaySlot.BELOW_NAME);
    Jugador.removeJugador(jug);
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    Entity entityDamager = event.getDamager();
    Entity entityDamaged = event.getEntity();
    if (entityDamager instanceof Arrow) {
        if (entityDamaged instanceof Player && ((Arrow)entityDamager).getShooter() instanceof Player) {
            Arrow arrow = (Arrow)entityDamager;
            Vector velocity = arrow.getVelocity();
            Player shooter = (Player)arrow.getShooter();
            Player damaged = (Player)entityDamaged;
            if (LCCHG.isSpectator(damaged).booleanValue()) {
                double x = (damaged.getLocation().getBlockX() + 2);
                double y = (damaged.getLocation().getBlockY() + 10);
                double z = (damaged.getLocation().getBlockZ() + 2);
                Location loc = new Location(damaged.getWorld(), x, y, z);
                damaged.teleport(loc);
                BGChat.printPlayerChat(damaged, ChatColor.RED + Translation.SPECTATOR_IN_THE_WAY.t());
                Arrow newArrow = (Arrow)shooter.launchProjectile(Arrow.class);
                newArrow.setShooter(shooter);
                newArrow.setVelocity(velocity);
                newArrow.setBounce(false);
                event.setCancelled(true);
                arrow.remove();
            }
        }
    } else if (entityDamager instanceof Player) {
        Player player = (Player)event.getDamager();
        if (LCCHG.isSpectator((Player)entityDamager).booleanValue()) {
            event.setCancelled(true);
            return;
        }
        if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            Collection<PotionEffect> pe = player.getActivePotionEffects();
            for (PotionEffect effect : pe) {
                if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                    if (effect.getAmplifier() == 0) {
                        event.setDamage(event.getDamage() - 9.0D);
                        continue;
                    }
                    event.setDamage(event.getDamage() - 11.5D);
                }
            }
        }
    }
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
    if (event.getRightClicked() instanceof Player && LCCHG.isSpectator((Player)event.getRightClicked()).booleanValue() &&
            !LCCHG.isSpectator(event.getPlayer()).booleanValue()) {
        event.getRightClicked().teleport(LCCHG.getSpawn());
        BGChat.printPlayerChat((Player)event.getRightClicked(), ChatColor.RED + Translation.SPECTATOR_IN_THE_WAY.t());
        event.setCancelled(true);
        return;
    }
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onEntityDamage(EntityDamageEvent event) {
    if (event.getEntity() instanceof Player) {
        Player p = (Player)event.getEntity();
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            if (LCCHG.isSpectator(p).booleanValue() || LCCHG.GAMESTATE == GameState.INVINCIBILITY || LCCHG.GAMESTATE == GameState.PREGAME) {
                event.setCancelled(true);
                p.setFallDistance(0.0F);
                p.teleport(p.getWorld().getSpawnLocation());
                p.playSound(p.getLocation(), Sound.HURT_FLESH, 1.0F, 1.3F);
            }
        } else if (LCCHG.isSpectator(p).booleanValue()) {
            event.setCancelled(true);
            return;
        }
    }
    if (LCCHG.GAMESTATE != GameState.GAME && event.getEntity() instanceof Player) {
        event.setCancelled(true);
        return;
    }
    if (LCCHG.GAMESTATE == GameState.PREGAME && !(event.getEntity() instanceof Player)) {
        event.setCancelled(true);
        return;
    }
}

@EventHandler
public void onPlayerRespawn(PlayerRespawnEvent e) {
    Player p = e.getPlayer();
    e.setRespawnLocation(p.getLocation());
    LCCHG.addSpectator(p);
    p.sendMessage(ChatColor.AQUA + "Ahora eres espectador. Para salir usa el comando /lobby!");
}

@EventHandler(priority = EventPriority.HIGH)
public void onDeath(PlayerDeathEvent e) {
    e.setDeathMessage(null);
    final Player p = e.getEntity();
    Jugador target = Jugador.getJugador(p);
    Player player1 = e.getEntity();
    LCCHG.setFame(p, Integer.valueOf(0));
    EntityDamageEvent.DamageCause damageCause = EntityDamageEvent.DamageCause.CUSTOM;
    if (player1.getLastDamageCause() != null)
        damageCause = player1.getLastDamageCause().getCause();
    if (LCCHG.gamers.contains(p))
        LCCHG.gamers.remove(p);
    LCCHG.spectators.add(p);
    BGChat.printPlayerChat(p, ChatColor.YELLOW + Translation.NOW_SPECTATOR.t());
    EntityDamageEvent.DamageCause dCause = damageCause;
    if (System.currentTimeMillis() - Tagged.getTime(target).longValue() < 10000L) {
        Jugador killer = Tagged.getKiller(target);
        if (killer != null) {
            LCCHG.AddFame(killer.getBukkitInstance());
            killer.getChgInfo().setKills(killer.getChgInfo().getKills() + 1);

            CHGRank rank = killer.getChgInfo().getRank();
            int kills = killer.getChgInfo().getKills();
            if (kills >= 300 && kills < 500) {
                if (rank != CHGRank.APRENDIZ) {
                    killer.getChgInfo().setRank(CHGRank.APRENDIZ);
                    killer.getBukkitInstance().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Aprendiz!!"));
                }
            } else if (kills >= 500 && kills < 1000) {
                if (rank != CHGRank.HÉROE) {
                    killer.getChgInfo().setRank(CHGRank.HÉROE);
                    killer.getBukkitInstance().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Heroe!!"));
                }
            } else if (kills >= 1000 && kills < 2000) {
                if (rank != CHGRank.FEROZ) {
                    killer.getChgInfo().setRank(CHGRank.FEROZ);
                    killer.getBukkitInstance().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Feroz!!"));
                }
            } else if (kills >= 2000 && kills < 3000) {
                if (rank != CHGRank.PODEROSO) {
                    killer.getChgInfo().setRank(CHGRank.PODEROSO);
                    killer.getBukkitInstance().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Poderoso!!"));
                }
            } else if (kills >= 3000 && kills < 4000) {
                if (rank != CHGRank.MORTAL) {
                    killer.getChgInfo().setRank(CHGRank.MORTAL);
                    killer.getBukkitInstance().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aFelicidades, ahora eres &2Mortal!!"));
                }
            } else if (kills >= 4000 && kills < 5000) {
                if (rank != CHGRank.TERRORÍFICO) {
                    killer.getChgInfo().setRank(CHGRank.TERRORÍFICO);
                    killer.getBukkitInstance().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&aFelicidades, ahora eres &2Terrorífico!!"));
                }
            } else if (kills >= 5000 && kills < 6000) {
                if (rank != CHGRank.CONQUISTADOR) {
                    killer.getChgInfo().setRank(CHGRank.CONQUISTADOR);
                    killer.getBukkitInstance().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&aFelicidades, ahora eres &2Conquistador!!"));
                }
            } else if (kills >= 6000 && kills < 7000) {
                if (rank != CHGRank.RENOMBRADO) {
                    killer.getChgInfo().setRank(CHGRank.RENOMBRADO);
                    killer.getBukkitInstance().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&aFelicidades, ahora eres &2 Renombrado!!"));
                }
            } else if (kills >= 7000 && kills < 8000) {
                if (rank != CHGRank.ILUSTRE) {
                    killer.getChgInfo().setRank(CHGRank.ILUSTRE);
                    killer.getBukkitInstance().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&aFelicidades, ahora eres &2Ilustre!!"));
                }
            } else if (kills >= 8000 && kills < 9000) {
                if (rank != CHGRank.EMINENTE) {
                    killer.getChgInfo().setRank(CHGRank.EMINENTE);
                    killer.getBukkitInstance().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&aFelicidades, ahora eres &2Eminente!!"));
                }
            } else if (kills >= 9000 && kills < 10000) {
                if (rank != CHGRank.REY) {
                    killer.getChgInfo().setRank(CHGRank.REY);
                    killer.getBukkitInstance().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&aFelicidades, ahora eres &2Rey!!"));
                }
            } else if (kills >= 10000 && kills < 15000) {
                if (rank != CHGRank.EMPERADOR) {
                    killer.getChgInfo().setRank(CHGRank.EMPERADOR);
                    killer.getBukkitInstance().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&aFelicidades, ahora eres &2Emperador!!"));
                }
            } else if (kills >= 15000 && kills < 20000) {
                if (rank != CHGRank.LEGENDARIO) {
                    killer.getChgInfo().setRank(CHGRank.LEGENDARIO);
                    killer.getBukkitInstance().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&aFelicidades, ahora eres &2Legendario!!"));
                }
            } else if (kills >= 20000 && rank != CHGRank.MÍTICO) {
                killer.getChgInfo().setRank(CHGRank.MÍTICO);
                killer.getBukkitInstance().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&aFelicidades, ahora eres &2Mítico!!"));
            }
            LCCHG.kills.put(killer.getBukkitInstance().getName(), Integer.valueOf(((Integer) LCCHG.kills.get(killer.getBukkitInstance().getName())).intValue() + 1));

            int i = 1;
            if(killer.isVIP()) i = 2;
            if(killer.isSVIP()) i = 3;
            if(killer.isELITE()) i = 4;
            if(killer.isRUBY()) i = 5;

            LCCHG.addBalance(target, i);
            CHGInfoQuery.saveCHGInfo(killer);
        }

    }
    target.getChgInfo().setPlayeds(target.getChgInfo().getPlayeds()+1);
    CHGInfoQuery.saveCHGInfo(target);
    Bukkit.getScheduler().runTaskLater((Plugin)LCCHG.instance, () -> {
        if (p.isDead())
            p.sendMessage("");
    },  8L);
    Location loc = p.getLocation();
    String fl = ChatColor.WHITE + ""+ChatColor.BOLD + LCConfig.dsign.getString("FIRST_LINE");
    String sl = ChatColor.DARK_RED + LCConfig.dsign.getString("SECOND_LINE");
    fl = fl.replace("[name]", p.getName());
    sl = sl.replace("[name]", p.getName());
    BGSign.createSign(loc, fl, sl, "", "");
    if (p.getKiller() != null && p.getKiller() instanceof Player) {
Player killer = p.getKiller();
      if (BGKit.hasAbility(killer, Habilidades.INVISIBLE_AL_COMER_MANZANA).booleanValue())
        if (killer.getFoodLevel() <= 14) {
            killer.setFoodLevel(killer.getFoodLevel() + 6);
        } else {
            killer.setFoodLevel(20);
        }
                }
                ((World)Bukkit.getServer().getWorlds().get(0)).strikeLightningEffect(p.getLocation().clone().add(0.0D, 50.0D, 0.0D));
        LCCHG.checkwinner();
  }

private void sendGameMessage(String message) {
    for (Player Online : Bukkit.getOnlinePlayers())
        Online.sendMessage(message);
}

private String getDeathMessage(EntityDamageEvent.DamageCause dCause, boolean withHelp, Jugador target, Jugador killer) {
    String first = "";
    String second = ChatColor.RED + " por " + ChatColor.RED + killer.getBukkitInstance().getName();
    try {
        if (dCause.equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || dCause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
            first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " exploto";
        } else if (dCause.equals(EntityDamageEvent.DamageCause.DROWNING)) {
            first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " se ahogo";
        } else if (dCause.equals(EntityDamageEvent.DamageCause.FIRE) || dCause.equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
            first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " murio quemado";
        } else if (dCause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            if (killer.getBukkitInstance().getItemInHand().getType() == null) {
                first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " fue asesinado por " + ChatColor.GREEN + killer.getBukkitInstance().getName();
                second = "";
            } else {
                String item = killer.getBukkitInstance().getItemInHand().getItemMeta().getDisplayName();
                if (item == null) {
                    first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " fue asesinado por " + ChatColor.GREEN + killer.getBukkitInstance().getName();
                    second = "";
                } else {
                    first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " fue asesinado por " + ChatColor.GREEN + killer.getBukkitInstance().getName() + ChatColor.RED + " usando " + ChatColor.DARK_RED + killer.getBukkitInstance().getItemInHand().getItemMeta().getDisplayName();
                    second = "";
                }
            }
        } else if (dCause.equals(EntityDamageEvent.DamageCause.FALLING_BLOCK)) {
            first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " fue aplastado";
        } else if (dCause.equals(EntityDamageEvent.DamageCause.WITHER)) {
            first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " murio por magia oscura";
        } else if (dCause.equals(EntityDamageEvent.DamageCause.POISON)) {
            first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " murio envenenado";
        } else if (dCause.equals(EntityDamageEvent.DamageCause.LAVA)) {
            first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " murio quemado";
        } else if (dCause.equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " fue disparado por " + ChatColor.GREEN + killer.getBukkitInstance().getName();
            second = "";
        } else if (dCause.equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
            first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " murio sofocado";
        } else if (dCause.equals(EntityDamageEvent.DamageCause.FALL)) {
            first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " se cayo de muy alto";
        } else if (dCause.equals(EntityDamageEvent.DamageCause.VOID)) {
            first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " cayo al vacio";
        } else {
            first = ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " murio";
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        return ChatColor.DARK_RED + target.getBukkitInstance().getName() + ChatColor.RED + " murio.";
    }
    if (withHelp)
        return String.valueOf(first) + second + ChatColor.RED + "!";
    return String.valueOf(first) + ChatColor.RED + ".";
}

@EventHandler
public void onEntityDamageEntity(EntityDamageByEntityEvent e) {
    Entity ent = e.getEntity();
    if (ent instanceof Player) {
        Jugador target = Jugador.getJugador((Player)ent);
        Entity damager = e.getDamager();
        if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            if (damager instanceof Snowball) {
                Snowball snowball = (Snowball)damager;
                if (snowball.getShooter() instanceof Player) {
                    Jugador killer = Jugador.getJugador((Player)snowball.getShooter());
                    Tagged.addTagged(target, killer, Long.valueOf(System.currentTimeMillis()));
                    return;
                }
            } else if (damager instanceof Egg) {
                Egg egg = (Egg)damager;
                if (egg.getShooter() instanceof Player) {
                    Jugador killer = Jugador.getJugador((Player)egg.getShooter());
                    Tagged.addTagged(target, killer, Long.valueOf(System.currentTimeMillis()));
                    return;
                }
            } else if (damager instanceof Arrow) {
                    Arrow arrow = (Arrow)damager;
                    if (arrow.getShooter() instanceof Player) {
                        Jugador killer = Jugador.getJugador((Player)arrow.getShooter());
                        Tagged.addTagged(target, killer, Long.valueOf(System.currentTimeMillis()));
                        return;
                    }
                } else if (damager instanceof EnderPearl) {
                    EnderPearl ePearl = (EnderPearl)damager;
                    if (ePearl.getShooter() instanceof Player) {
                        Jugador killer = Jugador.getJugador((Player)ePearl.getShooter());
                        Tagged.addTagged(target, killer, Long.valueOf(System.currentTimeMillis()));
                        return;
                    }
                } else if (damager instanceof ThrownPotion) {
                    ThrownPotion potion = (ThrownPotion)damager;
                    if (potion.getShooter() instanceof Player) {
                        Jugador killer = Jugador.getJugador((Player)potion.getShooter());
                        Tagged.addTagged(target, killer, Long.valueOf(System.currentTimeMillis()));
                        return;
                    }
                }
            } else if (damager instanceof Player) {
                Jugador killer = Jugador.getJugador((Player)damager);
                Tagged.addTagged(target, killer, Long.valueOf(System.currentTimeMillis()));
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().toLowerCase().startsWith("/me ") || event.getMessage().toLowerCase().startsWith("/kil") || event.getMessage().toLowerCase().contains(":me ")) {
            event.setCancelled(true);
            return;
        }
        if (event.getMessage().toLowerCase().startsWith("/say ")) {
            if (event.getPlayer().hasPermission("bg.admin.*")) {
                String say = event.getMessage().substring(5);
                BGChat.printInfoChat(say);
            }
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
        if (event.getEntity() instanceof Player && LCCHG.isSpectator((Player)event.getEntity()).booleanValue())
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        Entity entity = event.getAttacker();
        if (entity instanceof Player && LCCHG.isSpectator((Player)entity).booleanValue())
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleEnter(VehicleEnterEvent event) {
        Entity entity = event.getEntered();
        if (entity instanceof Player && LCCHG.isSpectator((Player)entity).booleanValue())
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleDamage(VehicleDamageEvent event) {
        Entity entity = event.getAttacker();
        if (entity instanceof Player && LCCHG.isSpectator((Player)entity).booleanValue())
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerEntityShear(PlayerShearEntityEvent event) {
        if (LCCHG.isSpectator(event.getPlayer()).booleanValue()) {
            event.setCancelled(true);
        }
    }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onProjectileHit(ProjectileHitEvent event) {
            if (event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Player &&
                    LCCHG.isSpectator((Player)event.getEntity().getShooter()).booleanValue()) {
                event.getEntity().remove();
                return;
            }
        }
}
