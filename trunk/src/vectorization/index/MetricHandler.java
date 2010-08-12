package vectorization.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import vectorization.Maths;
import vectorization.Signature;


import vectorization.index.metric.HDDMetricIndex;


public class MetricHandler {

    private static HDDMetricIndex index = null;

    

    public static void buildIndex(String signatureFilePath, String indexFilePath) throws IOException {

        index = HDDMetricIndex.getInstance();
        index.setLongestWordSize(getLongestDistance(signatureFilePath));
        //TODO: HACER EL LOAD y revisar la regular expresion
        index.buildMetricIndex(signatureFilePath, indexFilePath);
    }

    private static double getLongestDistance(String dictionaryPath) throws IOException {
        double dist = 0;
        List<Signature> signatures = new LinkedList<Signature>();

        FileChannel fc = new FileInputStream(dictionaryPath).getChannel();

        MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        Charset charset = Charset.forName("ISO-8859-1");
        CharsetDecoder decoder = charset.newDecoder();
        CharBuffer charBuffer = decoder.decode(byteBuffer);

        // Read file line by line
        Scanner sc = new Scanner(charBuffer).useDelimiter("\n");

        while (sc.hasNext()) {
            String line = sc.next();
            signatures.add(Signature.build(line));
        }

        for (Signature sign1 : signatures) {
            for (Signature sign2 : signatures) {
                double distActual = Maths.CalculateEuclideanDistance(sign1.getPoints(), sign2.getPoints());
                if (distActual > dist) {
                    dist = distActual;
                }
            }
        }
        fc.close();

        System.out.print(dist);
        return dist;
    }

    public static List<Signature> queryIndex(Signature signature, double tolerance) {
//		List<String> close_p = MetricHandler.index.solveSimilarQueryWithPivots(word, tolerance);
        List<Signature> close_c = MetricHandler.index.solveSimilarWordsQueryWithClusters(signature, tolerance);

//		if(close_p.size()<5){
//			close_p = MetricHandler.index.solveSimilarQueryWithPivots(word, tolerance+1);
//		}

        while ((close_c.size() == 0) && (tolerance <= 1)) {
            tolerance += 0.2;
            close_c = MetricHandler.index.solveSimilarWordsQueryWithClusters(signature, tolerance);
        }


        //if (close_c.size() < 0) {
            //close_c = MetricHandler.index.solveSimilarWordsQueryWithClusters(signature, tolerance + 1);
        //}

//		Iterator<String> i = close_p.iterator();
//		System.out.println("Terminos relacionados hallados mediante el uso de pivotes:");
//		while (i.hasNext()){
//			System.out.println("\t"+i.next());
//		}

        return close_c;
    }

    public static void loadIndexFromFile(String path) throws IOException {
        index = HDDMetricIndex.getInstance();
        index.loadFromFile(path);

    }
}