package fun.kaituo.voicechatautojoin;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import org.bukkit.plugin.java.JavaPlugin;

public class VoiceChatAutoJoin extends JavaPlugin {

    public static final String PLUGIN_ID = "voicechatautojoin";

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
    }

    @Override
    public void onDisable() {
    }
}
