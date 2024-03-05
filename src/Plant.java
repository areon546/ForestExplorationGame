import java.util.Stack;

public class Plant {
    static Stack<Plant> plants = new Stack<Plant>();
    String name;
    int numberOfPlant = 1;

    // creates a plant object
    public Plant(String name) {
        this.name = name;
    } // END Plant

    // creates a plant object
    public Plant(String name, int number) {
        this.name = name;
        this.numberOfPlant = number;
    } // END Plant

    // converts a Plant record to a String
    //
    public String toString() {
        return String.format("%s", this.name);
    } // END toString

    // returns the inputted plant's name field
    //
    public String getName() {
        return this.name;
    } // END getName

    /******************************/
    // Static methods

    // this method returns a specific plant from a list of plants
    public static Plant getPlant(String searchName) {
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
    public static void makePlantsArr() {
        plants.add(new Plant("Mint", 2));
        plants.add(new Plant("Berries", 3));
        plants.add(new Plant("Birch Bark", 5));
        plants.add(new Plant("Oak Bark", 6));
        plants.add(new Plant("Mushroom", 4));
    } // END makePlantsArr
}