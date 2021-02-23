package com.venomdevteam.venom.seasons;

import com.venomdevteam.venom.entity.season.Season;

import java.util.Calendar;

public class TestSeason extends Season {

    public TestSeason() {
        this.name = "test";

        Calendar startDate = Calendar.getInstance();

        startDate.set(Calendar.DAY_OF_MONTH, 21);
        startDate.set(Calendar.MONTH, Calendar.MARCH);

        this.startDate = startDate;

        Calendar endDate = Calendar.getInstance();

        endDate.set(Calendar.DAY_OF_MONTH, 23);
        endDate.set(Calendar.MONTH, Calendar.MARCH);

        this.endDate = endDate;
    }

}
