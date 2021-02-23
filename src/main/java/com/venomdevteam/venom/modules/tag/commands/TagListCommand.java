package com.venomdevteam.venom.modules.tag.commands;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.guild.VenomGuild;
import com.venomdevteam.venom.entity.guild.tag.GuildTag;
import com.venomdevteam.venom.entity.member.VenomMember;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class TagListCommand extends Command {

    public TagListCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "list";
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        StringBuilder sb = new StringBuilder();

        VenomMember venomMember = VenomMember.of(VenomGuild.from(venom, sender.getGuild()), sender);

        for (GuildTag tag : venomMember.getVenomGuild().getTagManager().getTags()) {
            sb
                    .append("`")
                    .append(tag.getName())
                    .append("`, ");
        }

        channel.sendMessage("**Tags in this guild:** " + sb.toString()).queue();

    }
}
