**Introduction**

Java version of the [Diceware][1] method for generating secure passwords.

**The method - Diceware**

The application simulates generating a secure passwords using the [Diceware][1] method. However, how _secure_ the password is also depends on a number of words in a password; essentially the more words the more secure passphrase. For certain applications, there is a suggested _minimum_ # of words. There are circumstances where a long passphrase is not recommended. Please consult [this website][1] as well as the available [FAQ][2].

**Installation**

In order to compile the project, the OS must have an access to a a java development kit, e.g. [OpenJDK][5]. In order to create a self-contained and executable `jar` file, it is also required to have the access to  `mvn` command, which is part of the [Maven][4] project. Once both a java JDK and [Maven][4] are available and the current folder is a folder of the project, the executable self-contained `jar` file can be created.

The following command is used to build the package `jar`:

    mvn clean package

First time Maven is executed, it will download a series of plug-ins and dependencies including [Commons CLI][6]. It may take a few minutes to complete this phase. Then, the application is built and ready to use. Assuming a user is in a root of the project, the command below invokes the application:

    java -jar target/diceware.jar

An output should be a list of 6 random English words separated by spaces. This is default output, which can be changed by adding extra options. Additionally, the application can generate a sequence of random alphanumeric characters instead, which can be mixed with one extra special character per word. This is switched by one of the options.

Currently, only two languages are available: `en` and `pl`.

**Resources and list of words**

According to [the method][1], this application selects random words from a list in one of the files: `diceware-wordlist-XX.asc`, where `XX` corresponds to a language. Currently, only two languages are available: `diceware-wordlist-en.asc` and `diceware-wordlist-pl.asc`.

Both files are signed digitally using the public key: [9AFEEB85ADC62CA12B99E500309F1EAF0848DCA3][3], whose content is following:

```
pub   rsa4096 2018-05-08 [C] [expires: 2022-07-26]
      9AFEEB85ADC62CA12B99E500309F1EAF0848DCA3
uid           [full] Zbigniew Koziel (PhD candidate ...) <zbigniew.koziel@manchester.ac.uk>
uid           [full] Zbigniew Koziel (PhD candidate ...) <zbigniew.koziel@postgrad.manchester.ac.uk>
sub   rsa4096 2018-05-08 [E] [expires: 2022-07-26]
      7D43A1193EE28688AB5AACCEAC35AEA0103F7353
sub   rsa4096 2018-05-08 [S] [expires: 2022-07-26]
      BD3A53EE9AF6BE2C6F49E6D0D10EE09E46AF2416
sub   rsa4096 2018-05-08 [A] [expires: 2022-07-26]
      E7F9724627161171CFC4B0E0796B182A63595EDB
```

The signatures are required to prevent from undetected changes (while this is out of scope, the list of 7776 words in each file are not chosen randomly, see [the website][1]). The GPG Key can be downloaded from a server `https://keys.openpgp.org/` or imported using `gpg` (recommended):

    gpg --import <file containing a key>.asc

or

    gpg --recv-keys 9AFEEB85ADC62CA12B99E500309F1EAF0848DCA3

Once the key is in a keyring, `gpg` can verify a signature:

    gpg --verify diceware-wordlist-en.asc

Two outcomes are possible, either **Good signature** or a warning **The validity of the signature cannot be verified**. The latter simply means a signature is OK but a holder of this key could be anyone. Until the key is validated, the signature still does not prove anything. That's what happens if a new key is imported. If the KeyID in the signature is the exact same as the key imported to a keyring, then the files have not been altered.

[1]: https://theworld.com/~reinhold/diceware.html
[2]: https://theworld.com/~reinhold/dicewarefaq.html
[3]: https://keys.openpgp.org/vks/v1/by-fingerprint/9AFEEB85ADC62CA12B99E500309F1EAF0848DCA3
[4]: https://maven.apache.org/index.html
[5]: https://openjdk.java.net/
[6]: https://commons.apache.org/proper/commons-cli/index.html
