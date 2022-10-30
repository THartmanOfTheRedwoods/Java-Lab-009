import org.apache.commons.codec.digest.Crypt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class Crack {
    private final ArrayList<User> users;
    private final String dictionary;

    public Crack(String shadowFile, String dictionary) throws FileNotFoundException {
        this.dictionary = dictionary;
        this.users = Crack.parseShadow(shadowFile);
    }

    public void crack() throws FileNotFoundException {
        // omitting the path parameter as we can just use the "dictionary" attribute
        FileInputStream fileInputStream = new FileInputStream(dictionary);
        Scanner scanner = new Scanner(fileInputStream);

        int hashesTried = 0;

        long startTime = System.nanoTime();

        while (scanner.hasNext()) {
            String word = scanner.nextLine();
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);

                String hash = Crypt.crypt(word, user.getPassHash()); // salt? unsure

                if (user.getPassHash().equals(hash)) {
                    System.out.println("Found password \"" + word + "\" for user " + user.getUsername());
                    users.remove(i);
                    i--;
                }
            }

            hashesTried++;
            if (hashesTried%1000 == 0) {
                System.out.printf("Hashes tried: %d, at %.3fs\n", hashesTried, (System.nanoTime() - startTime) / 1000000000.0);
            }

        }
        System.out.printf("\nCracking process finished after %.3fs\n", (System.nanoTime() - startTime) / 1000000000.0);
    }

    public static int getLineCount(String path) {
        int lineCount = 0;
        try (Stream<String> stream = Files.lines(Path.of(path), StandardCharsets.UTF_8)) {
            lineCount = (int)stream.count();
        } catch(IOException ignored) {}
        return lineCount;
    }

    /**
     * Returns a list of users with hashed passwords from a given shadow file
     * @param shadowFile the shadow file with users
     * @return the list of users
     * @throws FileNotFoundException
     */
    public static ArrayList<User> parseShadow(String shadowFile) throws FileNotFoundException {
        ArrayList<User> users = new ArrayList<>();

        FileInputStream fileInputStream = new FileInputStream(shadowFile);
        Scanner scanner = new Scanner(fileInputStream);
        while (scanner.hasNext()) {
            String[] splitLine = scanner.nextLine().split(":");
            // ignore users w/o passwords
            if (!splitLine[1].contains("$")) continue;

            users.add(new User(splitLine[0],splitLine[1]));
            System.out.println("User added: " + splitLine[0] + " with hash \"" + splitLine[1] + "\"");
        }
        System.out.println();
        return users;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Type the path to your shadow file (leave blank for \"resources/shadow\"): ");
        String shadowPath = sc.nextLine();
        if (shadowPath.equals("")) shadowPath = "resources/shadow";

        System.out.print("Type the path to your dictionary file (leave blank for \"resources/englishSmall.dic\"): ");
        String dictPath = sc.nextLine();
        if (dictPath.equals("")) dictPath = "resources/englishSmall.dic";

        System.out.println();
        Crack c = new Crack(shadowPath, dictPath);
        c.crack();
    }
}
