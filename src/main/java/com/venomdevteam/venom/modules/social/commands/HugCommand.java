package com.venomdevteam.venom.modules.social.commands;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class HugCommand extends Command {

    public HugCommand(Module module, Venom venom) {
        super(module, venom);
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {

        Member hugged = message.getMentionedMembers().get(0);

        if (hugged == null) {
            channel.sendMessage("<:checkno:505023190972497941> Invalid member!").queue();
        }

    }

}
