package ink.rainbowbridge.v1.arathoth.listener;

import ink.rainbowbridge.v1.arathoth.ArathothI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 寒雨
 * @Since 2021/1/26 6:55
 */
public class StatusCommandListener implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){
        if(e.getMessage().equals("/"+ArathothI.getInstance().getConfig().getString("StatusInfo.Command"))){
            e.setCancelled(true);
            Plugin plugin = ArathothI.getInstance();
            Player p = e.getPlayer();
            if (plugin.getConfig().getStringList("StatusInfo.Messages") == null) {
                ArathothI.send(p, "No status messages,please set it in config.yml!");
            } else {
                List<String> papi = new ArrayList<>();
                List<String> out = new ArrayList<>();
                for (String str : plugin.getConfig().getStringList("StatusInfo.RegisteredPAPI")) {
                    try {
                        if (Double.valueOf(PlaceholderAPI.setPlaceholders(p, str)).equals(0.0D)) {
                            papi.add(str);
                        }
                    } catch (NumberFormatException ex) {
                    }

                }
                for (String str : plugin.getConfig().getStringList("StatusInfo.Messages")) {
                    boolean iszero = false;
                    for (String placeholder : papi) {
                        if (str.contains(placeholder)) {
                            iszero = true;
                            break;
                        }
                    }
                    if (!iszero) {
                        out.add(str);
                    }
                }
                if (out.size() > plugin.getConfig().getInt("StatusInfo.MinLines")) {
                    for (String str : out) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(p, str)));
                    }
                } else {
                    p.sendTitle(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("StatusInfo.HideTitleMessage.Title")), ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("StatusInfo.HideTitleMessage.SubTitle")), 10, 40, 10);
                }
            }
        }
    }

    /**
     * Tab补全
     * @param e 事件
     */
    public void onTabComplete(PlayerChatTabCompleteEvent e){
        if (e.getChatMessage().startsWith("/")){
            e.getTabCompletions().add("/"+ArathothI.getInstance().getConfig().getString("StatusInfo.Command"));
        }
    }
}
