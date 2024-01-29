package lc.chg.game.placeholderapi;

import lc.chg.LCCHG;
import lc.core.entidades.Jugador;
import lc.core.entidades.minijuegos.CHGRank;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

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
        double r = 0.0;
        if (deaths == 0) return kills;
        else r = (double) kills / deaths;
        return formatKDR(r);
    }


    // Método para formatear el KDR con dos números decimales
    private static double formatKDR(double kdr) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(kdr));
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
            String a = LCCHG.getRankHGPrefix(CHGRank.NUEVO);
            if (kills >= 300 && kills < 500) {
                a = LCCHG.getRankHGPrefix(CHGRank.APRENDIZ);
            } else if (kills >= 500 && kills < 1000) {
                a = LCCHG.getRankHGPrefix(CHGRank.HÉROE);
            } else if (kills >= 1000 && kills < 2000) {
                a = LCCHG.getRankHGPrefix(CHGRank.FEROZ);
            } else if (kills >= 2000 && kills < 3000) {
                a = LCCHG.getRankHGPrefix(CHGRank.PODEROSO);
            } else if (kills >= 3000 && kills < 4000) {
                a = LCCHG.getRankHGPrefix(CHGRank.MORTAL);
            } else if (kills >= 4000 && kills < 5000) {
                a = LCCHG.getRankHGPrefix(CHGRank.TERRORÍFICO);
            } else if (kills >= 5000 && kills < 6000) {
                a = LCCHG.getRankHGPrefix(CHGRank.CONQUISTADOR);
            } else if (kills >= 6000 && kills < 7000) {
                a = LCCHG.getRankHGPrefix(CHGRank.RENOMBRADO);
            } else if (kills >= 7000 && kills < 8000) {
                a = LCCHG.getRankHGPrefix(CHGRank.ILUSTRE);
            } else if (kills >= 8000 && kills < 9000) {
                a = LCCHG.getRankHGPrefix(CHGRank.EMINENTE);
            } else if (kills >= 9000 && kills < 10000) {
                a = LCCHG.getRankHGPrefix(CHGRank.REY);
            } else if (kills >= 10000 && kills < 15000) {
                a = LCCHG.getRankHGPrefix(CHGRank.EMPERADOR);
            } else if (kills >= 15000 && kills < 20000) {
                a = LCCHG.getRankHGPrefix(CHGRank.LEGENDARIO);
            } else if (kills >= 20000) {
                a = LCCHG.getRankHGPrefix(CHGRank.MÍTICO);
            }
            return a;
        }
        if (identifier.equals("numerlevel")) {
            return String.valueOf(Jugador.getJugador(player).getChgInfo().getRank().getPriority());
        }
        if (identifier.equals("kills"))
            return (new StringBuilder(String.valueOf(Jugador.getJugador(player).getChgInfo().getKills()))).toString();
        if (identifier.equals("deaths"))
            return new StringBuilder(String.valueOf(Jugador.getJugador(player).getChgInfo().getMuertes())).toString();
        return identifier;
    }
}
