package com.venomdevteam.venom.entity.type;

import com.venomdevteam.venom.entity.guild.VenomGuild;

public interface GuildToggleable {

    boolean isEnabled(VenomGuild guild);

    void toggle(VenomGuild guild);

    void enable(VenomGuild guild);

    void disable(VenomGuild guild);

}
