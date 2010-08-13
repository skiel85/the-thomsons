package model.filters;

import model.filters.AbstractBufferedImageOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

public class OtsuThresholder  extends AbstractBufferedImageOp
{
	private int histData[];
	private int maxLevelValue;
	private int threshold;

        private static int MAX_VALUE = 256;

	public OtsuThresholder()
	{
		histData = new int[MAX_VALUE];
	}

	public int[] getHistData()
	{
		return histData;
	}

	public int getMaxLevelValue()
	{
		return maxLevelValue;
	}

	public int getThreshold()
	{
		return threshold;
	}

	public int doThreshold(byte[] srcData, byte[] monoData)
	{
		int ptr;

		// Clear histogram data
		// Set all values to zero
		ptr = 0;
		while (ptr < histData.length) histData[ptr++] = 0;

		// Calculate histogram and find the level with the max value
		// Note: the max level value isn't required by the Otsu method
		ptr = 0;
		maxLevelValue = 0;
		while (ptr < srcData.length)
		{
			int h = 0xFF & srcData[ptr];
			histData[h] ++;
			if (histData[h] > maxLevelValue) maxLevelValue = histData[h];
			ptr ++;
		}

		// Total number of pixels
		int total = srcData.length;

		float sum = 0;
		for (int t=0 ; t<MAX_VALUE ; t++) sum += t * histData[t];

		float sumB = 0;
		int wB = 0;
		int wF = 0;

		float varMax = 0;
		threshold = 0;

		for (int t=0 ; t<256 ; t++)
		{
			wB += histData[t];					// Weight Background
			if (wB == 0) continue;

			wF = total - wB;						// Weight Foreground
			if (wF == 0) break;

			sumB += (float) (t * histData[t]);

			float mB = sumB / wB;				// Mean Background
			float mF = (sum - sumB) / wF;		// Mean Foreground

			// Calculate Between Class Variance
			float varBetween = (float)wB * (float)wF * (mB - mF) * (mB - mF);	

			// Check if new maximum found
			if (varBetween > varMax) {
				varMax = varBetween;
				threshold = t;
			}
		}

		// Apply threshold to create binary image
		if (monoData != null)
		{
			ptr = 0;
			while (ptr < srcData.length)
			{
				monoData[ptr] = ((0xFF & srcData[ptr]) >= threshold) ? (byte) 255 : 0;
				ptr ++;
			}
		}

		return threshold;
	}

    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        WritableRaster srcRaster = src.getRaster();
        DataBufferByte srcByteBuffer = (DataBufferByte) srcRaster.getDataBuffer();

        WritableRaster destRaster = dest.getRaster();
        DataBufferByte destByteBuffer = (DataBufferByte) destRaster.getDataBuffer();
        
        byte[] srcData = srcByteBuffer.getData(0);
        byte[] dstData = destByteBuffer.getData(0);//new int[srcData.length];

        doThreshold(srcData, dstData);

        return dest;
    }
    
	public String toString() {
		return "OtsuThresholder";
	}
	
	//Agregados by Mauro
	public Parameter[] getParameters() {
		this.parameterCount = 0;
		parameters = new Parameter[getParameterCount()];
		return parameters;
	}
	
	public void setParameterAt(int index,Object parameter){

	}

}
