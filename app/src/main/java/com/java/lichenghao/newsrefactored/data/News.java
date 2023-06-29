package com.java.lichenghao.newsrefactored.data;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    private long id;
    private String title;
    private String source;
    private String time;
    private String content;
    private String images[];
    private String video[];
    private String idFromAPI;
    private String url;
    private boolean isFavorites;
    private boolean beenRead;

    @Override
    public String toString(){
        Gson gson = new Gson();
        String ans = gson.toJson(this);
        return ans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News that = (News) o;
        return (id == that.id) && (idFromAPI.equals(that.idFromAPI));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id + "HashCode" + idFromAPI);
    }

}
