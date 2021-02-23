package com.venomdevteam.venom.modules.music.commands.queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;
import com.venomdevteam.venom.entity.command.argument.Arguments;
import com.venomdevteam.venom.entity.command.argument.CommandArgument;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.music.MusicModule;
import com.venomdevteam.venom.modules.music.manager.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class QueueCommand extends Command {

    public QueueCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "queue";
        this.aliases = new String[]{"songs", "vcqueue", "vcq"};
        this.description = "Shows queued songs";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.arguments = new CommandArgument[]{
                new CommandArgument("page", ArgumentType.NUMBER, true)
        };

        this.child = new Command[]{new QueueRemove(module, venom)};
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

        int page = 1;

        double divided = ((double) queue.size() / 10);

        Arguments arguments = Arguments.of(this, args);

        if (arguments.size() > 0) {
            page = (int) arguments.next().getArgument().get();
        }

        double pagesTotal = Math.ceil(divided);

        if (pagesTotal < page) {
            channel.sendMessage("<:checkno:505023190972497941> Invalid page number").queue();
            return;
        }

        ArrayList<String> songs = new ArrayList<>();

        for (int i = (page * 10) - 10; i < (page * 10); i++) {

            if (i < queue.size()) {

                AudioTrack song = queueArray[i];

                songs.add("[" + (i + 1) + "] " + song.getInfo().title);

            }

        }

        StringBuilder sb = new StringBuilder();

        sb
                .append("Page ").append(page)
                .append("/")
                .append((int) pagesTotal)
                .append("\n\n")
                .append("**Now playing:** ")
                .append(musicManager.player.getPlayingTrack() == null ? "Nothing" : musicManager.player.getPlayingTrack().getInfo().title)
                .append("\n\n")
                .append("```ini\n")
                .append(String.join("\n", songs))
                .append("```");

        EmbedBuilder builder = this.venom.getBaseEmbed();

        builder
                .setAuthor(channel.getGuild().getName() + " - Music queue", null, channel.getGuild().getIconUrl())
                .setDescription(sb);

        channel.sendMessage(builder.build()).queue();

    }
}
