package com.epam.training.weather.model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * @author Jozsef_Koza
 */
public final class LocationInfo {
    private String title;
    private WoeId woeid;

    public String getName() {
        return title;
    }

    public WoeId getWoeid() {
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
        return Objects.equals(title, that.title) && Objects.equals(woeid, that.woeid);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("title", title)
                .add("woeid", woeid)
                .toString();
    }
}
