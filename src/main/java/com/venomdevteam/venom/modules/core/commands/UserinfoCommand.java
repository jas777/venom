package com.venomdevteam.venom.modules.core.commands;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.util.discord.DiscordUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class UserinfoCommand extends Command {

    public UserinfoCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "userinfo";
        this.aliases = new String[]{"useri", "ui", "uinfo"};
        this.description = "Shows information about given user";
        this.usage = "userinfo [user]";
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        EmbedBuilder builder = this.venom.getBaseEmbed();
        User user = message.getAuthor();

        if (args.length > 0) {
            if (message.getMentionedUsers().size() > 0) {
                user = message.getMentionedUsers().get(0);
            } else {
                user = DiscordUtil.getUser(this.venom.getClient(), String.join(" ", args));
            }
        }

        if (user == null) {
            builder
                    .setColor(Color.RED)
                    .setDescription(":x: I couldn't find user with id or named `" + String.join(" ", args) + "`");
            channel.sendMessage(builder.build()).queue();
            return;
        }

        builder
                .setAuthor(user.getAsTag() + " - User informations", user.getAvatarUrl())
                .setDescription("**Username:** " + user.getName() + "\n" +
                        "**Discriminator:** " + user.getDiscriminator() + "\n" +
                        "**ID:** " + user.getId() + "\n" +
                        "**Bot:** " + (user.isBot() ? "Yes" : "No"));

        channel.sendMessage(builder.build()).queue();
    }
}
