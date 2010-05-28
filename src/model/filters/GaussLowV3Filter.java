package model.filters;

public class GaussLowV3Filter extends AbstractFilter {

	@Override
	public float[] getPattern() {
		return new float[] {
				// 1.0f, -2.0f, 1.0f,
				// -2.0f, 4.0f, -2.0f,
				// 1.0f, -2.0f, 1.0f
				1 / 9f, 2 / 9f, 1 / 9f, 2 / 9f, 4 / 9f, 2 / 9f, 1 / 9f, 2 / 9f,
				1 / 9f
		};
	}
	


}
