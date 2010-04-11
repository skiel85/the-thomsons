package model.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
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
        
        big.drawImage(displayImage, 44, 104, this);
    
    }

    public void brightenLUT() {
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

    public void reset() {
        big.setColor(Color.black);
        big.clearRect(0, 0, bi.getWidth(this), bi.getHeight(this));
        big.drawImage(displayImage, 0, 0, this);
    }

    public void applyFilter() {
        LookupOp lop = new LookupOp(lookupTable, null);
        lop.filter(bi, bi);
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