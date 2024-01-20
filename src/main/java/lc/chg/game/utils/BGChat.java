package lc.chg.game.utils;

import lc.chg.LCCHG;
import lc.chg.configuration.LCConfig;
import lc.chg.game.BGKit;
import lc.chg.game.litesteners.habilidades.Habilidades;
import lc.core.entidades.Jugador;
import lc.core.utilidades.IconMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BGChat {
    static Integer TIP_COUNT = Integer.valueOf(0);

    static List<String> TIPS = new ArrayList<>();

    public BGChat() {
        List<String> tiplist = LCConfig.config.getStringList("TIPS");
        TIPS.addAll(tiplist);
    }

    public static void printInfoChat(String text) {
        broadcast(ChatColor.DARK_GREEN + text);
    }

    public static void printDeathChat(String text) {
        broadcast(ChatColor.RED + text);
    }

    public static void printTimeChat(String text) {
        broadcast(ChatColor.GREEN + text);
    }

    public static void printPlayerChat(Player player, String text) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + text));
    }

    private static void broadcast(String msg) {
        for (Player Online : Bukkit.getOnlinePlayers())
            Online.sendMessage(msg);
    }

    public static void printHelpChat(Player player) {
        printPlayerChat(player, LCCHG.SERVER_TITLE);
        String are = "Hay";
        String players = "jugadores";
        if (LCCHG.getGamers().size() == 1) {
            are = "Hay";
            players = "jugador";
        }
        Integer timeleft = Integer.valueOf(LCCHG.MAX_GAME_RUNNING_TIME.intValue() -
                LCCHG.GAME_RUNNING_TIME.intValue());
        String is = "Faltan";
        String minute = "minutos";
        if (timeleft.intValue() <= 1) {
            is = "Falta";
            minute = "minutos";
        }
        player.sendMessage(ChatColor.GRAY + " - " + are + " " +
                LCCHG.getGamers().size() + " " + players + " conectados.");
        player.sendMessage(ChatColor.GRAY + " - " + is + " " + timeleft + " " +
                minute + " para terminar el juego.");
        if (LCCHG.HELP_MESSAGE != null && LCCHG.HELP_MESSAGE != "")
            player.sendMessage(ChatColor.GRAY + " - " + LCCHG.HELP_MESSAGE);
    }

    public static void printKitChat(Player player) {
        Set<String> kits = LCConfig.kitconf.getKeys(false);
        Integer invsize = Integer.valueOf(9);
        for (int i = 0; i <= 10; i++) {
            if (i * 9 >= kits.size()) {
                invsize = Integer.valueOf(invsize.intValue() + i * 9);
                break;
            }
        }
        final Player pl = player;
        IconMenu menu = new IconMenu("Selecciona un KIT", invsize.intValue(), new IconMenu.OptionClickEventHandler() {
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                BGKit.setKit(pl, ChatColor.stripColor(event.getName()));
                event.setWillClose(true);
                event.setWillDestroy(false);
            }
        }, LCCHG.instance, true);
        Integer mypos = Integer.valueOf(0);
        Integer othpos = Integer.valueOf(1);
        for (String kitname : kits) {
            try {
                if (kitname.equalsIgnoreCase("default"))
                    continue;
                char[] stringArray = kitname.toCharArray();
                stringArray[0] = Character.toUpperCase(stringArray[0]);
                kitname = new String(stringArray);
                ArrayList<String> container = new ArrayList<>();
                ConfigurationSection kit = LCConfig.kitconf.getConfigurationSection(kitname.toLowerCase());
                List<String> kititems = kit.getStringList("ITEMS");
                for (String item : kititems) {
                    String[] oneitem = item.split(",");
                    String itemstring = null;
                    Integer id = null;
                    Integer amount = null;
                    String enchantment = null;
                    String ench_numb = null;
                    if (oneitem[0].contains(":")) {
                        String[] ITEM_ID = oneitem[0].split(":");
                        id = Integer.valueOf(Integer.parseInt(ITEM_ID[0]));
                        amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
                    } else {
                        id = Integer.valueOf(Integer.parseInt(oneitem[0]));
                        amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
                    }
                    itemstring = " - " +
                            amount +
                            "x " +
                            Material.getMaterial(id.intValue()).toString()
                                    .replace("_", " ").toLowerCase();
                    if (oneitem.length == 4) {
                        enchantment =
                                Enchantment.getById(Integer.parseInt(oneitem[2])).getName()
                                        .toLowerCase();
                        ench_numb = oneitem[3];
                        itemstring = String.valueOf(itemstring) + " with " + enchantment + " " +
                                ench_numb;
                    }
                    container.add(ChatColor.GRAY + itemstring);
                }
                List<String> pots = kit.getStringList("POTION");
                for (String pot : pots) {
                    if ((((pot != null) ? 1 : 0) & ((pot != "") ? 1 : 0)) != 0 &&
                            !pot.equals(Integer.valueOf(0))) {
                        String[] potion = pot.split(",");
                        if (Integer.parseInt(potion[0]) != 0) {
                            PotionEffectType pt = PotionEffectType.getById(Integer.parseInt(potion[0]));
                            String name = pt.getName();
                            if (Integer.parseInt(potion[1]) == 0) {
                                name = String.valueOf(name) + " (Duracion: Infinita)";
                            } else {
                                name = String.valueOf(name) + " (Duracion: " + potion[1] + " seg)";
                            }
                            container.add(ChatColor.AQUA + " * " + name);
                        }
                    }
                }
                List<String> abils = kit.getStringList("ABILITY");
                for (String abil : abils) {
                    String desc = BGKit.getAbilityDesc(Habilidades.valueOf(abil));
                    if (desc != null)
                        container.add(ChatColor.LIGHT_PURPLE + " + " + desc);
                }
                if (!kit.getString("PERMS").equalsIgnoreCase("default")) {
                    container.add(" ");
                    String pref = "";
                    String str1;
                    switch ((str1 = kit.getString("PERMS")).hashCode()) {
                        case 84989:
                            if (!str1.equals("VIP"))
                                break;
                            pref = "&b&lVIP";
                            break;
                        case 2526682:
                            if (!str1.equals("RUBY"))
                                break;
                            pref = "&c&lRUBY";
                            break;
                        case 2557642:
                            if (!str1.equals("SVIP"))
                                break;
                            pref = "&a&lSVIP";
                            break;
                        case 66059891:
                            if (!str1.equals("ELITE"))
                                break;
                            pref = "&6&lELITE";
                            break;
                    }
                    container.add(ChatColor.translateAlternateColorCodes('&', "&eExclusivo para " + pref));
                }
                Integer itemid = Integer.valueOf(kit.getInt("ITEMMENU"));
                Material kitem = Material.getMaterial(itemid.intValue());
                String perms = kit.getString("PERMS");
                Jugador jug = Jugador.getJugador(player);
                boolean hasperms = jug.getChgInfo().isWinner();
                if (perms.equalsIgnoreCase("VIP") && jug.isVIP()) {
                    hasperms = true;
                } else if (perms.equalsIgnoreCase("SVIP") && jug.isSVIP()) {
                    hasperms = true;
                } else if (perms.equalsIgnoreCase("ELITE") && jug.isELITE()) {
                    hasperms = true;
                } else if (perms.equalsIgnoreCase("RUBY") && jug.isRUBY()) {
                    hasperms = true;
                } else if (perms.equalsIgnoreCase("default")) {
                    hasperms = true;
                }
                if (hasperms) {
                    String[] info = new String[container.size()];
                    info = container.<String>toArray(info);
                    menu.setOption(mypos.intValue(), new ItemStack(kitem, 1), ChatColor.GREEN + kitname, info);
                    mypos = Integer.valueOf(mypos.intValue() + 1);
                } else {
                    String[] info = new String[container.size()];
                    info = container.<String>toArray(info);
                    menu.setOption(invsize.intValue() - othpos.intValue(), new ItemStack(kitem, 1), ChatColor.RED + kitname, info);
                    othpos = Integer.valueOf(othpos.intValue() + 1);
                }
                container.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        menu.open(player);
    }

    public static void printKitInfo(Player player, String kitname) {
        String kitinfoname = kitname;
        kitname = kitname.toLowerCase();
        ConfigurationSection kit = LCConfig.kitconf.getConfigurationSection(kitname);
        if (kit == null || !BGKit.isKit(kitname)) {
            printPlayerChat(player,
                    "Ese kit no existe, para ver los kits usa: /kit");
            return;
        }
        char[] stringArray = kitinfoname.toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        kitinfoname = new String(stringArray);
        player.sendMessage(ChatColor.GREEN + kitinfoname + " Kit incluye:");
        List<String> kititems = kit.getStringList("ITEMS");
        for (String item : kititems) {
            String[] oneitem = item.split(",");
            String itemstring = null;
            Integer id = null;
            Integer amount = null;
            String enchantment = null;
            String ench_numb = null;
            if (oneitem[0].contains(":")) {
                String[] ITEM_ID = oneitem[0].split(":");
                id = Integer.valueOf(Integer.parseInt(ITEM_ID[0]));
                amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
            } else {
                id = Integer.valueOf(Integer.parseInt(oneitem[0]));
                amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
            }
            itemstring = " - " +
                    amount +
                    "x " +
                    Material.getMaterial(id.intValue()).toString()
                            .replace("_", " ").toLowerCase();
            if (oneitem.length == 4) {
                enchantment =
                        Enchantment.getById(Integer.parseInt(oneitem[2])).getName()
                                .toLowerCase();
                ench_numb = oneitem[3];
                itemstring = String.valueOf(itemstring) + " with " + enchantment + " " +
                        ench_numb;
            }
            player.sendMessage(ChatColor.GRAY + itemstring);
        }
        List<String> pots = kit.getStringList("POTION");
        for (String pot : pots) {
            if ((((pot != null) ? 1 : 0) & ((pot != "") ? 1 : 0)) != 0 &&
                    !pot.equals(Integer.valueOf(0))) {
                String[] potion = pot.split(",");
                if (Integer.parseInt(potion[0]) != 0) {
                    PotionEffectType pt = PotionEffectType.getById(Integer.parseInt(potion[0]));
                    String name = pt.getName();
                    if (Integer.parseInt(potion[1]) == 0) {
                        name = String.valueOf(name) + " (Duracion: Infinita)";
                    } else {
                        name = String.valueOf(name) + " (Duracion: " + potion[1] + " seg)";
                    }
                    player.sendMessage(ChatColor.AQUA + " * " + name);
                }
            }
        }
        List<String> abils = kit.getStringList("ABILITY");
        for (String abil : abils) {
            String desc = BGKit.getAbilityDesc(Habilidades.valueOf(abil));
            if (desc != null)
                player.sendMessage(ChatColor.LIGHT_PURPLE + " + " + desc);
        }
    }

    public static void printTipChat(){
        if(!TIPS.isEmpty()){
            broadcast(ChatColor.GRAY + "[" + ChatColor.RED + "MineLC" + ChatColor.GRAY + "] " + ChatColor.GREEN + TIPS.get(LCCHG.random.nextInt(TIPS.size())));
        }

    }
}
