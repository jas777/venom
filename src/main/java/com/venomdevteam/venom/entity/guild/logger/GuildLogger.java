package com.venomdevteam.venom.entity.guild.logger;

import com.venomdevteam.venom.entity.guild.VenomGuild;
import net.dv8tion.jda.api.entities.TextChannel;

public class GuildLogger {

    private final VenomGuild guild;

    private TextChannel logChannel;

    public GuildLogger(VenomGuild guild, TextChannel logChannel) {

        this.guild = guild;
        this.logChannel = logChannel;

    }

    public VenomGuild getGuild() {
        return guild;
    }

    public void setLogChannel(TextChannel logChannel) {
        this.logChannel = logChannel;
    }

    public TextChannel getLogChannel() {
        return logChannel;
    }
}
