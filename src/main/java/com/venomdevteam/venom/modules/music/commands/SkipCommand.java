package com.venomdevteam.venom.modules.music.commands;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.music.MusicModule;
import com.venomdevteam.venom.modules.music.manager.GuildMusicManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;

public class SkipCommand extends Command {

    public SkipCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "skip";
        this.aliases = new String[]{"vcskip", "vcsp"};
        this.description = "Skips current song";
        this.usage = "resume";
        this.cooldown = 10;
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {
        MusicModule musicModule = (MusicModule) module;

        AudioManager audioManager = channel.getGuild().getAudioManager();
        GuildVoiceState memberVoiceState = sender.getVoiceState();
        GuildMusicManager musicManager = musicModule.getPlayerManager().getGuildMusicManager(channel.getGuild());

        int totalVotes = musicManager.scheduler.getTotalVotes();

        if (!Objects.requireNonNull(memberVoiceState).inVoiceChannel()) {
            channel.sendMessage("<:checkno:505023190972497941> You're not in a voice channel!").queue();
            return;
        }

        if (!audioManager.isConnected()) {
            channel.sendMessage("<:checkno:505023190972497941> I'm not connected to any voice channel!").queue();
            return;
        }

        if (!Objects.requireNonNull(channel.getGuild().getSelfMember().getVoiceState().getChannel()).getMembers().contains(sender)) {
            channel.sendMessage("<:checkno:505023190972497941> You have to be connected to the same voice channel as me!").queue();
            return;
        }

        if (musicManager.player.getPlayingTrack() == null) {
            channel.sendMessage("<:checkno:505023190972497941> There isn't any song playing!").queue();
            return;
        }

        if (Objects.requireNonNull(memberVoiceState.getChannel()).getMembers().size() == 2) {
            channel.sendMessage(":fast_forward: _Woosh!_ Skipping").queue();
            musicManager.scheduler.nextTrack();
            return;
        } else {

            if (totalVotes >= Math.ceil(memberVoiceState.getChannel().getMembers().size() / 2)) {

                channel.sendMessage("⏩ _Woosh!_ Skipping").queue();
                musicManager.scheduler.nextTrack();
                return;

            } else {

                if(musicManager.scheduler.getVotingToSkip().contains(sender.getUser())) {
                    channel.sendMessage("You've already voted!").queue();
                    return;
                }

                musicManager.scheduler.getVotingToSkip().add(sender.getUser());

                channel.sendMessage("⏩ Skip? (" +
                        musicManager.scheduler.getTotalVotes() +
                        "/" +
                        (memberVoiceState.getChannel().getMembers().size() / 2) + ")"
                ).queue();

            }

        }
    }

}
