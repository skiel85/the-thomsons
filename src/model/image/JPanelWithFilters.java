package model.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RenderedImage;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.BlurFilter;
import com.jhlabs.image.ConvolveFilter;
import com.jhlabs.image.ImageUtils;

import flanagan.math.FourierTransform;
import flanagan.plot.PlotGraph;

import model.filters.AbstractFilter;





@SuppressWarnings("serial")
public class JPanelWithFilters extends JPanel {

	Image displayImage;
	BufferedImage biDisplay;
	BufferedImage bi;
	Graphics2D big;
	LookupTable lookupTable;
	AbstractFilter filter;
	int bufimageWidth;
	int bufimageHeight;
	Point[] points;
	


	public JPanelWithFilters() {

		super();
	}

	public void loadImage(String string) {

		displayImage = Toolkit.getDefaultToolkit().getImage(string);
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(displayImage, 1);
		try {
			mt.waitForAll();
		} catch (Exception e) {
			System.out.println("Exception while loading.");
		}
		if (displayImage.getWidth(this) == -1) {
			System.out.println("No image file");
			System.exit(0);
		}
		createBufferedImage();
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(bi, 40, 120, this);
		//g2D.drawImage(bi.getScaledInstance(640, 480, Image.SCALE_AREA_AVERAGING), 0, 0, this);
	}

	public void createBufferedImage() {

		int imageWidth = displayImage.getWidth(this);
		int imageHeight = displayImage.getHeight(this);
		bufimageWidth = 640;
		if (bufimageWidth > imageWidth) bufimageWidth = imageWidth;
		bufimageHeight = 480;
		if (bufimageHeight > imageHeight) bufimageHeight = imageHeight;
		
		bi = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		//bi = ImageUtils.convertImageToARGB(displayImage);
		
		//biDisplay = new BufferedImage(IMG_FRAME_WIDTH, IMG_FRAME_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		big = bi.createGraphics();
		big.drawImage(displayImage, 0, 0, bufimageWidth, bufimageHeight, this);	
		big.dispose();
		
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

		byte brighten[] = new byte[256];
		for (int i = 0; i < 256; i++) {
			byte pixelValue = (byte) (i - 10);
			if (pixelValue > 255)
				pixelValue = (byte) 255;
			else if (pixelValue < 0)
				pixelValue = 0;
			brighten[i] = pixelValue;
		}
		//lookupTable = new ShortLookupTable(0, brighten);
		lookupTable = new ByteLookupTable(0, brighten);
		
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
	
	public void identityLUT() {

		byte reverse[] = new byte[256];
		for (int i = 0; i < 256; i++) {
			reverse[i] = (byte) (i);
		}
		lookupTable = new ByteLookupTable(0, reverse);
	}

	public void binarizeLUT(int valor) {

		byte reverse[] = new byte[256];
		for (int i = 0; i < 256; i++) {
			// primo mauro 150
			// eze 30
			if (i < valor)
				reverse[i] = (byte) 0;
			else
				reverse[i] = (byte) 255;
		}
		lookupTable = new ByteLookupTable(0, reverse);
	}

	public void toGrayScale() {

		ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(colorSpace, null);
		
		
		
		
		bi = op.filter(bi, null);
		
		
		
	}

	public void reset() {
		createBufferedImage();

	}

	public void applyFilterWithLookUpTable() {

		LookupOp lop = new LookupOp(lookupTable, null);
		/*if(BufferedImageChanges.getInstance().lastImageBinarized()) {
			BufferedImageChanges.getInstance().undo();
			bi = BufferedImageChanges.getInstance().getCurrentImage();
		}
		BufferedImageChanges.getInstance().binarize(true);
		BufferedImageChanges.getInstance().changeImage(bi);
		*/bi = lop.filter(bi, null);
	}

	public void applyFilter() {

		Kernel kernel = new Kernel(3, 3, filter.getPattern());
		ConvolveOp op = new ConvolveOp(kernel);
		BufferedImageChanges.getInstance().binarize(false);
		BufferedImageChanges.getInstance().changeImage(bi);
		bi = op.filter(bi, null);
	}

	public void update(Graphics g) {

		g.clearRect(0, 0, getWidth(), getHeight());
		paintComponent(g);
	}
	
	 public void guardarImagen(String nombre)
	    {
	    	RenderedImage rend=bi;
	    	
	    	try {
	    		   ImageIO.write(rend, "jpg", new File(nombre+".jpg"));
	    		} catch (IOException e) {
	    		   System.out.println("Error de escritura");
	    		}


	    }

	public void undo() {
		bi = BufferedImageChanges.getInstance().undo();		
	}

	public Point[] detectarBorde() {
		Bordeador bordeador = new Bordeador (bi,bufimageWidth,bufimageHeight);
		points= bordeador.bordear();
		return points;
	}

	public void setFilter(AbstractFilter filter) {
		this.filter = filter;
	}

	public void applyNewFilters(ConvolveFilter filter) {
		//Kernel kernel = filter.getKernel();
		//ConvolveOp op = new ConvolveOp(kernel);
		BufferedImageChanges.getInstance().binarize(false);
		BufferedImageChanges.getInstance().changeImage(bi);
		//bi = op.filter(bi, null);
		bi = filter.filter(bi, null);
	}
	
	public void transformadaFourier(){
		int cant=points.length;
		 double deltaT = 1.0D/200;
		double[] xdata = new double[cant];
        double[] ydata = new double[cant];
		System.out.println("Esta es la cantidad de puntos " + cant);
		for(int i=0; i<cant; i++){
			System.out.println("Este es el x " + points[i].x);
			System.out.println("Este es el y " + points[i].y);
			ydata[i]=points[i].y;
			xdata[i]=points[i].x;
        }
		FourierTransform ft0 = new FourierTransform(ydata);
        ft0.setDeltaT(deltaT);
        ft0.setData(ydata);
        //ft0.setData(xdata);
        

     
        
        ft0.powerSpectrum();
        
        //Descomentar la linea de abajo si se quiere ver el grafico de frecuencias
        //ft0.plotPowerSpectrum();

        // Obtain the transformed data
        double[] transformedData = ft0.getTransformedDataAsAlternate();
        
    	for(int i=0; i<transformedData.length; i++){
    		System.out.println("Esta es la transformada " + transformedData[i]);	
    	}
        
       
	}


}