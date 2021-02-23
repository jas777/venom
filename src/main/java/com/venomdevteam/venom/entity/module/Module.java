package com.venomdevteam.venom.entity.module;

import com.venomdevteam.venom.entity.guild.VenomGuild;
import com.venomdevteam.venom.entity.type.GuildToggleable;
import com.venomdevteam.venom.entity.type.Toggleable;
import com.venomdevteam.venom.main.Venom;

public abstract class Module implements Toggleable, GuildToggleable {

    protected final Venom venom;

    protected String name;

    protected String description;

    protected boolean enabled;

    public Module(Venom venom) {
        this.venom = venom;
    }

    public void setup() {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isEnabled(VenomGuild guild) {
        return false;
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
    public void toggle() {
        this.enabled = !this.enabled;
    }

    @Override
    public void enable(VenomGuild guild) {
        guild.getSettings().enableModule(this);
    }

    @Override
    public void disable(VenomGuild guild) {
        guild.getSettings().disableModule(this);
    }

    @Override
    public void toggle(VenomGuild guild) {

        boolean enable = !isEnabled(guild);

        if (enable) {
            enable(guild);
        } else {
            disable(guild);
        }

    }
}
