package com.venomdevteam.venom.util.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import java.net.URI;
import java.net.URISyntaxException;

public class AudioUtil {

    public static String getThumbnail(AudioTrackInfo info) {
        try {
            URI uri = new URI(info.uri);
            if (uri.getHost().contains("youtube.com") || uri.getHost().contains("youtu.be")) {
                return String.format("https://img.youtube.com/vi/%s/0.jpg", info.identifier);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
