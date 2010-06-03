package model.filters.convolve;

import model.filters.ConvolveFilter;


public class SmoothFilter extends ConvolveFilter{

	protected static float[] SmoothFilterMatrix = {
			0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f, 0.0625f, 0.125f, 0.0625f
		};	

		public SmoothFilter() {
			super( SmoothFilterMatrix );
		}
}
