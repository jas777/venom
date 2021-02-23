package com.venomdevteam.venom.entity.user.experience;

import com.venomdevteam.venom.entity.user.VenomUser;
import com.venomdevteam.venom.main.Venom;

public class GlobalExperience {

    private final Venom venom;

    private final VenomUser user;

    private int xp;
    private int level;

    public GlobalExperience(Venom venom, VenomUser user) {

        this.venom = venom;

        this.user = user;

    }

    public VenomUser getUser() {
        return user;
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }
}
