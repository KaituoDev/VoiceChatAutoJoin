package fun.kaituo.voicechatautojoin;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import org.bukkit.plugin.java.JavaPlugin;

public class VoiceChatAutoJoin extends JavaPlugin {

    private VoicechatServerApi api;

    public VoicechatServerApi getApi() {
        return api;
    }

    public void setApi(VoicechatServerApi api) {
        this.api = api;
    }

    @Override
    public void onEnable() {
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
