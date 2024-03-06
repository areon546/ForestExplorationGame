import java.util.Random;
import java.util.Scanner;

public class myDefaults {
    // this class contains a number of methods I frequently use

    // calls Scanner to receive a kb input
    //
    public static String inputString(String message) {
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);
        System.out.print(message);
        String input = sc.nextLine();

        return input.toUpperCase();
    } // END inputString

    // gets a character input from the user
    //
    public static char inputChar(String message) {
        System.out.println("The first character inputted will be considered your input. ");
        return inputString(message).charAt(0);
    } // END inputChar

    // gets an input from the user, and if it is an integer, it will return it
    // otherwise it will ask again. repeatedly.
    //
    public static int inputInt(String message) {
        String input = inputString(message);
        int n;

        // checks if the inputted text is an integer
        while (!isInt(input)) {
            System.out.println("Please input an integer, without any decimals, numbers, or special characters. ");
            input = inputString(message);

        }
        n = Integer.parseInt(input);

        return n;
    } // END inputInt

    // checks if the inputted string can be converted into an integer by checking if
    // the first character
    // can be converted, and recursively calling the rest of the string
    //
    public static boolean isInt(String s) {
        char c = s.charAt(0);
        boolean isInt = (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7'
                || c == '8' || c == '9');

        if (s.length() == 1) { // 1 digit
            return isInt;

        } else { // remaining digits
            return (isInt && isInt(s.substring(1)));

        }
    } // END isInt

    // prints the text input
    //
    public static void print(String message) {
        System.out.println(message);
        return;
    } // END print

    // creates a random integer between the bounds. min and max inclusive
    //
    public static int getRandomInt(int min, int max) {
        Random rand = new Random();
        int randNum = rand.nextInt(max + 1 - min) + min;

        return randNum;
    } // END getRandomInt

    // generates a random index based on the length of an array
    //
    public static int generateRandomIndex(int length) {
        return getRandomInt(0, (length - 1));
    } // END generateRandomIndex

    // prints the ingame time
    //
    public static void printTime(int time) { // TODO figure out why a leading zero changes the time by so much
        int hours = time / 100, minutes = time % 100;
        String displayedTime;

        minutes = (60 * minutes) / 100;

        displayedTime = String.format("%n%nThe current time is: %d:%02d", hours, minutes);

        // TODO figure out time=0500 displayedTime = 0312

        if (minutes >= 60 && hours < 25 && hours > -1) {
            displayedTime = "%n%nError, time out of bounds. ";
        } else {
            print(displayedTime);
        }

        return;
    } // END printTime

}
