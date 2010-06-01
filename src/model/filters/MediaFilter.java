package model.filters;


public class MediaFilter extends ConvolveFilter {

	 protected static float[] MediaFilterMatrix = {
			1.0f, -2.0f, 1.0f, -2.0f, 5.0f, -2.0f, 1.0f, -2.0f, 1.0f
		};
	
	
	public MediaFilter() {
		super( MediaFilterMatrix );
	}

}
