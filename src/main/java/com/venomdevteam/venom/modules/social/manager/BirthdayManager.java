package com.venomdevteam.venom.modules.social.manager;

import com.venomdevteam.venom.entity.birthday.Birthday;
import com.venomdevteam.venom.main.Venom;
import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.api.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

public class BirthdayManager {

    private final Venom venom;

    private final THashMap<User, Birthday> birthdays = new THashMap<>();

    public BirthdayManager(Venom venom) {
        this.venom = venom;
    }

    public void addBirthday(User user, Calendar date) {

        Connection conn = this.venom.getDatabase().getConnection();
        Birthday birthday = new Birthday(user, date);

        String stmt = "INSERT INTO birthday VALUES (?, ?, ?)";

        try {
            PreparedStatement prepared = conn.prepareStatement(stmt);
            prepared.setLong(1, user.getIdLong());
            prepared.setDate(2, new java.sql.Date(date.getTimeInMillis()));
            prepared.setString(3, date.getTimeZone().getDisplayName());
            prepared.execute();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        birthdays.put(user, new Birthday(user, date));
    }

    public THashMap<User, Birthday> getBirthdays() {
        return birthdays;
    }
}
