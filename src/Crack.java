import org.apache.commons.codec.digest.Crypt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;

public class Crack {
    private final User[] users;
    private final String dictionary;

    public static int classIndex = 0;

    public Crack(String shadowFile, String dictionary) throws FileNotFoundException {
        this.dictionary = dictionary;
        this.users = Crack.parseShadow(shadowFile);
    }

    public void crack() throws FileNotFoundException {
        FileInputStream fileboy = new FileInputStream(dictionary);
        Scanner cheese = new Scanner(fileboy);
        while(cheese.hasNextLine()) {
            String word = cheese.nextLine();
            for(int i = 0; i < this.users.length; i++) {
                if (users[i].getPassHash().contains("$")) {
                    String hash = Crypt.crypt(word, users[i].getPassHash());
                    if (hash.equals(users[i].getPassHash())) {
                        System.out.printf("Found password match %s for user %s%n", word, users[i].getUsername());
                    }
                }
            }
        }
    }

    public static int getLineCount(String path) {
        int lineCount = 0;
        try (Stream<String> stream = Files.lines(Path.of(path), StandardCharsets.UTF_8)) {
            lineCount = (int)stream.count();
        } catch(IOException ignored) {}
        return lineCount;
    }

    public static User[] parseShadow(String shadowFile) throws FileNotFoundException {
        User[] users = new User[getLineCount(shadowFile)];
        FileInputStream fileboy = new FileInputStream(shadowFile);
        Scanner scanman = new Scanner(fileboy);
        int index = classIndex;

        while(scanman.hasNextLine()) {
            String[] lineFinder = scanman.nextLine().split(":");
            User u = new User(lineFinder[0], lineFinder[1]);
            users[index] = u;
            index++;
        }
        return users;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Type the path to your shadow file: ");
        String shadowPath = sc.nextLine();
        System.out.print("Type the path to your dictionary file: ");
        String dictPath = sc.nextLine();

        Crack c = new Crack(shadowPath, dictPath);
        c.crack();
    }
}