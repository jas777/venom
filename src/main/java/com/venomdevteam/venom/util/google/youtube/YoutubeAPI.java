package com.venomdevteam.venom.util.google.youtube;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeAPI {

    private final String apiKey;

    private final long NUMBER_OF_VIDEOS_RETURNED = 10;

    private final YouTube youtube;

    public YoutubeAPI(String apiKey) {
        this.apiKey = apiKey;

        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {
        }).setApplicationName("venom").build();

    }

    public List<SearchResult> search(String query) {

        try
        {
            YouTube.Search.List search = youtube.search().list("id,snippet");

            search.setKey(apiKey);
            search.setQ(query);

            search.setType("video");

            search.setFields("items(id/kind,id/videoId,snippet/channelTitle,snippet/title,snippet/thumbnails/default/url)");

            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            SearchListResponse response = search.execute();

            return response.getItems();

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

}
