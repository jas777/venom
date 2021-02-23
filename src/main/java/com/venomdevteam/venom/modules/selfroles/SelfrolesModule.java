package com.venomdevteam.venom.modules.selfroles;

import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.entity.module.Module;

public class SelfrolesModule extends Module {

    public SelfrolesModule(Venom venom) {
        super(venom);

        this.name = "selfroles";
        this.description = "Allows to use self-assignable roles";
        this.enabled = true;
    }

    @Override
    public void setup() {

        venom.getLogger().info("Selfroles module loaded!");
    }
}
