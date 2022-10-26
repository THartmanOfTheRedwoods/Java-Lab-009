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

    public void crack() throws FileNotFoundException { //the file path should be passed in a parameter.
        //Use the **FileInputStream** and **Scanner** class to read the **resources/englishSmall.dic** file line by line

        FileInputStream fir = new FileInputStream(this.dictionary); //imported class FileInputStream
        Scanner s = new Scanner(fir); //imported class Scanner
        //a while loop that uses the above 2 imported classes to read the **resources/shadow** file line by line
        while(s.hasNextLine()) {
            String word = s.nextLine();
            for (User u : users){
                if(u.getPassHash().contains("$")) {
                    String hash = Crypt.crypt(word, u.getPassHash());
                    if(hash.equals(u.getPassHash())) {
                        System.out.println("Found password: " +word+ " " +u.getUsername());
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

    /*Analyze the method signature **parseShadow** and note what it's return type is below:
    *    Return Type? User[]
     */
    public static User[] parseShadow(String shadowFile) throws FileNotFoundException {

        //utilizing the pre-complete method **getLineCount** to create a user array called Users
        User[] users = new User[getLineCount(shadowFile)];
        FileInputStream fir = new FileInputStream(shadowFile); //imported class FileInputStream
        Scanner s = new Scanner(fir); //imported class Scanner
        //a while loop that uses the above 2 imported classes to read the **resources/shadow** file line by line
            int i = 0;
        while(s.hasNextLine()) {
                String Line = s.nextLine();
                //the **split** method and the delimiter **:** to split each line into a string array (i.e. String[])
                String[] splitLine = Line.split(":");
                //use the first 2 elements of the split string array to create a **new User(element1, element2)**
                User u = new User(splitLine[0], splitLine[1]);
                //store each new User into a User array (i.e. User[] users)
                users[i]= u;
                i++;
            }

            //return the array
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
