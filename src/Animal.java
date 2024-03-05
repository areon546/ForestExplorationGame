
public class Animal {
    String name;
    boolean nocturnal = false;
    boolean healthy = false;
    boolean aggresive = false;
    Plant herbRequired;
    String interactionText;

    // initialises an Animal object
    //
    public Animal(String name, boolean nocturnal, boolean healthy, boolean aggresive,
            String herbReq, String interactionText) {

        this.name = name;
        this.nocturnal = nocturnal;
        this.healthy = healthy;
        this.aggresive = aggresive;
        this.herbRequired = Plant.getPlant(herbReq);
        this.interactionText = interactionText;
    } // END initAnimal

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

    public boolean getHealthy() {
        return this.healthy;
    }
}