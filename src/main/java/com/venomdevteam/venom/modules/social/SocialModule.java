package com.venomdevteam.venom.modules.social;

import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.social.commands.BirthdayCommand;
import com.venomdevteam.venom.modules.social.events.ReadyEvent;
import com.venomdevteam.venom.modules.social.manager.BirthdayManager;

public class SocialModule extends Module {

    /**
     * Dedicated to Aliyss von Schnee
     * <p>
     * Copyright (c) 2019 - Vilmano Team
     */

    private BirthdayManager manager;

    public SocialModule(Venom venom) {
        super(venom);

        this.name = "social";
        this.description = "Contains all social commands such as hug, profile etc.";
        this.enabled = true;
    }

    @Override
    public void setup() {

        this.manager = new BirthdayManager(this.venom);

        this.venom.getCommandHandler().registerCommands(
                new BirthdayCommand(this, this.venom)
        );

        this.venom.getClient().addEventListener(
                new ReadyEvent(this.venom, this)
        );

    }

    public BirthdayManager getManager() {
        return manager;
    }
}
