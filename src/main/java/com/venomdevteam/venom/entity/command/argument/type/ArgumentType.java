package com.venomdevteam.venom.entity.command.argument.type;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.module.Module;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public enum ArgumentType {

    STRING((Argument<String>) value -> {
        if (value == null) {
            return Optional.empty();
        } else {
            if (value.equals("") || value.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(value);
        }
    }),

    NUMBER((Argument<Integer>) value -> value.codePoints()
            .allMatch(Character::isDigit) ? Optional.of(Integer.parseInt(value)) : Optional.empty()),

    DOUBLE((Argument<Double>) value -> Optional.of(Double.parseDouble(value))),

    BOOLEAN((Argument<Boolean>) value -> Optional.of(Boolean.parseBoolean(value))),

    COMMAND((ClientArgument<Command>) (venom, value) -> venom.getCommandHandler()
            .getCommand(value, false) == null ? Optional.empty() : Optional.of(venom.getCommandHandler()
            .getCommand(value, false))),

    MODULE((ClientArgument<Module>) (venom, value) -> venom.getModules().containsKey(value) ?
            Optional.of(venom.getModules().get(value)) : Optional.empty()),

    URL((Argument<URL>) value -> {
        try {
            URL url = new URL(value);
            return Optional.of(url);
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }),

    MODULE_OR_COMMAND((Argument<String>) s -> {
        if (s.equalsIgnoreCase("module") || s.equalsIgnoreCase("command")) {
            return Optional.of(s);
        }
        return Optional.empty();
    });

    // TODO: DATE((Argument<Date>) value -> );

    private final Object argument;

    ArgumentType(Argument<?> argument) {
        this.argument = argument;
    }

    ArgumentType(ClientArgument<?> argument) {
        this.argument = argument;
    }

    ArgumentType(DialogueArgument<?> argument) {
        this.argument = argument;
    }

    public Object getArgument() {
        return argument;
    }

}