package com.venomdevteam.venom.entity.command.argument;

import java.util.Optional;

public class ParsedArgument {

    private final String name;

    private final Optional<?> argument;

    public ParsedArgument(String name, Optional<?> argument) {

        this.name = name;
        this.argument = argument;

    }

    public String getName() {
        return name;
    }

    public Optional<?> getArgument() {
        return argument;
    }

}
