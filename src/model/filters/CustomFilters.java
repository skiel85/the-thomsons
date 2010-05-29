package model.filters;


public enum CustomFilters {

	SHARPENV3FILTER ( "SharpenV3", "SV3",new SharpenV3Filter()),
	SHARPENV2FILTER ( "SharpenV2","SV2", new SharpenV2Filter()),
	MOOTHFILTER ( "Smooth","SMO" ,new SmoothFilter()),
	MEDIAFILTER ( "Media","MED" ,new MediaFilter() ),
	LOWFILTER ( "Low","LOW" ,new LowFilter() ),
	GAUSSLOWV3FILTER ( "GaussLowV3", "GV3",new GaussLowV3Filter() );
	

	public final String name;
	public final String shortName;
	public final AbstractFilter filter;
	CustomFilters (String name, String shortName,AbstractFilter filter){
		this.name = name;
		this.filter = filter;
		this.shortName = shortName;
	}
	
	public String toString(){
		return name;
	}
}
