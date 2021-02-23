package com.venomdevteam.venom.modules.core.commands.help.dialogue;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.command.argument.Arguments;
import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;
import com.venomdevteam.venom.entity.command.dialogue.DialogueCommand;
import com.venomdevteam.venom.entity.command.dialogue.DialogueStep;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.core.commands.help.DisabledCommands;
import com.venomdevteam.venom.modules.core.commands.help.ToggleCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DialogueHelpCommand extends DialogueCommand {

    public DialogueHelpCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "help";
        this.aliases = new String[]{"commands", "cmds"};
        this.description = "Shows all available modules";
        this.cooldown = 3;

        this.dialogue = new DialogueStep[]{
                new DialogueStep(
                        this,
                        "Would you like to get information about a **module** or a **command**?",
                        ArgumentType.MODULE_OR_COMMAND,
                        false
                ),
                new DialogueStep(
                        this,
                        "Which one would you like me to show more information about?",
                        ArgumentType.STRING,
                        true
                )
        };

        this.child = new Command[]{
                new DisabledCommands(module, venom),
                new ToggleCommand(module, venom)
        };
    }

    @Override
    public void finalize(Member sender, TextChannel channel, DialogueStep[] args, Message message) {

        Arguments arguments = Arguments.of(this, args, message.getAuthor());

        boolean isModule = ((String) arguments.next().getArgument().get()).equalsIgnoreCase("module");

        EmbedBuilder builder = venom.getBaseEmbed();

        if (isModule) {

            if (venom.getModules().containsKey(((String) arguments.next().getArgument().get()).toLowerCase())) {

                Module module = venom.getModules().get(((String) arguments.get(1).getArgument().get()).toLowerCase());

                ArrayList<String> moduleCommands = new ArrayList<>();

                this.venom.getCommandHandler().getCommands().forEach((name, command) -> {
                    if (command.getModule().equals(module) && !command.isOwnerOnly()) {
                        moduleCommands.add(command.getName());
                    }
                });

                builder
                        .setTitle(module.getName() + " - Module help")
                        .setDescription("**Description:** " + module.getDescription() + "\n**Enabled:** "
                                + (module.isEnabled() ? "Yes" : "No") +
                                "\n\n**Commands:** `" +
                                String.join("`, `", moduleCommands) + "`");

            }

        } else {

            Command cmd = venom.getCommandHandler().getCommand(((String) arguments.next().getArgument().get()), false);

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

        channel.sendMessage(builder.build()).queue();

    }

}
