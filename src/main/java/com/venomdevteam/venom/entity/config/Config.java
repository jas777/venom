package com.venomdevteam.venom.entity.config;

import com.moandjiezana.toml.Toml;
import com.venomdevteam.venom.main.Venom;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Config {

    private String path;

    private final Venom venom;

    public Config(Venom venom) {
        this.venom = venom;
        try {
            this.path = Paths.get(this.venom.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }

    public Toml getConfig() {
        File configFile = new File(this.path + "/config.toml");

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                venom.getLogger().error("Config file doesn't exist in config path!");
            }
        }

        return new Toml().read(configFile);
    }

}
