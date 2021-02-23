package com.venomdevteam.venom.modules.core.commands.modules;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.guild.VenomGuild;
import com.venomdevteam.venom.entity.member.VenomMember;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ModulesCommand extends Command {

    public ModulesCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "modules";
        this.aliases = new String[]{"ms", "modulelist"};
        this.description = "Shows list of all modules";
        this.child = new Command[]{

        };
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        Module[] modules = venom.getModules().values().toArray(new Module[0]);

        VenomMember venomMember = VenomMember.of(VenomGuild.from(venom, sender.getGuild()), sender);

        StringBuilder builder = new StringBuilder();

        builder.append("```prolog\n");

        int longest = 0;

        for (String name : venom.getModules().keySet()) {
            if (name.length() > longest) longest = name.length();
        }

        for (Module module : modules) {

            builder
                    .append(module.getName())
                    .append("".repeat(longest - module.getName().length()))
                    .append(" :: ")
                    .append(module.isEnabled(venomMember.getVenomGuild()) ? "Enabled" : "Disabled");

        }

        EmbedBuilder embed = venom.getBaseEmbed();

        embed.setDescription(builder.toString());

        channel.sendMessage(embed.build()).queue();

    }
}
