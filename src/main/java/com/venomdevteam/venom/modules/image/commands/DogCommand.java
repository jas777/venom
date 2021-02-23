package com.venomdevteam.venom.modules.image.commands;

import com.google.gson.Gson;
import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DogCommand extends Command {

    public DogCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "dog";
        this.aliases = new String[]{"dogpic", "doggo"};
        this.description = "Sends a random dog pic";
        this.cooldown = 10;
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://dog.ceo/api/breeds/image/random"))
                .build();

        try {

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            ResponseBody body = new Gson().fromJson(response.body(), ResponseBody.class);

            EmbedBuilder builder = venom.getBaseEmbed()
                    .setImage(body.message)
                    .setTitle("Woof!");

            channel.sendMessage(builder.build()).queue();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static class ResponseBody {
        public String message;
    }

}
