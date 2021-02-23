package com.venomdevteam.venom.modules.core.commands;

import com.google.api.services.youtube.model.SearchResult;
import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;
import com.venomdevteam.venom.entity.command.argument.Arguments;
import com.venomdevteam.venom.entity.command.argument.CommandArgument;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.util.google.GoogleAPI;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class TestCommand extends Command {

    public TestCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "test";
        this.arguments = new CommandArgument[]{
                new CommandArgument("query", ArgumentType.STRING, true)
        };
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        Arguments arguments = Arguments.of(this, args);

        GoogleAPI googleAPI = new GoogleAPI(venom);

        if(arguments.size() < 1) {
            return;
        }

        String query = (String) arguments.next().getArgument().get();

        List<SearchResult> searchResults = googleAPI.getYoutube().search(query);

        for (SearchResult result : searchResults) {
            System.out.println(result.getSnippet().getTitle() + " | " + result.getSnippet().getChannelTitle());
        }

    }
}
