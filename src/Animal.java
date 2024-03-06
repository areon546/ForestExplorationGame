public class Animal {
    private static Animal[] animals = null;

    private String name;
    private boolean healthy = false;
    private boolean nocturnal = false, aggresive = false;
    private Plant herbRequired;
    private String interactionText, healedInteractionText, abandonedInteractionText;

    // initialises an Animal object
    //
    public Animal(String name, String interactionText) {
        this.name = name;
        this.healthy = true;
        this.interactionText = interactionText;
    } // END Animal

    // initialises an Animal object
    //
    public Animal(String name, boolean healthy,
            String herbReq, String interactionText, String healedText, String abandonedInteractionText) {

        this.name = name;
        // this.nocturnal = nocturnal;
        this.healthy = healthy;
        // this.aggresive = aggresive;
        this.herbRequired = Plant.getPlant(herbReq);
        this.interactionText = interactionText;
        this.healedInteractionText = healedText;
    } // END Animal

    // gets the animal's required herb
    //
    public Plant getHerbRequired() {
        return this.herbRequired;
    } // END getAnimalHerbReq

    // returns the inputted animal's name field
    //
    public String getName() {
        return this.name;
    } // END getAnimalName

    // returns the animal interaction
    //
    public String getInteractionText() {
        return this.interactionText;
    } // END getAnimalInteraction

    public String getHealedInteractionText() {
        return healedInteractionText;
    }

    public String getAbandonedInteractionText() {
        return abandonedInteractionText;
    }

    public boolean getHealthy() {
        return this.healthy;
    }

    public static Animal[] getAnimals() {
        if (Animal.animals == null) {
            makeAnimalsArray();
        }
        return Animal.animals;
    }

    // creates the animals array
    //
    public static Animal[] makeAnimalsArray() {
        final String wolf = "The Wolf snarls at you, trying to chase you away. You managed to get away with nothing to spare. ",
                sickWolf = "The Wolf snarls at you before fainting from exhaustaion. ",
                deer = "The Deer quickly ran away. ",
                sickDeer = "You found the Deer laying on the floor, in desparate need of assistance.",
                bear = "You tiptoed past the giant creature, hoping it wouldn't smell the fear your cold swear betrayed. \nYou got away. ",
                sickBear = "The Bear pawed at you weakly as you gathered the courage to face this beast. \nIt stopped being as aggressive when it noticed you were trying to help. ",
                healedWolf = "It woke up after you left, it pleasantly surprised, so far as a wolf experiences emotions as a human does. ",
                healedDeer = "\nYou tried to heal it the best you could so that it would be able to survive. \nYou helped it stand and left promptly. ",
                healedBear = "You cautiously drew near and managed to help. You did what you could and left immediately. ",
                abandonedWolf = "You don't have the correct herbs to heal this animal. You had to leave the wolf. ",
                abandonedDeer = "You don't have the correct herbs to heal this animal. You had to leave the deer. ",
                abandonedBear = "You don't have the correct herbs to heal this animal. You had to leave the bear. ";

        Animal.animals = new Animal[20];
        Animal.animals[0] = new Animal("Deer", deer);
        Animal.animals[1] = new Animal("Deer", deer);
        Animal.animals[2] = new Animal("Deer", deer);
        Animal.animals[3] = new Animal("Deer", deer);
        Animal.animals[4] = new Animal("Deer", deer);
        Animal.animals[5] = new Animal("Wolf", wolf);
        Animal.animals[6] = new Animal("Wolf", wolf);
        Animal.animals[7] = new Animal("Wolf", wolf);
        Animal.animals[8] = new Animal("Bear", bear);
        Animal.animals[9] = new Animal("Bear", bear);
        Animal.animals[10] = new Animal("Sick Bear", false, "Mushroom", sickBear, healedBear, abandonedBear);
        Animal.animals[11] = new Animal("Injured Bear", false, "Oak Bark", sickBear, healedBear, abandonedBear);
        Animal.animals[12] = new Animal("Injured Wolf", false, "Birch Bark", sickWolf, healedWolf, abandonedWolf);
        Animal.animals[13] = new Animal("Injured Wolf", false, "Birch Bark", sickWolf, healedWolf, abandonedWolf);
        Animal.animals[14] = new Animal("Injured Wolf", false, "Birch Bark", sickWolf, healedWolf, abandonedWolf);
        Animal.animals[15] = new Animal("Sick Deer", false, "Berries", sickDeer, healedDeer, abandonedDeer);
        Animal.animals[16] = new Animal("Sick Deer", false, "Berries", sickDeer, healedDeer, abandonedDeer);
        Animal.animals[17] = new Animal("Sick Deer", false, "Berries", sickDeer, healedDeer, abandonedDeer);
        Animal.animals[18] = new Animal("Injured Deer", false, "Oak Bark", sickDeer, healedDeer, abandonedDeer);
        Animal.animals[19] = new Animal("Injured Deer", false, "Oak Bark", sickDeer, healedDeer, abandonedDeer);

        return Animal.animals;
    } // END makeAnimalsArr

    public static Animal getRandomAnimal() {

        return Animal.makeAnimalsArray()[myDefaults.generateRandomIndex(20)];
    }

    public static Animal getRandomAnimal(Animal[] animals) {
        return animals[myDefaults.generateRandomIndex(20)];
    }

}