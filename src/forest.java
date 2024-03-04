
/* 
* 
* Author   : Artur Baran 
* Stud ID  : 230518733
* Version  : 18
* Date     : 2023.11.16
* 
* This program will make the user delve into a forest and have to get out alive through the forest while collecting herbs throughout the process. 
* This process will last multiple days. 
*/

import java.util.Scanner;
import java.io.*;
import java.util.Random;

public class forest {

    public static void main(String[] args) throws IOException {

        enterTheForest();

        return;

    }

    /*****************************************************************************************************/
    // Forest Game

    // creates the character and asks whether to use a character from a previous
    // game or not
    //
    public static void enterTheForest() throws IOException {
        String name = "", choice = "", saveFile = GET_SAVE_FILE_NAME();
        final int MAX_PLANT_INV_LEN = 30;
        Plant[] plantsInv = new Plant[MAX_PLANT_INV_LEN];
        int day = 0;

        print("Welcome to the Forest. ");
        print("Your goal is to get out alive. ");

        // choose between new character or old one
        choice = inputString(
                "Do you want to make a new character, or choose an pre-existing character? \nA) NEW \nB) PRE-EXISTING\n")
                .toUpperCase();

        if (choice.equals("A")) {
            name = inputString("Enter Name: ");

        } else if (choice.equals("B") && countLines(saveFile) > 0) {
            Save character = showPreExistingCharacters();
            name = getSaveName(character);
            day = getSaveDay(character);
            plantsInv = getSavePlantInventory(character);

        } else {
            System.out.println("ERROR PROGRAM TERMINATES");
            return;

        }

        exploreTheForest(initSave(name, day, plantsInv));

        return;
    } // END enterTheForest

    // loads the game character, and explores the forest on a daily loop
    //
    public static void exploreTheForest(Save characterSave) throws IOException {
        boolean inForest = true, alive = true;
        char saveProgram = 'N';
        String direction, name = getSaveName(characterSave);
        final int DEFAULT_TIME_INCREASE = 50, START_OF_DAY = 1400, END_OF_DAY = 2400, EVE = 1700,
                MAX_DAYS = 10;
        int time, timeIncrease, randomEvent, day = getSaveDay(characterSave);
        Plant[] plantsInv = getSavePlantInventory(characterSave);

        print(String.format(
                "Welcome %s, you are here to collect herbs for the town and preserve the wildlife. %nIf you anger the forest, you will not leave. %nIf you think it is getting late, leave. %nYou best not stay for too long. ",
                name));

        while (day < MAX_DAYS && alive) { // DAYS loop

            print(String.format("%nWelcome to day %d", (day + 1)));
            time = START_OF_DAY;
            inForest = true;

            while (time < END_OF_DAY && inForest) { // TIME loop

                // resets variables
                timeIncrease = DEFAULT_TIME_INCREASE;
                randomEvent = getRandomInt(1, 100);

                // display time
                printTime(time);

                if (time >= EVE) {
                    print("It is getting late. You should consider retracing your steps. ");
                }

                // choose direction
                direction = chooseDirection(time);

                if (direction.equals("GO HOME") || direction.equals("LEAVE")) {
                    inForest = false;
                    timeIncrease = 4 * DEFAULT_TIME_INCREASE;
                } else {

                    if (randomEvent > 00 && randomEvent <= 95) {
                        plantsInv = genEncounter(plantsInv);
                        timeIncrease = DEFAULT_TIME_INCREASE;
                    } else if (randomEvent > 95 && randomEvent <= 100) {
                        findRareEvent();
                        timeIncrease = 2 * DEFAULT_TIME_INCREASE;
                    } else {
                        print("Error. Random out of bounds. ");
                    }
                }

                // increment time
                time += timeIncrease;

            } // END TIME

            printTime(time);
            alive = printEndOfDayResult(time);
            day++;

            saveProgram = saveGamePrompt();
            if (saveProgram == 'y') {

                resetNumberOfSaves();
                saveNewCharacter(initSave(name, day, plantsInv));

                return; // EXIT game
            }
        } // END DAYS

        return;
    } // END exploreTheForest

    /*****************************************************************************************************/
    // Forest Gameplay Loop Methods

