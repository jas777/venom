package com.venomdevteam.venom.modules.core.commands;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class PingCommand extends Command {

    public PingCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "ping";
        this.aliases = new String[]{"pong"};
        this.description = "Pong!";
        // this.usage = "ping";
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {
        EmbedBuilder builder = this.venom.getBaseEmbed()
                .setDescription("Pong! " + Math.round(this.venom.getClient().getGatewayPing()) + "ms");

        channel.sendMessage(builder.build()).queue();
    }
}
