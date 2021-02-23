package com.venomdevteam.venom.modules.core.commands;

import com.venomdevteam.venom.entity.command.argument.Arguments;
import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;
import com.venomdevteam.venom.entity.command.dialogue.DialogueCommand;
import com.venomdevteam.venom.entity.command.dialogue.DialogueStep;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jsoup.internal.StringUtil;

import java.util.stream.Collectors;

public class TestDialogueCommand extends DialogueCommand {

    public TestDialogueCommand(Module module, Venom venom) {
        super(module, venom);

        this.name = "testd";
        this.dialogue = new DialogueStep[]{
                new DialogueStep(this, "Does this work?", ArgumentType.BOOLEAN, false),
                new DialogueStep(this, "Are you reaaaaally sure??", ArgumentType.BOOLEAN, true),
                new DialogueStep(this, "Were you wrong?", ArgumentType.BOOLEAN, false)
        };
    }

    @Override
    public void finalize(Member sender, TextChannel channel, DialogueStep[] args, Message message) {

        Arguments arguments = Arguments.of(this, args, sender.getUser());

        channel.sendMessage(StringUtil.join(arguments.getAll().stream().map(a -> a.getArgument().get().toString()).collect(Collectors.toList()), " ")).queue();

    }

}
