package com.venomdevteam.venom.modules.math;

import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.math.commands.CalculateCommand;

public class MathModule extends Module {

    public MathModule(Venom venom) {
        super(venom);

        this.name = "math";
        this.description = "Provides commands that... can calculate things";
        this.enabled = true;
    }

    @Override
    public void setup() {

        venom.getCommandHandler().registerCommands(
                new CalculateCommand(this, this.venom)
        );

    }
}
