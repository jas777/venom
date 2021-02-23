package com.venomdevteam.venom.modules.core;

import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.core.commands.*;
import com.venomdevteam.venom.modules.core.commands.help.HelpCommand;

public class CoreModule extends Module {

    public CoreModule(Venom venom) {
        super(venom);

        this.name = "core";
        this.description = "Contains all basic commands, cannot be disabled.";
        this.enabled = true;
    }

    @Override
    public void setup() {
        this.venom.getCommandHandler().registerCommands(
                new PingCommand(this, this.venom),
                new HelpCommand(this, this.venom),
                new UserinfoCommand(this, this.venom),
                new TestCommand(this, this.venom),

                new EvalCommand(this, this.venom),
                new TestDialogueCommand(this, this.venom)
        );
        venom.getLogger().info("Core module loaded!");

        this.venom.getClient().addEventListener();
    }
}
