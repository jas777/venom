package com.venomdevteam.venom.modules.music.commands.queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.music.MusicModule;
import com.venomdevteam.venom.modules.music.manager.GuildMusicManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.BlockingQueue;

public class QueueRemove extends Command {

    public QueueRemove(Module module, Venom venom) {
        super(module, venom);

        this.name = "remove";
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        MusicModule musicModule = (MusicModule) module;
        GuildMusicManager musicManager = musicModule.getPlayerManager().getGuildMusicManager(channel.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
        AudioTrack[] queueArray = queue.toArray(new AudioTrack[0]);

        if (queue.size() < 1) {
            channel.sendMessage("<:checkno:505023190972497941> The queue is empty!").queue();
            return;
        }

        int position = 0;

        if (args.length > 0) {
            try {
                position = Integer.parseInt(args[0]);
            } catch (Exception e) {
                channel.sendMessage("<:checkno:505023190972497941> Invalid song number!").queue();
                return;
            }
        }

        AudioTrack song = null;

        if (position > queue.size() || position < 1) {
            channel.sendMessage("<:checkno:505023190972497941> Invalid song number!").queue();
            return;
        }

        song = queueArray[position - 1];

        queue.remove(song);
        channel.sendMessageFormat("Removed **%s** from the queue", song.getInfo().title).queue();

    }
}
