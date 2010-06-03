package model.filters.convolve;

import model.filters.ConvolveFilter;


public class LowFilter extends ConvolveFilter {

	protected static float[] LowFilterMatrix = {
			0.0f, 0.1f, 0.0f, 0.1f, 0.6f, 0.1f, 0.0f, 0.1f, 0.0f
		};	
	
	public LowFilter() {
		super( LowFilterMatrix );
	}

	


}
