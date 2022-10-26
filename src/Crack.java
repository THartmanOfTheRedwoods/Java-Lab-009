import org.apache.commons.codec.digest.Crypt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;

class User {
   private String username;
   private String passHash;

   public User(String username, String passHash){
       this.username = username;
       this.passHash = passHash;
   }

   public String getUsername(){
       return username;
   }
    public String getPassHash(){
        return passHash;
    }

}

public class Crack {
    private final User[] users;
    private final String dictionary;

    public Crack(String shadowFile, String dictionary) throws FileNotFoundException {
        this.dictionary = dictionary;
        this.users = Crack.parseShadow(shadowFile);
    }

    public void crack() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(dictionary);
        Scanner scanner = new Scanner(fis);
        while(scanner.hasNextLine()) {
            String word = scanner.nextLine();
            for (int i = 0; i < users.length ; i++) {
                if (users[i].getPassHash().contains("$")) {
                    String hash = Crypt.crypt(word, users[i].getPassHash());
                    if (hash.equals(users[i].getPassHash())) {
                        System.out.println("Found password " + word + " for user " + users[i].getUsername());
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
        int index = 0;
        FileInputStream fis = new FileInputStream(shadowFile);
        Scanner scanner = new Scanner(fis);
            while(scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(":");
                User u = new User(line[0], line[1]);
                users[index] = u;
                index += 1;
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
