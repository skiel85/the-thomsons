package vectorization.index.metric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MetricIndex {

	/* Singleton */
	private static MetricIndex instance = null;

	private ArrayList<Cluster> clusters;
	private int longestWordSize;

	private static double SSS_PARAMETER = 0.38;
	private static String PIVOSTS_FILE_NAME = "pivots.txt";
	private static String NO_PIVOSTS_FILE_NAME = "nopivots.txt";
	private static String SIMPLE_WORDS_FILE_NAME = "rest.txt";

	private MetricIndex() {
		this.clusters = new ArrayList<Cluster>();
		this.longestWordSize = 0;
	}

	public static MetricIndex getInstance() {
		if (instance == null)
			instance = new MetricIndex();
		return instance;
	}

	public List<Cluster> getClusters() {
		return clusters;
	}

	public int getLongestWordSize() {
		return longestWordSize;
	}

	public void setLongestWordSize(int longestWordSize) {
		this.longestWordSize = longestWordSize;
	}

	public void buildMetricIndex(String dictionaryPath, String metricIndexPath)
			throws IOException {
		if (metricIndexPath.charAt(metricIndexPath.length() - 1) != '/') {
			metricIndexPath = metricIndexPath + "/";
		}
		determineCenters(dictionaryPath, metricIndexPath);

		fillClusters(metricIndexPath);

		compactIndex(metricIndexPath);
		
		PrintStream evaluationData = new PrintStream(metricIndexPath
				+ "_evaluate_this_.txt");

		Iterator<Cluster> it = this.clusters.iterator();
		while (it.hasNext()) {
			Cluster cluster = it.next();
			evaluationData.println(cluster.getCenter() + "\t"
					+ cluster.getTerms().size());
		}
	}

	private void compactIndex(String metricIndexPath) throws IOException {
		PrintStream rest = new PrintStream(metricIndexPath+ MetricIndex.SIMPLE_WORDS_FILE_NAME);
		PrintStream pivotsIndex = new PrintStream(metricIndexPath+ MetricIndex.PIVOSTS_FILE_NAME + ".ind");
		int offset = 0;
		Iterator<Cluster> it = this.clusters.iterator();
		while (it.hasNext()) {
			int tmpByteCount = offset;
			Cluster cluster = it.next();
			Iterator<String> i = cluster.getTerms().iterator();
			while (i.hasNext()) {
				String term = i.next();
				offset+= term.length()+ System.getProperty("line.separator").length();
				rest.println(term);
			}
			pivotsIndex.println(cluster.getCenter() + "|" + tmpByteCount);
			File aFile = new File(metricIndexPath + cluster.getCenter());
			aFile.delete();
		}
		pivotsIndex.close();
		rest.close();
		
	}

	/**
	 * 
	 * @param metricIndexPath
	 * @throws FileNotFoundException
	 */
	private void fillClusters(String metricIndexPath)
			throws FileNotFoundException, IOException {
		File missingWordsFile = new File(metricIndexPath + MetricIndex.NO_PIVOSTS_FILE_NAME);
		FileReader fr = new FileReader(missingWordsFile);
		BufferedReader br = new BufferedReader(fr);

		while (br.ready()) {
			String word = br.readLine();
			Cluster nearestCluster = getNearestCluster(word);
			nearestCluster.addTerm(word);
			FileWriter writer = new FileWriter(metricIndexPath
					+ nearestCluster.getCenter(), true);
			writer.write(word + System.getProperty("line.separator"));
			writer.close();

			System.out.println("palabra: \"" + word
					+ "\" asociada al cluster con centro: "
					+ nearestCluster.getCenter());
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

		File pivotsFile = new File(metricIndexPath + MetricIndex.PIVOSTS_FILE_NAME);
		PrintStream pivotsList = new PrintStream(pivotsFile);

		File noPivotsFile = new File(metricIndexPath + MetricIndex.NO_PIVOSTS_FILE_NAME);
		PrintStream unlinkedWordsList = new PrintStream(noPivotsFile);

		int i = 1;
		while (br.ready()) {
			String word = br.readLine();
			recalculateLongestWordSize(word);
			if (isCenter(word)) {
				this.clusters.add(new Cluster(word));
				pivotsList.println(word);
				System.out.println("NEW CLUSTER CENTER [" + (i++) + "]: "
						+ word);
			} else {
				Cluster nearestCluster = getNearestCluster(word);
				int distance = this.getLevenshteinDistance(nearestCluster
						.getCenter(), word);
				if (distance < (this.longestWordSize
						* MetricIndex.SSS_PARAMETER / 2)) {
					nearestCluster.addTerm(word);
					FileWriter writer = new FileWriter(metricIndexPath
							+ nearestCluster.getCenter(), true);
					writer.write(word + "\n");
					writer.close();

					System.out.println("palabra: \"" + word
							+ "\" asociada al cluster con centro: "
							+ nearestCluster.getCenter());
				} else {
					unlinkedWordsList.println(word);
					System.out
							.print("palabra: \""
									+ word
									+ "\" no asociada a ning�n cluster; queda en el achivo de no vinculadas\n");
				}
			}
			System.out.print("--->LONGITUD MAS LARGA: " + this.longestWordSize
					+ "\n");
		}
		pivotsList.close();
		unlinkedWordsList.close();
		System.out.print("----------->total clusters: "
				+ this.getClusters().size() + "\n");
	}

	// TODO definir empates!!!! - Creo que ya est� solucionado
	private Cluster getNearestCluster(String word) {
		Cluster nearest = null;
		int minDist = -1;

		Iterator<Cluster> it = getClusters().iterator();
		while (it.hasNext()) {
			Cluster cluster = it.next();

			int actualDist = getLevenshteinDistance(word, cluster.getCenter());
			if ((actualDist < minDist) || (minDist < 0)) {
				minDist = actualDist;
				nearest = cluster;
			} else if ((actualDist == minDist)) {
				/*
				 * En caso de empate me quedo con el que tiene la ra�z del
				 * t�rmino m�s parecida.
				 */
				int nearestRoot = getRootSimilarity(word, nearest.getCenter());
				int currentRoot = getRootSimilarity(word, cluster.getCenter());
				if (currentRoot < nearestRoot) {
					minDist = actualDist;
					nearest = cluster;
				}
			}

		}
		return nearest;
	}

	public int getRootSimilarity(String word_a, String word_b) {
		int i;
		word_a = word_a.toLowerCase();
		word_a = word_a.replaceAll("�", "a").replaceAll("�", "e").replaceAll(
				"�", "i").replaceAll("�", "o").replaceAll("�", "u");
		word_b = word_b.toLowerCase();
		word_b = word_b.replaceAll("�", "a").replaceAll("�", "e").replaceAll(
				"�", "i").replaceAll("�", "o").replaceAll("�", "u");
		for (i = 0; (i < word_a.length()) && (i < word_b.length())
				&& (word_a.charAt(i) == word_b.charAt(i)); i++)
			;
		return i;
	}

	// Implementacion del algoritmo SSS
	private boolean isCenter(String word) {
		Iterator<Cluster> it = this.clusters.iterator();

		// toda palabra es centro, hasta que se demuestre lo contrario
		boolean isCenter = true;
		while (it.hasNext() && isCenter) {
			Cluster cluster = it.next();

			int distance = getLevenshteinDistance(word, cluster.getCenter());
			// si la distancia es menor a la que dice SSS, NO es centro
			if (distance < (this.longestWordSize * MetricIndex.SSS_PARAMETER))
				isCenter = false;
		}
		return isCenter;
	}

	private void recalculateLongestWordSize(String word) {
		if (word.length() > this.longestWordSize)
			setLongestWordSize(word.length());
	}

	public int getLevenshteinDistance(String s, String t) {

		int n = s.length();
		int m = t.length();
		int[][] d = new int[n + 1][m + 1];
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		for (int i = 0; i <= n; d[i][0] = i++) {
		}
		for (int j = 0; j <= m; d[0][j] = j++) {
		}

		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				int costo;
				if (t.charAt(j - 1) == s.charAt(i - 1))
					costo = 0;
				else
					costo = 1;
				d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1),
						d[i - 1][j - 1] + costo);
			}
		}
		return d[n][m];
	}

	public List<String> solveSimilarWordsQueryWithPivots(String word, int tolerance) {
		List<String> result = new ArrayList<String>();
		HashMap<String, Integer> suitablePivots = new HashMap<String, Integer>();
		int minimumDistance = this.longestWordSize+1;
		Iterator<Cluster> cit = this.clusters.iterator();
		
		for (int i=0; cit.hasNext(); i++) {
			Cluster cluster = cit.next();
			int distance = this.getLevenshteinDistance(cluster.getCenter(), word);
			
			// las condiciones no son excluyentes, ya que en ambos casos hago el put, pero solo en el estricto hago el clear
			if( distance<minimumDistance ){ // si encuentro una que est�, m�s cerca, descarto las que consider� antes
				suitablePivots.clear();
				minimumDistance = distance;
			} 
			if( distance<=minimumDistance){ // si equidistan, tengo que evaluarlas a todas
				suitablePivots.put(cluster.getCenter(), i);
			}
		}
		Iterator<String> it = suitablePivots.keySet().iterator();
		while (it.hasNext()) {
			String string = it.next();
			result.addAll( this.clusters.get(suitablePivots.get(string)).getCloseWordsInCluster(word, tolerance) );
		}
		
		return result;
	}
	
	public List<String> solveSimilarWordsQueryWithClusters(String word, int tolerance) {
		List<String> result = new ArrayList<String>();
		HashMap<String, Integer> suitableClusters = new HashMap<String, Integer>();
		Iterator<Cluster> cit = this.clusters.iterator();
		
		for (int i=0; cit.hasNext(); i++) {
			Cluster cluster = cit.next();
			int distance = this.getLevenshteinDistance(cluster.getCenter(), word);
			
			if( distance <= tolerance+cluster.getRadius() ){
				suitableClusters.put(cluster.getCenter(), i);
			}
		}
		Iterator<String> it = suitableClusters.keySet().iterator();
		while (it.hasNext()) {
			String string = it.next();
			result.addAll( this.clusters.get(suitableClusters.get(string)).getCloseWordsInCluster(word, tolerance) );
		}
		
		return result;
	}
	
	
	
}