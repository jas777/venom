package com.venomdevteam.venom.entity.guild.settings;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.guild.VenomGuild;
import com.venomdevteam.venom.entity.locale.Locale;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class GuildSettings {

    private final Venom venom;

    private final VenomGuild guild;

    private Locale locale;

    private final ArrayList<Command> disabledCommands;
    private final ArrayList<Module> disabledModules;

    public GuildSettings(VenomGuild guild) {

        this.guild = guild;
        this.venom = guild.getVenom();

        this.disabledCommands = new ArrayList<>();
        this.disabledModules = new ArrayList<>();

        Connection connection = guild.getVenom().getDatabase().getConnection();

        String[] disabledCommands = new String[]{};

        try {

            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM " + guild.getVenom().getDatabase().GUILD_DATA_TABLE +
                            " WHERE id = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            statement.setString(1, guild.getGuild().getId());

            ResultSet result = statement.executeQuery();

            if (!result.next()) {

                statement = connection
                        .prepareStatement("INSERT INTO " + venom.getDatabase().GUILD_DATA_TABLE + " VALUES" +
                                "(?, ?);", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                statement.setString(1, guild.getGuild().getId());
                statement.setArray(2, connection.createArrayOf("TEXT", new String[]{}));

                this.locale = null;

            } else {

                disabledCommands = (String[]) result.getArray("disabled_commands").getArray();

                this.locale = null;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Arrays.asList(disabledCommands).forEach(name -> {
            Command command = venom.getCommandHandler().getCommand(name, false);
            this.disabledCommands.add(command);
        });

    }

    public Locale getLocale() {
        return locale;
    }

    public ArrayList<Command> getDisabledCommands() {
        return disabledCommands;
    }

    public ArrayList<Module> getDisabledModules() {
        return disabledModules;
    }

    public boolean isDisabled(Command command) {
        return disabledCommands.contains(command);
    }

    public boolean isDisabled(Module module) {
        return disabledModules.contains(module);
    }

    public void disableCommand(Command command) {
        disabledCommands.add(command);
    }

    public void enableCommand(Command command) {
        if (isDisabled(command)) {
            disabledCommands.remove(command);
        }
    }

    public void disableModule(Module module) {
        disabledModules.add(module);
    }

    public void enableModule(Module module) {
        if (isDisabled(module)) {
            disabledModules.remove(module);
        }
    }

    public void save() {

        Connection connection = venom.getDatabase().getConnection();

        try {

            PreparedStatement statement = connection
                    .prepareStatement("UPDATE " + venom.getDatabase().GUILD_DATA_TABLE
                            + " SET disabled_commands = ? WHERE id = ?");

            statement.setArray(1, connection.createArrayOf("TEXT", disabledCommands.stream()
                    .map(Command::getName).toArray()));

            statement.setString(2, guild.getGuild().getId());

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public VenomGuild getGuild() {
        return guild;
    }
}
