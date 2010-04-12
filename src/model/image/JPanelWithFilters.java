package model.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class JPanelWithFilters extends JPanel {
   
	Image displayImage;

    BufferedImage bi;
    
    Graphics2D big;
    
    LookupTable lookupTable;
    
    float[] pattern;
   
    public JPanelWithFilters() {
		
    	super();
    	
	}

	public void loadImage( String string ) {
        
		displayImage = Toolkit.getDefaultToolkit().getImage( string );
		
		MediaTracker mt = new MediaTracker(this);
        
		mt.addImage(displayImage, 1);
        
        try {
        	
            mt.waitForAll();
        
        } catch (Exception e) {
            
        	System.out.println("Exception while loading.");
        
        }

        if (displayImage.getWidth(this) == -1) {
        
        	System.out.println("No jpg file");
            
        	System.exit(0);
        
        }
        
        createBufferedImage();
        
    }

    public void createBufferedImage() {
        
    	bi = new BufferedImage(displayImage.getWidth(this), displayImage.getHeight(this), BufferedImage.TYPE_INT_ARGB);

        big = bi.createGraphics();
        
        big.drawImage(displayImage, 44, 104, 700, 600, this);
    
    }

    public void brightenLUT(){
    
        short brighten[] = new short[256];
        
        for (int i = 0; i < 256; i++) {
            short pixelValue = (short) (i + 10);
            if (pixelValue > 255)
                pixelValue = 255;
            else if (pixelValue < 0)
                pixelValue = 0;
            brighten[i] = pixelValue;
        }
        
        lookupTable = new ShortLookupTable(0, brighten);
    
    }

    public void darkenLUT() {
        short brighten[] = new short[256];
        for (int i = 0; i < 256; i++) {
            short pixelValue = (short) (i - 10);
            if (pixelValue > 255)
                pixelValue = 255;
            else if (pixelValue < 0)
                pixelValue = 0;
            brighten[i] = pixelValue;
        }
        lookupTable = new ShortLookupTable(0, brighten);
    }

    public void contrastIncLUT() {
        short brighten[] = new short[256];
        for (int i = 0; i < 256; i++) {
            short pixelValue = (short) (i * 1.2);
            if (pixelValue > 255)
                pixelValue = 255;
            else if (pixelValue < 0)
                pixelValue = 0;
            brighten[i] = pixelValue;
        }
        lookupTable = new ShortLookupTable(0, brighten);
    }

    public void contrastDecLUT() {
        short brighten[] = new short[256];
        for (int i = 0; i < 256; i++) {
            short pixelValue = (short) (i / 1.2);
            if (pixelValue > 255)
                pixelValue = 255;
            else if (pixelValue < 0)
                pixelValue = 0;
            brighten[i] = pixelValue;
        }
        lookupTable = new ShortLookupTable(0, brighten);
    }

    public void reverseLUT() {

    	byte reverse[] = new byte[256];
        
    	for (int i = 0; i < 256; i++) {
        
    		reverse[i] = (byte) (255 - i);
        
    	}
        
    	lookupTable = new ByteLookupTable(0, reverse);
    }
    
    public void binarizeLUT(){
    	    	
        byte reverse[] = new byte[256];
        
        for (int i = 0; i < 256; i++) {
        
        	//primo mauro 150
        	
        	//eze 30
        	
        	 if( i < 150 ) reverse[i] = (byte) 0;
        	         	 
        	 else reverse[i] = (byte) 255;
        	
        }
    
        lookupTable = new ByteLookupTable(0, reverse);
    
    }
    
    public void toGrayScale(){
    	
    	ColorSpace colorSpace = ColorSpace.getInstance( ColorSpace.CS_GRAY );
    	
    	ColorConvertOp op = new ColorConvertOp(colorSpace, null);
    	
    	bi = op.filter(bi, null);
    	    	    
    }
    
    public void reset() {
    	
    	bi = new BufferedImage(displayImage.getWidth(this), displayImage.getHeight(this), BufferedImage.TYPE_INT_ARGB);

        big = bi.createGraphics();
    	        
        big.drawImage(displayImage, 44, 104, 700, 600, this);
        
    }
    
    public void sharpenV3(){
    	   
    	pattern = new float[] {
    			
      		     0.0f, -1.0f, 0.0f,
      		    -1.0f, 5.0f, -1.0f,
      		     0.0f, -1.0f, 0.0f
      		
    	};
    	
    }
    
    public void sharpenV2(){
 	   
    	pattern = new float[] {
    			
      		     -1.0f, -1.0f, -1.0f,
      		    -1.0f, 9.0f, -1.0f,
      		     -1.0f, -1.0f, -1.0f
      		
    	};
    	
    }
    
    public void lowFilter(){
 	   
    	pattern = new float[] {
    			
      		     0.0f, 0.1f, 0.0f,
      		     0.1f, 0.6f, 0.1f,
      		     0.0f, 0.1f, 0.0f
      		
    	};
    	
    }

    public void smooth(){
  	   
    	pattern = new float[] {
    			
      		     0.0625f, 0.125f, 0.0625f,
      		     0.125f, 0.25f, 0.125f,
      		   0.0625f, 0.125f, 0.0625f
      		
    	};
    	
    }
    
    public void media(){
       	
    	pattern = new float[] {

    	     		     1.0f, -2.0f, 1.0f,
    	     		     -2.0f, 5.0f, -2.0f,
    	        		 1.0f, -2.0f, 1.0f
      		
    	};
    	
    }
    
    public void gaussLowFilterV3(){
       	
    	pattern = new float[] {
    			
    	     	//	     1.0f, -2.0f, 1.0f,
    	       	//	    -2.0f, 4.0f, -2.0f,
    	       	//	     1.0f, -2.0f, 1.0f
    	       		     
    	     		     1/9f, 2/9f, 1/9f,
    	     		     2/9f, 4/9f, 2/9f,
    	        		 1/9f, 2/9f, 1/9f
      		
    	};
    	
    }
    
    public void applyFilterWithLookUpTable() {
   		
    	LookupOp lop = new LookupOp(lookupTable, null);
    	
        lop.filter( bi, bi);
    
    }
    
    public void applyFilter() {
   		
    	Kernel kernel = new Kernel( 3, 3, pattern );
   		
    	ConvolveOp op = new ConvolveOp(kernel);
   	
    	bi = op.filter( bi, null);
    
    }

    public void update(Graphics g) {
    	
        g.clearRect(0, 0, getWidth(), getHeight());
    
        paintComponent(g);
    
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(bi, 0, 0, this);
    }
}