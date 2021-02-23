package com.venomdevteam.venom.modules.music.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.music.audio.AudioPlayerSendHandler;
import com.venomdevteam.venom.modules.music.audio.TrackScheduler;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {
    /**
     * Audio player for the guild.
     */
    public final AudioPlayer player;
    /**
     * Track scheduler for the player.
     */
    public final TrackScheduler scheduler;

    /**
     * Creates a player and a track scheduler.
     *
     * @param manager Audio player manager to use for creating the player.
     */
    public GuildMusicManager(Venom venom, AudioPlayerManager manager) {
        player = manager.createPlayer();
        player.setFilterFactory(null);
        scheduler = new TrackScheduler(venom, player);
        player.addListener(scheduler);
    }

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }
}