package com.epam.training.weather.model;

/**
 * Represents a geographical position.
 *
 * @author Jozsef_Koza
 */
public final class Geoposition {
    private float latitude;
    private float longitude;

    private Geoposition(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Geoposition of(float latitude, float longitude) {
        return new Geoposition(latitude, longitude);
    }

    public float latitude() {
        return latitude;
    }

    public float longitude() {
        return longitude;
    }
}
