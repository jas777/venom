package com.venomdevteam.venom.modules.tag;

import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.tag.commands.TagCommand;

public class TagModule extends Module {

    public TagModule(Venom venom) {
        super(venom);

        this.name = "tags";
        this.description = "Contains all commands responsible for tags";
        this.enabled = true;
    }

    @Override
    public void setup() {

        venom.getCommandHandler().registerCommands(
                new TagCommand(this, this.venom)
        );

    }
}
