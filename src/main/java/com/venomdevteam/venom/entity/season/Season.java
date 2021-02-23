package com.venomdevteam.venom.entity.season;

import com.venomdevteam.venom.entity.type.Toggleable;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

public abstract class Season implements Toggleable {

    protected String name;

    protected boolean enabled;

    protected Calendar startDate;
    protected Calendar endDate;

    public Season() {

    }

    public String getName() {
        return name;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public Duration getUntilStart() {

        return Duration.between(new Date().toInstant(), getStartDate().toInstant());

    }

    public Duration getUntilEnd() {

        return Duration.between(new Date().toInstant(), getEndDate().toInstant());

    }

    public boolean isActive() {
        return (new Date().after(startDate.getTime()) && getUntilEnd().toDays() >= 0);
    }

    @Override
    public void enable() {
        this.enabled = true;
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public void toggle() {
        this.enabled = !this.enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
