package fun.kaituo.voicechatautojoin;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VoiceChatAutoJoin extends JavaPlugin implements Listener {

    public static final String PLUGIN_ID = "voicechatautojoin";

    private VoicechatServerApi api;

    public void setApi(VoicechatServerApi api) {
        this.api = api;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
        VoicechatConnection connection = api.getConnectionOf(e.getPlayer().getUniqueId());
        if (connection == null) {
            return;
        }
        if (e.getNewGameMode().equals(GameMode.SPECTATOR)) {
            connection.setGroup(api.getGroup(AutoJoinVoiceChatPlugin.spectatorGroupUuid));
            e.getPlayer().sendMessage("§f已将你自动加入观察者语音聊天频道！");
        } else {
            connection.setGroup(api.getGroup(AutoJoinVoiceChatPlugin.defaultGroupUuid));
            e.getPlayer().sendMessage("§f已将你自动加入默认语音聊天频道！");
        }
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
    }

    @Override
    public void onDisable() {
    }
}
