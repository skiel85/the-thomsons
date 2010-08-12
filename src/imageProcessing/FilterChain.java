package imageProcessing;

import java.awt.image.BufferedImage;

import model.filters.AbstractBufferedImageOp;

public class FilterChain {
	
	private AbstractBufferedImageOp filter = null;
	private FilterChain nextFilter = null;
		
	
	public FilterChain( AbstractBufferedImageOp filter ){
		this.filter = filter;		
	}
	
	public FilterChain (AbstractBufferedImageOp filter, FilterChain next){
		this.filter = filter;
		this.nextFilter = next;
	}
	
	/**
	 * Adds the next filter to the chain of filters
	 * @param nextFilter Next Filter to be applied
	 */
	public void setNextFilter(FilterChain nextFilter){
		this.nextFilter = nextFilter;
	}
	
	/**
	 * 	 * Applies the filter and after that applies the next one if exists
	 * @param image Image to apply the filter
	 */
	public BufferedImage apply(BufferedImage image){
		BufferedImage result = new BufferedImage( image.getWidth(),	image.getHeight(), BufferedImage.TYPE_BYTE_GRAY );
			
		result = this.filter.filter(image,result);	
		if( nextFilter != null)
			result = this.nextFilter.apply(result);
		return result;
	}
	
}