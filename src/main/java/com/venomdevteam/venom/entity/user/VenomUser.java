package com.venomdevteam.venom.entity.user;

import com.venomdevteam.venom.entity.user.experience.GlobalExperience;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.entities.User;

public class VenomUser {

    private final User user;

    private final Venom venom;

    private final GlobalExperience experience;

    public VenomUser(Venom venom, User user) {

        this.user = user;

        this.venom = venom;

        this.experience = new GlobalExperience(venom, this);

    }

    public User getJDAUser() {
        return user;
    }

    public GlobalExperience getExperience() {
        return experience;
    }
}
