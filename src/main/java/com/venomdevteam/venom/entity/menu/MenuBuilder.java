package com.venomdevteam.venom.entity.menu;

import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.function.Function;

public class MenuBuilder extends EmbedBuilder {

    private final JDA client;

    private THashMap<String, Function<Member, Void>> emotes;

    public MenuBuilder(JDA client) {
        this.client = client;
    }

    public MenuBuilder addEmote(@NotNull String emote, @NotNull Function<Member, Void> handler) {

        if (client.getEmoteById(emote) == null && client.getEmotesByName(emote, false).size() <= 0) throw new InvalidParameterException("Invalid emote!");

        emotes.put(emote, handler);

        return this;

    }

    public THashMap<String, Function<Member, Void>> getEmotes() {
        return emotes;
    }
}
