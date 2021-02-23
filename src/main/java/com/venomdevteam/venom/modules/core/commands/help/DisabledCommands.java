package com.venomdevteam.venom.modules.core.commands.help;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.guild.VenomGuild;
import com.venomdevteam.venom.entity.member.VenomMember;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.stream.Collectors;

public class DisabledCommands extends Command {

    public DisabledCommands(Module module, Venom venom) {
        super(module, venom);

        this.name = "disabled";
        this.aliases = new String[]{"off"};
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        int longest = 0;

        VenomMember venomMember = VenomMember.of(VenomGuild.from(venom, sender.getGuild()), sender);

        if(venomMember.getVenomGuild().getSettings().getDisabledCommands().isEmpty()) {
            channel.sendMessage("There are no disabled commands here! :D").queue();
            return;
        }

        for (String name : venomMember.getVenomGuild().getSettings().getDisabledCommands().stream().map(Command::getName)
                .collect(Collectors.toList())) {

            if (name.length() > longest) {
                longest = name.length();
            }

        }

        StringBuilder builder = new StringBuilder();

        for (Command command : venomMember.getVenomGuild().getSettings().getDisabledCommands()) {

            if (!command.isEnabled()) {
                builder
                        .append(command.getName())
                        .append(" ".repeat(longest - command.getName().length()))
                        .append(" :: ")
                        .append("Globally Disabled\n");
            } else if (!command.isEnabled(venomMember.getVenomGuild())) {
                builder
                        .append(command.getName())
                        .append(" ".repeat(longest - command.getName().length()))
                        .append(" :: ")
                        .append("Locally Disabled\n");
            }

        }

        EmbedBuilder embed = venom.getBaseEmbed();

        embed
                .setAuthor(
                        sender.getGuild().getName() + " - Disabled commands",
                        null,
                        sender.getGuild().getIconUrl())
                .setDescription("```prolog\n" + builder.toString() + "```");

        channel.sendMessage(embed.build()).queue();

    }
}
