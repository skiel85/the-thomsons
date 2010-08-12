package vectorization.index.metric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import vectorization.Maths;
import vectorization.Signature;
import vectorization.index.Directory;

public class HDDMetricIndex {

    /* Singleton */
    private static HDDMetricIndex instance = null;
    private ArrayList<HDDCluster> clusters;
    private double longestWordSize;
    private static double SSS_PARAMETER = 0.7;//0.38;
    private static String PIVOSTS_FILE_NAME = "pivots.txt";
    private static String NO_PIVOSTS_FILE_NAME = "nopivots.txt";
    private static String SIMPLE_WORDS_FILE_NAME = "rest.txt";
    private static String INDEX_CONFIGURATION_FILE_NAME = "conf.ind";
    private static int MAXIMUM_CLUSTER_LOAD = 2500;

    private HDDMetricIndex() {
        this.clusters = new ArrayList<HDDCluster>();
        this.longestWordSize = 0;
    }

    public static HDDMetricIndex getInstance() {
        if (instance == null) {
            instance = new HDDMetricIndex();
        }
        return instance;
    }

    public List<HDDCluster> getClusters() {
        return clusters;
    }

    public double getLongestWordSize() {
        return longestWordSize;
    }

    public void setLongestWordSize(double longestWordSize) {
        this.longestWordSize = longestWordSize;
    }

    public void buildMetricIndex(String dictionaryPath, String metricIndexPath)
            throws IOException {
        metricIndexPath = metricIndexPath.replaceAll("\\\\", "/");
        if (metricIndexPath.charAt(metricIndexPath.length() - 1) != '/') {
            metricIndexPath = metricIndexPath + "/";
        }

        Directory aDirectory = new Directory(metricIndexPath);
        aDirectory.cleanDirectory();

        determineCenters(dictionaryPath, metricIndexPath);
        fillClusters(metricIndexPath);
        balanceIndex(metricIndexPath);
        compactIndex(metricIndexPath);

        PrintStream evaluationData = new PrintStream(metricIndexPath
                + "_evaluate_this_.txt");

        Iterator<HDDCluster> it = this.clusters.iterator();
        while (it.hasNext()) {
            HDDCluster cluster = it.next();
            evaluationData.println(cluster.getCenter().getImagePath() + "\t"
                    + cluster.getTerms().size());
        }
        evaluationData.close();
    }

    private void balanceIndex(String metricIndexPath) throws FileNotFoundException, IOException {
        Iterator<HDDCluster> it = this.clusters.iterator();
        ArrayList<HDDCluster> finalClusters = new ArrayList<HDDCluster>();
        while (it.hasNext()) {
            HDDCluster cluster = it.next();
            List<Signature> terms = cluster.getTerms();
            if (terms.size() > MAXIMUM_CLUSTER_LOAD) {
                List<HDDCluster> newClusters = cluster.splitCluster();
                finalClusters.addAll(newClusters);
                cluster.kill();
            } else {
                finalClusters.add(cluster);
            }
        }
        this.clusters = finalClusters;
    }

    private void compactIndex(String metricIndexPath) throws IOException {
        PrintStream rest = new PrintStream(metricIndexPath + HDDMetricIndex.SIMPLE_WORDS_FILE_NAME);
        PrintStream pivotsIndex = new PrintStream(metricIndexPath + HDDMetricIndex.PIVOSTS_FILE_NAME + ".ind");
        int offset = 0;
        Iterator<HDDCluster> it = this.clusters.iterator();
        while (it.hasNext()) {
            int tmpByteCount = offset;
            HDDCluster cluster = it.next();
            Iterator<Signature> i = cluster.getTerms().iterator();
            while (i.hasNext()) {
                String term = i.next().toString();
                offset += term.length() + System.getProperty("line.separator").length();
                rest.println(term);
            }
            pivotsIndex.println(cluster.getCenter() + "|"
                    + tmpByteCount + "|" + // offset
                    (offset - tmpByteCount) + "|" + // length
                    cluster.getRadius()); // radius
            File aFile = new File(metricIndexPath + cluster.getCenter());
            aFile.delete();
            cluster.setFileName(metricIndexPath + HDDMetricIndex.SIMPLE_WORDS_FILE_NAME);
            cluster.setOffset(tmpByteCount);
            cluster.setLength(offset - tmpByteCount);
        }
        pivotsIndex.close();
        rest.close();

        PrintStream conf = new PrintStream(metricIndexPath + HDDMetricIndex.INDEX_CONFIGURATION_FILE_NAME);
        conf.println("longestDistance=" + this.longestWordSize);
        conf.close();
    }

