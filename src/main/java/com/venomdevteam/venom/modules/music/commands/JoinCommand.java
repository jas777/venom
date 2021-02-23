package com.venomdevteam.venom.modules.music.commands;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;

public class JoinCommand extends Command {

    public JoinCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "join";
        this.aliases = new String[]{"connect", "vcjoin", "vcj"};
        this.description = "Makes the bot join your voice channel";
        this.botPermissions = new Permission[]{Permission.VOICE_CONNECT};
        this.usage = "join";
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        AudioManager audioManager = channel.getGuild().getAudioManager();
        GuildVoiceState memberVoiceState = sender.getVoiceState();

        if (audioManager.isConnected()) {
            channel.sendMessage("<:checkno:505023190972497941> I'm already connected to a channel!").queue();
            return;
        }

        if (!Objects.requireNonNull(memberVoiceState).inVoiceChannel()) {
            channel.sendMessage("<:checkno:505023190972497941> You're not in a voice channel!").queue();
            return;
        }

        if (!sender.getGuild().getSelfMember().hasPermission(Objects.requireNonNull(memberVoiceState.getChannel()), Permission.VOICE_CONNECT)) {
            channel.sendMessageFormat("<:checkno:505023190972497941> I have no permission to join **%s**!",
                    memberVoiceState.getChannel().getName()).queue();
            return;
        }

        audioManager.openAudioConnection(memberVoiceState.getChannel());
        channel.sendMessage("<:check:505023087159541802> Successfully connected to your voice channel!").queue();
    }
}
