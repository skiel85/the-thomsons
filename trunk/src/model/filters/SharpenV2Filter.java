package model.filters;


public class SharpenV2Filter extends ConvolveFilter {

	 protected static float[] SharpenV2FilterMatrix = {
			-1.0f, -1.0f, -1.0f, -1.0f, 9.0f, -1.0f, -1.0f, -1.0f, -1.0f
		};
	
		public SharpenV2Filter() {
			super( SharpenV2FilterMatrix );
		}
	


}
