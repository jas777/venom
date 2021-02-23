package com.venomdevteam.venom.entity.command.argument.type;

import java.util.Optional;
import java.util.function.Function;

public interface Argument<T> extends Function<String, Optional<T>> {

    @Override
    Optional<T> apply(String s);

}
