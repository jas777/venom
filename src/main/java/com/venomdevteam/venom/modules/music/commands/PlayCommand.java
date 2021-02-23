package com.venomdevteam.venom.modules.music.commands;

import com.google.api.services.youtube.model.SearchResult;
import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.command.argument.Arguments;
import com.venomdevteam.venom.entity.command.argument.CommandArgument;
import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;
import com.venomdevteam.venom.entity.event.waiter.EventWaiter;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.music.MusicModule;
import com.venomdevteam.venom.modules.music.manager.PlayerManager;
import com.venomdevteam.venom.util.checks.Checks;
import com.venomdevteam.venom.util.google.GoogleAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PlayCommand extends Command {

    private final String VIDEO_URL = "https://www.youtube.com/watch?v=";

    public PlayCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "play";
        this.description = "Adds given song to the queue";
        this.botPermissions = new Permission[]{Permission.VOICE_SPEAK};
        this.usage = "play <song>";
        this.arguments = new CommandArgument[]{
                new CommandArgument("song", ArgumentType.STRING, true)
        };
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {
        MusicModule musicModule = (MusicModule) this.module;
        PlayerManager playerManager = musicModule.getPlayerManager();
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

        playerManager.getGuildMusicManager(channel.getGuild()).scheduler.setChannel(channel);

        Arguments arguments = Arguments.of(this, args);

        System.out.println(arguments.getAll().toString());

        try {
            URL url = new URL((String) arguments.get(0).getArgument().get());
            playerManager.loadAndPlay(channel, url.toString());
        } catch (MalformedURLException e) {

            EventWaiter waiter = venom.getEventWaiter();

            EmbedBuilder embed = venom.getBaseEmbed();

            StringBuilder builder = new StringBuilder();

            GoogleAPI googleAPI = new GoogleAPI(venom);

            String query = String.join(" ", args);

            List<SearchResult> resultList = googleAPI.getYoutube().search(query);

            int i = 1;

            for (SearchResult result : resultList) {

                builder
                        .append("`")
                        .append(i)
                        .append(" - ")
                        .append(result.getSnippet().getTitle())
                        .append(" by ")
                        .append(result.getSnippet().getChannelTitle())
                        .append("`\n");

                i++;
            }

            builder
                    .append("\n**Please respond with a number to choose song, respond with 'cancel' to cancel input." +
                            "Input will auto-cancel after 30 seconds**");

            embed
                    .setDescription(builder)
                    .setTitle("Search results");

            channel.sendMessage(embed.build()).queue();

            waiter.waitForEvent(
                    MessageReceivedEvent.class,
                    event ->
                            event.getAuthor().equals(message.getAuthor()) &&
                                    event.getTextChannel().equals(channel) &&
                                    (Checks.isDigit(event.getMessage().getContentDisplay()) ||
                                            event.getMessage().getContentDisplay().equalsIgnoreCase("cancel")),
                    messageReceivedEvent -> {

                        if (messageReceivedEvent.getMessage().getContentDisplay().equalsIgnoreCase("cancel")) {
                            channel.sendMessage("Input cancelled!").queue();
                        } else {

                            int index = Integer.parseInt(messageReceivedEvent.getMessage().getContentDisplay()) - 1;

                            if (index >= resultList.size()) {
                                channel.sendMessage("Invalid song! Please try again!").queue();
                            } else {

                                SearchResult result = resultList.get(index);

                                playerManager
                                        .loadAndPlay(channel, VIDEO_URL + result.getId().getVideoId());
                            }
                        }

                    }, 30L, TimeUnit.SECONDS, () -> channel.sendMessage("You did not input any number - aborting.").queue());

        }

    }
}
