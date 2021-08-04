package org.diceware;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourcesReader {
    public static <T> BufferedReader getReader(String fileName, Class<T> type) throws FileNotFoundException {
        InputStream is = type.getClassLoader().getResourceAsStream(fileName);
        if (is == null) throw new FileNotFoundException("Error: " + fileName);
        return new BufferedReader(new InputStreamReader(is));
    }
}
