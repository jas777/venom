package com.venomdevteam.venom.modules.social.events;

import com.venomdevteam.venom.entity.birthday.Birthday;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.social.SocialModule;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

public class ReadyEvent extends ListenerAdapter {

    private final Venom venom;
    private final SocialModule module;

    public ReadyEvent(Venom venom, SocialModule module) {
        this.venom = venom;
        this.module = module;
    }

    @Override
    public void onReady(net.dv8tion.jda.api.events.ReadyEvent event) {
        Connection conn = this.venom.getDatabase().getConnection();

        try {
            ResultSet result = conn.prepareStatement("SELECT * FROM birthday").executeQuery();

            while (result.next()) {
                User user = this.venom.getClient().getUserById(result.getLong("id"));
                TimeZone tz = TimeZone.getTimeZone(result.getString("timezone"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(result.getDate("date"));
                calendar.setTimeZone(tz);
                this.module.getManager().getBirthdays().put(user, new Birthday(user, calendar));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
