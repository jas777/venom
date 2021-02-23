package com.venomdevteam.venom.entity.command.argument;

import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;

public class CommandArgument {

    private final String name;

    private final ArgumentType type;

    private final boolean optional;

    public CommandArgument(String name, ArgumentType type, boolean optional) {
        this.name = name;
        this.type = type;
        this.optional = optional;
    }

    public String getName() {
        return this.name;
    }

    public ArgumentType getType() {
        return this.type;
    }

    public boolean isOptional() {
        return this.optional;
    }

}