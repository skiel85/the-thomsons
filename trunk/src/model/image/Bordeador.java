package model.image;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Bordeador {

	public BufferedImage bi;
	public int maxY, minY, maxX, minX;
	public Point faceTop, faceBottom, faceCenter;
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
	// Cantidad hacia arriba medido en unid de cara desde nariz
	public static double TIMES_FACE_UP = 0.5;
	// Cantidad hacia abajo medido en unid de cara desde labio inf
	public static double TIMES_FACE_DOWN = 0.5;
	// Cantidad hacia adentro medido en unid de cara desde nariz
	public static double TIMES_FACE_CENTER = 3.0;
	// Resolucion de puntos, para la trasnformada tiene que ser multiplo de 2 o si no tira warnings
	public static int RESOLUTION = 256;

	public Bordeador(BufferedImage bi, int bufimageWidth, int bufimageHeight) {
		this.bi = bi;
		maxX = (int) (bufimageWidth * ((double) (100 - PERC_IMG_LIMIT) / 100));
		minX = (int) (bufimageWidth * ((double) (PERC_IMG_LIMIT) / 100));
		maxY = (int) (bufimageHeight * ((double) (100 - PERC_IMG_LIMIT) / 100));
		minY = (int) (bufimageHeight * ((double) (PERC_IMG_LIMIT) / 100));

		ONE_PERC_IMG_X = bufimageWidth / 100;
		ONE_PERC_IMG_Y = bufimageHeight / 100;
	}

	public Point[] bordear() {

		Point nose = getNose(minY);
		Point philtrum = getLowPoint(nose.y);
		Point upperlip = getHighPoint(philtrum.y);
		Point mouth = getLowPoint(upperlip.y);
		Point lowerlip = getHighPoint(mouth.y);
		
		
		Point minFacePoint = nose;
		Point maxFacePoint = lowerlip;
		
		getLimits(minFacePoint,maxFacePoint);
		
		if (isInvalid(maxFacePoint) || isInvalid(faceBottom)){
			maxFacePoint = mouth;
			getLimits(minFacePoint,maxFacePoint);
			if (isInvalid(mouth) || isInvalid(faceBottom)){
				maxFacePoint = upperlip;
				getLimits(minFacePoint,maxFacePoint);
			}
		}
		if (isInvalid(minFacePoint) || isInvalid(faceTop)){
			minFacePoint = philtrum;
			getLimits(minFacePoint,maxFacePoint);
			if (isInvalid(philtrum) || isInvalid(faceTop)){
				minFacePoint = upperlip;
				getLimits(minFacePoint,maxFacePoint);
			}
		}

		
		Point[] points = getBorderPoints (faceTop,faceBottom,faceCenter);
		

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
		for (int i=0; i<RESOLUTION;i++){
			if (points[i]!=null)
				drawMark(points[i]);
			else
				System.out.println("!!!PUNTO NULL: "+points[i]+" con i="+i);
		}
		return points;

	}
	
	private boolean isInvalid(Point point) {
		if (point == null)
			return true;
		if(point.x ==0 || point.y ==0 || point.x<minX || point.x>maxX || point.y<minY || point.y>maxY )
			return true;
		return false;
	}

	private void getLimits(Point nose, Point lowerlip) {
		
		ONE_FACE_SIZE_Y = lowerlip.y - nose.y;
		
		int faceTopY = nose.y - (int)(ONE_FACE_SIZE_Y*TIMES_FACE_UP);
		faceTop = new Point (swipeFromRightToLeft(faceTopY, maxX, minX),faceTopY);
		int faceBottomY = lowerlip.y + (int)(ONE_FACE_SIZE_Y*TIMES_FACE_DOWN);
		faceBottom = new Point (swipeFromRightToLeft(faceBottomY, maxX, minX),faceBottomY);
		faceCenter = new Point (nose.x-(int)(ONE_FACE_SIZE_Y*TIMES_FACE_CENTER),nose.y);
	}
	
	

	private Point[] getBorderPoints(Point faceTop, Point faceBottom, Point faceCenter){
		
		Point[] points = new Point[RESOLUTION];
		
		double faceHeight = (1+TIMES_FACE_UP+TIMES_FACE_DOWN); // Medido en caras
		
		double faceWidth =  (TIMES_FACE_CENTER); //Medido en caras
		
		int faceFrontRes = (int) (RESOLUTION*faceHeight/(2*faceHeight + 2*faceWidth)); //2*resolution/(2+2+3+3) = 60
		
		int faceBorderRes = (int) (RESOLUTION*faceWidth/(2*faceHeight + 2*faceWidth)); //3*resolution/(2+2+3+3) = 90
		
		int faceMirrorEnd = faceFrontRes*2 + faceBorderRes ; //60+90+60
		
		double salto = (double)(faceHeight*ONE_FACE_SIZE_Y /(double) faceFrontRes);
		
		int y = faceTop.y;
		
		for (int i=0; i <= faceFrontRes; i++){
		
			points[i] = new Point (swipeFromRightToLeft((int)(y+i*salto), maxX, minX),(int)(y+i*salto));
			
			points[faceMirrorEnd-i] = new Point(faceCenter.x - (points[i].x-faceCenter.x), points[i].y);
		
		}
		
		double saltoBottom = (double)((2*(faceBottom.x - faceCenter.x)) /(double) faceBorderRes);
		
		double saltoTop = (double)((2*(faceTop.x - faceCenter.x)) /(double) faceBorderRes);
		
		System.out.println( "faceTop.y: " + faceTop.y );
		
		for (int i=0; i <= faceBorderRes; i++){
		
			int xbottom = (int) ( faceBottom.x - i*saltoBottom );
			
			int xtop = (int) ( faceTop.x - i*saltoTop );
			
			points[i+faceFrontRes] = new Point ( xbottom, (int) ( ( i - 0 ) * ( i - faceBorderRes ) * ( -1 ) * 0.025 + faceBottom.y ) );
			
			points[RESOLUTION-1 -i] = new Point( xtop , (int) ( ( i - 0 ) * ( i - faceBorderRes ) * ( +1 ) * 0.025 + faceTop.y ) );
		
			System.out.println( "delta: " + ( i - 0 ) * ( i - faceBorderRes ) * ( -1 ) * 0.5 + "     y: " + ( ( i - 0 ) * ( i - faceBorderRes ) * ( -1 ) * 0.5 + faceTop.y ) ); 
			
		}
		
		return points;
	
	}

	private Point getHighPoint(int y){
		
		return getNextCriticalPoint(y,+1,+1);
	
	}

	private Point getLowPoint(int y){
		
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
			
			//System.out.println( "x1: " + x1 );
			
			if( ( x1 - x0) * raise < 0 ) found = true;
			
			x0 = x1;
			
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
