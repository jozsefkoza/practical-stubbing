package com.epam.training.weather.metaweather.messaging;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.epam.training.weather.model.WeatherInfo;
import com.epam.training.weather.model.WeatherInfo.Temperature;
import com.epam.training.weather.model.WeatherInfo.WeatherState;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

final class WeatherInfoTypeAdapter extends TypeAdapter<WeatherInfo> {
    @Override
    public void write(JsonWriter out, WeatherInfo value) {
        throw new UnsupportedOperationException("Write is not supported!");
    }

    @Override
    public WeatherInfo read(JsonReader in) throws IOException {
        WeatherInfo.Builder weatherInfo = WeatherInfo.builder();
        Double minTemp = null;
        Double maxTemp = null;
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "applicable_date":
                    weatherInfo.forDate(parseDate(in.nextString()));
                    break;
                case "weather_state_name":
                    weatherInfo.withWeatherState(WeatherState.resolveByName(in.nextString()));
                    break;
                case "min_temp":
                    minTemp = in.nextDouble();
                    break;
                case "max_temp":
                    maxTemp = in.nextDouble();
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        if (minTemp != null && maxTemp != null) {
            weatherInfo.withTemperature(Temperature.of(minTemp, maxTemp));
        }
        return weatherInfo.build();
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
