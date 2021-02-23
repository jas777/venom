package com.venomdevteam.venom.entity.season.manager;

import com.venomdevteam.venom.entity.season.Season;
import com.venomdevteam.venom.main.Venom;
import gnu.trove.map.hash.THashMap;

import java.util.List;

public class SeasonManager {

    private Venom venom;

    private final THashMap<String, Season> seasons;

    public SeasonManager(Venom venom) {

        this.venom = venom;
        this.seasons = new THashMap<>();

    }

    public SeasonManager(List<Season> seasons) {

        this.seasons = new THashMap<>();

        seasons.forEach(season -> this.seasons.put(season.getName(), season));

    }

    public void registerSeason(Season season) {
        seasons.put(season.getName(), season);
    }

    public void registerSeasons(Season... seasons) {
        for (Season season : seasons) {
            this.seasons.put(season.getName(), season);

            if (season.getUntilStart().toDays() <= 5 && season.getUntilStart().toDays() >= 0) {
                venom.getLogger().warn(season.getUntilStart().toDays() + " day(s) left until " +
                        season.getName() + " season will start!");
            }
        }
    }

    public Season getSeason(String seasonName) {
        return seasons.get(seasonName);
    }

    public void removeSeason(String seasonName) {
        seasons.remove(seasonName);
    }

    public Season getCurrentSeason() {
        return null;
    }


}
