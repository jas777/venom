package com.venomdevteam.venom.util.google;

import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.util.google.youtube.YoutubeAPI;

public class GoogleAPI {

    private final Venom venom;

    public GoogleAPI (Venom venom) {
        this.venom = venom;
    }

    public YoutubeAPI getYoutube() {
        return new YoutubeAPI(venom.getConfig().getString("google.youtube.key"));
    }

}
