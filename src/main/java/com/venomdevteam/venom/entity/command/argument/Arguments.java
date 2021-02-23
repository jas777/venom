package com.venomdevteam.venom.entity.command.argument;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.command.argument.type.Argument;
import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;
import com.venomdevteam.venom.entity.command.argument.type.ClientArgument;
import com.venomdevteam.venom.entity.command.argument.type.DialogueArgument;
import com.venomdevteam.venom.entity.command.dialogue.DialogueCommand;
import com.venomdevteam.venom.entity.command.dialogue.DialogueStep;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.Optional;

public class Arguments {

    public static Arguments of(Command command, String[] args) {

        ArrayList<ParsedArgument> parsedArguments = new ArrayList<>();

        int a = 0;

        if (args.length < 1) return new Arguments(parsedArguments);

        for (int i = 0; i < args.length && a < command.getArguments().length;) {

            CommandArgument argument = command.getArguments()[a];

            Object argumentObjectType = argument.getType().getArgument();

            if (argumentObjectType instanceof Argument) {

                ArgumentType argumentType = argument.getType();
                Argument arg = (Argument) argumentType.getArgument();

                Optional result = arg.apply(args[i]);

                if (result.isPresent()) {
                    parsedArguments.add(new ParsedArgument(argument.getName(), result));
                    i++;
                    a++;
                }

                if (result.isEmpty() && argument.isOptional()) {
                    a++;
                }

            }

            if (argumentObjectType instanceof ClientArgument) {

                ArgumentType argumentType = argument.getType();
                ClientArgument arg = (ClientArgument) argumentType.getArgument();

                Optional result = arg.apply(command.getVenom(), args[i]);

                System.out.println(argument.getName());
                System.out.println(args[i]);

                if (result.isPresent()) {
                    parsedArguments.add(new ParsedArgument(argument.getName(), result));
                    i++;
                    a++;
                }

                if (result.isEmpty() && argument.isOptional()) {
                    a++;
                }

            }

        }

        return new Arguments(parsedArguments);

    }

    public static Arguments of(DialogueStep step, String args) {

        ArrayList<ParsedArgument> parsedArguments = new ArrayList<>();

        if (args.isEmpty() || args.isBlank()) return new Arguments(parsedArguments);

        CommandArgument argument = new CommandArgument("response", step.getResponseType(), false);

        Object argumentObjectType = argument.getType().getArgument();

        if (argumentObjectType instanceof Argument) {

            ArgumentType argumentType = argument.getType();
            Argument arg = (Argument) argumentType.getArgument();

            Optional result = arg.apply(args);

            if (result.isPresent()) {
                parsedArguments.add(new ParsedArgument(argument.getName(), result));
            }

        }

        if (argumentObjectType instanceof ClientArgument) {

            ArgumentType argumentType = argument.getType();
            ClientArgument arg = (ClientArgument) argumentType.getArgument();

            Optional result = arg.apply(step.getParent().getVenom(), args);

            System.out.println(argument.getName());
            System.out.println(args);

            if (result.isPresent()) {
                parsedArguments.add(new ParsedArgument(argument.getName(), result));
            }

        }

        if (argumentObjectType instanceof DialogueArgument) {

            ArgumentType argumentType = argument.getType();
            DialogueArgument arg = (DialogueArgument) argumentType.getArgument();

            Optional result = arg.apply(args);

        }

        return new Arguments(parsedArguments);

    }

    public static Arguments of(DialogueCommand command, DialogueStep[] steps, User user) {

        ArrayList<ParsedArgument> parsedArguments = new ArrayList<>();

        for (DialogueStep step : steps) {

            if (step.getResult(user) == null) break;

            CommandArgument argument = new CommandArgument("response", step.getResponseType(), false);

            Object argumentObjectType = argument.getType().getArgument();

            if (argumentObjectType instanceof Argument) {

                ArgumentType argumentType = argument.getType();
                Argument<?> arg = (Argument<?>) argumentType.getArgument();

                Optional<?> result = arg.apply(step.getResult(user));

                if (result.isPresent()) {
                    parsedArguments.add(new ParsedArgument(argument.getName(), result));
                }

            }

            if (argumentObjectType instanceof ClientArgument) {

                ArgumentType argumentType = argument.getType();
                ClientArgument<?> arg = (ClientArgument<?>) argumentType.getArgument();

                Optional result = arg.apply(step.getParent().getVenom(), step.getResult(user));

                System.out.println(argument.getName());
                System.out.println(step.getResult(user));

                if (result.isPresent()) {
                    parsedArguments.add(new ParsedArgument(argument.getName(), result));
                }

            }

            if (argumentObjectType instanceof DialogueArgument) {

                ArgumentType argumentType = argument.getType();
                DialogueArgument<?> arg = (DialogueArgument<?>) argumentType.getArgument();

                Optional<?> result = arg.apply(step.getResult(user));

            }
        }

        return new Arguments(parsedArguments);

    }

    private final ArrayList<ParsedArgument> arguments;

    private int index = -1;

    public Arguments(ArrayList<ParsedArgument> arguments) {
        this.arguments = arguments;
    }

    public int size() {
        return arguments.size();
    }

    public ArrayList<ParsedArgument> getAll() {
        return arguments;
    }

    public ParsedArgument get(int index) {
        return arguments.get(index);
    }

    public ParsedArgument next() {
        return arguments.get(++index);
    }


}
