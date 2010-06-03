package model.filters;

import model.filters.convolve.*;
import model.filters.filters.*;
import model.filters.point.*;



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
	BicubicScaleFilter ("BicubicScaleFilter", "BSF", new BicubicScaleFilter()),
	BlockFilter("BlockFilter","BKF",new BlockFilter()),
	//Point Filters
	_DUMMY_POINT ("-Point Filters-","DM3",null),
	ChannelMixFilter ( "ChannelMixFilter", "CMF",new ChannelMixFilter()),	
	DissolveFilter ("DissolveFilter","DIS",new DissolveFilter()),
	ErodeAlphaFilter ("ErodeAlphaFilter","ERO",new ErodeAlphaFilter()),
	FourColorFilter ("FourColorFilter","4CO",new FourColorFilter()),
	GrayFilter ("GrayFilter","GRA",new GrayFilter()),
	GrayscaleFilter ("GrayscaleFilter","GRS",new GrayscaleFilter()),
	HSBAdjustFilter ("HSBAdjustFilter","HSB",new HSBAdjustFilter()),
	InvertAlphaFilter ("InvertAlphaFilter","INA",new InvertAlphaFilter()),
	InvertFilter ("InvertFilter","INV",new InvertFilter()),
	JavaLnFFilter ("JavaLnFFilter","JAV",new JavaLnFFilter()),
	NoiseFilter ("NoiseFilter","NOI",new NoiseFilter()),
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


