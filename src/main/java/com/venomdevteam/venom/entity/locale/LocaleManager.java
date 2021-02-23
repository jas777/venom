package com.venomdevteam.venom.entity.locale;

import com.venomdevteam.venom.main.Venom;
import gnu.trove.map.hash.THashMap;

public class LocaleManager {

    private final THashMap<String, Locale> locales = new THashMap<>();

    private final Venom venom;

    public LocaleManager(Venom venom) {
        this.venom = venom;
    }

    public void registerLocales(Locale... locales) {
        for (Locale l : locales) {
            this.locales.put(l.getCountryCode().toLocale().getISO3Language(), l);

            this.venom.getLogger().info("Registered locale: " + l.getName() + " (" + l.getCountryCode().toLocale().getISO3Language() + ")");
        }
    }

}
