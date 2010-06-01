package model.filters;


public class LowFilter extends ConvolveFilter {

	protected static float[] LowFilterMatrix = {
			0.0f, 0.1f, 0.0f, 0.1f, 0.6f, 0.1f, 0.0f, 0.1f, 0.0f
		};	
	
	public LowFilter() {
		super( LowFilterMatrix );
	}

	


}
