package lc.chg.game.placeholderapi;

import lc.chg.LCCHG;
import lc.core.entidades.Jugador;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class LCPAPI extends PlaceholderExpansion {
    private LCCHG plugin;

    public LCPAPI(LCCHG plugin) {
        this.plugin = plugin;
    }

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return "GatitoGuapote";
    }

    public String getIdentifier() {
        return "minelcchg";
    }

    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    public static double kdr(int kills, int deaths) {
        if (deaths == 0) return kills;
        else return (double) kills / deaths;
    }
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null)
            return "";
        if (identifier.equals("wins"))
            return (new StringBuilder(String.valueOf(Jugador.getJugador(player).getChgInfo().getWins()))).toString();
        if (identifier.equals("playeds"))
            return (new StringBuilder(String.valueOf(Jugador.getJugador(player).getChgInfo().getPlayeds()))).toString();
        if (identifier.equals("kdr"))
            return (new StringBuilder(String.valueOf(kdr(Jugador.getJugador(player).getChgInfo().getKills(), Jugador.getJugador(player).getChgInfo().getPlayeds() - Jugador.getJugador(player).getChgInfo().getWins()))).toString());
        if (identifier.equals("enpartida"))
            return (new StringBuilder(String.valueOf(LCCHG.getGamers().size()))).toString();
        if (identifier.equals("espectando"))
            return (new StringBuilder(String.valueOf(LCCHG.getSpectators().size()))).toString();
        if (identifier.equals("lcoins"))
            return (new StringBuilder(String.valueOf(Jugador.getJugador(player).getCoins()))).toString();
        if (identifier.equals("vippoints"))
            return (new StringBuilder(String.valueOf(Jugador.getJugador(player).getVippoints()))).toString();
        if (identifier.equals("alphalevel")) {
            int kills = Jugador.getJugador(player).getChgInfo().getFama();
            String a = LCCHG.getRankHGPrefix("Nuevo");
            if (kills >= 300 && kills < 500) {
                a = LCCHG.getRankHGPrefix("Aprendiz");
            } else if (kills >= 500 && kills < 1000) {
                a = LCCHG.getRankHGPrefix("Heroe");
            } else if (kills >= 1000 && kills < 2000) {
                a = LCCHG.getRankHGPrefix("Feroz");
            } else if (kills >= 2000 && kills < 3000) {
                a = LCCHG.getRankHGPrefix("Poderoso");
            } else if (kills >= 3000 && kills < 4000) {
                a = LCCHG.getRankHGPrefix("Mortal");
            } else if (kills >= 4000 && kills < 5000) {
                a = LCCHG.getRankHGPrefix("Terrorifico");
            } else if (kills >= 5000 && kills < 6000) {
                a = LCCHG.getRankHGPrefix("Conquistador");
            } else if (kills >= 6000 && kills < 7000) {
                a = LCCHG.getRankHGPrefix("Renombrado");
            } else if (kills >= 7000 && kills < 8000) {
                a = LCCHG.getRankHGPrefix("Ilustre");
            } else if (kills >= 8000 && kills < 9000) {
                a = LCCHG.getRankHGPrefix("Eminente");
            } else if (kills >= 9000 && kills < 10000) {
                a = LCCHG.getRankHGPrefix("Rey");
            } else if (kills >= 10000 && kills < 15000) {
                a = LCCHG.getRankHGPrefix("Emperador");
            } else if (kills >= 15000 && kills < 20000) {
                a = LCCHG.getRankHGPrefix("Legendario");
            } else if (kills >= 20000) {
                a = LCCHG.getRankHGPrefix("Mitico");
            }
            return a;
        }
        if (identifier.equals("numerlevel")) {
            return String.valueOf(Jugador.getJugador(player).getChgInfo().getRank().getPriority());
        }
        if (identifier.equals("kills"))
            return (new StringBuilder(String.valueOf(Jugador.getJugador(player).getChgInfo().getKills()))).toString();
        if (identifier.equals("deaths"))
            return (new StringBuilder(String.valueOf(Jugador.getJugador(player).getChgInfo().getPlayeds() - Jugador.getJugador(player).getChgInfo().getWins()))).toString();
        return identifier;
    }
}
