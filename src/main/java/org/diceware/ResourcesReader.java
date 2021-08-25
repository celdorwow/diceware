package org.diceware;
/** Reads files from the Resources folder and returns an object with access to Stream\<T\>
 * @author Zbigniew Koziel
 * @version 0.1-beta
 *
 * @param type The type of a class this is used in
 */

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
