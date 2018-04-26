package com.epam.training.weather.metaweather.messaging;

import java.io.IOException;

import com.epam.training.weather.model.WoeId;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

final class WoeIdTypAdapter extends TypeAdapter<WoeId> {
    @Override
    public void write(JsonWriter out, WoeId value) {
        throw new UnsupportedOperationException("Write is not supported!");
    }

    @Override
    public WoeId read(JsonReader in) throws IOException {
        return WoeId.of(in.nextLong());
    }
}
