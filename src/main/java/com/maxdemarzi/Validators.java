package com.maxdemarzi;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

public class Validators {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static HashMap getValidPotentialAddressQueryInput(String body) throws IOException {
        HashMap input;

        // Parse the input
        try {
            input = objectMapper.readValue(body, HashMap.class);
        } catch (Exceptions e) {
            throw Exceptions.invalidInput;
        }
        // Make sure it has an Address parameter
        if (!input.containsKey("address")) {
            throw Exceptions.missingAddressParameter;
        }

        // Make sure the name is not blank
        if (input.get("address") == "") {
            throw Exceptions.invalidAddressParameter;
        }

        return input;
    }
}
