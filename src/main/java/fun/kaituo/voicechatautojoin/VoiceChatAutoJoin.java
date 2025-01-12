package fun.kaituo.voicechatautojoin;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VoiceChatAutoJoin extends JavaPlugin implements Listener {

    private VoicechatServerApi api;

    public void setApi(VoicechatServerApi api) {
        this.api = api;
    }

    public VoicechatServerApi getApi() {
        return api;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
        VoicechatConnection connection = api.getConnectionOf(e.getPlayer().getUniqueId());
        if (connection == null) {
            return;
        }
        if (e.getNewGameMode().equals(GameMode.SPECTATOR)) {
            connection.setGroup(api.getGroup(AutoJoinVoiceChatPlugin.SPECTATOR_GROUP_UUID));
            e.getPlayer().sendMessage(AutoJoinVoiceChatPlugin.SPECTATOR_JOIN_MESSAGE);
        } else {
            connection.setGroup(api.getGroup(AutoJoinVoiceChatPlugin.DEFAULT_GROUP_UUID));
            e.getPlayer().sendMessage(AutoJoinVoiceChatPlugin.DEFAULT_JOIN_MESSAGE);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        BukkitVoicechatService service = getServer().getServicesManager().load(BukkitVoicechatService.class);
        if (service != null) {
            AutoJoinVoiceChatPlugin voiceChatPlugin = new AutoJoinVoiceChatPlugin(this);
            service.registerPlugin(voiceChatPlugin);
            getLogger().info("VoiceChatAutoJoin enabled");
        } else {
            getLogger().warning("VoiceChatAutoJoin failed to get bukkit voice chat service!");
        }
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("switchchat").setExecutor(new AutoJoinCommandExecutor(this));
    }

    @Override
    public void onDisable() {
    }
}
