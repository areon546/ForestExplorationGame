import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveFile {
    private static SaveFile singleton;
    private String path;
    private String[] headings;
    private String[] lines;
    private int linesInFile;
    private boolean emtpy = false;

    public static SaveFile createSaveFile(String path) throws IOException {
        if (singleton == null) {
            singleton = new SaveFile(path);
        }

        return singleton;
    }

    private SaveFile(String path) throws IOException {
        this.path = path;
        this.linesInFile = this.countLines();
        if (this.linesInFile > 1) {
            this.lines = new String[this.linesInFile - 1];
            this.readFile(); // assigns headings and lines
        } else {
            this.emtpy = true;
        }

        // read through the save file and place it into the array headings and lines

        // headings = line 1.split(,)
        // lines = line 2 - n, if length >= 2
    }

    // counts the number of lines in a given file
    //
    public int countLines() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(this.path));
        int lines = 0;
        String line = br.readLine();

        while (line != null && !line.equals("")) {
            line = br.readLine();
            lines++;
        } // END while

        br.close();

        return lines;
    } // END countLines

    // reads all of the lines of a file, and returns them in a string array
    //
    public void readFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(this.path));
        int index = 0, fileLength = this.linesInFile;
        String[] s = new String[fileLength];
        String fileInput = br.readLine();

        if (fileInput == null) {
            br.close();
            return;
        }

        // loops through every line in the file and adds it to the array
        while (fileInput != null) {
            s[index] = fileInput;

            fileInput = br.readLine();
            index++;
        } // END while

        br.close();

        this.headings = s[0].split(",");

        if (fileLength > 1) {
            for (int i = 1; i < fileLength; i++) {
                lines[i - 1] = s[i];
            }
        }

    } // END readFile

    // editing the string arrays

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
    public void removeStringFromFile(String fileName, String stringToRemove) throws IOException {

        // load file as String[]
        String[] fileContents = this.lines;

        // search through String[] and remove string to remove
        fileContents = removeStringFromArr(fileContents, stringToRemove);

        // write to file as String[]
        if (fileContents == null) {
            this.writeFile("", false);
        } else {
            this.writeFile(fileContents);
        }

        return;
    } // END removeStringFromFile

    // Write to the file

    // writes a given string into a given file
    //
    private void writeFile(String s, boolean append) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(this.path, append));

        pw.println(s);

        pw.close();
        return;
    } // END writeFile

    // writes a given string array into a given file
    //
    private void writeFile(String[] s) throws IOException {

        this.writeFile(s[0], true);
        for (int i = 1; i < s.length; i++) {
            this.writeFile(s[i], true);
        } // END for

        return;
    } // END writeFile

    // creates a new array - i dont think i want this functionality but will keep
    // this here if it turns out i need to do this
    public void editFile() {

    }

    // appends a new save to the end
    public void addToFile(Save newSave) {

    }

    // changes 1 line in the lines array with a new one
    public void replaceLine(String oldString, String newString) {

    }

    public String headingsToString() {
        String output = "";
        final int LEN = headings.length;

        for (int i = 0; i < LEN; i++) {
            output += headings[i];

            if (i != LEN - 1) {
                output += ",";
            }
        }

        return output;
    }

    // this closes the file
    public void closeSave() throws IOException {
        this.writeFile(this.headingsToString(), false);

        for (int i = 0; i < this.lines.length; i++) {
            this.writeFile(this.lines[i], true);
        } // END for

        SaveFile.singleton = null;
    }

}
