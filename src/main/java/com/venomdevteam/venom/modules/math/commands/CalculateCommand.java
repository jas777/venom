package com.venomdevteam.venom.modules.math.commands;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;
import com.venomdevteam.venom.entity.command.argument.CommandArgument;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.util.math.MathUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CalculateCommand extends Command {

    public CalculateCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "calculate";
        this.aliases = new String[]{"calc", "calculate"};
        this.description = "Returns result of given expression";
        this.arguments = new CommandArgument[]{
                new CommandArgument("to calculate", ArgumentType.STRING, false)
        };
        this.cooldown = 5;
    }

    // URGENT: Add max number check for fibonacci

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        String toEval =  String.join(" ", args);

        try {

            double result = MathUtil.eval(toEval);

            EmbedBuilder embed = venom.getBaseEmbed();

            embed
                    .addField("Input", "```js\n" + toEval + "\n```", false)
                    .addField("Output", "```js\n" + result + "\n```", false);

            channel.sendMessage(embed.build()).queue();

        } catch (Exception e) {
            channel.sendMessage("Invalid expression!").queue();
        }



    }
}
