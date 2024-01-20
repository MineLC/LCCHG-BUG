package lc.chg.game.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class BGTeam {
    private static HashMap<Player, ArrayList<String>> teams = new HashMap<>();

    public static void addMember(Player player, String memberName) {
        ArrayList<String> members = teams.get(player);
        if (members == null)
            members = new ArrayList<>();
        members.add(memberName);
        teams.put(player, members);
    }

    public static void removeMember(Player player, String memberName) {
        ArrayList<String> members = teams.get(player);
        if (members == null)
            return;
        members.remove(memberName);
        teams.put(player, members);
    }

    public static ArrayList<String> getTeamList(Player player) {
        ArrayList<String> members = teams.get(player);
        if (members == null)
            return null;
        if (members.size() == 0)
            return null;
        return members;
    }

    public static boolean isInTeam(Player player, String memberName) {
        ArrayList<String> members = teams.get(player);
        if (members == null)
            return false;
        if (members.contains(memberName))
            return true;
        return false;
    }
}
