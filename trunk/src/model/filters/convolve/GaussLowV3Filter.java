package model.filters.convolve;

import model.filters.ConvolveFilter;



public class GaussLowV3Filter extends ConvolveFilter {


	protected static float[] gaussLowV3Matrix = {
				// 1.0f, -2.0f, 1.0f,
				// -2.0f, 4.0f, -2.0f,
				// 1.0f, -2.0f, 1.0f
				1 / 9f, 2 / 9f, 1 / 9f, 2 / 9f, 4 / 9f, 2 / 9f, 1 / 9f, 2 / 9f,
				1 / 9f
		};
	
	public GaussLowV3Filter() {
		super( gaussLowV3Matrix );
	}
	


}
