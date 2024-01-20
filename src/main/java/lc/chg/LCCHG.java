package lc.chg;

import com.google.common.collect.Lists;
import lc.chg.commands.BGConsole;
import lc.chg.commands.BGPlayer;
import lc.chg.configuration.LCConfig;
import lc.chg.configuration.Translation;
import lc.chg.game.BGKit;
import lc.chg.game.GameState;
import lc.chg.game.litesteners.BGEnchantFixer;
import lc.chg.game.litesteners.BGGameListener;
import lc.chg.game.litesteners.habilidades.BGHabilidadesListener;
import lc.chg.game.placeholderapi.LCPAPI;
import lc.chg.game.timers.GameTimer;
import lc.chg.game.timers.InvincibilityTimer;
import lc.chg.game.timers.PreGameTimer;
import lc.chg.game.utils.BGChat;
import lc.core.entidades.Jugador;
import lc.core.entidades.database.CHGInfoQuery;
import lc.core.entidades.database.LCoinsQuery;
import lc.core.entidades.database.VipPointsQuery;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class LCCHG extends JavaPlugin {

    public static LCCHG instance;

    public static GameState GAMESTATE = GameState.PREGAME;

    public static String HELP_MESSAGE = null;

    public static String SERVER_FULL_MSG = "";

    public static String GAME_IN_PROGRESS_MSG = "";

    public static String MOTD_PROGRESS_MSG = "";

    public static String MOTD_COUNTDOWN_MSG = "";

    public static String NO_KIT_MSG = "";

    public static String SERVER_TITLE = null;

    public static Integer COUNTDOWN_SECONDS = Integer.valueOf(120);

    public static Integer FINAL_COUNTDOWN_SECONDS = Integer.valueOf(60);

    public static Integer END_GAME_TIME = Integer.valueOf(1);

    public static Integer MAX_GAME_RUNNING_TIME = Integer.valueOf(60);

    public static Integer MINIMUM_PLAYERS = Integer.valueOf(4);

    public static Integer GAME_ENDING_TIME = Integer.valueOf(50);

    public static Boolean DEFAULT_KIT = Boolean.valueOf(false);

    public static Boolean END_GAME = Boolean.valueOf(true);

    public static Location spawn;

    public static ArrayList<Player> spectators = new ArrayList<>();

    public static Integer COUNTDOWN = Integer.valueOf(0);

    public static Integer FINAL_COUNTDOWN = Integer.valueOf(0);

    public static Integer GAME_RUNNING_TIME = Integer.valueOf(0);

    public static Integer WORLDRADIUS = Integer.valueOf(250);

    public static HashMap<String, Integer> kills = new HashMap<>();

    public static Logger log = Bukkit.getLogger();

    public static HashMap<String, Team> TEAMS = new HashMap<>();

    public static String mapa = "default";

    public static LinkedList<Player> gamers = new LinkedList<>();

    public static String ganador = "nadie";

    public static SplittableRandom random = new SplittableRandom();

    public static HashMap<Player, Integer> Fame = new HashMap<>();

    public static World mainWorld = Bukkit.getWorld("world");

    public static ArrayList<Player> frezee;

    public static boolean GEN_MAPS;

    public static Integer GetFame(Player p) {
        return Fame.get(p);
    }

    public static void setFame(Player p, Integer a) {
        Fame.remove(p);
        Fame.put(p, a);
    }

    public static void AddFame(Player p) {
        if (!Fame.containsKey(p))
            Fame.put(p, Integer.valueOf(0));
        Integer fame = Fame.get(p);
        if (fame.intValue() < 4) {
            fame = Integer.valueOf(fame.intValue() + 1);
        } else if (fame.intValue() < 8) {
            fame = Integer.valueOf(fame.intValue() + 2);
        } else if (fame.intValue() < 40) {
            fame = Integer.valueOf(fame.intValue() + 4);
        } else {
            fame = Integer.valueOf(fame.intValue() + 40);
        }
        p.sendMessage(ChatColor.GREEN + "Has recibido " + Integer.toString(fame.intValue()) + " de Fama.");
        Integer total = Integer.valueOf(((Integer)Fame.get(p)).intValue() + fame.intValue());
        Fame.remove(p);
        Fame.put(p, total);
        Jugador jug = Jugador.getJugador(p);
        jug.getChgInfo().setFama(jug.getChgInfo().getFama() + fame);
        CHGInfoQuery.saveCHGInfo(jug);
    }

    public static ArrayList<Player> getFrezee() {
        return frezee;
    }

    public void onLoad() {
        instance = this;
        new LCConfig();
        log.info("Deleting old world.");
        Bukkit.getServer().unloadWorld("world", false);
        deleteDir(new File("world"));
        File[] maps = getDataFolder().listFiles();
        assert maps != null;
        File sf = maps[random.nextInt(maps.length)];
        if(!sf.isDirectory()){
            log.info("Hubo un error y hay que reiniciar.");
            Bukkit.shutdown();
            return;
        }
        mapa = sf.getName();
        log.info("Copying saved world. (" + mapa + ")");
        try {
            copyDirectory(sf,
                    new File("world"));
        } catch (IOException e) {
            log.warning("Error: " + e.toString());
        }
    }

    private void registerEvents() {
        BGGameListener gl = new BGGameListener();
        BGEnchantFixer gg = new BGEnchantFixer();
        BGHabilidadesListener al = new BGHabilidadesListener();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents((Listener)gl, (Plugin)this);
        pm.registerEvents(gg, (Plugin)this);
        pm.registerEvents((Listener)al, (Plugin)this);
    }

    public void registerCommands() {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        if (getCommand("help") != null) {
            getCommand("help").setExecutor((CommandExecutor)new BGPlayer());
        } else {
            console.sendMessage(ChatColor.RED + "getCommand help returns null");
        }
        if (getCommand("kit") != null) {
            getCommand("kit").setExecutor((CommandExecutor)new BGPlayer());
        } else {
            console.sendMessage(ChatColor.RED + "getCommand kit returns null");
        }
        if (getCommand("rank") != null) {
            getCommand("rank").setExecutor((CommandExecutor)new BGPlayer());
        } else {
            console.sendMessage(ChatColor.RED + "getCommand rank returns null");
        }
        if (getCommand("kitinfo") != null) {
            getCommand("kitinfo").setExecutor((CommandExecutor)new BGPlayer());
        } else {
            console.sendMessage(ChatColor.RED + "getCommand kitinfo returns null");
        }
        if (getCommand("start") != null) {
            getCommand("start").setExecutor((CommandExecutor)new BGConsole());
        } else {
            console.sendMessage(ChatColor.RED + "getCommand start returns null");
        }
        if (getCommand("spawn") != null) {
            getCommand("spawn").setExecutor((CommandExecutor)new BGPlayer());
        } else {
            console.sendMessage(ChatColor.RED + "getCommand spawn returns null");
        }
        if (getCommand("desbug") != null) {
            getCommand("desbug").setExecutor((CommandExecutor)new BGPlayer());
        } else {
            console.sendMessage(ChatColor.RED + "getCommand desbug returns null");
        }
        if (getCommand("hack") != null) {
            getCommand("hack").setExecutor((CommandExecutor)new BGPlayer());
        } else {
            console.sendMessage("");
        }
        if (getCommand("fbattle") != null) {
            getCommand("fbattle").setExecutor((CommandExecutor)new BGConsole());
        } else {
            console.sendMessage(ChatColor.RED + "getCommand fbattle returns null");
        }
        if (getCommand("team") != null) {
            getCommand("team").setExecutor((CommandExecutor)new BGPlayer());
        } else {
            console.sendMessage(ChatColor.RED + "getCommand team returns null");
        }
        if (getCommand("gamemaker") != null) {
            getCommand("gamemaker").setExecutor((CommandExecutor)new BGPlayer());
        } else {
            console.sendMessage(ChatColor.RED + "getCommand gamemaker returns null");
        }
        if (getCommand("vanish") != null) {
            getCommand("vanish").setExecutor((CommandExecutor)new BGPlayer());
        } else {
            console.sendMessage(ChatColor.RED + "getCommand vanish returns null");
        }
        if (getCommand("teleport") != null) {
            getCommand("teleport").setExecutor((CommandExecutor)new BGPlayer());
        } else {
            console.sendMessage(ChatColor.RED + "getCommand teleport returns null");
        }
    }

    public void onEnable() {
        instance = this;
        ((World)Bukkit.getServer().getWorlds().get(0)).setDifficulty(Difficulty.PEACEFUL);
        log = Bukkit.getLogger();
        log.info("Loading configuration options.");
        SERVER_TITLE = getConfig().getString("MESSAGE.SERVER_TITLE");
        HELP_MESSAGE = getConfig().getString("MESSAGE.HELP_MESSAGE");
        DEFAULT_KIT = Boolean.valueOf(getConfig().getBoolean("DEFAULT_KIT"));
        NO_KIT_MSG = getConfig().getString("MESSAGE.NO_KIT_PERMISSION");
        GAME_IN_PROGRESS_MSG = getConfig().getString("MESSAGE.GAME_PROGRESS");
        SERVER_FULL_MSG = getConfig().getString("MESSAGE.SERVER_FULL");
        MOTD_PROGRESS_MSG = getConfig().getString("MESSAGE.MOTD_PROGRESS");
        MOTD_COUNTDOWN_MSG = getConfig().getString("MESSAGE.MOTD_COUNTDOWN");
        MINIMUM_PLAYERS = Integer.valueOf(getConfig().getInt("MINIMUM_PLAYERS_START"));
        MAX_GAME_RUNNING_TIME = Integer.valueOf(getConfig().getInt("TIME.MAX_GAME-MIN"));
        FINAL_COUNTDOWN_SECONDS = Integer.valueOf(getConfig().getInt("TIME.FINAL_COUNTDOWN-SEC"));
        END_GAME_TIME = Integer.valueOf(getConfig().getInt("TIME.INCREASE_DIFFICULTY-MIN"));
        copy(instance.getResource("en.yml"), new File(instance.getDataFolder(), "lang.yml"));
        Translation.e = (FileConfiguration) YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), "lang.yml"));
        if (WORLDRADIUS.intValue() < 60) {
            log.warning("Worldborder radius has to be 60 or higher!");
            WORLDRADIUS = Integer.valueOf(100);
        }
        (new LCPAPI(this)).register();
        registerEvents();
        registerCommands();
        World world = Bukkit.getServer().getWorlds().get(0);
        spawn = world.getSpawnLocation();
        world.setAutoSave(true);
        WorldBorder wb = world.getWorldBorder();
        wb.setCenter(spawn);
        wb.setWarningDistance(15);
        wb.setSize(250.0D);
        world.setTime(6000L);
        world.setGameRuleValue("spectatorsGenerateChunks", "false");
        world.setGameRuleValue("KeepInventory", "false");
        COUNTDOWN = COUNTDOWN_SECONDS;
        FINAL_COUNTDOWN = FINAL_COUNTDOWN_SECONDS;
        GAME_RUNNING_TIME = Integer.valueOf(0);
        GAMESTATE = GameState.PREGAME;
        log.info("Fase De Juego: 1 - Esperando");
        String command = "minecraft:gamerule sendCommandFeedback false";
        Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command);
        frezee = new ArrayList<>();
        new BGKit();
    }

    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers())
            p.kickPlayer(ChatColor.GOLD + ganador + " es el ganador del juego!");
        Bukkit.getServer().getScheduler().cancelAllTasks();
    }

    public static void startgame() {
        Fame.clear();
        log.info("Fase de juego: 2 - Comenzando");
        PreGameTimer.cancel();
        GAMESTATE = GameState.INVINCIBILITY;
        new InvincibilityTimer();
        World world = Bukkit.getWorlds().get(0);
        world.setAutoSave(false);
        world.setDifficulty(Difficulty.NORMAL);
        world.setTime(0L);
        List<Location> randomlocs = Lists.newArrayList();
        randomlocs.add(getSpawn().clone().add(0.0D, 0.0D, 1.0D));
        randomlocs.add(getSpawn().clone().add(0.0D, 0.0D, 2.0D));
        randomlocs.add(getSpawn().clone().add(0.0D, 0.0D, 3.0D));
        randomlocs.add(getSpawn().clone().add(0.0D, 0.0D, 4.0D));
        randomlocs.add(getSpawn().clone().add(0.0D, 0.0D, -1.0D));
        randomlocs.add(getSpawn().clone().add(0.0D, 0.0D, -2.0D));
        randomlocs.add(getSpawn().clone().add(0.0D, 0.0D, -3.0D));
        randomlocs.add(getSpawn().clone().add(0.0D, 0.0D, -4.0D));
        randomlocs.add(getSpawn().clone().add(1.0D, 0.0D, 0.0D));
        randomlocs.add(getSpawn().clone().add(2.0D, 0.0D, 0.0D));
        randomlocs.add(getSpawn().clone().add(3.0D, 0.0D, 0.0D));
        randomlocs.add(getSpawn().clone().add(4.0D, 0.0D, 0.0D));
        randomlocs.add(getSpawn().clone().add(-1.0D, 0.0D, 0.0D));
        randomlocs.add(getSpawn().clone().add(-2.0D, 0.0D, 0.0D));
        randomlocs.add(getSpawn().clone().add(-3.0D, 0.0D, 0.0D));
        randomlocs.add(getSpawn().clone().add(-4.0D, 0.0D, 0.0D));
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (isSpectator(p).booleanValue())
                continue;
            if (p.isInsideVehicle())
                p.getVehicle().eject();
            p.teleport(randomlocs.get(random.nextInt(16)));
            p.setFlying(false);
            p.setFireTicks(0);
            p.setAllowFlight(false);
            BGKit.giveKit(p);
        }
        BGChat.printTimeChat(Translation.GAMES_HAVE_BEGUN.t());
        BGChat.printTimeChat(Translation.INVINCIBLE_FOR.t().replace("<time>", TIME(FINAL_COUNTDOWN_SECONDS)));
        world.getWorldBorder().setSize(550.0D);
        world.getWorldBorder().setSize(100.0D, 600L);
    }

    public static void addBalance(Jugador jug, int x) {
        sendMessage(jug, x);
        jug.setCoins(jug.getCoins()+x);
        LCoinsQuery.savePlayerCoins_ASYNC(jug);
    }

    public static void sendMessage(Jugador jug, int x) {
        jug.getBukkitInstance().playSound(jug.getBukkitInstance().getLocation(), Sound.NOTE_PLING, 1.0F, 1.3F);
        jug.getBukkitInstance().sendMessage(ChatColor.GOLD + "+" + x + " LCoins");
    }

    public static void sendVipMessage(Jugador jug, int x) {
        jug.getBukkitInstance().playSound(jug.getBukkitInstance().getLocation(), Sound.NOTE_PLING, 1.0F, 1.3F);
        jug.getBukkitInstance().sendMessage(ChatColor.GOLD + "+" + x + " Vip Points");
    }

    private void copyDirectory(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists())
                targetLocation.mkdir();
            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++)
                copyDirectory(new File(sourceLocation, children[i]), new File(
                        targetLocation, children[i]));
        } else {
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0)
                out.write(buf, 0, len);
            in.close();
            out.close();
        }
    }


    public static Location getSpawn() {
        return spawn;
    }

    public static LinkedList<Player> getGamers() {
        return gamers;
    }

    public static void checkwinner() {
        if (getGamers().size() <= 1)
            if (getGamers().size() == 0) {
                GameTimer.cancel();
                Bukkit.getServer().shutdown();
            } else {
                GameTimer.cancel();
                Jugador jug = Jugador.getJugador(getGamers().get(0));
                ganador = jug.getNombre();
                jug.getChgInfo().setWins(jug.getChgInfo().getWins()+1);
                jug.getChgInfo().setPlayeds(jug.getChgInfo().getPlayeds()+1);
                jug.setVippoints(jug.getVippoints()+10);
                sendVipMessage(jug, 10);
                jug.getChgInfo().setWinner(true);
                CHGInfoQuery.saveCHGInfo(jug);
                VipPointsQuery.savePlayerVipPoints_ASYNC(jug);
                String title = "title " + jug.getBukkitInstance().getName() + " title [{\"text\":\"Ganaste!\",\"color\":\"gold\"}]";
                Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), title);
                Location loc = jug.getBukkitInstance().getEyeLocation();
                spawnRandomFirework(loc.clone().add(1.0D, 3.0D, 1.0D));
                spawnRandomFirework(loc.clone().add(-1.0D, 3.0D, -1.0D));
                spawnRandomFirework(loc.clone().add(1.0D, 3.0D, -1.0D));
                spawnRandomFirework(loc.clone().add(-1.0D, 3.0D, 1.0D));
                jug.getBukkitInstance().playSound(loc, Sound.LEVEL_UP, 1.0F, 2.5F);
                int winTotal = 20;
                addBalance(jug, winTotal);

                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)instance, new Runnable() {
                    public void run() {
                        Bukkit.shutdown();
                    }
                },  200L);
                for (Player Online : Bukkit.getOnlinePlayers()) {
                    Online.sendMessage(ChatColor.GREEN + ""+ChatColor.BOLD + ChatColor.STRIKETHROUGH + "----------------------------------");
                    Online.sendMessage(ChatColor.GOLD + ""+ChatColor.BOLD + "                      HG");
                    Online.sendMessage("");
                    Online.sendMessage(ChatColor.YELLOW + "                   Ganador: " + ChatColor.GRAY + jug.getBukkitInstance().getName());
                    Online.sendMessage("");
                    SayKillWinners(Online);
                    Online.sendMessage("");
                    Online.sendMessage(ChatColor.GREEN + ""+ChatColor.BOLD + ChatColor.STRIKETHROUGH + "----------------------------------");
                }
            }
    }
    public static void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0)
                out.write(buf, 0, len);
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SayKillWinners(Player p) {
        Map<String, Integer> asesinatos = sortByValue(kills);
        int st = 0;
        ChatColor color = ChatColor.YELLOW;
        for (Map.Entry<String, Integer> pk : asesinatos.entrySet()) {
            st++;
            if (st == 2) {
                color = ChatColor.GOLD;
            } else if (st == 3) {
                color = ChatColor.RED;
            }
            p.sendMessage(color + "             Asesino #" + st + ": " + ChatColor.GRAY + (String)pk.getKey() + " - " + pk.getValue());
            if (st >= 3)
                break;
        }
    }

    public static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Utilizar una expresión lambda con conversión explícita de tipo
        Collections.sort(list, (o1, o2) -> ((Comparable<Integer>) o1.getValue()).compareTo(o2.getValue()));

        Collections.reverse(list);

        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static void spawnRandomFirework(Location loc) {
        Firework fw = (Firework)loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        int rt = random.nextInt(2) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.STAR;
        if (rt == 1)
            type = FireworkEffect.Type.STAR;
        if (rt == 2)
            type = FireworkEffect.Type.STAR;
        Color c1 = Color.RED;
        Color c2 = Color.YELLOW;
        Color c3 = Color.ORANGE;
        FireworkEffect effect = FireworkEffect.builder().flicker(random.nextBoolean()).withColor(c1).withColor(c2).withFade(c3).with(type).trail(random.nextBoolean()).build();
        fwm.addEffect(effect);
        int rp = random.nextInt(3) + 1;
        fwm.setPower(rp);
        fw.setFireworkMeta(fwm);
    }

    public static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
                deleteDir(new File(dir, children[i]));
        }
        dir.delete();
    }

    public static String TIME(Integer i) {
        if (i.intValue() >= 60) {
            Integer integer = Integer.valueOf(i.intValue() / 60);
            String str = "";
            if (integer.intValue() > 1)
                str = "s";
            return integer + ChatColor.GREEN.toString() + " minuto" + str;
        }
        Integer time = i;
        String add = "";
        if (time.intValue() > 1)
            add = "s";
        return time + ChatColor.GREEN.toString() + " segundo" + add;
    }

    public static Boolean isSpectator(Player p) {
        return Boolean.valueOf(spectators.contains(p));
    }

    public static ArrayList<Player> getSpectators() {
        return spectators;
    }

    public static void addSpectator(final Player p) {
        spectators.add(p);
        Bukkit.getScheduler().runTaskLater((Plugin)instance, new Runnable() {
            public void run() {
                p.setGameMode(GameMode.SPECTATOR);
                p.setAllowFlight(true);
                p.setFlying(true);
            }
        },  2L);
    }

    public static void remSpectator(Player p) {
        spectators.remove(p);
        p.getInventory().clear();
    }

    public static String getRankHGPrefix(String rank) {
        String c = "";
        String str1;
        switch ((str1 = rank).hashCode()) {
            case -1989993123:
                if (!str1.equals("Mitico"))
                    break;
                c = "&d";
                break;
            case -728166682:
                if (!str1.equals("Ilustre"))
                    break;
                c = "&1";
                break;
            case -208615670:
                if (!str1.equals("Conquistador"))
                    break;
                c = "&3";
                break;
            case 82054:
                if (!str1.equals("Rey"))
                    break;
                c = "&c";
                break;
            case 67768478:
                if (!str1.equals("Feroz"))
                    break;
                c = "&e";
                break;
            case 69615499:
                if (!str1.equals("Heroe"))
                    break;
                c = "&b";
                break;
            case 74530587:
                if (!str1.equals("Moral"))
                    break;
                c = "&f";
                break;
            case 75621015:
                if (!str1.equals("Nuevo"))
                    break;
                c = "&7";
                break;
            case 148471323:
                if (!str1.equals("Emperador"))
                    break;
                c = "&4";
                break;
            case 280331875:
                if (!str1.equals("Renombrado"))
                    break;
                c = "&9";
                break;
            case 379264057:
                if (!str1.equals("Poderoso"))
                    break;
                c = "&6";
                break;
            case 1059248711:
                if (!str1.equals("Eminente"))
                    break;
                c = "&2";
                break;
            case 1295094569:
                if (!str1.equals("Aprendiz"))
                    break;
                c = "&a";
                break;
            case 1889526468:
                if (!str1.equals("Legendario"))
                    break;
                c = "&5";
                break;
        }
        return ChatColor.translateAlternateColorCodes('&', String.valueOf(c) + rank);
    }
}

