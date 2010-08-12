package vectorization.index.metric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vectorization.Maths;
import vectorization.Signature;
import vectorization.index.split.SSSAlgorithm;

public class HDDCluster {

    private Signature center;
    private String fileName;
    private String workingDir;
    private int offset;
    private int length;
    private double radius;

    public HDDCluster(Signature center, String dir) {
        this.center = center;
        this.workingDir = dir.replaceAll("\\\\", "/");
        if (this.workingDir.charAt(this.workingDir.length() - 1) != '/') {
            this.workingDir = this.workingDir + "/";
        }
        this.fileName = center.getImagePath() + "_cluster";
        this.offset = 0;
        this.length = 0;
        this.radius = 0;
    }

    public HDDCluster(Signature center, String dir, String fileName) {
        this.center = center;
        this.fileName = fileName;
        this.workingDir = dir.replaceAll("\\\\", "/");
        if (this.workingDir.charAt(this.workingDir.length() - 1) != '/') {
            this.workingDir = this.workingDir + "/";
        }
        this.offset = 0;
        this.length = 0;
        this.radius = 0;
    }

    public void setFileName(String filepath) {
        String path = filepath.replaceAll("\\\\", "/");
        int index = path.lastIndexOf("/");
        if (index > 0) {
            this.workingDir = path.substring(0, index) + "/";
            this.fileName = path.substring(index + 1);
        }
    }

    public double getRadius() {
        return radius;
    }

    public Signature getCenter() {
        return center;
    }

    /**
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void addTerm(Signature newTerm) throws IOException {
        //FileWriter writer = new FileWriter(this.workingDir + this.center, true);
        FileWriter writer = new FileWriter(this.workingDir + this.fileName, true);
        writer.write(newTerm.toString() + System.getProperty("line.separator"));
        writer.close();
        this.length += newTerm.toString().length() + System.getProperty("line.separator").length();
        double dist = Maths.CalculateEuclideanDistance(newTerm.getPoints(), center.getPoints());

        if (this.radius < dist) {
            this.radius = dist;
        }
    }

    public List<Signature> getTerms() {
        File clusteredWordsFile = new File(this.workingDir + this.fileName);
        List<Signature> result = new ArrayList<Signature>();
        FileReader fileReader;
        try {
            fileReader = new FileReader(clusteredWordsFile);
            BufferedReader reader = new BufferedReader(fileReader);
            reader.skip(this.offset);

            int count = 0;
            while (reader.ready() && count < this.length) {
                String signatureAsString = reader.readLine();
                count += signatureAsString.length() + System.getProperty("line.separator").length();
                result.add(Signature.build(signatureAsString));
            }
            reader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    public List<Signature> getCloseSignatureInCluster(Signature signature, double distance) throws IOException {
        File clusteredWordsFile = new File(this.workingDir + this.fileName);
        FileReader fileReader = new FileReader(clusteredWordsFile);
        BufferedReader reader = new BufferedReader(fileReader);
        List<Signature> result = new ArrayList<Signature>();

        reader.skip(this.offset);

        if (Maths.CalculateEuclideanDistance(this.center.getPoints(), signature.getPoints()) <= distance) {
            result.add(this.center);
            this.center.setDistance(distance);
        }


        int count = 0;
        while (reader.ready() && count < this.length) {
            String current = reader.readLine();
            count += current.length() + System.getProperty("line.separator").length();
            Signature currentSignature = Signature.build(current);
            if (Maths.CalculateEuclideanDistance(signature.getPoints(), currentSignature.getPoints()) <= distance) {
                result.add(currentSignature);
                currentSignature.setDistance(distance);
            }
        }
        return result;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public List<HDDCluster> splitCluster() throws FileNotFoundException, IOException {
        List<Signature> termsList = this.getTerms();
        //int longest = this.center.length();
        double longest = 0; //Maths.CalculateEuclideanNorm(this.center.getPoints());

        for (Signature sign1 : termsList) {
            for (Signature sign2 : termsList) {
                double distActual = Maths.CalculateEuclideanDistance(sign1.getPoints(), sign2.getPoints());
                if (distActual > longest) {
                    longest = distActual;
                }
            }
        }

        termsList.add(this.center);
        return sssInternalClusterization(termsList, longest);
    }

    private List<HDDCluster> sssInternalClusterization(List<Signature> termsList,
            double longest) throws FileNotFoundException, IOException {
        SSSAlgorithm algorithm = new SSSAlgorithm();
        return algorithm.splitMetricSpace(termsList, longest, workingDir);
    }

    public void kill() {
        if (this.fileName.compareTo(this.center.getImagePath() + "_cluster") == 0) {
            File aFile = new File(this.workingDir + this.center);
            aFile.delete();
        }
    }
}