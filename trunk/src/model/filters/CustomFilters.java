package model.filters;



public enum CustomFilters {

	//Convolve Filters
	_DUMMY_CONVOLVE ("-Convolve Filters-","DM1",null),
	SHARPENV3FILTER ( "SharpenV3", "SV3",new SharpenV3Filter()),
	SHARPENV2FILTER ( "SharpenV2","SV2", new SharpenV2Filter()),
	MOOTHFILTER ( "Smooth","SMO" ,new SmoothFilter()),
	MEDIAFILTER ( "Media","MED" ,new MediaFilter() ),
	LOWFILTER ( "Low","LOW" ,new LowFilter() ),
	GAUSSLOWV3FILTER ( "GaussLowV3", "GV3",new GaussLowV3Filter() ),	
	BLURFILTER ("BlurFilter","BLR",new BlurFilter()),
	AVERAGEFILTER ("Average","AVG",new AverageFilter()),
	BUMPFILTER ("Bump","BUM",new BumpFilter()),
	GAUSSIANFILTER ("Gaussian","GAU",new GaussianFilter()),
	SHARPENFILTER ("Sharpen","SHA",new SharpenFilter()),
	//Filters Varios
	_DUMMY_VARIOUS ("-Various Filters-","DM2",null),
	BOXBLURFILTER ( "BoxBlur", "BBF",new BoxBlurFilter()),	
	;
	
	public final String name;
	public final String shortName;
	public final AbstractBufferedImageOp filter;
	CustomFilters (String name, String shortName,AbstractBufferedImageOp filter){
		this.name = name;
		this.filter = filter;
		this.shortName = shortName;
	}
	
	public String toString(){
		return name;
	}
}
