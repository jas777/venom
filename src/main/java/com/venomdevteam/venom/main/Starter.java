package com.venomdevteam.venom.main;

public class Starter {

    // TODO: Move birthday module to social module

    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            new Venom(false);
        } else if (args[0].equalsIgnoreCase("debug")) {
            new Venom(true);
        }

    }

}
