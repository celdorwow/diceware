package org.diceware;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

public class DicewareMain {
    public static void main(String[] args) {
        int n = 6;   // number of words in a phrase
        int w = 0;   // number of altered words
        String language = "en";

        try {
            Options options = new Options();
            options.addOption(new Option("l", "language", true, "language"));
            options.addOption(new Option("w", "words", true, "thread"));
            options.addOption(new Option("a", "altered", true, "script file"));
            CommandLineParser commandLineParser = new DefaultParser();
            CommandLine cmd = commandLineParser.parse(options, args);

            if (cmd.hasOption("a"))
                w = Integer.parseInt(cmd.getOptionValue("a"));
            if (cmd.hasOption("w"))
                n = Integer.parseInt(cmd.getOptionValue("w"));
            if (cmd.hasOption("language"))
                language = cmd.getOptionValue("language");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.out.println();
            printHelp();
            System.out.println();
            System.exit(-1);
        }

        DiceWare dw = new DiceWare(language);
        System.out.printf("Password:%n    %s%n%n", dw.complex(n, w));
    }

    private static void printHelp() {
        System.out.println("Usage: java -jar diceware.jar [OPTIONS]");
        System.out.println();
        System.out.println("-w, --words      number of words in the phrase, default 6");
        System.out.println("-a, --altered    number of words altered with special characters, default 0");
        System.out.println("-l, --language   specified a language: {en} or pl");
    }
}
