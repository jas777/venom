package com.venomdevteam.venom.entity.command;

import com.venomdevteam.venom.entity.command.argument.CommandArgument;
import com.venomdevteam.venom.entity.command.dialogue.DialogueCommand;
import com.venomdevteam.venom.entity.guild.VenomGuild;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.entity.type.GuildToggleable;
import com.venomdevteam.venom.entity.type.Toggleable;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class Command implements Toggleable, GuildToggleable {

    protected String name = null;
    protected String description = "no description provided...";
    @Deprecated
    protected String usage = "no usage provided...";
    protected String[] aliases = new String[0];

    protected final boolean dm = false;
    protected boolean ownerOnly = false;
    protected final boolean hidden = false;
    protected boolean enabled = true;

    protected int cooldown = 0;

    protected Permission[] userPermissions = new Permission[0];
    protected Permission[] botPermissions = new Permission[0];

    protected CommandArgument[] arguments = new CommandArgument[0];

    protected Command[] child = null;
    protected DialogueCommand dialogueVersion = null;

    protected final CommandCategory commandCategory = null;

    protected final Module module;

    protected final Venom venom;

    public Command(Module module, Venom venom) {
        this.module = module;
        this.venom = venom;
    }

    public void run(Member sender, TextChannel channel, String[] args, Message message) {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }

    public boolean isDm() {
        return dm;
    }

    public boolean isOwnerOnly() {
        return ownerOnly;
    }

    public boolean isHidden() {
        return hidden;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Permission[] getUserPermissions() {
        return userPermissions;
    }

    public Permission[] getBotPermissions() {
        return botPermissions;
    }

    public CommandArgument[] getArguments() {
        return arguments;
    }

    public Command[] getChild() {
        return child;
    }

    public DialogueCommand getDialogueVersion() {
        return dialogueVersion;
    }

    public CommandCategory getCategory() {
        return commandCategory;
    }

    public Module getModule() {
        return module;
    }

    public Venom getVenom() {
        return venom;
    }

    // Global toggling

    @Override
    public void toggle() {
        this.enabled = !this.enabled;
    }

    @Override
    public void enable() {
        this.enabled = true;
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // Local toggling

    @Override
    public void toggle(VenomGuild guild) {
        boolean enable = !isEnabled(guild);

        if(enable) {
            enable(guild);
        } else {
            disable(guild);
        }
    }

    @Override
    public void enable(VenomGuild guild) {
        if(!isEnabled(guild)) {
            guild.getSettings().enableCommand(this);
            guild.getSettings().save();
        }
    }

    @Override
    public void disable(VenomGuild guild) {
        if(isEnabled(guild)) {
            guild.getSettings().disableCommand(this);
            guild.getSettings().save();
        }
    }

    @Override
    public boolean isEnabled(VenomGuild guild) {
        return !guild.getSettings().isDisabled(this);
    }

    public String getUsage() {

        StringBuilder sb = new StringBuilder();

        sb.append(this.name);

        for (CommandArgument argument : this.arguments) {
            if (argument.isOptional()) {
                sb.append(" [").append(argument.getName()).append("]");
            } else {
                sb.append(" <").append(argument.getName()).append(">");
            }
        }

        return sb.toString();

    }
}
