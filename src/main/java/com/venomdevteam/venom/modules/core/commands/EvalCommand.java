package com.venomdevteam.venom.modules.core.commands;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.commons.jexl3.*;

import java.awt.*;

public class EvalCommand extends Command {

    public EvalCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "eval";
        this.ownerOnly = true;
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        EmbedBuilder embed = venom.getBaseEmbed();

        embed
                .setDescription("Compiling...");

        channel.sendMessage(embed.build()).queue(message1 -> {

            JexlEngine jexl = new JexlBuilder().create();

            JexlContext context = new MapContext();

            context.set("venom", venom);
            context.set("sender", sender);
            context.set("channel", channel);
            context.set("args", args);
            context.set("message", message);

            String expression = String.join(" ", args);

            try {
                JexlExpression jexlExpression = jexl.createExpression(expression);

                Object result = jexlExpression.evaluate(context);

                embed
                        .setTitle("Result")
                        .setColor(Color.GREEN)
                        .setDescription(result.toString());

                message1.editMessage(embed.build()).queue();

            } catch (JexlException e) {

                embed
                        .setTitle("Error!")
                        .setColor(Color.RED)
                        .setDescription(e.getMessage());

                message1.editMessage(embed.build()).queue();

            }

        });

    }
}
