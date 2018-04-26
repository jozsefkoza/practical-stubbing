package com.epam.training.weather.model;

import java.util.Objects;
import java.util.Optional;

import com.google.common.base.MoreObjects;
import com.google.common.primitives.Longs;


/**
 * Represents a Where On Earth ID.
 *
 * @author Jozsef_Koza
 */
public final class WoeId {
    private static final WoeId UNKNOWN = new WoeId(-1);

    private final long id;

    private WoeId(long id) {
        this.id = id;
    }

    public static WoeId of(long id) {
        return id > 0 ? new WoeId(id) : UNKNOWN;
    }

    public static Optional<WoeId> of(String value) {
        return Optional.ofNullable(value).map(Longs::tryParse).map(WoeId::of);
    }

    public long id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WoeId woeId = (WoeId) o;
        return id == woeId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
