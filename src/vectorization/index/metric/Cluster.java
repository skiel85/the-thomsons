package vectorization.index.metric;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cluster {
	
	private String center;
	private List<String> terms;
	private int radius;
	
	public int getRadius() {
		return radius;
	}

	public Cluster(String center){
		this.center = center;
		this.terms = new ArrayList<String>();
		this.radius = 0;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
		// TODO: recalcular radio
	}

	public List<String> getTerms() {
		return terms;
	}

	public void setTerms(List<String> terms) {
		this.terms = terms;
	}
	
	public void addTerm(String term){
		this.terms.add(term);
		int dist = MetricIndex.getInstance().getLevenshteinDistance(term, this.center);
		if(this.radius<dist){
			this.radius = dist;
		}
	}
	
	public List<String> getCloseWordsInCluster (String word, int distance){
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> it = this.terms.iterator();
		while (it.hasNext()) {
			String string = it.next();
			if(MetricIndex.getInstance().getLevenshteinDistance(word, string)<=distance){
				result.add(string);
			}
		}
		return result;
	}
}