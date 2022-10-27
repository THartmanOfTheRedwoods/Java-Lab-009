import org.apache.commons.codec.digest.Crypt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

public class Crack {
    private final User[] users;
    private final String dictionary;

    public Crack(String shadowFile, String dictionary) throws FileNotFoundException {
        this.dictionary = dictionary;
        this.users = Crack.parseShadow(shadowFile);
    }

    public void crack(String dictionary) throws FileNotFoundException {
        FileInputStream FIn = new FileInputStream(dictionary);
        Scanner s = new Scanner(FIn);

        while(s.hasNextLine()){
            String word = s.nextLine();
            int i = 0;
//            System.out.println("First while");//TEST   DELETE

            while (i < users.length) {

//                System.out.println("2nd while  "+i);//TEST   DELETE

                if ((users[i].getPassHash()).contains("$")){
                    String hash = Crypt.crypt(word , users[i].getPassHash());

 //                   System.out.println(users[i].getUsername()+"   "+users[i].getPassHash());//TEST   DELETE

                    if (hash.equals(users[i].getPassHash())) {
                        System.out.println("Found password " + word + " for user " + users[i].getUsername());
                    }
                }
//                System.out.println("End 2st while\n");
                i++;
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
        FileInputStream FIn = new FileInputStream(shadowFile);
        Scanner s = new Scanner(FIn);
        User[] users = new User[getLineCount(shadowFile)];

        int i = 0;
        while(getLineCount(shadowFile) > i){
            String line = s.nextLine();
            User u = new User(line.split(":")[0], line.split(":")[1]);
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
        c.crack(dictPath);
    }
}