    // lets the user choose a direction to go in
    //
    public static String chooseDirection(int time) {
        String input;
        final int END_OF_DAY_WARNING = 1900, EVE = 1700;

        print("You can choose any of these paths \nPath North (GO NORTH) \nPath East (GO EAST) \nPath South (GO SOUTH) \nPath West (GO WEST) ");

        // if its late
        if (time >= END_OF_DAY_WARNING) {
            print("You really should take the path home. (GO HOME)");
        } else if (time >= EVE) {
            print("It's getting late. Do you wanna take the path home? (GO HOME)");
        }

        input = inputString("Which way will you go? ");

        // standard directions
        if (input.equals("GO NORTH")) {
            return "NORTH";
        } else if (input.equals("GO EAST")) {
            return "EAST";
        } else if (input.equals("GO SOUTH")) {
            return "SOUTH";
        } else if (input.equals("GO WEST")) {
            return "WEST";
        } else if (input.equals("GO HOME")) {
            return "GO HOME";
        } else if (input.equals("LEAVE")) {
            return "LEAVE";
        } else {
            System.out.println("Please type in something in the brackets, carefully. ");
            return chooseDirection(time);
        }
    } // END chooseDirection

    // prints the messages that occur at the end of the day
    //
    public static boolean printEndOfDayResult(int time) {
        final int NIGHT_TIME = 2100;
        boolean survivedTheForest = true;

        if (time < NIGHT_TIME) {
            print("It is late. You got home, hung up your herbs and any mushrooms to dry, and went to bed. Goodnight. ");
        } else if (time == NIGHT_TIME) {
            print("You lost half of your herbs but managed to get back safely. The forest's paitence wanes. ");
        } else {
            print("You were in the forest for too long. The spiders got to you. ");
            survivedTheForest = false;
        }

        return survivedTheForest;
    } // END printEndOfDayResult

    // creates an encounter based on RNG.
    //
    public static Plant[] genEncounter(Plant[] plantInv) {
        int randomEvent = getRandomInt(1, 100);

        if (randomEvent <= 60) {
            Plant herb = findHerb();

            if (!isPlantInvFull(plantInv)) {
                plantInv = addPlantToArr(plantInv, herb);
                plantInv = addPlantToArr(plantInv, herb);
                print("You collected the herb and added it to your bag. ");
            } else {
                print("You weren't able to collect it. Your bag was full. ");
            }
        } else if (randomEvent > 60 && randomEvent <= 90) { // check if animal is sick and heal
            Animal animal = findAnimal();
            if (canHealAnimal(plantInv, animal)) {
                plantInv = removePlantFromArr(plantInv, animal);
                System.out.println("You managed to heal the animal. ");
            } else if (!getAnimalHerbReq(animal).equals("")) {
                System.out.println(
                        "You don't have the correct herbs to heal this animal. You had to leave unfortunately. ");
            }
        } else if (randomEvent > 90 && randomEvent <= 100) {
            print("You found nothing of interest. ");
        } else {
            print("Error. Random out of bounds. ");
        }

        return plantInv;
    } // END getEncounter

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

    // asks the user whether to save the game
    //
    public static char saveGamePrompt() throws IOException {
        char saveGame = 'N';

        saveGame = inputChar("Do you want to save and exit the game (y/N))? \n");

        // TODO add a check if the inputted character is allowed

        if (saveGame != 'Y' || saveGame != 'N') {
            System.out.println("Please input y for yes or N for no");
            return saveGamePrompt();
        }

        return saveGame;
    } // END saveGamePrompt

    /*****************************************************************************************************/
    // Encounters

    // generates a herb randomly
    //
    public static Plant findHerb() {

        Plant plant = makePlantsArr()[generateRandomIndex(20)];

        System.out.printf("You found a herb!!%n It was a %s %n", plant.name);

        return plant;
    } // END findHerb

    // generates an animal randomly
    //
    public static Animal findAnimal() {
        Animal animal = makeAnimalsArr()[generateRandomIndex(20)];
        String name = getAnimalName(animal);

        System.out.printf("You found a %s.   %n", name);

        System.out.println(getAnimalInteraction(animal));

        return animal;
    } // END findAnimal

