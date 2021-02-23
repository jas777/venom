package com.venomdevteam.venom.entity.birthday;

import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class Birthday implements Comparable<Birthday> {

    private final User user;

    private Calendar date;

    public Birthday(User user, Calendar date) {
        this.user = user;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int daysLeft() {

        Calendar currCalendar = Calendar.getInstance(this.date.getTimeZone());

        int left = Calendar.DAY_OF_YEAR - Calendar.DAY_OF_YEAR;

        return left;

    }

    @Override
    public int compareTo(@NotNull Birthday o) {
        if (this.daysLeft() > o.daysLeft()) return 1;
        if (this.daysLeft() == o.daysLeft()) return 0;
        if (this.daysLeft() < o.daysLeft()) return -1;
        else return 0;
    }

    @Override
    public String toString() {

        return this.user.getName() + "#" + this.user.getDiscriminator() + " - " + this.date.getTime().toString() + " (" + this.date.getTimeZone().getID() + ")";
    }


}
