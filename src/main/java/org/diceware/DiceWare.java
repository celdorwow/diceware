package org.diceware;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.diceware.ResourcesReader.getReader;

public class DiceWare {
    private final HashMap<String, String> diceWareList;
    private final String fileName;
    private final SecureRandom random;
    private final String delimiter;
    private final boolean capitals;

    public DiceWare(String language, String delimiter, boolean capitalLetters) {
        this.fileName = String.format("diceware-wordlist-%s.asc", language);
        this.diceWareList = new HashMap<>();
        this.random = getRandom();
        this.delimiter = delimiter;
        this.capitals = capitalLetters;
    }
    public DiceWare(String language, String delimiter) {
        this(language, delimiter, false);
    }
    public DiceWare(String language) {
        this(language, " ", false);
    }
    public DiceWare() {
        this("en", " ", false);
    }

    public String plain(int n) {
        check();
        return codes(n).stream()
            .map(diceWareList::get)
            .collect(Collectors.joining(delimiter));
    }

    public String complex(int n, int w) {
        check();
        ArrayList<String> words = codes(n).stream()
            .map(diceWareList::get)
            .collect(Collectors.toCollection(ArrayList::new));
        return insertSpecialCharacters(words, w);
    }

    public String alphanumeric(int n) {
        String[][] seed = new String[][] {
            {"ABCDEF", "GHIJKL", "MNOPQR", "STUVWX", "YZ0123", "456789"},
            {"abcdef", "ghijkl", "mnopqr", "stuvwx", "yz"},
        };
        return generateRandomCharacters(n, seed);
    }

    public String characters(int n) {
        String[][] seed = new String[][] {
            {"ABCDEF", "GHIJKL", "MNOPQR", "STUVWX", "YZ0123", "456789"},
            {"abcdef", "ghijkl", "mnopqr", "stuvwx", "yz~_ "},
            {"!@#$%^", "&*()-=", "+[]{}\\", "|`;:'\"", "<>/?.,"},
        };
        return generateRandomCharacters(n, seed);
    }

    void printAboutRandom() {
        System.out.println("Random generator:");
        System.out.println("    provider:  " + random.getProvider());
        System.out.println("    algorithm: " + random.getAlgorithm());
    }

    public static String firstCapitalLetters(String password, String delimiter) {
        return Arrays.stream(password.split(" "))
            .map(String::toLowerCase)
            .map(e -> e.substring(0, 1).toUpperCase() + e.substring(1))
            .collect(Collectors.joining(delimiter));
    }

    // Private methods
    private List<String> codes(int n) {
        return IntStream.range(0, n)
            .boxed()
            .map(i -> random.ints(5, 1, 7)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString()
            ).collect(Collectors.toList());
    }

    private String generateRandomCharacters(int n, String[][] charTable) {
        return IntStream.range(0, n).boxed()
            .map(e -> {
                int firstRoll = random.nextInt(charTable.length);
                int secondRoll = random.nextInt(charTable[firstRoll].length);
                int thirdRoll = random.nextInt(charTable[firstRoll][secondRoll].length());
                return charTable[firstRoll][secondRoll].split("")[thirdRoll];
            })
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();
    }

    private String insertSpecialCharacters(List<String> words, int w) {
        int n = words.size();
        w = Math.max(0, Math.min(w, n));
        ArrayList<String> output = new ArrayList<>(words);
        HashSet<String> processed = new HashSet<>();
        String[] charTable = new String[] {
            "~!#$%^", "&*()-=", "+[]\\{}",
            ":;\"'<>", "?/0123", "456789"
        };
        while (w > 0) {
            int n_w = random.nextInt(n);
            if (!processed.contains(words.get(n_w))) {
                processed.add(words.get(n_w));
                String specialChar = charTable[random.nextInt(6)].split("")[random.nextInt(6)];
                ArrayList<String> charList = Arrays.stream(output.get(n_w).split(""))
                    .collect(Collectors.toCollection(ArrayList::new));
                charList.add(random.nextInt(charList.size()), specialChar);
                output.set(n_w, String.join("", charList));
                w--;
            }
        }
        return String.join(" ", output);
    }

    private void populateWordList() throws FileNotFoundException {
        Pattern pattern = Pattern.compile("(\\d{5})\\s+(.+)");
        BufferedReader br = getReader(this.fileName, DiceWare.class);
        br.lines()
            .forEach(line -> {
                if (pattern.matcher(line).matches()) {
                    pattern.matcher(line).results().forEach(e -> {
                        String[] array = new String[] {e.group(1), e.group(2)};
                        this.diceWareList.put(array[0], array[1]);
                    });
                }
            });
    }

    private SecureRandom getRandom() {
        try {
            // return SecureRandom.getInstance("SHA1PRNG");
            // return SecureRandom.getInstance("NativePRNG");
            return SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private void check() {
        try {
            populateWordList();
        } catch (FileNotFoundException e) {
            System.err.println("Cannot read resources");
            System.exit(-1);
        }
        if (diceWareList == null || diceWareList.size() == 0) {
            throw new RuntimeException("Invalid list of Diceward words");
        }
        if (random == null) {
            throw new RuntimeException("Invalid SecureRandom generator");
        }
    }
}
