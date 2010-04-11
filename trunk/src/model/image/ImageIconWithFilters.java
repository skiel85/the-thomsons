package model.image;

import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class ImageIconWithFilters extends ImageIcon {

	LookupTable lookupTable;
	
    public void brighten(){
    	
        short brighten[] = new short[256];
        
        for (int i = 0; i < 256; i++) {
        
        	short pixelValue = (short) (i + 10);
            
        	if (pixelValue > 255)	pixelValue = 255;
            
        	else if (pixelValue < 0)	pixelValue = 0;
        	
            brighten[i] = pixelValue;
        
        }
        
        lookupTable = new ShortLookupTable(0, brighten);
    }
	
}
