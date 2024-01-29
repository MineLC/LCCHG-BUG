package lc.chg.game.utils;

import org.bukkit.entity.Player;

import static lc.core.utilidades.Util.color;

public class BGKick {

    public static void kick(Player p, String msg){
        StringBuilder builder = new StringBuilder();
        builder.append("&r&6&lMine&b&lLC");
        builder.append("\n\n&r");
        builder.append("&fHas sido expulsado de &6&lCHG &fpor:\n&r");
        builder.append("&eEl juego ha terminado ¡&a").append(msg).append(" &eha ganado!");
        p.kickPlayer(color(msg));
    }

}
