package lc.chg.game;

import lc.chg.LCCHG;
import lc.chg.configuration.LCConfig;
import lc.chg.game.litesteners.habilidades.Habilidades;
import lc.chg.game.utils.BGChat;
import lc.core.entidades.Jugador;
import lc.core.entidades.database.CHGInfoQuery;
import lc.core.utilidades.ItemUtils;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BGKit {
    public static ArrayList<String> kits = new ArrayList<>();

    private static HashMap<Habilidades, String> ABILITY_DESC = new HashMap<>();

    private static ItemStack compass = new ItemUtils(Material.COMPASS, Short.valueOf((short) 0), Integer.valueOf(1), ChatColor.AQUA + "Rastreador" + ChatColor.BOLD, ChatColor.GRAY + "Click para rastrar al jugador mas cercano");

    public BGKit() {
        Set<String> kitList = LCConfig.kitconf.getKeys(false);
        for (String kit : kitList) {
            if (kit.equalsIgnoreCase("default"))
                continue;
            kits.add(kit.toLowerCase());
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED+ ""+kitList+"\n"+kits);
        ABILITY_DESC.put(Habilidades.FLECHAS_EXPLOTAN, LCConfig.abconf.getString("AB.1.Desc"));
        ABILITY_DESC.put(Habilidades.ROMP_ARBOLES_RAPIDO, LCConfig.abconf.getString("AB.2.Desc"));
        ABILITY_DESC.put(Habilidades.DISPAROS_DANAN_ENEMIGOS, LCConfig.abconf.getString("AB.3.Desc"));
        ABILITY_DESC.put(Habilidades.LANZA_FUEGO, LCConfig.abconf.getString("AB.4.Desc"));
        ABILITY_DESC.put(Habilidades.COMER_GALLETA, LCConfig.abconf.getString("AB.5.Desc"));
        ABILITY_DESC.put(Habilidades.GANAS_FUERZA_CON_FUEGO, LCConfig.abconf.getString("AB.6.Desc"));
        ABILITY_DESC.put(Habilidades.CHULETAS_POR_CERDOS, LCConfig.abconf.getString("AB.7.Desc"));
        ABILITY_DESC.put(Habilidades.DANO_AL_CAER, LCConfig.abconf.getString("AB.8.Desc"));
        ABILITY_DESC.put(Habilidades.MUERTE_INSTANTANEA, LCConfig.abconf.getString("AB.9.Desc"));
        ABILITY_DESC.put(Habilidades.CULTIVOS_INMEDIATOS, LCConfig.abconf.getString("AB.10.Desc"));
        ABILITY_DESC.put(Habilidades.TRUENOS_CON_HACHA, LCConfig.abconf.getString("AB.11.Desc"));
        ABILITY_DESC.put(Habilidades.ROBA_VIDA_AL_PEGAR, LCConfig.abconf.getString("AB.12.Desc"));
        ABILITY_DESC.put(Habilidades.ROBO_DE_OBJETOS, LCConfig.abconf.getString("AB.13.Desc"));
        ABILITY_DESC.put(Habilidades.RECUPERAR_HAMBRE, LCConfig.abconf.getString("AB.14.Desc"));
        ABILITY_DESC.put(Habilidades.ROBO_DE_OBJETOS_2, LCConfig.abconf.getString("AB.15.Desc"));
        ABILITY_DESC.put(Habilidades.INVISIBLE_AL_COMER_MANZANA, LCConfig.abconf.getString("AB.16.Desc"));
        ABILITY_DESC.put(Habilidades.TRANSFORMACION_EN_MOB, LCConfig.abconf.getString("AB.17.Desc"));
        ABILITY_DESC.put(Habilidades.PEGAR_CON_MANOS, LCConfig.abconf.getString("AB.18.Desc"));
        ABILITY_DESC.put(Habilidades.ENVENENAR_JUGADORES, LCConfig.abconf.getString("AB.19.Desc"));
        ABILITY_DESC.put(Habilidades.MOBS_NO_ATACAN, LCConfig.abconf.getString("AB.20.Desc"));
        ABILITY_DESC.put(Habilidades.VISION_NOCTURNA_CON_PAPA, LCConfig.abconf.getString("AB.21.Desc"));
        ABILITY_DESC.put(Habilidades.CONGELAR_JUGADORES, LCConfig.abconf.getString("AB.22.Desc"));
        ABILITY_DESC.put(Habilidades.EXPLOTAR_AL_MORIR, LCConfig.abconf.getString("AB.23.Desc"));
        ABILITY_DESC.put(Habilidades.GANAS_FUERZA_2_CON_FUEGO, LCConfig.abconf.getString("AB.30.Desc"));
        ABILITY_DESC.put(Habilidades.TELETRANSPORTACION_CON_ANTORCHA, LCConfig.abconf.getString("AB.31.Desc"));
        ABILITY_DESC.put(Habilidades.GRANDES_SALTOS_CON_FUEGO_ARTIFICIAL, LCConfig.abconf.getString("AB.32.Desc"));
        ABILITY_DESC.put(Habilidades.RECUPERAR_CORAZONES_CON_SOPAS_2, LCConfig.abconf.getString("AB.33.Desc"));
        ABILITY_DESC.put(Habilidades.RECUPERAR_CORAZONES_CON_SOPAS_3, LCConfig.abconf.getString("AB.34.Desc"));
        ABILITY_DESC.put(Habilidades.DEBILITAR_JUGADORES, LCConfig.abconf.getString("AB.35.Desc"));
        ABILITY_DESC.put(Habilidades.CEGAR_JUGADORES, LCConfig.abconf.getString("AB.36.Desc"));
        ABILITY_DESC.put(Habilidades.GENERAR_TELARANIA_CON_BOLAS_DE_NIEVE, LCConfig.abconf.getString("AB.37.Desc"));
    }


    public static void giveKit(Player p) {
        p.getInventory().clear();
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
        p.setExp(0.0F);
        p.setLevel(0);
        p.setFoodLevel(20);
        p.setFlying(false);
        p.setAllowFlight(false);
        Jugador jug = Jugador.getJugador(p);
        String perms = LCConfig.kitconf.getConfigurationSection(jug.getChgInfo().getKit().toLowerCase()).getString("PERMS");
        boolean hasperms = jug.getChgInfo().isWinner();
        if (hasperms) {
            jug.getChgInfo().setWinner(false);
            CHGInfoQuery.saveCHGInfo(jug);
        }
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
        if (!hasperms)
            jug.getChgInfo().setKit("default");
        if (!kits.contains(jug.getChgInfo().getKit().toLowerCase())) {
            p.getInventory().addItem(new ItemStack[] { compass });
            if (LCCHG.DEFAULT_KIT.booleanValue())
                try {
                    ConfigurationSection def = LCConfig.kitconf.getConfigurationSection("default");
                    List<String> kititems = def.getStringList("ITEMS");
                    for (String item : kititems) {
                        String[] oneitem = item.split(",");
                        ItemStack i = null;
                        Integer id = null;
                        Integer amount = null;
                        Short durability = null;
                        if (oneitem[0].contains(":")) {
                            String[] ITEM_ID = oneitem[0].split(":");
                            id = Integer.valueOf(Integer.parseInt(ITEM_ID[0]));
                            amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
                            durability = Short.valueOf(Short.parseShort(ITEM_ID[1]));
                            i = new ItemStack(id.intValue(), amount.intValue(),
                                    durability.shortValue());
                        } else {
                            id = Integer.valueOf(Integer.parseInt(oneitem[0]));
                            amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
                            i = new ItemStack(id.intValue(), amount.intValue());
                        }
                        if (oneitem.length == 4)
                            i.addUnsafeEnchantment(
                                    Enchantment.getById(Integer.parseInt(oneitem[2])),
                                    Integer.parseInt(oneitem[3]));
                        if (id.intValue() < 298 || 317 < id.intValue()) {
                            p.getInventory().addItem(new ItemStack[] { i });
                            continue;
                        }
                        if (id.intValue() == 298 || id.intValue() == 302 ||
                                id.intValue() == 306 || id.intValue() == 310 ||
                                id.intValue() == 314) {
                            i.setAmount(1);
                            p.getInventory().setHelmet(i);
                            continue;
                        }
                        if (id.intValue() == 299 || id.intValue() == 303 ||
                                id.intValue() == 307 || id.intValue() == 311 ||
                                id.intValue() == 315) {
                            i.setAmount(1);
                            p.getInventory().setChestplate(i);
                            continue;
                        }
                        if (id.intValue() == 300 || id.intValue() == 304 ||
                                id.intValue() == 308 || id.intValue() == 312 ||
                                id.intValue() == 316) {
                            i.setAmount(1);
                            p.getInventory().setLeggings(i);
                            continue;
                        }
                        if (id.intValue() == 301 || id.intValue() == 305 ||
                                id.intValue() == 309 || id.intValue() == 313 ||
                                id.intValue() == 317) {
                            i.setAmount(1);
                            p.getInventory().setBoots(i);
                        }
                    }
                    List<String> pots = def.getStringList("POTION");
                    for (String pot : pots) {
                        if ((((pot != null) ? 1 : 0) & ((pot != "") ? 1 : 0)) != 0 &&
                                !pot.equals(Integer.valueOf(0))) {
                            String[] potion = pot.split(",");
                            if (Integer.parseInt(potion[0]) != 0) {
                                if (Integer.parseInt(potion[1]) == 0) {
                                    p.addPotionEffect(new PotionEffect(
                                            PotionEffectType.getById(Integer.parseInt(potion[0])),
                                            LCCHG.MAX_GAME_RUNNING_TIME.intValue() * 1200,
                                            Integer.parseInt(potion[2])));
                                    continue;
                                }
                                p.addPotionEffect(new PotionEffect(
                                        PotionEffectType.getById(Integer.parseInt(potion[0])),
                                        Integer.parseInt(potion[1]) * 20,
                                        Integer.parseInt(potion[2])));
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            return;
        }
        String kitname = jug.getChgInfo().getKit().toLowerCase();
        try {
            ConfigurationSection kit = LCConfig.kitconf.getConfigurationSection(kitname.toLowerCase());
            List<String> kititems = kit.getStringList("ITEMS");
            for (String item : kititems) {
                String[] oneitem = item.split(",");
                ItemStack i = null;
                Integer id = null;
                Integer amount = null;
                Short durability = null;
                int blue = 0;
                int green = 0;
                int red = 0;
                if (item.toLowerCase().contains(">")) {
                    String[] color = item.split(">");
                    if (color[1].toLowerCase().contains("blue")) {
                        blue = 255;
                        green = 0;
                        red = 0;
                    } else if (color[1].toLowerCase().contains("green")) {
                        blue = 0;
                        green = 255;
                        red = 0;
                    } else if (color[1].toLowerCase().contains("red")) {
                        blue = 0;
                        green = 0;
                        red = 255;
                    } else if (color[1].toLowerCase().contains("black")) {
                        blue = 0;
                        green = 0;
                        red = 0;
                    } else if (color[1].toLowerCase().contains("white")) {
                        blue = 255;
                        green = 255;
                        red = 255;
                    }
                }
                if (oneitem[0].contains(":")) {
                    String[] ITEM_ID = oneitem[0].split(":");
                    id = Integer.valueOf(Integer.parseInt(ITEM_ID[0]));
                    amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
                    durability = Short.valueOf(Short.parseShort(ITEM_ID[1]));
                    i = new ItemStack(id.intValue(), amount.intValue(),
                            durability.shortValue());
                } else {
                    id = Integer.valueOf(Integer.parseInt(oneitem[0]));
                    amount = Integer.valueOf(Integer.parseInt(oneitem[1]));
                    i = new ItemStack(id.intValue(), amount.intValue());
                }
                if (oneitem.length == 4) {
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[2])),
                            Integer.parseInt(oneitem[3]));
                } else if (oneitem.length == 6) {
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[2])),
                            Integer.parseInt(oneitem[3]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[4])),
                            Integer.parseInt(oneitem[5]));
                } else if (oneitem.length == 8) {
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[2])),
                            Integer.parseInt(oneitem[3]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[4])),
                            Integer.parseInt(oneitem[5]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[6])),
                            Integer.parseInt(oneitem[7]));
                } else if (oneitem.length == 10) {
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[2])),
                            Integer.parseInt(oneitem[3]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[4])),
                            Integer.parseInt(oneitem[5]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[6])),
                            Integer.parseInt(oneitem[7]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[8])),
                            Integer.parseInt(oneitem[9]));
                } else if (oneitem.length == 12) {
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[2])),
                            Integer.parseInt(oneitem[3]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[4])),
                            Integer.parseInt(oneitem[5]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[6])),
                            Integer.parseInt(oneitem[7]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[8])),
                            Integer.parseInt(oneitem[9]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[10])),
                            Integer.parseInt(oneitem[11]));
                } else if (oneitem.length == 14) {
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[2])),
                            Integer.parseInt(oneitem[3]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[4])),
                            Integer.parseInt(oneitem[5]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[6])),
                            Integer.parseInt(oneitem[7]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[8])),
                            Integer.parseInt(oneitem[9]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[10])),
                            Integer.parseInt(oneitem[11]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[12])),
                            Integer.parseInt(oneitem[13]));
                } else if (oneitem.length == 16) {
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[2])),
                            Integer.parseInt(oneitem[3]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[4])),
                            Integer.parseInt(oneitem[5]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[6])),
                            Integer.parseInt(oneitem[7]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[8])),
                            Integer.parseInt(oneitem[9]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[10])),
                            Integer.parseInt(oneitem[11]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[12])),
                            Integer.parseInt(oneitem[13]));
                    i.addUnsafeEnchantment(
                            Enchantment.getById(Integer.parseInt(oneitem[14])),
                            Integer.parseInt(oneitem[15]));
                }
                if (id.intValue() < 298 || 317 < id.intValue()) {
                    p.getInventory().addItem(new ItemStack[] { i });
                    continue;
                }
                if (id.intValue() == 298 || id.intValue() == 302 ||
                        id.intValue() == 306 || id.intValue() == 310 ||
                        id.intValue() == 314) {
                    if (id.intValue() == 298 && kitname.equalsIgnoreCase("spiderman")) {
                        LeatherArmorMeta h = (LeatherArmorMeta)i.getItemMeta();
                        h.setColor(Color.RED);
                        i.setItemMeta(h);
                    }
                    i.setAmount(1);
                    p.getInventory().setHelmet(i);
                    continue;
                }
                if (id.intValue() == 299 || id.intValue() == 303 ||
                        id.intValue() == 307 || id.intValue() == 311 ||
                        id.intValue() == 315) {
                    if (id.intValue() == 299 && kitname.equalsIgnoreCase("spiderman")) {
                        LeatherArmorMeta c = (LeatherArmorMeta)i.getItemMeta();
                        c.setColor(Color.BLUE);
                        i.setItemMeta((ItemMeta)c);
                    }
                    i.setAmount(1);
                    p.getInventory().setChestplate(i);
                    continue;
                }
                if (id.intValue() == 300 || id.intValue() == 304 ||
                        id.intValue() == 308 || id.intValue() == 312 ||
                        id.intValue() == 316) {
                    if (id.intValue() == 300 && kitname.equalsIgnoreCase("spiderman")) {
                        LeatherArmorMeta l = (LeatherArmorMeta)i.getItemMeta();
                        l.setColor(Color.RED);
                        i.setItemMeta((ItemMeta)l);
                    }
                    i.setAmount(1);
                    p.getInventory().setLeggings(i);
                    continue;
                }
                if (id.intValue() == 301 || id.intValue() == 305 ||
                        id.intValue() == 309 || id.intValue() == 313 ||
                        id.intValue() == 317) {
                    if (id.intValue() == 301 && kitname.equalsIgnoreCase("spiderman")) {
                        LeatherArmorMeta b = (LeatherArmorMeta)i.getItemMeta();
                        b.setColor(Color.BLUE);
                        i.setItemMeta((ItemMeta)b);
                    }
                    i.setAmount(1);
                    p.getInventory().setBoots(i);
                }
            }
            List<String> pots = kit.getStringList("POTION");
            for (String pot : pots) {
                if ((((pot != null) ? 1 : 0) & ((pot != "") ? 1 : 0)) != 0 &&
                        !pot.equals(Integer.valueOf(0))) {
                    String[] potion = pot.split(",");
                    if (Integer.parseInt(potion[0]) != 0) {
                        if (Integer.parseInt(potion[1]) == 0) {
                            p.addPotionEffect(new PotionEffect(
                                    PotionEffectType.getById(Integer.parseInt(potion[0])),
                                    LCCHG.MAX_GAME_RUNNING_TIME.intValue() * 1200,
                                    Integer.parseInt(potion[2])));
                            continue;
                        }
                        p.addPotionEffect(new PotionEffect(
                                PotionEffectType.getById(Integer.parseInt(potion[0])),
                                Integer.parseInt(potion[1]) * 20,
                                Integer.parseInt(potion[2])));
                    }
                }
            }
            p.getInventory().addItem(new ItemStack[] { compass });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setKit(Player player, String kitname) {
        kitname = kitname.toLowerCase();
        kitname = kitname.replace(".", "");
        ConfigurationSection kit = LCConfig.kitconf.getConfigurationSection(kitname);
        if (kit == null && !kits.contains(kitname)) {
            BGChat.printPlayerChat(player, ChatColor.RED + "El kit no existe!");
            return;
        }
        Jugador jug = Jugador.getJugador(player);
        if (jug.getChgInfo().getKit().equalsIgnoreCase(kitname)) {
            player.sendMessage(ChatColor.RED + "Ya tienes seleccionado este kit!");
            return;
        }
        String perms = LCConfig.kitconf.getConfigurationSection(kitname).getString("PERMS");
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
            jug.getChgInfo().setKit(kitname);
            char[] stringArray = kitname.toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            kitname = new String(stringArray);
            BGChat.printPlayerChat(player, ChatColor.GREEN + "Seleccionaste " + ChatColor.DARK_GREEN + ChatColor.ITALIC + kitname + ChatColor.RESET + ChatColor.GREEN + " como tu kit.");
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.5F);
            CHGInfoQuery.saveCHGInfo(jug);
        } else {
            BGChat.printPlayerChat(player, ChatColor.RED + LCCHG.NO_KIT_MSG);
            return;
        }
    }

    public static Boolean hasAbility(Player player, Habilidades ability) {
        if (LCCHG.isSpectator(player))
            return false;
        Jugador jug = Jugador.getJugador(player);
        if (!kits.contains(jug.getChgInfo().getKit().toLowerCase())) {
            if (LCCHG.DEFAULT_KIT) {
                ConfigurationSection def = LCConfig.kitconf.getConfigurationSection("default");
                List<String> list = def.getStringList("ABILITY");
                for (String i : list) {
                    if (i.toUpperCase().equals(ability.name()))
                        return true;
                }
                return Boolean.valueOf(false);
            }
            return Boolean.valueOf(false);
        }
        String kitname = jug.getChgInfo().getKit().toLowerCase();
        ConfigurationSection kit = LCConfig.kitconf.getConfigurationSection(kitname);
        List<String> list = kit.getStringList("ABILITY");
        for (String i : list) {
            if (i.toUpperCase().equals(ability.name()))
                return true;
        }
        return Boolean.valueOf(false);
    }

    public static boolean isKit(String kitName) {
        return kits.contains(kitName);
    }

    public static String getAbilityDesc(Habilidades ability) {
        if (ABILITY_DESC.containsKey(ability))
            return ABILITY_DESC.get(ability);
        return null;
    }
}
