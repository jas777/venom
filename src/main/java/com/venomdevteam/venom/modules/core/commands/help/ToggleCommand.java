package com.venomdevteam.venom.modules.core.commands.help;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;
import com.venomdevteam.venom.entity.command.argument.Arguments;
import com.venomdevteam.venom.entity.command.argument.CommandArgument;
import com.venomdevteam.venom.entity.guild.VenomGuild;
import com.venomdevteam.venom.entity.member.VenomMember;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ToggleCommand extends Command {

    public ToggleCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "toggle";
        this.aliases = new String[]{"switch"};
        this.arguments = new CommandArgument[]{
                new CommandArgument("command", ArgumentType.COMMAND, false)
        };
        this.userPermissions = new Permission[]{
                Permission.ADMINISTRATOR
        };
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        Arguments arguments = Arguments.of(this, args);

        VenomMember venomMember = VenomMember.of(VenomGuild.from(venom, sender.getGuild()), sender);

        Command toDisable = (Command) arguments.next().getArgument().get();

        toDisable.toggle(venomMember.getVenomGuild());

        boolean enabled = toDisable.isEnabled(venomMember.getVenomGuild());

        channel.sendMessage(
                "<:check:505023087159541802> Command successfully " + (enabled ? "**enabled!**" : "**disabled!**")
        ).queue();

    }
}
