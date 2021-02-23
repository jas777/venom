package com.venomdevteam.venom.util.checks;

public class Checks {

    public static boolean isDigit(String value) {
        return value.codePoints().allMatch(Character::isDigit);
    }

}
