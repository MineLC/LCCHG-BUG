package lc.chg.game;

import java.util.LinkedList;
import java.util.SplittableRandom;

import lc.chg.LCCHG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

public class BGFBattle implements Listener {
    private static Block mainBlock;

    private static SplittableRandom random = LCCHG.random;

    public static void createBattle() {
        Bukkit.getPluginManager().registerEvents(new BGFBattle(), (Plugin)LCCHG.instance);
        mainBlock = LCCHG.getSpawn().add(0.0D, 25.0D, 0.0D).getBlock();
        Location loc = mainBlock.getLocation();
        Integer[] co = {
                Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1),
                Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4),
                Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4),
                Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4),
                Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(2), Integer.valueOf(2),
                Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(2),
                Integer.valueOf(2), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4),
                Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(4),
                Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4),
                Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1),
                Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(-2),
                Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1),
                Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1),
                Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(-2),
                Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1),
                Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1),
                Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(-2),
                Integer.valueOf(3), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(2),
                Integer.valueOf(-1),
                Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(3), Integer.valueOf(-1),
                Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(2), Integer.valueOf(-1),
                Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(3), Integer.valueOf(-1),
                Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(2), Integer.valueOf(-1),
                Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(3), Integer.valueOf(-1),
                Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(2), Integer.valueOf(-1),
                Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(3), Integer.valueOf(-1),
                Integer.valueOf(3), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(2), Integer.valueOf(-1),
                Integer.valueOf(2),
                Integer.valueOf(3), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(-2),
                Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1),
                Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(0),
                Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1),
                Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(-2),
                Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1),
                Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4),
                Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4),
                Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4),
                Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(3), Integer.valueOf(3),
                Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(3),
                Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4),
                Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(4),
                Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1), Integer.valueOf(4),
                Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(-1),
                Integer.valueOf(1),
                Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(-2) };
        byte b;
        int i;
        Integer[] arrayOfInteger1;
        for (i = (arrayOfInteger1 = co).length, b = 0; b < i; ) {
            Integer integer = arrayOfInteger1[b];
            Material m = Material.AIR;
            switch (integer.intValue()) {
                case 0:
                    m = Material.AIR;
                    break;
                case 1:
                    m = Material.QUARTZ_BLOCK;
                    break;
                case 2:
                    m = Material.STAINED_GLASS;
                    break;
                case 3:
                    m = Material.SEA_LANTERN;
                    break;
                case 4:
                    m = Material.QUARTZ_BLOCK;
                    break;
            }
            if (integer.intValue() == -1) {
                loc.add(0.0D, 0.0D, 1.0D);
                loc.subtract(10.0D, 0.0D, 0.0D);
            } else if (integer.intValue() == -2) {
                loc.add(0.0D, 1.0D, 0.0D);
                loc.subtract(10.0D, 0.0D, 9.0D);
            } else {
                loc.getBlock().setType(m);
                if (loc.getBlock().getType() == Material.STAINED_GLASS)
                    loc.getBlock().setData((byte)7);
                loc.add(1.0D, 0.0D, 0.0D);
            }
            b++;
        }
    }

    public static void teleportGamers(LinkedList<Player> linkedList) {
        for (Player p : linkedList) {
            p.leaveVehicle();
            Location loc = mainBlock.getLocation().add(random.nextInt(5) + 0.5D, 1.0D, random.nextInt(5) + 0.5D);
            p.teleport(loc);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL))
            event.setCancelled(true);
    }

    public static Block getMainBlock() {
        return mainBlock;
    }
}
