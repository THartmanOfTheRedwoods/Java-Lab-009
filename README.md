# Java-Lab-009

## PART 1 - Create User Class

* Create a Class called User that has 2 String instance variables:
```java
private String username;
private String passHash;
```
* Create a User constructor that has 2 parameters **username** and **passHash** and assigns these parameters to its instance variables.
* Create 2 methods getUsername and getPassHash that return the instance variables respectively.

## PART 2 - Implement method parseShadow
* Analyze the method signature **parseShadow** and note what it's return type is below:
    * Return Type? USER [] which is a USER ARRAY
* A Unix/Linux Shadow file contains username and password hashes for each user seperated by a delimiter **:**
* In this section, follow the steps below to parse each username and password hash out of the provided Shadow file and store it in a user array.
* Complete the body of the **parseShadow** method utilizing:
    * the pre-complete method **getLineCount** to create a user array called Users
    * imported class FileInputStream
    * imported class Scanner
    * a while loop that uses the above 2 imported classes to read the **resources/shadow** file line by line.
    * the **split** method and the delimiter **:** to split each line into a string array (i.e. String[])
    * use the first 2 elements of the split string array to create a **new User(element1, element2)**
    * and finally store each new User into a User array (i.e. User[] users), and return the array.
  
## PART 3 - Implement method crack
* Finish implementing the method crack to do the following:
    * Similar to your parseShadow method, use the **FileInputStream** and **Scanner** class to read the **resources/englishSmall.dic** file line by line.
         * Note: the file path should be passed in a parameter.
    * For each word read from your englishSmall.dic dictionary **iterate** over your User[] array and use the **Crypt** library  to build a password hash of the dictionary word.
      ```java
      String hash = Crypt.crypt(word, user.getPassHash());
      ```
    * Add a conditional to compare the generated hash with each user's password hash.
        * Note: only compare users with **$** in their password hashes, otherwise you'll get an error.
    * If the user's password hash matches the generated hash, print out:
      ```
      Found password X for user Y.
      ```

## PART 4 - Turn In
* Complete the Pull Request Steps that you do every week!!!