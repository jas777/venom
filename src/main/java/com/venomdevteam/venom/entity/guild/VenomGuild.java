package com.venomdevteam.venom.entity.guild;

import com.venomdevteam.venom.entity.guild.settings.GuildSettings;
import com.venomdevteam.venom.entity.guild.tag.TagManager;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.entities.Guild;

public class VenomGuild {

    public static VenomGuild from(Venom venom, Guild guild) {
        return new VenomGuild(venom, guild);
    }

    private final Venom venom;

    private final Guild guild;

    private final GuildSettings settings;

    private TagManager tagManager;

    public VenomGuild(Venom venom, Guild guild) {

        this.venom = venom;

        this.guild = guild;
        this.settings = new GuildSettings(this);

        // this.tagManager = new TagManager(this);

    }

    public GuildSettings getSettings() {
        return settings;
    }

    public Guild getGuild() {
        return guild;
    }

    public TagManager getTagManager() {
        return tagManager;
    }

    public Venom getVenom() {
        return venom;
    }
}
