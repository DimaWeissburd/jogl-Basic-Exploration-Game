import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class World {
    Triangle[] blueTerrain = new Triangle[127];
    Triangle[] brownTerrain = new Triangle[2180];
    Triangle[] greenTerrain = new Triangle[2567];
    Triangle[] whiteTerrain = new Triangle[746];

    public World() {
        int counter = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("terrain/blueTerrain"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splittedValues = line.split(" ");
                blueTerrain[counter] = new Triangle(new Vertex(Float.parseFloat(splittedValues[0]), Float.parseFloat(splittedValues[1]), Float.parseFloat(splittedValues[2])),
                        new Vertex(Float.parseFloat(splittedValues[3]), Float.parseFloat(splittedValues[4]), Float.parseFloat(splittedValues[5])),
                        new Vertex(Float.parseFloat(splittedValues[6]), Float.parseFloat(splittedValues[7]), Float.parseFloat(splittedValues[8])));
                counter++;
            }
        }
        catch (IOException e) {
            System.err.println("Error reading file.");
        }
        counter = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("terrain/brownTerrain"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splittedValues = line.split(" ");
                brownTerrain[counter] = new Triangle(new Vertex(Float.parseFloat(splittedValues[0]), Float.parseFloat(splittedValues[1]), Float.parseFloat(splittedValues[2])),
                        new Vertex(Float.parseFloat(splittedValues[3]), Float.parseFloat(splittedValues[4]), Float.parseFloat(splittedValues[5])),
                        new Vertex(Float.parseFloat(splittedValues[6]), Float.parseFloat(splittedValues[7]), Float.parseFloat(splittedValues[8])));
                counter++;
            }
        }
        catch (IOException e) {
            System.err.println("Error reading file.");
        }
        counter = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("terrain/greenTerrain"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splittedValues = line.split(" ");
                greenTerrain[counter] = new Triangle(new Vertex(Float.parseFloat(splittedValues[0]), Float.parseFloat(splittedValues[1]), Float.parseFloat(splittedValues[2])),
                        new Vertex(Float.parseFloat(splittedValues[3]), Float.parseFloat(splittedValues[4]), Float.parseFloat(splittedValues[5])),
                        new Vertex(Float.parseFloat(splittedValues[6]), Float.parseFloat(splittedValues[7]), Float.parseFloat(splittedValues[8])));
                counter++;
            }
        }
        catch (IOException e) {
            System.err.println("Error reading file.");
        }
        counter = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("terrain/whiteTerrain"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splittedValues = line.split(" ");
                whiteTerrain[counter] = new Triangle(new Vertex(Float.parseFloat(splittedValues[0]), Float.parseFloat(splittedValues[1]), Float.parseFloat(splittedValues[2])),
                        new Vertex(Float.parseFloat(splittedValues[3]), Float.parseFloat(splittedValues[4]), Float.parseFloat(splittedValues[5])),
                        new Vertex(Float.parseFloat(splittedValues[6]), Float.parseFloat(splittedValues[7]), Float.parseFloat(splittedValues[8])));
                counter++;
            }
        }
        catch (IOException e) {
            System.err.println("Error reading file.");
        }
    }








    public Triangle[] getBlueTerrain() {
        return this.blueTerrain;
    }

    public int getBlueN() {
        return this.blueTerrain.length;
    }

    public Triangle[] getBrownTerrain() {
        return this.brownTerrain;
    }

    public int getBrownN() {
        return this.brownTerrain.length;
    }

    public Triangle[] getGreenTerrain() {
        return this.greenTerrain;
    }

    public int getGreenN() {
        return this.greenTerrain.length;
    }

    public Triangle[] getWhiteTerrain() {
        return this.whiteTerrain;
    }

    public int getWhiteN() {
        return this.whiteTerrain.length;
    }


}