    // randomly selects a rare event
    //
    public static void findRareEvent() {
        int randomEvent = getRandomInt(1, 100);

        // RNG table for rare events
        if (randomEvent <= 50) {
            System.out.println("You got stuck in a bush. It took a while to get out, you got scratched. ");
        } else if (randomEvent > 50 && randomEvent <= 100) {
            System.out.println("You listened to the birds chriping. It was quite pleasant. ");
        } else {
            print("Error. Random out of bounds. ");
        }

        return;
    } // END findRareEvent

    /*****************************************************************************************************/
    // General Procedures

    // calls Scanner to receive a kb input
    //
    public static String inputString(String message) { // TODO update uses to account for being uppercase
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

    // creates a random integer between the bounds. min and max inclusive
    //
    public static int getRandomInt(int min, int max) {
        Random rand = new Random();
        int randNum = rand.nextInt(max + 1 - min) + min;

        return randNum;
    } // END getRandomInt

    // prints the text input
    //
    public static void print(String message) {
        System.out.println(message);
        return;
    } // END print

    // generates a random index based on the length of an array
    //
    public static int generateRandomIndex(int length) {
        return getRandomInt(0, (length - 1));
    } // END generateRandomIndex

    // General File IO

    // reads all of the lines of a file, and returns them in a string array
    //
    public static String[] readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String[] s = new String[countLines(fileName)];
        int index = 0;
        String fileInput = br.readLine();

        if (fileInput == null) {
            br.close();
            return null;
        }

        // loops through every line in the file and adds it to the array
        while (fileInput != null) {
            s[index] = fileInput;

            fileInput = br.readLine();
            index++;
        } // END while

        br.close();
        return s;
    } // END readFile

    // counts the number of lines in a given file
    //
    public static int countLines(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        int lines = 0;
        String line = br.readLine();

        while (line != null && !line.equals("")) {
            line = br.readLine();
            lines++;
        } // END while

        br.close();

        return lines;
    } // END countLines

