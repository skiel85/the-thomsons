package model.image;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Bordeador {

	public BufferedImage bi;
	public int maxY, minY, maxX, minX;
	// Porcentaje de la pantalla a partir de la cual mira
	public static final int PERC_IMG_LIMIT = 5;
	// Porcentaje de tolerancia de PELO
	public static final int PERC_HAIR_TOL = 30;
	// Unidad de medida de 1% de la imagen
	public static int ONE_PERC_IMG_Y;
	public static int ONE_PERC_IMG_X;
	// Unidad de medida de 1 de la cara
	public static int ONE_FACE_SIZE_Y;
	public static int ONE_FACE_SIZE_X;

	public Bordeador(BufferedImage bi, int bufimageWidth, int bufimageHeight) {
		this.bi = bi;
		maxX = (int) (bufimageWidth * ((double) (100 - PERC_IMG_LIMIT) / 100));
		minX = (int) (bufimageWidth * ((double) (PERC_IMG_LIMIT) / 100));
		maxY = (int) (bufimageHeight * ((double) (100 - PERC_IMG_LIMIT) / 100));
		minY = (int) (bufimageHeight * ((double) (PERC_IMG_LIMIT) / 100));

		ONE_PERC_IMG_X = bufimageWidth / 100;
		ONE_PERC_IMG_Y = bufimageHeight / 100;
	}

	public void bordear() {

		Point nose = getNose(minY);
		Point philtrum = getLowPoint(nose.y);
		Point upperlip = getHighPoint(philtrum.y);
		Point mouth = getLowPoint(upperlip.y);
		Point lowerlip = getHighPoint(mouth.y);

		ONE_FACE_SIZE_Y = lowerlip.y - nose.y;
		
		int faceTopY = nose.y - ONE_FACE_SIZE_Y/2;
		Point faceTop = new Point (swipeFromRightToLeft(faceTopY, maxX, minX),faceTopY);
		int faceBottomY = lowerlip.y + ONE_FACE_SIZE_Y/2;
		Point faceBottom = new Point (swipeFromRightToLeft(faceBottomY, maxX, minX),faceBottomY);
		
		Point faceCenter = new Point (3*ONE_FACE_SIZE_Y,nose.y);
		
		int resolution = 300;
		Point[] points = new Point[resolution];
		int faceFrontRes = 60; //2*resolution/10
		int faceMirrorIni = 150; //60+90
		int x=0;
		int y=0;
		for (int i=0; i < faceFrontRes; i++){
//			points[i].setLocation(x, y);
//			points[faceMirrorIni-i].setLocation(x, y);
		}
		
		
		

		// TODO sacar los println
		System.out.println("nose: "+nose);
		drawMark(nose);
		System.out.println("philtrum: "+philtrum);
		drawMark(philtrum);
		System.out.println("upperlip: "+upperlip);
		drawMark(upperlip);
		System.out.println("mouth: "+mouth);
		drawMark(mouth);
		System.out.println("lowerlip: "+lowerlip);
		drawMark(lowerlip);
		System.out.println("faceTop: "+faceTop);
		drawMark(faceTop);
		System.out.println("faceBottom: "+faceBottom);
		drawMark(faceBottom);
		System.out.println("faceCenter: "+faceCenter);
		drawMark(faceCenter);

	}

	private Point getHighPoint(int y) {
		return getNextCriticalPoint(y,+1,+1);
	}

	private Point getLowPoint(int y) {
		return getNextCriticalPoint(y,+1,-1);
	}

	public int swipeFromRightToLeft(int y, int maxX, int minX) {

		int actualColor = bi.getRGB(maxX - 1, y);
		int lastColor = actualColor;
		for (int i = minX; i < maxX - 1; i++) {
			actualColor = bi.getRGB(maxX - i - 1, y);
			int deltaColor = lastColor - actualColor;
			if (deltaColor != 0)
				return maxX - i - 1;
		}
		return 0;
	}

	/*
	 * Barrido de arriba hacia abajo para detectar el punto oscuro mas
	 * sobresaliente, osea con X mayor.
	 */
	public Point getFarthest(int maxY, int minY, int maxX, int minX) {
		int x = 0;
		int y = 0;
		for (int i = minY; i < maxY - 1; i++) {
			int newX = swipeFromRightToLeft(i, maxX, minX);
			if (newX > x) {
				x = newX;
				y = i;
			}
		}
		return new Point(x, y);
	}

	public boolean isWhiteStripe(int y, int maxX, int minX) {

		int newX = swipeFromRightToLeft(y, maxX, minX);
		if (newX <= minX || newX == 0)
			return true;
		return false;
	}

	public Point getNose(int localMinY) {

		Point nose = getFarthest(maxY, localMinY, maxX, minX);
		// Si el barrido de una linea superior por PERC_HAIR_TOL% da vacio x=0 o
		// x=minX, no era nariz sino pelo
		int yWhiteStripe = nose.y - ONE_PERC_IMG_Y * PERC_HAIR_TOL;
		if (yWhiteStripe <= minY || isWhiteStripe(yWhiteStripe,maxX,minX)==true) {
			nose = getNose(nose.y + 2);
		}
		return nose;
	}
	
	/*
	 * 
	 *  Encuentra el proximo pozo o relieve en el sentido way desde yInitial
	 *  
	 *  way = +1 para arriba, -1 para abajo
	 *  
	 *  raise = +1 relieve, -1 pozo  
	 *  
	 */
	
	public Point getNextCriticalPoint( int yInitial, int way, int raise ){
		
		int y = yInitial;
		
		int x0 = swipeFromRightToLeft( y, maxX, minX );
		
		int x1 = x0;
		
		boolean found = false;
		
		while( !found && y < maxY && y > minY){
			
			y = y + way;
			
			x1 = swipeFromRightToLeft( y, maxX, minX );
			
			if( ( x1 - x0) * raise < 0 ) found = true;
			
		}
		
		Point point = new Point( x1, y );
		
		return point;
		
	}



	public void drawMark(Point p) {
		drawRedMark(p.x, p.y);
	}

	public void drawRedMark(int x, int y) {
		for (int i = -5; i < 5; i++) {
			bi.setRGB(x + i, y + i, Color.RED.getRGB());
			bi.setRGB(x - i, y + i, Color.RED.getRGB());
		}
	}

}
