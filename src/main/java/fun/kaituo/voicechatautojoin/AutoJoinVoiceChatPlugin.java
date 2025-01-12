package fun.kaituo.voicechatautojoin;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.PlayerConnectedEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AutoJoinVoiceChatPlugin implements VoicechatPlugin {
    private final VoiceChatAutoJoin plugin;

    public static final String PLUGIN_ID = "voicechatautojoin";

    public static final String DEFAULT_GROUP_NAME = "Default";
    public static final UUID DEFAULT_GROUP_UUID = UUID.fromString("e723bdf9-f7e3-4099-a1c9-03a1d77402e4");
    public static final String DEFAULT_JOIN_MESSAGE = "§f已将你加入默认语音聊天频道！";

    public static final int CLEAR_GROUPS_DELAY = 20; // In server ticks

    public AutoJoinVoiceChatPlugin(VoiceChatAutoJoin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPluginId() {
        return PLUGIN_ID;
    }

    public void onServerStarted(VoicechatServerStartedEvent e) {
        plugin.setApi(e.getVoicechat());
        Bukkit.getScheduler().runTaskLater(plugin, this::initializeGroups, CLEAR_GROUPS_DELAY);
    }

    public void initializeGroups() {
        // Remove all groups
        List<UUID> groupIds = new ArrayList<>();
        for (Group group : plugin.getApi().getGroups()) {
            groupIds.add(group.getId());
        }
        for (UUID groupId : groupIds) {
            plugin.getApi().removeGroup(groupId);
        }
        // Create default and spectator groups
        plugin.getApi().groupBuilder()
                .setId(DEFAULT_GROUP_UUID)
                .setName(DEFAULT_GROUP_NAME)
                .setPersistent(true)
                .setType(Group.Type.ISOLATED)
                .build();
    }

    public void onPlayerConnected(PlayerConnectedEvent e) {
        e.getConnection().setGroup(e.getVoicechat().getGroup(DEFAULT_GROUP_UUID));
        Player p = (Player) e.getConnection().getPlayer().getPlayer();
        p.sendMessage(DEFAULT_JOIN_MESSAGE);
    }


    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(PlayerConnectedEvent.class, this::onPlayerConnected);
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
    }

}
