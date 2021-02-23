package com.venomdevteam.venom.modules.image.commands;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.command.argument.Arguments;
import com.venomdevteam.venom.entity.command.argument.CommandArgument;
import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.util.image.noise.Perlin;
import com.venomdevteam.venom.util.image.noise.PerlinFlowField;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PerlinNoiseCommand extends Command {

    public PerlinNoiseCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "perlin";
        this.aliases = new String[]{"perlinnoise", "pnoise"};
        this.description = "Generates image of the Perlin noise";
        this.botPermissions = new Permission[]{Permission.MESSAGE_ATTACH_FILES};
        this.usage = "perlin <frequency>";
        this.arguments = new CommandArgument[]{
                new CommandArgument("type", ArgumentType.STRING, false),
                new CommandArgument("option", ArgumentType.NUMBER, false)
        };
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        Arguments arguments = Arguments.of(this, args);

        if (((String) arguments.next().getArgument().get()).equalsIgnoreCase("noise")) {
            if (arguments.get(1).getArgument().isEmpty() || ((Integer) arguments.get(1).getArgument().get()) < 1) {
                channel.sendMessage("Please specify **frequency value**!").queue();
            } else {
                int frequency;

                try {
                    frequency = (Integer) arguments.get(1).getArgument().get();
                } catch (Exception e) {
                    channel.sendMessage("Frequency value must be a number!").queue();
                    return;
                }

                channel.sendMessage("Generating image...").queue(message1 -> {
                    Perlin noise = new Perlin(1024, 1024, frequency);

                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        ImageIO.write(noise.getNoiseImage(), "png", bytes);
                        bytes.flush();

                        channel.sendFile(bytes.toByteArray(), "perlin.png").queue(msg -> message1.delete().queue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } else if (((String) arguments.get(0).getArgument().get()).equalsIgnoreCase("flow")) {

            Runnable runnable = () -> {
                int option = (Integer) arguments.get(1).getArgument().get();

                channel.sendMessage("Generating image...").queue(message1 -> {
                    PerlinFlowField flowField = new PerlinFlowField(1024, 1024, option);

                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        ImageIO.write(flowField.getNoiseImage(), "png", bytes);
                        bytes.flush();

                        channel.sendFile(bytes.toByteArray(), "perlin.png").queue(msg -> {
                            message1.delete().queue();
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            };

            Thread t = new Thread(runnable, "noisegen");
            t.start();

        }


    }
}
