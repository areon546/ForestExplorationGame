import java.io.IOException;

public class ForestGame extends myDefaults {

    public static void main(String[] args) throws IOException {

        // create the save file csv file

        // start game
        enterTheForest();

        return;

    }

    /*****************************************************************************************************/
    // Forest Game

    // creates the character and asks whether to use a character from a previous
    // game or not
    //
    public static void enterTheForest() throws IOException {
        SaveFile sF = SaveFile.createSaveFile(GET_SAVE_FILE_PATH());
        String choice = "";

        // load save file

        // ask if they wanna start a new character or a previous one

        print("Welcome to the Forest. ");
        print("Your goal is to get out alive. ");

        // choose between new character or old one
        choice = inputString(
                "Do you want to make a new character, or choose an pre-existing character? \nA) NEW \nB) PRE-EXISTING\n")
                .toUpperCase();

        if (choice.equals("A")) { // new character
            newCharacter(sF);
            return;
        } else if (choice.equals("B")) { // choose a character
            if (sF.getLineCount() > 1) {
                chooseCharacter(sF);
                return;
            } else {
                System.out.println(
                        "There are no characters for you to select. Please make a new character or type 'XXX' to terminate. ");
                newCharacter(sF);
                return;
            }
        }

        System.out.println("ERROR PROGRAM TERMINATES");
        return;
    } // END enterTheForest

    public static void newCharacter(SaveFile sF) throws IOException { // TODO add check if the character added already
                                                                      // exists in the save file
        String name = "";
        final int MAX_PLANT_INV_LEN = 30;
        Plant[] plantsInv = new Plant[MAX_PLANT_INV_LEN];

        name = inputString("Enter Name: ");

        if (name.equals("XXX")) {
            return;
        }

        exploreTheForest(sF, new Save(name, 0, plantsInv));
        return;
    }

    public static void chooseCharacter(SaveFile sF) throws IOException {
        Save character = showPreExistingCharacters(sF);
        exploreTheForest(sF, character);
        return;
    }

    // loads the game character, and explores the forest on a daily loop
    //
    public static void exploreTheForest(SaveFile sF, Save characterSave) throws IOException {
        boolean alive = true;
        char saveProgram = 'N';
        String name = characterSave.getName();
        final int MAX_DAYS = 10;
        int day = characterSave.getDay();
        Plant[] plantsInv = characterSave.getPlantInventory();

        print(String.format(
                "Welcome %s, you are here to collect herbs for the town and preserve the wildlife. %nIf you anger the forest, you will not leave. %nIf you think it is getting late, leave. %nYou best not stay for too long. ",
                name));

        while (day < MAX_DAYS && alive) { // DAYS loop
            int time = goThroughDay(day, plantsInv);

            alive = printEndOfDayResult(time);
            day++;

            saveProgram = saveGamePrompt();
            if (saveProgram == 'Y') {
                sF.saveCharacter(characterSave); // TODO when saving, characterSave isn't updated with the new
                                                 // information since goThroughDay doesn't change the value of
                                                 // characterSave

                return; // EXIT game
            }
        } // END DAYS

        return;
    } // END exploreTheForest

