package com.tomchinsky.imdb.service;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.stream.Stream;

public class IMDBMovieListParserImpl implements IMDBMovieListParser {
    private static class IMDBItem {
        @SerializedName("l")
        private String name;
        @SerializedName("q")
        private String type;

        public String getName() {
            return name;
        }
        public boolean isMovie() {
            return "feature".equals(type);
        }
    }

    private static class JsonContainer {
        @SerializedName("d")
        private ArrayList<IMDBItem> data;
        public ArrayList<IMDBItem> getData() {
            return data;
        }
    }

    @Override
    public Stream<String> getMovieTitles(String jsonp) {
        String json = jsonp.substring(jsonp.indexOf('(') + 1, jsonp.length() - 1);
        JsonContainer container = new Gson()
                .fromJson(json, JsonContainer.class);

        return container.getData()
                .stream()
                .filter(IMDBItem::isMovie)
                .map(IMDBItem::getName);
    }
}
