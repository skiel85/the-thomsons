package model.filters;

public class SharpenV2Filter extends AbstractFilter {

	@Override
	public float[] getPattern() {
		return new float[] {
			-1.0f, -1.0f, -1.0f, -1.0f, 9.0f, -1.0f, -1.0f, -1.0f, -1.0f
		};
	}
	


}
