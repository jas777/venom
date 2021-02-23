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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.Date;

public class TagCreateCommand extends Command {

    public TagCreateCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "create";
        this.arguments = new CommandArgument[]{
                new CommandArgument("name", ArgumentType.STRING, false),
                new CommandArgument("content", ArgumentType.STRING, false)
        };
        this.cooldown = 30;
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        Arguments arguments = Arguments.of(this, args);

        String name = (String) arguments.next().getArgument().get();
        String content = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        GuildTag tag = new GuildTag(VenomMember.of(VenomGuild.from(venom, sender.getGuild()), sender), name, content, new Date());

        tag.save();

        channel.sendMessage("<:check:505023087159541802> Successfully created tag `" + tag.getName() + "`").queue();

    }
}
