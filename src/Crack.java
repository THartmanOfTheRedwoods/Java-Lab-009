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
        FileInputStream fis = new FileInputStream(dictionary);
        Scanner s = new Scanner(fis);
        int index = 0;
        String[] str = new String[dictionary.length()];
        while (s.hasNextLine()) {
            String line = s.nextLine();
            for (User user : users) {
                if (user.getPassHash().contains("$")) {
                    String hash = Crypt.crypt(line, user.getPassHash());
                    if (hash.equals(user.getPassHash())) {
                        System.out.println("Found password " + line + " for user " + user.getUsername());
                    }
                }
            }
            str[index] = line;
            index++;
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
        FileInputStream fis = new FileInputStream(shadowFile);
        Scanner s = new Scanner(fis);
        int index = 0;
        while (s.hasNextLine()) {
            String line = s.nextLine();
            String[] splitter = line.split(":");
            User u = new User(splitter[0],splitter[1]);
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