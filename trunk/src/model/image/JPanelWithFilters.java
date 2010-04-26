package model.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
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
	BufferedImage biOld;
	Graphics2D big;
	LookupTable lookupTable;
	float[] pattern;

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

	public void createBufferedImage() {

		//bi = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
		bi = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
		biOld = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
		big = bi.createGraphics();
		big.drawImage(displayImage, 0, 0, 640, 480, this);	
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


	public int swipeFromRightToLeft( int y ) {

		
		int actualColor = bi.getRGB( bi.getWidth() - 1, y );

		int lastColor = actualColor;

		for (int i = 0; i < bi.getWidth() - 1; i++) {

			actualColor = bi.getRGB( bi.getWidth() - i - 1, y );

			int deltaColor = lastColor - actualColor;

			if( deltaColor > 0 ) return bi.getWidth() - i - 1;

		}

		return 0;

	}

	public Point getNose() {

		int x = 0;

		int y = 0;

		for (int i = 0; i < bi.getHeight() - 1; i++) {

			int newX = swipeFromRightToLeft(i);

			if (newX > x) {

				x = newX;

				y = i;

			}

		}

		Point p = new Point(x, y);

		return p;

	}

	public void sharpenV3() {

		pattern = new float[] {

		0.0f, -1.0f, 0.0f, -1.0f, 5.0f, -1.0f, 0.0f, -1.0f, 0.0f

		};

	}

	public void sharpenV2() {

		pattern = new float[] {

		-1.0f, -1.0f, -1.0f, -1.0f, 9.0f, -1.0f, -1.0f, -1.0f, -1.0f

		};

	}

	public void lowFilter() {

		pattern = new float[] {

		0.0f, 0.1f, 0.0f, 0.1f, 0.6f, 0.1f, 0.0f, 0.1f, 0.0f

		};

	}

	public void smooth() {

		pattern = new float[] {

		0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f, 0.0625f, 0.125f,
				0.0625f

		};

	}

	public void media() {

		pattern = new float[] {

		1.0f, -2.0f, 1.0f, -2.0f, 5.0f, -2.0f, 1.0f, -2.0f, 1.0f

		};

	}

	public void gaussLowFilterV3() {

		pattern = new float[] {

				// 1.0f, -2.0f, 1.0f,
				// -2.0f, 4.0f, -2.0f,
				// 1.0f, -2.0f, 1.0f

				1 / 9f, 2 / 9f, 1 / 9f, 2 / 9f, 4 / 9f, 2 / 9f, 1 / 9f, 2 / 9f,
				1 / 9f

		};

	}
	
	public void undo(){
		bi = biOld;
	}

	public void applyFilterWithLookUpTable() {

		LookupOp lop = new LookupOp(lookupTable, null);
		biOld = bi;
		bi = lop.filter(bi, null);
	}

	public void applyFilter() {

		Kernel kernel = new Kernel(3, 3, pattern);
		ConvolveOp op = new ConvolveOp(kernel);
		biOld = bi;
		bi = op.filter(bi, null);

	}

	public void update(Graphics g) {

		g.clearRect(0, 0, getWidth(), getHeight());
		paintComponent(g);
	}

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(bi, 40, 120, this);
		//g2D.drawImage(bi.getScaledInstance(640, 480, Image.SCALE_AREA_AVERAGING), 0, 0, this);
	}
}