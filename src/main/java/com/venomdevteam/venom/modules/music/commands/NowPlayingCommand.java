package com.venomdevteam.venom.modules.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.music.MusicModule;
import com.venomdevteam.venom.modules.music.manager.GuildMusicManager;
import com.venomdevteam.venom.util.audio.AudioUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.text.DecimalFormat;
import java.time.Duration;

public class NowPlayingCommand extends Command {

    public NowPlayingCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "now";
        this.aliases = new String[]{"np", "nowplaying"};
        this.description = "Shows info about current song";
        this.usage = "now";
        this.cooldown = 10;
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        MusicModule musicModule = (MusicModule) module;

        AudioManager audioManager = channel.getGuild().getAudioManager();
        GuildVoiceState memberVoiceState = sender.getVoiceState();
        GuildMusicManager musicManager = musicModule.getPlayerManager().getGuildMusicManager(channel.getGuild());

        if (!audioManager.isConnected()) {
            channel.sendMessage("<:checkno:505023190972497941> I'm not connected to any voice channel!").queue();
            return;
        }

        if (musicManager.player.getPlayingTrack() == null) {
            channel.sendMessage("<:checkno:505023190972497941> There isn't any song playing!").queue();
            return;
        }

        EmbedBuilder embed = venom.getBaseEmbed();

        AudioTrack track = musicManager.player.getPlayingTrack();

        final int maxTicks = 30;

        long durationDividable = track.getDuration() - (track.getDuration() % maxTicks);
        long timePerTick = durationDividable / maxTicks;

        long currentDividable = track.getPosition() - (track.getPosition() % maxTicks);
        long ticksElapsed = currentDividable / timePerTick;

        StringBuilder progress = new StringBuilder();

        for (int i = 0; i <= maxTicks; i++) {
            if (i == ticksElapsed) {
                progress.append(":radio_button:");
                continue;
            }
            progress.append("-");
        }

        Duration trackDuration = Duration.ofMillis(track.getDuration());
        Duration elapsedDuration = Duration.ofMillis(track.getPosition());

        long minutesTotal = (long) Math.floor(trackDuration.getSeconds() / 60);
        long secondsTotal = trackDuration.getSeconds() % 60;

        long minutesElapsed = (long) Math.floor(elapsedDuration.getSeconds() / 60);
        long secondsElapsed = elapsedDuration.getSeconds() % 60;

        DecimalFormat formatter = new DecimalFormat("00");

        embed
                .setTitle(":notes: Now playing")
                .setThumbnail(AudioUtil.getThumbnail(track.getInfo()))
                .setDescription(
                        "**" + track.getInfo().title + " by " + track.getInfo().author + "**" +
                                "\n**Duration:** " + formatter.format(minutesElapsed) + ":" + formatter.format(secondsElapsed) + " / " +
                                formatter.format(minutesTotal) + ":" + formatter.format(secondsTotal) +
                                "\n\n**" + progress.toString() + "**"
                );

        channel.sendMessage(embed.build()).queue();

    }
}
