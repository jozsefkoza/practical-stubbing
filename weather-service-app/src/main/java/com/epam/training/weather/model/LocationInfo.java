package com.epam.training.weather.model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * @author JoeZee
 */
public final class LocationInfo {
    private String title;
    private long woeid;

    public String getTitle() {
        return title;
    }

    public long getWoeid() {
        return woeid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, woeid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationInfo that = (LocationInfo) o;
        return woeid == that.woeid && Objects.equals(title, that.title);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("title", title)
            .add("woeid", woeid)
            .toString();
    }
}
