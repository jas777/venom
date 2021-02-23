package com.venomdevteam.venom.modules.music.commands;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;

public class LeaveCommand extends Command {

    public LeaveCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "leave";
        this.aliases = new String[]{"disconnect", "vcleave", "vcl"};
        this.description = "Makes the bot leave your voice channel";
        this.usage = "join";
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        AudioManager audioManager = channel.getGuild().getAudioManager();
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!audioManager.isConnected()) {
            channel.sendMessage("<:checkno:505023190972497941> I'm not connected to any voice channel!").queue();
            return;
        }

        if (!Objects.requireNonNull(voiceChannel).getMembers().contains(sender)) {
            channel.sendMessage("<:checkno:505023190972497941> You have to be connected to the voice channel!").queue();
            return;
        }

        audioManager.closeAudioConnection();
        channel.sendMessage("<:check:505023087159541802> Successfully left the voice channel!").queue();

    }
}
