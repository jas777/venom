package com.venomdevteam.venom.modules.image.commands;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;
import com.venomdevteam.venom.entity.command.argument.CommandArgument;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.util.image.noise.Gaussian;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class GaussianNoiseCommand extends Command {

    public GaussianNoiseCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "gaussian";
        this.aliases = new String[]{"gaussiannoise", "gnoise"};
        this.description = "Adds Gaussian noise to given image";
        this.botPermissions = new Permission[]{Permission.MESSAGE_ATTACH_FILES};

        // OPTIMIZE: Remove usage field and completely move to argument parsing system

        this.arguments = new CommandArgument[]{
                new CommandArgument("mean", ArgumentType.NUMBER, false),
                new CommandArgument("sigma", ArgumentType.NUMBER, false),
                // TODO: Add image/attachment argument type
                new CommandArgument("image", null, false)
        };
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        if (args.length < 2) {
            channel.sendMessage("Please specify **mean and sigma values**!").queue();
        } else {

            double mean = 0;
            double sigma = 0;

            try {
                mean = Double.parseDouble(args[0]);
                sigma = Double.parseDouble(args[1]);
            } catch (Exception e) {
                channel.sendMessage("Mean and sigma values need to be numbers!").queue();
                return;
            }

            Gaussian gaussian = new Gaussian(mean, sigma);
            BufferedImage image = null;

            if (args.length > 2 && args[2] != null) {
                try {
                    URL imageURL = new URL(args[2]);
                    image = ImageIO.read(imageURL);
                } catch (MalformedURLException e) {
                    channel.sendMessage("Invalid URL provided!").queue();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (message.getAttachments().size() < 1) {
                    channel.sendMessage("No image provided!").queue();
                    return;
                } else {
                    if (message.getAttachments().get(0).isImage()) {
                        try {
                            image = ImageIO.read(message.getAttachments().get(0).retrieveInputStream().get());
                        } catch (IOException | InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else {
                        channel.sendMessage("No image provided!").queue();
                        return;
                    }
                }
            }

            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                ImageIO.write(gaussian.processImage(Objects.requireNonNull(image)), "png", bytes);
                bytes.flush();

                channel.sendFile(bytes.toByteArray(), "gaussian.png").queue();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
