package com.venomdevteam.venom.modules.social.commands;

import com.venomdevteam.venom.entity.birthday.Birthday;
import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.social.SocialModule;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;

public class BirthdayCommand extends Command {

    public BirthdayCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "birthday";
        this.aliases = new String[]{"bday"};
        this.description = "Set your & see others birthday dates!";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.usage = "birthday [set|remove]";
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        EmbedBuilder embed = this.venom.getBaseEmbed();

        SocialModule socialModule = (SocialModule) this.module;

        ArrayList<String> birthdays = new ArrayList<>();

        for (Birthday bday : socialModule.getManager().getBirthdays().values()) {
            birthdays.add(bday.toString());
        }

        StringBuilder sb = new StringBuilder();

        sb
                .append("```\n")
                .append(String.join("\n", birthdays))
                .append("```");

        embed.setDescription(sb);
        channel.sendMessage(embed.build()).queue();

    }
}
