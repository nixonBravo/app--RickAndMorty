package com.example.rickandmorty.resource.remote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EpisodesResponse {
    @SerializedName("info")
    @Expose
    private Info info;
    @SerializedName("results")
    @Expose
    private List<Episode> results = null;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<Episode> getResults() {
        return results;
    }

    public void setResults(List<Episode> results) {
        this.results = results;
    }

    public class Info {

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("pages")
        @Expose
        private Integer pages;
        @SerializedName("next")
        @Expose
        private String next;
        @SerializedName("prev")
        @Expose
        private Object prev;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Integer getPages() {
            return pages;
        }

        public void setPages(Integer pages) {
            this.pages = pages;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public Object getPrev() {
            return prev;
        }

        public void setPrev(Object prev) {
            this.prev = prev;
        }
    }
}
