package model.filters;

public class LowFilter extends AbstractFilter {

	@Override
	public float[] getPattern() {
		return new float[] {
			0.0f, 0.1f, 0.0f, 0.1f, 0.6f, 0.1f, 0.0f, 0.1f, 0.0f
		};	
	}
	
	public String toString() {
		return "LOW";	
	}

}
