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

    public void crack(String path) throws FileNotFoundException {
        int length = getLineCount(path);
        int i = 0;
        FileInputStream stream = new FileInputStream(path);
        Scanner read = new Scanner(stream);

        while (read.hasNext()){
            String word = read.nextLine();
            for (int o = 0; o < users.length;  o++){
                if(users[o].getPassHash().contains("$")){
                String hash = Crypt.crypt(word, users[o].getPassHash());
                     if(hash.equals(users[o].getPassHash())) {
                           System.out.printf("Found password %s for user %s%n",word, users[o].getUsername());
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
        int length = getLineCount(shadowFile);
        int i = 0;
        FileInputStream stream = new FileInputStream(shadowFile);
        Scanner read = new Scanner(stream);
        User[] Users = new User[length];

        while (i < length){
            String line = read.nextLine();

            String[] lines = new String[3];
            lines = line.split(":", 3);

            Users[i] = new User(lines[0], lines[1]);
            i++;
        }
        return Users;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Type the path to your shadow file: ");
        String shadowPath = sc.nextLine();
        System.out.print("Type the path to your dictionary file: ");
        String dictPath = sc.nextLine();

        Crack c = new Crack(shadowPath, dictPath);
        c.crack(dictPath);
    }
}