    public static int goThroughDay(int day, Plant[] plantsInv) {
        final int DEFAULT_TIME_INCREASE = 50, START_OF_DAY = 1400, END_OF_DAY = 2400;
        int time, timeIncrease, seed;
        boolean inForest = true;
        String direction;

        print(String.format("%nWelcome to day %d", (day + 1)));
        time = START_OF_DAY;
        inForest = true;

        while (time < END_OF_DAY && inForest) { // TIME loop

            // resets variables
            timeIncrease = DEFAULT_TIME_INCREASE;
            seed = getRandomInt(1, 100);

            // display time
            printTime(time);
            displayTimeBasedMessages(time);

            // choose direction
            direction = chooseDirection(time);

            if (direction.equals("GO HOME") || direction.equals("LEAVE")) {
                inForest = false;
                timeIncrease = 4 * DEFAULT_TIME_INCREASE;
            } else {

                // TODO generateEvent(seed, plantsInv); // return plantsInv and timeIncrease
                if (seed > 00 && seed <= 95) {
                    plantsInv = genEncounter(plantsInv);
                    timeIncrease = DEFAULT_TIME_INCREASE;
                } else if (seed > 95 && seed <= 100) {
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

        return time;
    }

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
                plantInv = addPlantToArr(plantInv, herb, 2);
                print("You collected the herb and added it to your bag. ");
            } else {
                print("You weren't able to collect it. Your bag was full. ");
            }
        } else if (randomEvent > 60 && randomEvent <= 90) { // check if animal is sick and heal
            Animal animal = findAnimal();
            boolean healthy = animal.getHealthy();

            // if animal isn't healthy, try to heal it
            if (!healthy) {
                if (canHealAnimal(plantInv, animal)) {
                    plantInv = removePlantFromArr(plantInv, animal);
                    System.out.println(animal.getHealedInteractionText());
                } else {
                    System.out.println(animal.getAbandonedInteractionText());
                }
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
    public static void printTime(int time) {
        int hours = time / 100, minutes = time % 100;
        String displayedTime;

        minutes = (60 * minutes) / 100;
        displayedTime = String.format("%n%nThe current time is: %d:%02d", hours, minutes);

        if (minutes >= 60 && hours >= 25 && hours <= -1) {
            displayedTime = "%n%nError, time out of bounds. ";
        } else {
            print(displayedTime);
        }

        return;
    } // END printTime

    public static void displayTimeBasedMessages(int time) {
        final int EVE = 1700;

        if (time >= EVE) {
            print("It is getting late. You should consider retracing your steps. ");
        }

    }

    // asks the user whether to save the game
    //
    public static char saveGamePrompt() {
        char saveGame = 'N';

        saveGame = inputChar("Do you want to save and exit the game (y/N))? \n");

        if (saveGame != 'Y' && saveGame != 'N') {
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

        Plant plant = Plant.getRandomPlant();

        System.out.printf("You found a herb!!%n It was a %s %n", plant.getName());

        return plant;
    } // END findHerb

    // generates an animal randomly
    //
    public static Animal findAnimal() {
        Animal animal = Animal.getRandomAnimal();
        String name = animal.getName();

        System.out.printf("You found a %s.   %n", name);
        System.out.println(animal.getInteractionText());

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
    // Misc Procedures

    // checks if a plant array is full
    //
    public static boolean isPlantInvFull(Plant[] plantInv) {
        boolean isFull = true;
        final int length = plantInv.length;

        if ((plantInv[length - 1] == null || plantInv[length - 1].getName() == null
                || plantInv[length - 1].getName().equals(" "))
                && length > 0) {
            return false;
        }

        return isFull;
    } // END plantInvFull

    // removes a specific plant from an array by overriding it with the next element
    // in the array
    //
    public static Plant[] removePlantFromArr(Plant[] plantArr, Animal animal) {
        final int highestIndex = plantArr.length;
        boolean swappingValues = false;
        int swappingIndex = highestIndex;
        Plant requiredHerb = animal.getHerbRequired();

        // find a herb

        // decrease its quantity by 1
        // if new quantity is zero, set to null and move everything back by one

        for (int i = 0; i < highestIndex; i++) {
            Plant p = plantArr[i];

            // check if p is the plant we need
            if (p.equals(requiredHerb)) {
                p.decreaseNumberOfPlant(1);

                if (p.getNumberOfPlant() == 0) {
                    swappingValues = true;
                    swappingIndex = i;
                }
            }

        }

        // move everything back by one
        if (swappingValues) {
            for (int i = swappingIndex; i < highestIndex; i++) {
                plantArr[i] = plantArr[i + 1];
            }

            plantArr[highestIndex - 1] = null;
        }

        return plantArr;
    } // END removePlantFromArray

    // adds a plant to a plant array
    //
    public static Plant[] addPlantToArr(Plant[] plantArr, Plant plant, int quantity) {
        int i = 0;
        boolean added = false;

        // loop to first empty spot and set as plant, or the location of that plant in
        // the array
        while (i < plantArr.length && !added) {
            if (plantArr[i] == null || plantArr[i].getName().equals(plant.getName())) {
                plantArr[i] = plant;
                plant.increaseNumberOfPlant(quantity);
                added = true;
            }
            i++;
        }

        return plantArr;
    } // END addPlantToArray

    // checks if a plant array contains the herbRequired field for the animal to be
    // able to heal them
    //
    public static boolean canHealAnimal(Plant[] plantArr, Animal a) {
        boolean sick = !a.getHealthy(), healable = false;
        String herbReq = a.getHerbRequired().getName();

        if (plantInArr(plantArr, herbReq)) {
            healable = true;
        }

        return (healable && sick);
    } // END canHealAnimal

    // checks if a specific plant is in a plant array
    //
    public static boolean plantInArr(Plant[] plantArr, String plantName) {
        boolean found = false;

        for (int i = 0; i < plantArr.length; i++) {
            if (!(plantArr[i] == null) && plantArr[i].getName().equals(plantName)) {
                found = true;
            }
        }

        return found;
    } // END plantInArray

    // returns the path to the Save file
    //
    public static String GET_SAVE_FILE_PATH() {
        return "Saves.csv";
    } // END GET_SAVE_FILE_PATH

    // show characters for the user to choose, and use this to determine which
    // character to load
    public static Save showPreExistingCharacters(SaveFile sF) {
        Save[] saves = sF.readSaves();
        int numberOfSaves = saves.length, userChoice = -1;
        String input = "Choose one of the below characters: \n";
        Save character;

        // checks if the array is empty
        if (saves[0] == null) {
            // output that there are no characters to chose from
            return null;
        }

        // creates a string showing all the characters and possible
        for (int i = 0; i < numberOfSaves; i++) {
            input += String.format("%d) %s%n", i, saves[i].getName());
        } // END for

        // show names of previous characters, and ask for input
        while (!(userChoice >= 0 && userChoice < numberOfSaves)) {
            userChoice = inputInt(input);
        } // END while

        character = saves[userChoice];

        return character;
    } // END showPreExistingCharacters

}
