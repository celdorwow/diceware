package org.diceware;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.diceware.ResourcesReader.getReader;

public class DiceWare {
    private final HashMap<String, String> diceWareList;
    private final String fileName;
    private final SecureRandom random;

    public DiceWare(String language) {
        this.fileName = String.format("diceware-wordlist-%s.asc", language);
        this.diceWareList = new HashMap<>();
        this.random = getRandom();
    }
    public DiceWare() {
        this("en");
    }

    public String plain(int n) {
        check();
        return codes(n).stream()
            .map(diceWareList::get)
            .collect(Collectors.joining(" "));
    }

    public String complex(int n, int w) {
        check();
        ArrayList<String> words = codes(n).stream()
            .map(diceWareList::get)
            .collect(Collectors.toCollection(ArrayList::new));
        return generateComplex(words, w);
    }

    // Private methods
    private List<String> codes(int n) {
        return IntStream.range(0, n)
            .boxed()
            .map(i -> IntStream.range(0, 5)
                .boxed()
                .map(e -> random.nextInt(6) + 1)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString()
        ).collect(Collectors.toList());
    }

    private String generateComplex(List<String> words, int w) {
        int n = words.size();
        w = Math.max(0, Math.min(w, n));
        ArrayList<String> output = new ArrayList<>(words);
        HashSet<String> processed = new HashSet<>();
        String[] chars = new String[] {
            "~!#$%^", "&*()-=", "+[]\\{}",
            ":;\"'<>", "?/0123", "456789"
        };
        while (w > 0) {
            int n_w = random.nextInt(n);
            if (!processed.contains(words.get(n_w))) {
                processed.add(words.get(n_w));
                String specialChar = chars[random.nextInt(6)].split("")[random.nextInt(6)];
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
            return SecureRandom.getInstance("SHA1PRNG");
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
