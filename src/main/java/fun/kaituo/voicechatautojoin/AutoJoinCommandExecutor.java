package fun.kaituo.voicechatautojoin;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AutoJoinCommandExecutor implements CommandExecutor {

    private final VoiceChatAutoJoin plugin;

    public AutoJoinCommandExecutor(VoiceChatAutoJoin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("switchchat")) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage("§c只有玩家才能使用此命令！");
                return true;
            }
            if (args.length != 0) {
                sender.sendMessage("§c用法错误！正确用法：/s 或者使用全称 /switchchat");
                return true;
            }

            VoicechatConnection connection = plugin.getApi().getConnectionOf(p.getUniqueId());
            if (connection == null) {
                sender.sendMessage("§c你不在语音聊天中！");
                return true;
            }
            Group oldGroup = connection.getGroup();
            if (oldGroup == null) {
                sender.sendMessage("§c你不在指定的两个语音频道中！");
                connection.setGroup(plugin.getApi().getGroup(AutoJoinVoiceChatPlugin.DEFAULT_GROUP_UUID));
                p.sendMessage(AutoJoinVoiceChatPlugin.DEFAULT_JOIN_MESSAGE);
                return true;
            }
            if (oldGroup.getId().equals(AutoJoinVoiceChatPlugin.DEFAULT_GROUP_UUID)) {
                connection.setGroup(plugin.getApi().getGroup(AutoJoinVoiceChatPlugin.SPECTATOR_GROUP_UUID));
                p.sendMessage(AutoJoinVoiceChatPlugin.SPECTATOR_JOIN_MESSAGE);
            } else if (oldGroup.getId().equals(AutoJoinVoiceChatPlugin.SPECTATOR_GROUP_UUID)) {
                connection.setGroup(plugin.getApi().getGroup(AutoJoinVoiceChatPlugin.DEFAULT_GROUP_UUID));
                p.sendMessage(AutoJoinVoiceChatPlugin.DEFAULT_JOIN_MESSAGE);
            } else {
                sender.sendMessage("§c你不在指定的两个语音频道中！");
                connection.setGroup(plugin.getApi().getGroup(AutoJoinVoiceChatPlugin.DEFAULT_GROUP_UUID));
                p.sendMessage(AutoJoinVoiceChatPlugin.DEFAULT_JOIN_MESSAGE);
            }
            return true;
        }
        return false;
    }
}
