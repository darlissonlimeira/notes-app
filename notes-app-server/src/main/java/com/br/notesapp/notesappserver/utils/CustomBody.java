package com.br.notesapp.notesappserver.utils;

import java.util.HashMap;

public class CustomBody {

    private final HashMap<String, Object> body = new HashMap<>();

    public CustomBody() {
    }

    public void addProperty(String name, String value) {
        body.put(name, value);
    }
}
