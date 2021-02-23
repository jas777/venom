package com.venomdevteam.venom.entity.locale;

import com.neovisionaries.i18n.CountryCode;

public class Locale {

    protected String name;

    protected CountryCode countryCode;

    // Global messages

    public static String ERROR_GENERAL_SERVERSIDE;

    // Argument errors

    public static String ERROR_ARG_MUST_BE_MEMBER;
    public static String ERROR_ARG_MUST_BE_NUMBER;
    public static String ERROR_ARG_MUST_BE_STRING;

    public Locale() {

    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
