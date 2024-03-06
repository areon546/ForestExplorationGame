public class Plant {
    private static Plant[] plants = null;
    private String name;
    private int numberOfPlant = 1;

    // creates a plant object
    public Plant(String name) {
        this.name = name;
    } // END Plant

    // creates a plant object
    public Plant(String name, int number) {
        this.name = name;
        this.numberOfPlant = number;
    } // END Plant

    public int getNumberOfPlant() {
        return numberOfPlant;
    }

    public void increaseNumberOfPlant(int change) {
        this.numberOfPlant += change;
    }

    public void decreaseNumberOfPlant(int change) {
        this.numberOfPlant -= change;
    }

    // converts a Plant record to a String
    //
    @Override
    public String toString() {
        return String.format("%s_%d", this.name, this.numberOfPlant);
    } // END toString

    public static Plant stringToPlant(String s) {
        // string: %s_%d
        String[] components = s.split("_");

        return new Plant(components[0], Integer.parseInt(components[1]));
    }

    public boolean equals(Plant p) {
        if (p == null) {
            return false;
        }
        return (this.name.equals(p.getName()));
    }

    // returns the inputted plant's name field
    //
    public String getName() {
        return this.name;
    } // END getName

    /******************************/
    // Static methods

    // this method returns a specific plant from a list of plants
    public static Plant getPlant(String searchName) {
        Plant.generatePlants();
        // loop through plants to find the one with the name

        for (Plant pi : plants) {
            if (pi != null && pi.name.equals(searchName)) {
                return pi;
            }
        }

        return null;
    }

    // creates a specific arrays of plants to act as a drop pool for whenether the
    // user finds a plant
    //
    public static Plant[] generatePlants() {
        if (Plant.plants == null) {
            Plant.plants = new Plant[20];
            Plant.plants[0] = new Plant("Mint");
            Plant.plants[1] = new Plant("Mint");
            Plant.plants[2] = new Plant("Berries");
            Plant.plants[3] = new Plant("Berries");
            Plant.plants[4] = new Plant("Berries");
            Plant.plants[5] = new Plant("Birch Bark");
            Plant.plants[6] = new Plant("Birch Bark");
            Plant.plants[7] = new Plant("Birch Bark");
            Plant.plants[8] = new Plant("Birch Bark");
            Plant.plants[9] = new Plant("Birch Bark");
            Plant.plants[10] = new Plant("Oak Bark");
            Plant.plants[11] = new Plant("Oak Bark");
            Plant.plants[12] = new Plant("Oak Bark");
            Plant.plants[13] = new Plant("Oak Bark");
            Plant.plants[14] = new Plant("Oak Bark");
            Plant.plants[15] = new Plant("Oak Bark");
            Plant.plants[16] = new Plant("Mushroom");
            Plant.plants[17] = new Plant("Mushroom");
            Plant.plants[18] = new Plant("Mushroom");
            Plant.plants[19] = new Plant("Mushroom");
        }

        return Plant.plants;
    } // END generatePlants

    public static Plant getRandomPlant() {
        return generatePlants()[myDefaults.generateRandomIndex(20)];
    }
}