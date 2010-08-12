package vectorization.index.split;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import vectorization.Maths;
import vectorization.Signature;

import vectorization.index.metric.HDDCluster;
import vectorization.index.sort.Streamer;

public class SSSAlgorithm {

	
	private static double SSS_PARAMETER = 0.38;
	private static String PIVOSTS_FILE_NAME = "pivots.txt";
	private static String NO_PIVOSTS_FILE_NAME = "nopivots.txt";
	
	
	public List<HDDCluster> splitMetricSpace (List<Signature> terms, double longest, String workingPath) throws FileNotFoundException, IOException {
		
		if (workingPath.charAt(workingPath.length() - 1) != '/') {
			workingPath = workingPath + "/";
		}
		
		List<HDDCluster> clusters = determineCenters(terms.iterator(), workingPath, longest);
		fillClusters(clusters, workingPath);
		
		return clusters;
	}
	
	
	public List<HDDCluster> splitMetricSpace (String termsFile, int longest, String workingPath) throws FileNotFoundException, IOException {
		
		if (workingPath.charAt(workingPath.length() - 1) != '/') {
			workingPath = workingPath + "/";
		}
		
		InputStreamReader input = new InputStreamReader(new FileInputStream(termsFile));
		Streamer aStreamer = new Streamer(input);

                List<Signature> signatures = new ArrayList<Signature>();

                Iterator<String> iterator = aStreamer.iterator();

                while(iterator.hasNext())
                    signatures.add(Signature.build(iterator.next()));


		List<HDDCluster> clusters = determineCenters(signatures.iterator(), workingPath, longest);
		fillClusters(clusters, workingPath);
		
		return clusters;
	}
	
	
	/**
	 * 
	 * @param clusters 
	 * @param metricIndexPath
	 * @throws FileNotFoundException
	 */
	private void fillClusters(List<HDDCluster> clusters, String metricIndexPath)
			throws FileNotFoundException, IOException {
		File missingWordsFile = new File(metricIndexPath + SSSAlgorithm.NO_PIVOSTS_FILE_NAME);
		FileReader fr = new FileReader(missingWordsFile);
		BufferedReader br = new BufferedReader(fr);

		while (br.ready()) {
			String signatureAsString = br.readLine();
                        Signature signature = Signature.build(signatureAsString);
			HDDCluster nearestCluster = getNearestCluster(clusters, signature);
			nearestCluster.addTerm(signature);
		}
	}

	/**
	 * @param iterator
	 * @param metricIndexPath
	 * @param longest 
	 * @return 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private List<HDDCluster> determineCenters(Iterator<Signature> iterator, String metricIndexPath, double longest)
			throws FileNotFoundException, IOException {
		
		ArrayList<HDDCluster> clusters = new ArrayList<HDDCluster>();
		
		File pivotsFile = new File(metricIndexPath + SSSAlgorithm.PIVOSTS_FILE_NAME);
		PrintStream pivotsList = new PrintStream(pivotsFile);

		File noPivotsFile = new File(metricIndexPath + SSSAlgorithm.NO_PIVOSTS_FILE_NAME);
		PrintStream unlinkedWordsList = new PrintStream(noPivotsFile);

		int i = 1;
		while (iterator.hasNext()) {
			Signature signature = iterator.next();
			if (isCenter(clusters, signature, longest)) {
				clusters.add(new HDDCluster(signature, metricIndexPath));
				pivotsList.println(signature);
				System.out.println(" splitting cluster: NEW CLUSTER CENTER [" + (i++) + "]: "
						+ signature);
			} else {
				HDDCluster nearestCluster = getNearestCluster(clusters, signature);
				double distance = Maths.CalculateEuclideanDistance(nearestCluster.getCenter().getPoints(), signature.getPoints());
                                        
				if (distance < (longest * SSSAlgorithm.SSS_PARAMETER / 2f)) {
					nearestCluster.addTerm(signature);
				} else {
					unlinkedWordsList.println(signature);
				}
			}
		}
		pivotsList.close();
		unlinkedWordsList.close();
		return clusters;
	}

	// TODO definir empates!!!! - Creo que ya est� solucionado
	private HDDCluster getNearestCluster(List<HDDCluster> clusters, Signature signature) {
		HDDCluster nearest = null;
		double minDist = -1;

		Iterator<HDDCluster> it = clusters.iterator();
		while (it.hasNext()) {
			HDDCluster cluster = it.next();

			double actualDist = Maths.CalculateEuclideanDistance(signature.getPoints(),cluster.getCenter().getPoints());
                                
			if ((actualDist < minDist) || (minDist < 0)) {
				minDist = actualDist;
				nearest = cluster;
			} /*else if ((actualDist == minDist)) {
				/*
				 * En caso de empate me quedo con el que tiene la ra�z del
				 * t�rmino m�s parecida.
				 
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
	private boolean isCenter(List<HDDCluster> clusters, Signature signature, double longest) {
		Iterator<HDDCluster> it = clusters.iterator();

		// toda palabra es centro, hasta que se demuestre lo contrario
		boolean isCenter = true;
		while (it.hasNext() && isCenter) {
			HDDCluster cluster = it.next();

			double distance = Maths.CalculateEuclideanDistance(signature.getPoints(), cluster.getCenter().getPoints());
                                
			// si la distancia es menor a la que dice SSS, NO es centro
			if (distance < (longest * SSSAlgorithm.SSS_PARAMETER))
				isCenter = false;
		}
		return isCenter;
	}
}