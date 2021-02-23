package com.venomdevteam.venom.modules.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.util.audio.AudioUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {

    private final Venom venom;

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    private TextChannel channel = null;

    private final ArrayList<User> votingToSkip;

    private boolean looped;

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(Venom venom, AudioPlayer player) {

        this.venom = venom;

        this.player = player;
        this.queue = new LinkedBlockingQueue<>();

        this.votingToSkip = new ArrayList<>();

        this.looped = false;
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        if (this.channel != null) {

            EmbedBuilder embed = venom.getBaseEmbed();

            Duration trackDuration = Duration.ofMillis(track.getDuration());

            /*
             *
             * TODO: Thumbnails from soundcloud
             * URGENT: Age-restricted vids
             *
             * NEW MODULE: Rank ladder
             *
             */

            long minutes = (long) Math.floor(trackDuration.getSeconds() / 60);
            long seconds = trackDuration.getSeconds() % 60;

            DecimalFormat formatter = new DecimalFormat("00");

            AudioTrack next = queue.peek();

            embed
                    .setTitle(":notes: Now playing")
                    .setThumbnail(AudioUtil.getThumbnail(track.getInfo()))
                    .setDescription(
                            "**Song:** " + track.getInfo().title + " by " + track.getInfo().author +
                            "\n**Duration:** " + formatter.format(minutes) + ":" + formatter.format(seconds) +
                            "\n**Next:** " + (next != null ? next.getInfo().title + " by " + next.getInfo().author
                                    : "Nothing")
                    );

            channel.sendMessage(embed.build()).queue();

            this.votingToSkip.clear();
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            if(!looped) {
                if (queue.isEmpty()) {
                    if (channel != null) {
                        this.channel.sendMessage(":notes: Finished playing - leaving").queue();
                        channel.getGuild().getAudioManager().closeAudioConnection();
                    }
                } else {
                    nextTrack();
                }
            } else {
                queue.offer(track.makeClone());
                nextTrack();
            }

        } else {
            if (endReason.equals(AudioTrackEndReason.LOAD_FAILED)) {
                if (channel != null) {
                    this.channel.sendMessage("<:checkno:505023190972497941> Could not load - skipping").queue();
                    nextTrack();
                }
            }
        }
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        nextTrack();
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        if (channel != null) {
            this.channel.sendMessage("<:checkno:505023190972497941> An error occurred while playing - skipping").queue();
            nextTrack();
        }
    }

    public void setChannel(TextChannel channel) {
        this.channel = channel;
    }

    public ArrayList<User> getVotingToSkip() {
        return votingToSkip;
    }

    public int getTotalVotes() {
        return votingToSkip.size();
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public void setLooped(boolean looped) {
        this.looped = looped;
    }

    public boolean isLooped() {
        return looped;
    }
}