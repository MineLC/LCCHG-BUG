package lc.chg.game;

import lc.chg.LCCHG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.LinkedList;
import java.util.SplittableRandom;

public class BGFBattle implements Listener {
    private static Block mainBlock;
    private static SplittableRandom random = LCCHG.random;

    public static void createBattle() {
        Bukkit.getPluginManager().registerEvents(new BGFBattle(), LCCHG.instance);
        mainBlock = LCCHG.getSpawn().add(0.0D, 25, 0.0D).getBlock();
        Location loc = mainBlock.getLocation();
        Integer[] co = {
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1,
                1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
                1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
                1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
                1, 4, 4, 4, 2, 2, 4, 4, 4, 1, -1,
                1, 4, 4, 4, 2, 2, 4, 4, 4, 1, -1,
                1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
                1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
                1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2,

                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2,

                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2,

                3, 2, 3, 2, 3, 2, 3, 2, 3, 2, -1,
                2, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 2, -1,
                2, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 2, -1,
                2, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 2, -1,
                2, 0, 0, 0, 0, 0, 0, 0, 0, 3, -1,
                3, 0, 0, 0, 0, 0, 0, 0, 0, 2, -1,
                2, 3, 2, 3, 2, 3, 2, 3, 2, 3, -2,

                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2,

                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1,
                1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
                1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
                1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
                1, 4, 4, 4, 3, 3, 4, 4, 4, 1, -1,
                1, 4, 4, 4, 3, 3, 4, 4, 4, 1, -1,
                1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
                1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
                1, 4, 4, 4, 4, 4, 4, 4, 4, 1, -1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2 };
        for (Integer i : co) {
            Material m = Material.AIR;
            byte b = 0;
            switch (i.intValue()) {
                case 0:
                    m = Material.AIR;
                    break;
                case 1:
                    m = Material.QUARTZ_BLOCK;
                    break;
                case 2:
                    m = Material.SEA_LANTERN;
                    break;
                case 3:
                    m = Material.STAINED_GLASS;
                    b = 7;
                    break;
                case 4:
                    m = Material.QUARTZ_BLOCK;
                    break;
                case -1:
                    break;
                case -2:
                    break;
                default:
            }
            if (i.intValue() == -1)
            {
                loc.add(0.0D, 0.0D, 1.0D);
                loc.subtract(10.0D, 0.0D, 0.0D);
            }
            else if (i.intValue() == -2)
            {
                loc.add(0.0D, 1.0D, 0.0D);
                loc.subtract(10.0D, 0.0D, 9.0D);
            }
            else
            {
                loc.getBlock().setType(m);
                loc.getBlock().setData(b);
                loc.add(1.0D, 0.0D, 0.0D);
            }
        }
    }

    public static void teleportGamers(LinkedList<Player> linkedList) {
        for (Player p : linkedList) {
            p.leaveVehicle();
            Location loc = mainBlock.getLocation().add(random.nextInt(5) + 0.5, 1, random.nextInt(5) + 0.5);
            p.teleport(loc);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if(event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            event.setCancelled(true);
        }
    }

    public static Block getMainBlock() {
        return mainBlock;
    }
}