package com.venomdevteam.venom.modules.image;

import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.image.commands.*;

public class ImageModule extends Module {

    public ImageModule(Venom venom) {
        super(venom);

        this.name = "image";
        this.description = "Provides all image manipulation commands";
        this.enabled = true;
    }

    @Override
    public void setup() {
        // Registering commands

        this.venom.getCommandHandler().registerCommands(

                // Noises

                new PerlinNoiseCommand(this, this.venom),
                new GaussianNoiseCommand(this, this.venom),

                // Filters

                new SepiaCommand(this, this.venom),
                new MonochromeCommand(this, this.venom),

                // [*] My best friend

                new DogCommand(this, this.venom)
        );
        // Registering events

        venom.getLogger().info("Image module loaded!");
    }
}
