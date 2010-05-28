package model.filters;

public class MediaFilter extends AbstractFilter {

	@Override
	public float[] getPattern() {
		return new float[] {
			1.0f, -2.0f, 1.0f, -2.0f, 5.0f, -2.0f, 1.0f, -2.0f, 1.0f
		};
	}
	
	public String toString() {
		return "MED";	
	}
}
