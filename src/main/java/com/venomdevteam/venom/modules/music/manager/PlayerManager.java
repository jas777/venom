package com.venomdevteam.venom.modules.music.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.venomdevteam.venom.main.Venom;
import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class PlayerManager {

    private final Venom venom;

    private final AudioPlayerManager playerManager;

    private final THashMap<Long, GuildMusicManager> musicManagers;

    public PlayerManager(Venom venom) {

        this.venom = venom;
        this.musicManagers = new THashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.playerManager);
        AudioSourceManagers.registerLocalSource(this.playerManager);

    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        GuildMusicManager musicManager = this.musicManagers.get(guild.getIdLong());

        if (musicManager == null) {
            musicManager = new GuildMusicManager(venom, playerManager);
            this.musicManagers.put(guild.getIdLong(), musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String trackURL) {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessageFormat(":notes: Success! Adding **%s** to queue!", track.getInfo().title).queue();

                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

                channel.sendMessageFormat(":notes: Success! Adding **%s** songs to queue!", playlist.getTracks().size()).queue();

                for (AudioTrack track : playlist.getTracks()) {
                    play(musicManager, track);
                }
            }

            @Override
            public void noMatches() {
                channel.sendMessage("<:checkno:505023190972497941> Nothing found").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("<:checkno:505023190972497941> Couldn't play: `" + exception.getMessage() + "`").queue();
            }
        });

    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public THashMap<Long, GuildMusicManager> getPlayers() {
        return this.musicManagers;
    }

}