    // writes a given string into a given file
    //
    public static void writeFile(String fileName, String s, boolean append) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(fileName, append));

        pw.println(s);

        pw.close();
        return;
    } // END writeFile

    // writes a given string array into a given file
    //
    public static void writeFile(String fileName, String[] s) throws IOException {

        writeFile(fileName, s[0], false);
        for (int i = 1; i < s.length; i++) {
            writeFile(fileName, s[i], true);
        } // END for

        return;
    } // END writeFile

    // removes a specific index from a String array
    //
    public static String[] removeStringFromArr(String[] arr, String searchKey) {
        final int arrLength = arr.length;
        boolean found = false;
        String[] newArr = new String[arrLength - 1];
        int i = 0;

        if (arrLength == 1) {
            return null;
        }

        for (int newArrIndex = 0; (newArrIndex < arrLength - 1) && i < arrLength; newArrIndex++) {
            if (arr[i].equals(searchKey) && !found) {
                i++;
                found = true;
            }

            newArr[newArrIndex] = arr[i];

            i++;

        } // END for

        return newArr;
    } // END removeStringFromArr

    // removes a specified line from a file
    //
    public static void removeStringFromFile(String fileName, String stringToRemove) throws IOException {

        // load file as String[]
        String[] fileContents = readFile(fileName);

        // search through String[] and remove string to remove
        fileContents = removeStringFromArr(fileContents, stringToRemove);

        // write to file as String[]
        if (fileContents == null) {
            writeFile(fileName, "", false);
        } else {
            writeFile(fileName, fileContents);
        }

        return;
    } // END removeStringFromFile

    /*****************************************************************************************************/
    // Misc Procedures

    // creates the animals array
    //
    public static Animal[] makeAnimalsArr() {
        Animal[] animals = new Animal[20];
        final String wolfInteraction = "The Wolf snarls at you, trying to chase you away. You managed to get away with nothing to spare. ",
                sickWolfInteraction = "The Wolf snarls at you before fainting from exhaustaion. It woke up after you left, it pleasantly surprised, so far as a wolf experiences emotions as a human does. ",
                deerInteraction = "The Deer quickly ran away. ",
                sickDeerInteraction = "You found the Deer laying on the floor, in desparate need of assistance. \nYou tried to heal it the best you could so that it would be able to survive. \nYou helped it stand and left promptly. ",
                bearInteraction = "You tiptoed past the giant creature, hoping it wouldn't smell the fear your cold swear betrayed. \nYou got away. ",
                sickBearInteraction = "The Bear pawed at you weakly as you gathered the courage to face this beast. \nIt stopped being as aggressive when it noticed you were trying to help. You did your best and left immediately. ";

        animals[0] = initAnimal("Deer", false, true, false, "", deerInteraction);
        animals[1] = initAnimal("Deer", false, true, false, "", deerInteraction);
        animals[2] = initAnimal("Deer", false, true, false, "", deerInteraction);
        animals[3] = initAnimal("Deer", false, true, false, "", deerInteraction);
        animals[4] = initAnimal("Deer", false, true, false, "", deerInteraction);
        animals[5] = initAnimal("Wolf", false, true, true, "", wolfInteraction);
        animals[6] = initAnimal("Wolf", false, true, true, "", wolfInteraction);
        animals[7] = initAnimal("Wolf", false, true, true, "", wolfInteraction);
        animals[8] = initAnimal("Bear", true, true, true, "", bearInteraction);
        animals[9] = initAnimal("Bear", true, true, true, "", bearInteraction);
        animals[10] = initAnimal("Sick Bear", true, false, false, "Mushroom", sickBearInteraction);
        animals[11] = initAnimal("Injured Bear", true, false, true, "Oak Bark", sickBearInteraction);
        animals[12] = initAnimal("Injured Wolf", false, false, false, "Birch Bark", sickWolfInteraction);
        animals[13] = initAnimal("Injured Wolf", false, false, false, "Birch Bark", sickWolfInteraction);
        animals[14] = initAnimal("Injured Wolf", false, false, false, "Birch Bark", sickWolfInteraction);
        animals[15] = initAnimal("Sick Deer", false, false, false, "Berries", sickDeerInteraction);
        animals[16] = initAnimal("Sick Deer", false, false, false, "Berries", sickDeerInteraction);
        animals[17] = initAnimal("Sick Deer", false, false, false, "Berries", sickDeerInteraction);
        animals[18] = initAnimal("Injured Deer", false, false, false, "Oak Bark", sickDeerInteraction);
        animals[19] = initAnimal("Injured Deer", false, false, false, "Oak Bark", sickDeerInteraction);

        return animals;
    } // END makeAnimalsArr

    // creates a specific arrays of plants to act as a drop pool for whenether the
    // user finds a plant
    //
    public static Plant[] makePlantsArr() {
        Plant[] plants = new Plant[20];

        plants[0] = initPlant("Mint");
        plants[1] = initPlant("Mint");
        plants[2] = initPlant("Berries");
        plants[3] = initPlant("Berries");
        plants[4] = initPlant("Berries");
        plants[5] = initPlant("Birch Bark");
        plants[6] = initPlant("Birch Bark");
        plants[7] = initPlant("Birch Bark");
        plants[8] = initPlant("Birch Bark");
        plants[9] = initPlant("Birch Bark");
        plants[10] = initPlant("Oak Bark");
        plants[11] = initPlant("Oak Bark");
        plants[12] = initPlant("Oak Bark");
        plants[13] = initPlant("Oak Bark");
        plants[14] = initPlant("Oak Bark");
        plants[15] = initPlant("Oak Bark");
        plants[16] = initPlant("Mushroom");
        plants[17] = initPlant("Mushroom");
        plants[18] = initPlant("Mushroom");
        plants[19] = initPlant("Mushroom");

        return plants;
    } // END makePlantsArr

    // checks if a plant array is full
    //
    public static boolean isPlantInvFull(Plant[] plantInv) {
        boolean isFull = true;
        final int length = plantInv.length;

        if ((plantInv[length - 1] == null || getPlantName(plantInv[length - 1]) == null
                || getPlantName(plantInv[length - 1]).equals(" "))
                && length > 0) {
            return false;
        }

        return isFull;
    } // END plantInvFull

    // removes a specific plant from an array by overriding it with the next element
    // in the array
    //
    public static Plant[] removePlantFromArr(Plant[] plantArr, Animal animal) { // TODO make so that it uses a Plant as
                                                                                // a parameter
                                                                                // TODO check if it works, and fix which
                                                                                // element gets removed
        final int highestIndex = plantArr.length;
        boolean swappingValues = false;
        int plantArrayIndex = 0;
        String herbName = getAnimalHerbReq(animal), currHerb = getPlantName(plantArr[plantArrayIndex]);

        // moves everything back by one, after herbName is found
        while (!swappingValues && plantArrayIndex < highestIndex) { // TODO convert into 1 for loop, instead of a while
                                                                    // and for loop
            currHerb = getPlantName(plantArr[plantArrayIndex]);

            // replacement check
            if (swappingValues || currHerb.equals(herbName)) {
                swappingValues = true;

                // replaces the current index with the next one and loops through the remainder
                // of the array
                for (int i = plantArrayIndex; i < highestIndex - 1; i++) {
                    plantArr[plantArrayIndex] = plantArr[plantArrayIndex + 1];
                }

                // sets the last element to null
                plantArr[highestIndex - 1] = null;

            }
            plantArrayIndex++;
        }

        return plantArr;
    } // END removePlantFromArray

    // adds a plant to a plant array
    //
    public static Plant[] addPlantToArr(Plant[] plantArr, Plant plant) {
        int i = 0;
        boolean added = false;

        // loop to first empty spot and set as plant
        while (i < plantArr.length && !added) {
            if (plantArr[i] == null) {
                plantArr[i] = plant;
                added = true;
            }
            i++;
        }

        return plantArr;
    } // END addPlantToArray

    // checks if a specific plant is in a plant array
    //
    public static boolean plantInArr(Plant[] plantArr, String plantName) {
        boolean found = false;

        for (int i = 0; i < plantArr.length; i++) {
            if (!(plantArr[i] == null) && getPlantName(plantArr[i]).equals(plantName)) {
                found = true;
            }
        }

        return found;
    } // END plantInArray

    // converts an array of Save records to an array of their CSVs
    //
    public static String[] saveArrToCSVArr(Save[] s) { // TODO unnecessary
        int arrLen = s.length;
        String[] stringArr = new String[arrLen];

        for (int i = 0; i < arrLen; i++) {
            stringArr[i] = saveToCSV(s[i]);
        } // END for

        return stringArr;
    } // END saveArrToCSVArr

    /*****************************************************************************************************/
    // Plant Record procedures

    // initialises a Plant record
    //
    public static Plant initPlant(String name) {
        Plant p = new Plant();

        p.name = name;

        return p;
    } // END initPlant

    // returns the inputted plant's name field
    //
    public static String getPlantName(Plant p) {
        return p.name;
    } // END getPlantName

    // converts a Plant record to a String
    //
    public static String plantToString(Plant p) {
        return String.format("%s", p.name);
    } // END plantToString

    /*****************************************************************************************************/
    // Animal Record procedures

    // initialises an Animal object
    //
    public static Animal initAnimal(String name, boolean nocturnal, boolean healthy, boolean aggresive,
            String herbReq, String interactionText) {
        Animal a = new Animal();

        a.name = name;
        a.nocturnal = nocturnal;
        a.healthy = healthy;
        a.aggresive = aggresive;
        a.herbRequired = herbReq;
        a.interactionText = interactionText;

        return a;
    } // END initAnimal

    // gets the animal's required herb
    //
    public static String getAnimalHerbReq(Animal a) {
        return a.herbRequired;
    } // END getAnimalHerbReq

    // returns the inputted animal's name field
    //
    public static String getAnimalName(Animal a) {
        return a.name;
    } // END getAnimalName

    // returns the animal interaction
    //
    public static String getAnimalInteraction(Animal a) {
        return a.interactionText;
    } // END getAnimalInteraction

    // checks if a plant array contains the herbRequired field for the animal to be
    // able to heal them
    //
    public static boolean canHealAnimal(Plant[] plantArr, Animal animal) {
        boolean sick = !animal.healthy, healable = false;
        String herbReq = animal.herbRequired;

        if (plantInArr(plantArr, herbReq)) {
            healable = true;
        }

        return (healable && sick);
    } // END canHealAnimal

    /*****************************************************************************************************/
    // Save Record Procedures

    // create a save record
    //
    public static Save initSave(String name, int day, Plant[] plantInv) {
        Save s = new Save();

        s.name = name;
        s.day = day;
        s.plantInventory = plantInv;
        Save.numberOfSaves++; // this can become quite large if not managed properly

        // System.out.printf("Number of saves: %d %n", Save.numberOfSaves);

        return s;
    } // END initSave

    // resets the static numberOfSaves field to zero
    //
    public static void resetNumberOfSaves() {

        Save.numberOfSaves = 0;
        return;
    } // END resetNumberOfSaves

    // returns the Save name field
    //
    public static String getSaveName(Save s) {
        return s.name;
    } // END getSaveName

    // returns the plant inventory Plant[]
    //
    public static Plant[] getSavePlantInventory(Save s) {
        return s.plantInventory;
    } // END getSavePlantInventory

    // returns the Save day field
    //
    public static int getSaveDay(Save s) {
        return s.day;
    } // END getSaveDay

    // converts a Save record to a CSV
    //
    public static String saveToCSV(Save s) {
        String saveString = "";
        Plant[] pInv = s.plantInventory;
        int length = pInv.length;

        saveString += s.name + "," + s.day + ",";

        for (int i = 0; i < length; i++) {
            Plant p = pInv[i];
            if (p == null) {
                saveString += " ";
            } else {
                saveString += plantToString(p);
            }

            if (i < length - 1) {
                saveString += "-";
            }
        } // END for

        return saveString;
    } // END saveToCSV

    // converts a line in a CSV file to a Save record object
    //
    public static Save csvToSave(String csv) {
        String[] csvArray = csv.split(",");
        String name = csvArray[0];
        int day = Integer.parseInt(csvArray[1]);
        String[] plantInvString = csvArray[2].split("-");
        int arrLen = plantInvString.length;
        Plant[] plantInv = new Plant[arrLen];

        for (int i = 0; i < arrLen; i++) {
            String plantName = plantInvString[i];

            if (plantName != " ") {
                plantInv[i] = initPlant(plantName);
            }
        } // END for

        return initSave(name, day, plantInv);
    } // END csvToSave

    /*****************************************************************************************************/
    // Save File IO

    // returns the path to the Save file
    //
    public static String GET_SAVE_FILE_NAME() {
        return "Miniproject/Saves.csv";
    } // END GET_SAVE_FILE_NAME

    // writes a single Save object into a file
    //
    public static void writeSaveToSaveCSV(Save s, boolean append) throws IOException {
        writeFile(GET_SAVE_FILE_NAME(), saveToCSV(s), append);

        return;
    } // END writeSaveToSaveCSV

    // writes multiple save records to a file
    //
    public static void saveNewCharacter(Save newSave) throws IOException {
        String fileName = GET_SAVE_FILE_NAME();

        writeFile(fileName, saveToCSV(newSave), true);
        return;
    } // END saveNewCharacter

    // loop through saves array and create Save records for each line
    //
    public static Save[] readSavesInSavesCSV() throws IOException {
        String fileName = GET_SAVE_FILE_NAME();
        String[] fileOutput = readFile(fileName);
        int fileLength = countLines(fileName);
        Save[] saves = new Save[fileLength];

        for (int i = 0; i < fileLength; i++) {
            String fileOutputI = fileOutput[i];
            saves[i] = csvToSave(fileOutputI);

        } // END for
        return saves;
    } // END readSavesInSavesCSV

    // show characters for the user to choose, and use this to determine which
    // character to load
    public static Save showPreExistingCharacters() throws IOException {
        Save[] saves = readSavesInSavesCSV();
        int numberOfSaves = saves.length, userChoice = -1;
        String query = "Choose one of the below characters: \n";
        Save character;

        // checks if the array is empty
        if (saves[0] == null) { // TODO check necesary
            // output that there are no characters to chose from
            return null;
        }

        // creates a string showing all the characters and possible
        for (int i = 0; i < numberOfSaves; i++) {
            query += String.format("%d) %s%n", i, getSaveName(saves[i]));
        } // END for

        // show names of previous characters, and ask for input
        while (!(userChoice >= 0 && userChoice < numberOfSaves)) {
            userChoice = inputInt(query);
        } // END while

        character = saves[userChoice];

        // remove the index entered
        resetNumberOfSaves();
        removeStringFromFile(GET_SAVE_FILE_NAME(), saveToCSV(character));

        return character;
    } // END showPreExistingCharacters

} // END forest