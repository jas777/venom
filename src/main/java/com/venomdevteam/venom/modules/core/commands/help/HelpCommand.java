package com.venomdevteam.venom.modules.core.commands.help;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;
import com.venomdevteam.venom.entity.command.argument.Arguments;
import com.venomdevteam.venom.entity.command.argument.CommandArgument;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.core.commands.help.dialogue.DialogueHelpCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HelpCommand extends Command {

    public HelpCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "help";
        this.aliases = new String[]{"commands", "cmds"};
        this.description = "Shows all available modules";
        this.cooldown = 3;
        this.arguments = new CommandArgument[]{
                new CommandArgument("command", ArgumentType.COMMAND, true),
                new CommandArgument("module", ArgumentType.MODULE, true)
        };
        this.child = new Command[]{
                new DisabledCommands(module, venom),
                new ToggleCommand(module, venom)
        };
        this.dialogueVersion = new DialogueHelpCommand(module, venom);
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        EmbedBuilder builder = this.venom.getBaseEmbed();

        ArrayList<String> moduleNames = new ArrayList<>();

        for (Module module : this.venom.getModules().values()) {
            moduleNames.add(module.getName());
        }

        Arguments arguments = Arguments.of(this, args);

        if (arguments.size() == 0) {
            builder.setDescription(this.venom.getConfig().getString("commands.help.description").replace("{MODULES}", String.join(", ", moduleNames)));
            channel.sendMessage(builder.build()).queue();
        }

        if (arguments.size() >= 1) {

            if (arguments.get(0).getArgument().isEmpty()) {
                builder
                        .setColor(Color.RED)
                        .setDescription(":x: I couldn't find module or command named `" + String.join(" ", args) + "`");

                channel.sendMessage(builder.build()).queue();
                return;
            }

            if ((arguments.get(0).getArgument().get() instanceof Command) || (arguments.get(0).getArgument().get() instanceof Module)) {

                if (arguments.get(0).getArgument().get() instanceof Module) {

                    Module module = (Module) arguments.get(0).getArgument().get();

                    ArrayList<String> moduleCommands = new ArrayList<>();

                    this.venom.getCommandHandler().getCommands().forEach((name, command) -> {
                        if (command.getModule().equals(module) && !command.isOwnerOnly()) {
                            moduleCommands.add(command.getName());
                        }
                    });

                    builder
                            .setTitle(module.getName() + " - Module help")
                            .setDescription("**Description:** " + module.getDescription() + "\n**Enabled:** " + (module.isEnabled() ? "Yes" : "No") + "\n\n**Commands:** `" + String.join("`, `", moduleCommands) + "`");
                }

                if (arguments.get(0).getArgument().get() instanceof Command) {

                    Command cmd = (Command) arguments.get(0).getArgument().get();

                    if (cmd.isOwnerOnly()) {
                        builder
                                .setColor(Color.RED)
                                .setDescription(":x: I couldn't find module or command named `" + String.join(" ", args) + "`");

                        channel.sendMessage(builder.build()).queue();
                        return;
                    }

                    ArrayList<String> perms = new ArrayList<>();

                    for (Permission perm : cmd.getUserPermissions()) {
                        perms.add(perm.getName());
                    }

                    builder
                            .setTitle(cmd.getName() + " - Command help")
                            .setDescription("**Description:** " + cmd.getDescription() +
                                    "\n**Aliases:** " + (cmd.getAliases() != null && cmd.getAliases().length > 0 ? String.join(", ", cmd.getAliases()) : "None") +
                                    "\n**Child commands:** " + (cmd.getChild() != null && cmd.getChild().length > 0 ? Arrays.stream(cmd.getChild())
                                    .map(Command::getName).collect(Collectors.joining(", ")) : "None") +
                                    "\n**Usage:** `" + cmd.getUsage() +
                                    "`\n**Required permissions:** " + (perms.size() > 0 ? String.join(", ", perms) : "None"));
                }

            }

            channel.sendMessage(builder.build()).queue();
        }

    }
}
