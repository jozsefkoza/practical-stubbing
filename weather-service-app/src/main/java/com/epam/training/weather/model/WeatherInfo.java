package com.epam.training.weather.model;

import java.time.LocalDate;
import java.util.Arrays;

import com.google.common.base.MoreObjects;

/**
 * @author Jozsef_Koza
 */
public final class WeatherInfo {
    private final LocalDate date;
    private final Temperature temperature;
    private final WeatherState weatherState;

    private WeatherInfo(Builder builder) {
        date = builder.date;
        temperature = builder.temperature;
        weatherState = builder.weatherState;
    }

    public static WeatherInfo.Builder builder() {
        return new Builder();
    }

    public LocalDate getDate() {
        return date;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public WeatherState getWeatherState() {
        return weatherState;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("date", date)
                .add("temperature", temperature)
                .add("weatherState", weatherState)
                .toString();
    }

    public enum WeatherState {
        SNOW("Snow", "sn"),
        SLEET("Sleet", "sl"),
        HAIL("Hail", "h"),
        THUNDERSTORM("Thunderstorm", "t"),
        HEAVY_RAIN("Heavy Rain", "hr"),
        LIGHT_RAIN("Light Rain", "lr"),
        SHOWERS("Showers", "s"),
        HEAVY_CLOUD("Heavy Cloud", "hc"),
        LIGHT_CLOUD("Light Cloud", "lc"),
        CLEAR("Clear", "c");

        private final String name;
        private final String abbreviation;

        WeatherState(String name, String abbreviation) {
            this.name = name;
            this.abbreviation = abbreviation;
        }

        public static WeatherState resolveByName(String name) {
            return Arrays.stream(values()).filter(state -> state.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        }

        public String getName() {
            return name;
        }

        public String getAbbreviation() {
            return abbreviation;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("name", name)
                    .add("abbreviation", abbreviation)
                    .toString();
        }
    }

    public static final class Temperature {
        private final double min;
        private final double max;

        private Temperature(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public static Temperature of(double min, double max) {
            return new Temperature(min, max);
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("min", min)
                    .add("max", max)
                    .toString();
        }
    }

    public static final class Builder {
        private LocalDate date;
        private Temperature temperature;
        private WeatherState weatherState;

        public Builder forDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder withTemperature(Temperature temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder withWeatherState(WeatherState state) {
            this.weatherState = state;
            return this;
        }

        public WeatherInfo build() {
            return new WeatherInfo(this);
        }
    }
}
