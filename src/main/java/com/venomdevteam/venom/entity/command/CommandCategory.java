package com.venomdevteam.venom.entity.command;

import java.util.Objects;

public class CommandCategory {

    private final String name;

    public CommandCategory(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CommandCategory))
            return false;
        CommandCategory other = (CommandCategory) obj;
        return Objects.equals(name, other.name);
    }
}
