package model.filters;

public class SharpenV3Filter extends AbstractFilter {

	@Override
	public float[] getPattern() {
		return new float[] { 
				0.0f, -1.0f, 0.0f, -1.0f, 5.0f, -1.0f, 0.0f, -1.0f, 0.0f 
		};
	}

}