    /**
     *
     * @param metricIndexPath
     * @throws FileNotFoundException
     */
    private void fillClusters(String metricIndexPath)
            throws FileNotFoundException, IOException {
        File missingWordsFile = new File(metricIndexPath + HDDMetricIndex.NO_PIVOSTS_FILE_NAME);
        FileReader fr = new FileReader(missingWordsFile);
        BufferedReader br = new BufferedReader(fr);

        while (br.ready()) {
            String signatureAsString = br.readLine();
            Signature signature = Signature.build(signatureAsString);
            HDDCluster nearestCluster = getNearestCluster(signature);
            nearestCluster.addTerm(signature);

            System.out.println("Imagen: \"" + signature.getImagePath()
                    + "\" asociada al cluster con centro: "
                    + nearestCluster.getCenter().getImagePath());
        }
    }

    /**
     * @param dictionaryPath
     * @param metricIndexPath
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void determineCenters(String dictionaryPath, String metricIndexPath)
            throws FileNotFoundException, IOException {
        File dictionary = new File(dictionaryPath);
        FileReader fr = new FileReader(dictionary);
        BufferedReader br = new BufferedReader(fr);

        File pivotsFile = new File(metricIndexPath + HDDMetricIndex.PIVOSTS_FILE_NAME);
        PrintStream pivotsList = new PrintStream(pivotsFile);

        File noPivotsFile = new File(metricIndexPath + HDDMetricIndex.NO_PIVOSTS_FILE_NAME);
        PrintStream unlinkedWordsList = new PrintStream(noPivotsFile);

        int i = 1;
        while (br.ready()) {
            String line = br.readLine();
            Signature signature = Signature.build(line);
            //recalculateLongestWordSize(word);
            if (isCenter(signature)) {
                this.clusters.add(new HDDCluster(signature, metricIndexPath));
                pivotsList.println(signature);
                System.out.println("NEW CLUSTER CENTER [" + (i++) + "]: " + signature.getImagePath());
            } else {
                HDDCluster nearestCluster = getNearestCluster(signature);
                double distance = Maths.CalculateEuclideanDistance(nearestCluster.getCenter().getPoints(), signature.getPoints());
                        
                if (distance < (this.longestWordSize
                        * HDDMetricIndex.SSS_PARAMETER / 2)) {
                    nearestCluster.addTerm(signature);

                    System.out.println("palabra: \"" + signature.getImagePath()
                            + "\" asociada al cluster con centro: "
                            + nearestCluster.getCenter());
                } else {
                    unlinkedWordsList.println(signature);
                }
            }
        }
        pivotsList.close();
        unlinkedWordsList.close();
        System.out.print("----------->total clusters: "
                + this.getClusters().size() + "\n");
    }

    private HDDCluster getNearestCluster(Signature signature) {
        HDDCluster nearest = null;
        double minDist = -1;

        Iterator<HDDCluster> it = this.clusters.iterator();
        while (it.hasNext()) {
            HDDCluster cluster = it.next();

            double actualDist = Maths.CalculateEuclideanDistance(signature.getPoints(), cluster.getCenter().getPoints());

            if ((actualDist < minDist) || (minDist < 0)) {
                minDist = actualDist;
                nearest = cluster;
            } /*else if ((actualDist == minDist)) {

            int nearestRoot = getRootSimilarity(word, nearest.getCenter());
            int currentRoot = getRootSimilarity(word, cluster.getCenter());
            if (currentRoot < nearestRoot) {
            minDist = actualDist;
            nearest = cluster;
            }
            }*/

        }
        return nearest;
    }

    // Implementacion del algoritmo SSS
    private boolean isCenter(Signature signature) {
        Iterator<HDDCluster> it = this.clusters.iterator();

        // toda firma es centro, hasta que se demuestre lo contrario
        boolean isCenter = true;
        while (it.hasNext() && isCenter) {
            HDDCluster cluster = it.next();

            double distance = Maths.CalculateEuclideanDistance(signature.getPoints(), cluster.getCenter().getPoints());

            // si la distancia es menor a la que dice SSS, NO es centro
            if (distance < (this.longestWordSize * HDDMetricIndex.SSS_PARAMETER)) {
                isCenter = false;
            }
        }
        return isCenter;
    }

    /*private void recalculateLongestWordSize(String word) {
    if (word.length() > this.longestWordSize)
    setLongestWordSize(word.length());
    }*/
    public void loadFromFile(String metricIndexPath) throws IOException {
        if (metricIndexPath.charAt(metricIndexPath.length() - 1) != '/') {
            metricIndexPath = metricIndexPath + "/";
        }
        File pivotsIndexFile = new File(metricIndexPath + HDDMetricIndex.PIVOSTS_FILE_NAME + ".ind");
        FileReader pivotsIndexFileReader = new FileReader(pivotsIndexFile);
        BufferedReader pivotsIndexReader = new BufferedReader(pivotsIndexFileReader);

        while (pivotsIndexReader.ready()) {
            String line = pivotsIndexReader.readLine();
            String[] values = line.split("\\|");
            Signature signature = Signature.build(values[0]);
            HDDCluster cluster = new HDDCluster(signature, metricIndexPath, HDDMetricIndex.SIMPLE_WORDS_FILE_NAME);
            cluster.setOffset(Integer.parseInt(values[1]));
            cluster.setLength(Integer.parseInt(values[2]));
            cluster.setRadius(Double.parseDouble(values[3]));
            this.clusters.add(cluster);
        }

        pivotsIndexReader.close();
        pivotsIndexFileReader.close();

        FileInputStream configFile = new FileInputStream(metricIndexPath + HDDMetricIndex.INDEX_CONFIGURATION_FILE_NAME);
        InputStreamReader configFileReader = new InputStreamReader(configFile);
        BufferedReader configReader = new BufferedReader(configFileReader);
        if (configReader.ready()) {
            this.longestWordSize = Double.parseDouble(configReader.readLine().split("=")[1]);
        }


    }

    public List<Signature> solveSimilarQueryWithPivots(Signature signature, int tolerance) {
        List<Signature> result = new ArrayList<Signature>();
        HashMap<Signature, Integer> suitablePivots = new HashMap<Signature, Integer>();
        double minimumDistance = this.longestWordSize + 1;
        Iterator<HDDCluster> cit = this.clusters.iterator();

        for (int i = 0; cit.hasNext(); i++) {
            HDDCluster cluster = cit.next();
            double distance = Maths.CalculateEuclideanDistance(cluster.getCenter().getPoints(), signature.getPoints());
                    

            // las condiciones no son excluyentes, ya que en ambos casos hago el put, pero solo en el estricto hago el clear
            if (distance < minimumDistance) { // si encuentro una que est�, m�s cerca, descarto las que consider� antes
                suitablePivots.clear();
                minimumDistance = distance;
            }
            if (distance <= minimumDistance) { // si equidistan, tengo que evaluarlas a todas
                suitablePivots.put(cluster.getCenter(), i);
            }
        }
        Iterator<Signature> it = suitablePivots.keySet().iterator();
        while (it.hasNext()) {
            Signature key = it.next();
            try {
                result.addAll(this.clusters.get(suitablePivots.get(key)).getCloseSignatureInCluster(key, tolerance));
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }

        return result;
    }

    public List<Signature> solveSimilarWordsQueryWithClusters(Signature signature, double tolerance) {
        
        List<Signature> result = new ArrayList<Signature>();
        HashMap<Signature, Integer> suitableClusters = new HashMap<Signature, Integer>();
        Iterator<HDDCluster> cit = this.clusters.iterator();

        for (int i = 0; cit.hasNext(); i++) {
            HDDCluster cluster = cit.next();
            double distance = Maths.CalculateEuclideanDistance(cluster.getCenter().getPoints(), signature.getPoints());
                    

            if (distance <= tolerance + cluster.getRadius()) {
                suitableClusters.put(cluster.getCenter(), i);
            }
        }
        Iterator<Signature> it = suitableClusters.keySet().iterator();
        while (it.hasNext()) {
            Signature key = it.next();
            try {
                result.addAll(this.clusters.get(suitableClusters.get(key)).getCloseSignatureInCluster(signature, tolerance));
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }

        return result;
    }
}