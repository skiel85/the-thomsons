package vectorization.index.sort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Streamer implements Iterable<String> {

	private static String FIRST_REPLACE_PATTERN = "[\\s\\t\\r\\n\\(\\)\\[\\]\\{\\}\\.\\*\\^\\$\\+\\-%\\|#&/=�!�?�,;:<>_\\\"'`�]+";
	private static String SECOND_REPLACE_PATTERN = "\\b([0-9]+)\\b";
	private static final String THIRD_REPLACE_PATTERN = "\\s+";
	
	private BufferedReader input;
	private String[] currentLine;

	public Streamer(Reader aReader) {
		this.input = new BufferedReader(aReader);
		this.currentLine = null;
	}

	public void close() throws IOException {
		this.input.close();
	}

	private boolean hasNext() throws IOException {
		return ((this.currentLine != null) && (this.currentLine.length > 0))
				|| (this.input.ready());
	}

	private String next() throws IOException {
		if ((this.currentLine == null) || (this.currentLine.length == 0)) {
			if (this.input.ready()) {
				String tmp = this.input.readLine();
				Pattern pattern = Pattern.compile("^<.*>$");
				Matcher matcher = pattern.matcher(tmp);
				while ((matcher.matches()) && (this.input.ready())) {
					tmp = this.input.readLine();
					matcher = pattern.matcher(tmp);
				}
				if ((matcher.matches()) && (!this.input.ready())) {
					return null; // solo quedaban tags para leer
				}
				
				tmp = tmp.replaceAll(Streamer.FIRST_REPLACE_PATTERN, " "); // Saco los signos de puntuaci�n
				tmp = tmp.replaceAll(Streamer.SECOND_REPLACE_PATTERN, " "); // Saco los n�meros
				tmp = tmp.replaceAll(Streamer.THIRD_REPLACE_PATTERN, " "); // Saco los espacios m�ltiples que hayan quedado
				tmp = tmp.trim(); // Saco los espacios iniciales y finales que pudieran quedar
				this.currentLine = tmp.split(" ");
			} else {
				this.currentLine = null;
			}
		}
		if (this.currentLine == null) {
			return null;
		}

		String result = this.currentLine[0];
		String tmp[] = new String[this.currentLine.length - 1];
		for (int i = 0; i < this.currentLine.length - 1; i++) {
			tmp[i] = this.currentLine[i + 1];
		}
		this.currentLine = tmp;

		return result;

	}

	private class StreamIterator implements Iterator<String> {

		Streamer toIterate;

		public StreamIterator(Streamer streamer) {
			toIterate = streamer;
		}

		public boolean hasNext() {
			try {
				return this.toIterate.hasNext();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		public String next() {
			String res = null;
			try {
				res = this.toIterate.next();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return res;
		}

		public void remove() {
		}

	}

	public Iterator<String> iterator() {
		return new StreamIterator(this);
	}

}