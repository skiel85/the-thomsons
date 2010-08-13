package model.image;

import imageProcessing.ImageWrapper;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RenderedImage;
import java.awt.image.ShortLookupTable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.filters.AbstractBufferedImageOp;
import model.filters.ConvolveFilter;
import model.filters.CustomFilters;
import vectorization.Point;

@SuppressWarnings("serial")
public class JPanelWithFilters extends JPanel {


	Image displayImage;
	BufferedImage biDisplay;
	BufferedImage bi;
	Graphics2D big;
	LookupTable lookupTable;
	AbstractBufferedImageOp filter;
	int bufimageWidth;
	int bufimageHeight;
	Point[] points;
	Point puntoCentro;
	boolean sigue=false;
	String nombreComparacion="";
	String [][] matriz=new String [600][513];
	int j=0;
	ImageWrapper imagewrapper;
	

	public JPanelWithFilters() {

		super();
	}

	public void loadImage(String string) {

		imagewrapper = new ImageWrapper();
		imagewrapper.setPath(string);
		
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
		// g2D.drawImage(bi.getScaledInstance(640, 480,
		// Image.SCALE_AREA_AVERAGING), 0, 0, this);
	}

	public void createBufferedImage() {

		int imageWidth = displayImage.getWidth(this);
		int imageHeight = displayImage.getHeight(this);
		bufimageWidth = 640;
		if (bufimageWidth > imageWidth)
			bufimageWidth = imageWidth;
		bufimageHeight = 480;
		if (bufimageHeight > imageHeight)
			bufimageHeight = imageHeight;
		
		
		imagewrapper.setBufimageHeight(bufimageHeight);
		imagewrapper.setBufimageWidth(bufimageWidth);
		imagewrapper.setImageHeight(imageHeight);
		imagewrapper.setImageWidth(imageWidth);

		bi = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_ARGB);
		// bi = ImageUtils.convertImageToARGB(displayImage);

		// biDisplay = new BufferedImage(IMG_FRAME_WIDTH, IMG_FRAME_HEIGHT,
		// BufferedImage.TYPE_INT_ARGB);

		big = bi.createGraphics();
		
		imagewrapper.setBufferedImage(bi);
		
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
		// lookupTable = new ShortLookupTable(0, brighten);
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

		BufferedImageChanges.getInstance().changeImage(bi);
		bi = op.filter(bi, null);
	}

	public void reset() {
		createBufferedImage();

	}

	public void applyFilterWithLookUpTable() {
		LookupOp lop = new LookupOp(lookupTable, null);
		BufferedImageChanges.getInstance().changeImage(bi);
		bi = lop.filter(bi, null);
	}

	public void update(Graphics g) {

		g.clearRect(0, 0, getWidth(), getHeight());
		paintComponent(g);
	}

	public void guardarImagen(String nombre) {
		RenderedImage rend = bi;

		try {
			ImageIO.write(rend, "jpg", new File(nombre + ".jpg"));
		} catch (IOException e) {
			System.out.println("Error de escritura");
		}

	}

	public void undo() {
		bi = BufferedImageChanges.getInstance().undo();
	}

	public Point[] detectarBorde() {
		Bordeador bordeador = new Bordeador(bi, bufimageWidth, bufimageHeight);
		points = bordeador.bordear();
		puntoCentro=bordeador.faceCenter;
		
		return points;
	}

	public void setFilter(AbstractBufferedImageOp filter) {
		this.filter = filter;
	}

	public void applyNewFilters(ConvolveFilter filter) {
		// Kernel kernel = filter.getKernel();
		// ConvolveOp op = new ConvolveOp(kernel);
		BufferedImageChanges.getInstance().changeImage(bi);
		// bi = op.filter(bi, null);
		bi = filter.filter(bi, null);
	}



	public void applyFilter(CustomFilters filter2) {
		BufferedImageChanges.getInstance().changeImage(bi);
		bi = filter2.filter.filter(bi, null);

	}

}