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

    public Crack(String shadowFile, String dictionary) throws FileNotFoundException {
        this.dictionary = dictionary;
        this.users = Crack.parseShadow(shadowFile);
    }

    public void crack() throws FileNotFoundException {
        FileInputStream fi = new FileInputStream(this.dictionary);
        Scanner s = new Scanner(fi);
        while (s.hasNextLine()) {
            String password = s.nextLine();
            for (User u : this.users) {
                if (u.getPassHash().contains("$")) {
                    String hash = Crypt.crypt(password, u.getPassHash());
                    if (hash.equals(u.getPassHash())) {
                        System.out.printf("Match found for user %s with password %s%n", u.getUsername(), password);
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
        FileInputStream fi = new FileInputStream(shadowFile);
        Scanner s = new Scanner(fi);
        int i =0;
        while(s.hasNextLine()){
            String line = s.nextLine();
            String[] splitline = line.split(":");
            User u = new User(splitline[0], splitline[1]);
            users[i] = u;
            i++;
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
