package com.venomdevteam.venom.entity.command;

import net.dv8tion.jda.api.entities.User;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Cooldown {

    private final Command command;

    private final User user;

    private final Date expires;

    /**
     * @param user     - User that needs to cool down
     * @param command  - Command that the user has used
     * @param cooldown - Amount of seconds that user needs to wait
     */
    public Cooldown(User user, Command command, int cooldown) {

        this.command = command;

        this.user = user;

        this.expires = new Date(new Date().getTime() + TimeUnit.SECONDS.toMillis(cooldown));

    }

    public boolean isExpired() {
        return new Date().after(expires);
    }

    public int getSecondsLeft() {
        return (int) TimeUnit.MILLISECONDS.toSeconds(expires.getTime() - new Date().getTime());
    }

    public Command getCommand() {
        return command;
    }

    public User getUser() {
        return user;
    }
}
