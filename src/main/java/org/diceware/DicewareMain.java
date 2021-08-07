package org.diceware;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

import java.util.List;
import java.util.stream.Stream;

public class DicewareMain {
    public static void main(String[] args) {
        int n = 6;   // number of words in a phrase
        int w = 0;   // number of altered words
        String language = "en";
        String passType = "w";
        boolean v = false;

        try {
            Options options = new Options();
            options.addOption(new Option("l", "language", true, "language"));
            options.addOption(new Option("n", "number", true, "number of words/characters"));
            options.addOption(new Option("i", "inserts", true, "number of altered words"));
            options.addOption(new Option("p", "password", true, "type of password"));
            options.addOption(new Option("v", "verbose", false, "print parameters of random generator"));
            CommandLineParser commandLineParser = new DefaultParser();
            CommandLine cmd = commandLineParser.parse(options, args);

            if (cmd.hasOption("a")) w = Integer.parseInt(cmd.getOptionValue("a"));
            if (cmd.hasOption("n")) n = Integer.parseInt(cmd.getOptionValue("n"));
            if (cmd.hasOption("l")) language = cmd.getOptionValue("language");
            if (cmd.hasOption("p")) passType = cmd.getOptionValue("p");
            if (cmd.hasOption("v")) v = true;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.out.println();
            printHelp();
            System.out.println();
            System.exit(-1);
        }

        // Object
        DiceWare dw = new DiceWare(language);

        if (v) {
            dw.printAboutRandom();
            System.out.println();
        };

        if (List.of("words", "word", "w").contains(passType)) {
            String password = dw.complex(n, w);
            System.out.printf("Password:%n    %s%n", password);
            System.out.printf("    %s%n%n", DiceWare.firstCapitalLetters(password, ""));
        } else if (List.of("alphanumeric", "alphanum", "a").contains(passType)) {
            System.out.printf("Password:%n    %s%n%n", dw.alphanumeric(n));
        } else if (List.of("characters", "character", "chars", "char", "c").contains(passType)) {
            System.out.printf("Password:%n    %s%n%n", dw.characters(n));
        } else {
            System.out.println("Invalid argument for password");
        }
    }

    private static void printHelp() {
        System.out.println("Usage: java -jar DiceWareProject.jar [OPTIONS]");
        System.out.println();
        System.out.println("-n, --number     number of words in the phrase, default 6");
        System.out.println("-i, --inserts    number of words altered with special characters, default 0");
        System.out.println("-l, --language   specified a language (en or pl), default en");
        System.out.println("-p, --password   type of password: words (w), alphanumeric (a), or characters (c)");
        System.out.println("-v, --verbose    print parameters of the random generator");
    }
}
