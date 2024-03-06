
public class Save {
    String name;
    int day = 0, time = 1400;
    Plant[] plantInventory;

    // create a Save object
    public Save(String name, int day, Plant[] plantInv) {
        this.name = name;
        this.day = day;
        this.plantInventory = plantInv;
    } // END initSave

    // returns the Save name field
    //
    public String getName() {
        return this.name;
    } // END getSaveName

    // returns the plant inventory Plant[]
    //
    public Plant[] getPlantInventory() {
        return this.plantInventory;
    } // END getSavePlantInventory

    // returns the Save day field
    //
    public int getDay() {
        return this.day;
    } // END getSaveDay

    /* general methods */

    // converts a Save record to a CSV
    //
    public String toCSV() {
        String saveString = "";
        Plant[] pInv = this.plantInventory;
        int length = pInv.length;

        saveString += this.name + "," + this.day + ",";

        for (int i = 0; i < length; i++) {
            Plant p = pInv[i];
            if (p == null) {
                saveString += " ";
            } else {
                saveString += p.toString();
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
            String plantName = plantInvString[i].split("_")[0];

            if (plantName != " ") {
                plantInv[i] = new Plant(plantName);
            }
        } // END for

        return new Save(name, day, plantInv);
    } // END csvToSave

}
