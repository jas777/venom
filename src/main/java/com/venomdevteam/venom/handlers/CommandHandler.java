package com.venomdevteam.venom.handlers;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.command.Cooldown;
import com.venomdevteam.venom.entity.command.argument.Arguments;
import com.venomdevteam.venom.entity.command.argument.CommandArgument;
import com.venomdevteam.venom.entity.command.argument.ParsedArgument;
import com.venomdevteam.venom.entity.command.dialogue.DialogueCommand;
import com.venomdevteam.venom.entity.guild.VenomGuild;
import com.venomdevteam.venom.main.Venom;
import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.*;

public class CommandHandler extends ListenerAdapter {

    private JDA bot;

    private final THashMap<String, Command> commands = new THashMap<>();
    private final THashMap<User, Cooldown> cooldowns = new THashMap<>();

    final List<String> specialArguments = Arrays.asList("-f", "-d");

    private final String prefix;

    private final Venom venom;

    public CommandHandler(Venom venom) {
        this.venom = venom;
        this.prefix = venom.getConfig().getString("client.defaultPrefix");
    }

    public void registerCommands(Command... cmds) {
        for (Command command : cmds) {
            this.commands.put(command.getName(), command);
        }
    }

    public Command getCommand(String name, boolean ownerCommands) {
        Command cmd = null;
        for (Command command : this.commands.values()) {
            if (command.getName().equalsIgnoreCase(name) || Arrays.asList(command.getAliases()).contains(name)) {
                if (ownerCommands) {
                    cmd = command;
                } else {
                    if (!command.isOwnerOnly()) {
                        cmd = command;
                    }
                }
            }
        }
        return cmd;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public THashMap<String, Command> getCommands() {
        return this.commands;
    }

    private static String[] splitOnPrefixLength(String rawContent, int length) {
        return Arrays.copyOf(rawContent.substring(length).trim().split("\\s+", 2), 2);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        try {

            event.getGuild();
            if (!event.getMessage().getContentRaw().startsWith(this.prefix)) return;
            if (event.getAuthor().isBot()) return;

            EnumSet<Permission> botPerms = event.getGuild().getSelfMember().getPermissions(event.getTextChannel());

            if (!botPerms.contains(Permission.MESSAGE_WRITE)) return;

            String[] splitMessage = splitOnPrefixLength(event.getMessage().getContentRaw(), this.prefix.length());
            String command = splitMessage[0];
            String[] args;

            if (splitMessage.length > 1 && splitMessage[1] != null) {
                args = splitMessage[1].split(" ");
            } else {
                args = new String[]{};
            }

            if (getCommand(command, true) != null) {

                Command cmd = getCommand(command, true);

                if ((cmd.getChild() != null) && (args.length > 0)) {
                    for (Command childCmd : cmd.getChild()) {
                        List<String> aliases = Arrays.asList(childCmd.getAliases());
                        System.out.println("c");
                        if (childCmd.getName().equalsIgnoreCase(args[0]) || aliases.contains(args[0])) {
                            cmd = childCmd;
                            if (args.length > 1) {
                                args = Arrays.copyOfRange(args, 1, args.length);
                            } else {
                                args = new String[]{};
                            }
                            break;
                        }
                    }
                }

                if (cmd.isOwnerOnly()) {

                    boolean isOwner = venom.getConfig().getList("other.owners").contains(event.getAuthor().getId());

                    if (!isOwner) return;

                }

                // OPTIMIZE: Redo owner argument handling

                if (args.length > 0 && specialArguments.contains(args[args.length - 1])) {

                    switch (args[args.length - 1]) {
                        case "-f":
                            if (venom.getConfig().getList("other.owners").contains(event.getAuthor().getId())) {

                                try {

                                    List<String> argsToFormat = new ArrayList<>(Arrays.asList(args));

                                    argsToFormat.remove(args.length - 1);

                                    args = argsToFormat.toArray(new String[]{});

                                    venom.getLogger().warn(event.getAuthor().getAsTag() + " used owner override on " + event.getGuild().getId());

                                    cmd.run(event.getMember(), event.getTextChannel(), args, event.getMessage());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                            break;

                        case "-d":
                            if (cmd instanceof DialogueCommand) return;
                            if (cmd.getDialogueVersion() != null) cmd = cmd.getDialogueVersion();
                            break;

                    }

                }

                if (!cmd.isEnabled() || !cmd.isEnabled(VenomGuild.from(venom, event.getGuild()))) {

                    event.getMessage().getChannel().sendMessage(
                            "<:checkno:505023190972497941> This command is disabled here!"
                    ).queue();

                    return;

                }

                if (cooldowns.containsKey(event.getAuthor())) {

                    Cooldown cooldown = cooldowns.get(event.getAuthor());

                    if (!cooldown.isExpired() && cooldown.getCommand().equals(cmd)) {
                        event.getMessage().getChannel()
                                .sendMessage("Whoa there! Cool down a bit! Wait " +
                                        cooldown.getSecondsLeft() +
                                        " seconds to use this command again!").queue();
                        return;
                    }

                    if (cooldown.isExpired() && cooldown.getCommand().equals(cmd)) {
                        cooldowns.remove(event.getAuthor());
                    }

                } else {
                    cooldowns.put(event.getAuthor(), new Cooldown(event.getAuthor(), cmd, cmd.getCooldown()));
                }

                if (!(cmd instanceof DialogueCommand)) {

                    Arguments arguments = Arguments.of(cmd, args);

                    ArrayList<String> missingArguments = new ArrayList<>();

                    ArrayList<String> invalidArguments = new ArrayList<>();

                    int a = 0;

                    if (cmd.getArguments().length > 0) {
                        for (int i = 0; i < cmd.getArguments().length; i++) {
                            System.out.println("u");
                            CommandArgument commandArgument = cmd.getArguments()[i];
                            System.out.println("b");
                            ParsedArgument parsedArgument = null;

                            if (arguments.size() > a) {
                                parsedArgument = arguments.get(a);
                            }

                            if (parsedArgument != null) {

                                if (commandArgument.isOptional()) {
                                    if (parsedArgument.getName().equals(commandArgument.getName())) {
                                        if (parsedArgument.getArgument().isEmpty()) {
                                            invalidArguments.add(commandArgument.getName());
                                            a++;
                                        }
                                    }
                                } else {
                                    if (!parsedArgument.getName().equals(commandArgument.getName())) {
                                        missingArguments.add(commandArgument.getName());
                                        a++;
                                    }

                                    if (parsedArgument.getArgument().isEmpty()) {
                                        invalidArguments.add(commandArgument.getName());
                                        a++;
                                    }

                                    a++;
                                }

                            }
                        }
                    }

                    if (missingArguments.size() > 0 || invalidArguments.size() > 0) {

                        EmbedBuilder embed = venom.getBaseEmbed();

                        StringBuilder sb = new StringBuilder();

                        StringBuilder usage = new StringBuilder();

                        usage.append("`").append(cmd.getName()).append(" ");

                        for (CommandArgument commandArgument : cmd.getArguments()) {
                            if (commandArgument.isOptional()) {
                                usage.append("[").append(commandArgument.getName()).append("] ");
                            } else {
                                usage.append("<").append(commandArgument.getName()).append("> ");
                            }
                        }

                        if (missingArguments.size() > 0) {
                            sb
                                    .append("Missing arguments: `")
                                    .append(String.join(", ", missingArguments))
                                    .append("`\n\n")
                                    .append("Usage: ")
                                    .append(usage)
                                    .append("`");

                        }

                        embed.setDescription(sb);

                        event.getMessage().getChannel().sendMessage(embed.build()).queue();
                        return;

                    }
                }

                List<Permission> missingPerms = new LinkedList<>(Arrays.asList(cmd.getUserPermissions()));

                missingPerms.removeAll(Objects.requireNonNull(event.getMember()).getPermissions(event.getTextChannel()));

                List<String> formatPerms = new ArrayList<>();

                for (Permission perm : missingPerms) {
                    formatPerms.add(perm.getName());
                }

                if (missingPerms.size() > 0) {
                    if (missingPerms.size() == 1) {
                        event.getChannel()
                                .sendMessage(":x: You need to have **" +
                                        String.join("", formatPerms) +
                                        "** permission to use this command!").queue();
                    } else {
                        event.getChannel()
                                .sendMessage(":x: You need to have **" +
                                        String.join(", ", formatPerms) +
                                        "** permissions to use this command!").queue();
                    }
                } else {
                    try {
                        cmd.run(event.getMember(), event.getTextChannel(), args, event.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();

                        EmbedBuilder builder = new EmbedBuilder()
                                .setColor(Color.RED)
                                .setDescription("<:checkno:505023190972497941> Whoops! Something went wrong! Try again later or contact developer.");
                        event.getChannel().sendMessage(builder.build()).queue();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
