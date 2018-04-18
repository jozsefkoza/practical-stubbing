package com.epam.training.weather.model;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Optional;

import com.google.common.primitives.Longs;


/**
 * Represents a Where On Earth ID.
 *
 * @author Jozsef_Koza
 */
public final class WoeId {
    private final long id;

    private WoeId(long id) {
        checkArgument(id > 0, "ID must be non-negative");
        this.id = id;
    }

    public static WoeId of(long id) {
        return new WoeId(id);
    }

    public static Optional<WoeId> of(String value) {
        return Optional.ofNullable(value).map(Longs::tryParse).map(WoeId::of);
    }

    public long id() {
        return id;
    }
}
