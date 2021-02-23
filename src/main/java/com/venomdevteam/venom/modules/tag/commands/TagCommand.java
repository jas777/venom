package com.venomdevteam.venom.modules.tag.commands;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;
import com.venomdevteam.venom.entity.command.argument.Arguments;
import com.venomdevteam.venom.entity.command.argument.CommandArgument;
import com.venomdevteam.venom.entity.guild.VenomGuild;
import com.venomdevteam.venom.entity.guild.tag.GuildTag;
import com.venomdevteam.venom.entity.member.VenomMember;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.stream.Collectors;

public class TagCommand extends Command {

    public TagCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "tag";
        this.description = "Shows given tag (`tag list` for list of tags)";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.arguments = new CommandArgument[]{
                new CommandArgument("tag", ArgumentType.STRING, false)
        };
        this.child = new Command[]{
                new TagCreateCommand(this.module, this.venom),
                new TagListCommand(this.module, this.venom)
        };
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        VenomGuild guild = VenomGuild.from(venom, sender.getGuild());
        VenomMember venomMember = VenomMember.of(guild, sender);

        Arguments arguments = Arguments.of(this, args);

        String tagName = (String) arguments.next().getArgument().get();

        GuildTag tag = guild.getTagManager().getTags().stream()
                .filter(guildTag -> guildTag.getName().equals(tagName))
                .collect(Collectors.toList()).get(0);

        channel.sendMessage(tag.getContent()).queue();

    }
}
