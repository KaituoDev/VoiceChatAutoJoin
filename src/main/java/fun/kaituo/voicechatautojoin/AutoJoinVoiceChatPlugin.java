package fun.kaituo.voicechatautojoin;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.PlayerConnectedEvent;
import de.maxhenkel.voicechat.api.events.ServerEvent;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AutoJoinVoiceChatPlugin implements VoicechatPlugin {
    private final VoiceChatAutoJoin plugin;

    private static final UUID uuid = UUID.randomUUID();

    public AutoJoinVoiceChatPlugin(VoiceChatAutoJoin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPluginId() {
        return VoiceChatAutoJoin.PLUGIN_ID;
    }

    private boolean doesGroupExist(ServerEvent e) {
        for (Group g : e.getVoicechat().getGroups()) {
            if (g.getId().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    private void createGroupIfNotExists(ServerEvent e) {
        if (!doesGroupExist(e)) {
            plugin.getLogger().info("Does not exist, creating group");
            e.getVoicechat().groupBuilder()
                    .setId(uuid)
                    .setName(plugin.getConfig().getString("group-name"))
                    .setPersistent(true)
                    .setType(Group.Type.OPEN)
                    .build();
        }
    }

    public void onPlayerConnected(PlayerConnectedEvent e) {
        createGroupIfNotExists(e);
        e.getConnection().setGroup(e.getVoicechat().getGroup(uuid));
        Player p = (Player) e.getConnection().getPlayer().getPlayer();
        String msg = "§f已将你自动加入全服语音聊天频道！";
        p.sendMessage(msg);
    }



    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(PlayerConnectedEvent.class, this::onPlayerConnected);
    }


}
