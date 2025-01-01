package fun.kaituo.voicechatautojoin;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.PlayerConnectedEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AutoJoinVoiceChatPlugin implements VoicechatPlugin {
    private final VoiceChatAutoJoin plugin;

    public static final UUID defaultGroupUuid = UUID.randomUUID();
    public static final UUID spectatorGroupUuid = UUID.randomUUID();

    public AutoJoinVoiceChatPlugin(VoiceChatAutoJoin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPluginId() {
        return VoiceChatAutoJoin.PLUGIN_ID;
    }

    public void onServerStarted(VoicechatServerStartedEvent e) {
        plugin.setApi(e.getVoicechat());
        e.getVoicechat().groupBuilder()
                .setId(defaultGroupUuid)
                .setName("Default")
                .setPersistent(true)
                .setType(Group.Type.ISOLATED)
                .build();
        e.getVoicechat().groupBuilder()
                .setId(spectatorGroupUuid)
                .setName("Spectator")
                .setPersistent(true)
                .setType(Group.Type.ISOLATED)
                .build();
    }

    public void onPlayerConnected(PlayerConnectedEvent e) {
        Player p = (Player) e.getConnection().getPlayer().getPlayer();
        if (p.getGameMode().equals(org.bukkit.GameMode.SPECTATOR)) {
            e.getConnection().setGroup(e.getVoicechat().getGroup(spectatorGroupUuid));
            String msg = "§f已将你自动加入观察者语音聊天频道！";
            p.sendMessage(msg);
        } else {
            e.getConnection().setGroup(e.getVoicechat().getGroup(defaultGroupUuid));
            String msg = "§f已将你自动加入默认语音聊天频道！";
            p.sendMessage(msg);
        }
    }



    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(PlayerConnectedEvent.class, this::onPlayerConnected);
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
    }


}
