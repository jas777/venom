package com.venomdevteam.venom.entity.command.argument.type;

import com.venomdevteam.venom.main.Venom;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Argument that requires client object
 */
public interface ClientArgument<T> extends BiFunction<Venom, String, Optional<T>> {

    /**
     *
     * @param venom - Client object
     * @param s - String (from message)
     * @return Parsed argument
     */
    @Override
    Optional<T> apply(Venom venom, String s);

}
