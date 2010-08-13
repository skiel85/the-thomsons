/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vectorization;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Signature {

    private List<Point> points = new LinkedList<Point>();
    private String imagePath;
    private double distance = 0.0f;

    public void Save(String filePath) {
        try {
            // Create file
            FileWriter fstream = new FileWriter(filePath, true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(this.toString());
            out.newLine();
            //Close the output stream
            out.close();
            System.out.println("Se escribio la firma: " + this.toString());
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        String s = this.imagePath.replace('\\','_').replace(':', '-') + ';';
        for (Point point : this.points) 
            s += point.toString();
        return s;

    }

    public static Signature build(String line) {
        Signature sign = new Signature();

        String[] splitted = line.split(";");
        sign.setImagePath(splitted[0]);

        String regEx = "\\(([0-9.,-]+)\\)";
        Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(splitted[1]);

        boolean mustContinue = matcher.find();
        while (mustContinue) {

            String[] coord = matcher.group(1).split(",");
            String[] coord0 = coord[0].split("[.]");
            String[] coord1 = coord[1].split("[.]");
            sign.getPoints().add(new Point(Integer.parseInt(coord0[0]), Integer.parseInt(coord1[0])));
            mustContinue = matcher.find();
        }

        return sign;
    }

    /**
     * @return the points
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(List<Point> points) {
        this.points = points;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return this.distance;
    }
}