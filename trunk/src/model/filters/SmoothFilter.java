package model.filters;

public class SmoothFilter extends AbstractFilter{

	@Override
	public float[] getPattern() {
		return new float[] {
			0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f, 0.0625f, 0.125f, 0.0625f
		};	
	}
	public String toString() {
		return "SMO";	
	}
}
