package com.venomdevteam.venom.modules.image.commands;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;
import com.venomdevteam.venom.entity.command.argument.Arguments;
import com.venomdevteam.venom.entity.command.argument.CommandArgument;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.util.file.FileUtil;
import com.venomdevteam.venom.util.image.filter.Monochrome;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MonochromeCommand extends Command {

    public MonochromeCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "monochrome";
        this.aliases = new String[]{"blackwhite", "bw"};
        this.description = "Applies monochrome (black/white) filter to given image (url or attachment)";
        this.cooldown = 20;
        this.arguments = new CommandArgument[]{
                new CommandArgument("url", ArgumentType.URL, true)
        };
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        Arguments arguments = Arguments.of(this, args);

        if (arguments.size() < 1 && message.getAttachments().isEmpty()) {

            channel.sendMessage("Please provide image URL or attach the image to message!")
                    .queue();

            return;

        }

        if (message.getAttachments().isEmpty()) {

            if (arguments.size() < 1) {

                channel.sendMessage("Please provide image URL or attach the image to message!")
                        .queue();

                return;

            }

            ByteArrayOutputStream outputStream = FileUtil.download((URL) arguments.next().getArgument().get());

            if (outputStream.toByteArray().length >= 8388608) {

                channel.sendMessage("File cannot be bigger than 8MB!")
                        .queue();

                return;

            }

            try {

                BufferedImage original = ImageIO.read(new ByteArrayInputStream(outputStream.toByteArray()));

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                ImageIO.write(Monochrome.apply(original), "png", bytes);

                bytes.flush();

                if (bytes.toByteArray().length > 8388608) {

                    channel.sendMessage("Processed file exceeded 8MB limit!")
                            .queue();

                } else {

                    channel.sendMessage("Here's your processed image!")
                            .addFile(bytes.toByteArray(), "monochrome.png")
                            .queue();

                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        } else {

            if (!message.getAttachments().get(0).isImage()) {

                channel.sendMessage("Please provide image URL or attach the image to message!")
                        .queue();

                return;

            }

            try {

                BufferedImage original = ImageIO.read(message.getAttachments().get(0).retrieveInputStream().get());

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                ImageIO.write(Monochrome.apply(original), "png", bytes);

                bytes.flush();

                if (bytes.toByteArray().length > 8388608) {

                    channel.sendMessage("Processed file exceeded 8MB limit!")
                            .queue();

                    return;

                } else {

                    channel.sendMessage("Here's your processed image!")
                            .addFile(bytes.toByteArray(), "monochrome.png")
                            .queue();

                }
            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }

    }

}
