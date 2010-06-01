package model.filters;


public class SharpenV3Filter extends ConvolveFilter {

	protected static float[] SharpenV3FilterMatrix = { 
				0.0f, -1.0f, 0.0f, -1.0f, 5.0f, -1.0f, 0.0f, -1.0f, 0.0f 
		};
	
	public SharpenV3Filter() {
		super( SharpenV3FilterMatrix );
	}

}
