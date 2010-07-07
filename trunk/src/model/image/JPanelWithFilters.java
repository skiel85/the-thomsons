package model.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.List;
import java.awt.MediaTracker;
import java.awt.Point;
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
import java.io.BufferedWriter;
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
import flanagan.complex.Complex;
import flanagan.math.FourierTransform;
import java.io.FileOutputStream;

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

		bi = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_ARGB);
		// bi = ImageUtils.convertImageToARGB(displayImage);

		// biDisplay = new BufferedImage(IMG_FRAME_WIDTH, IMG_FRAME_HEIGHT,
		// BufferedImage.TYPE_INT_ARGB);

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

	public void transformadaFourier() {

		int cant = points.length;

		

		ordenar();

		double resta = points[cant - 1].x - points[0].x;

		double deltaT = (resta) / cant;

		System.out.println("Este es el delta " + deltaT);

		double[] ydata = new double[cant];

		System.out.println("Esta es la cantidad de puntos " + cant);

		for (int i = 0; i < cant; i++) {
			// System.out.println("Este es el punto x "+ points[i].x);
			// System.out.println("Este es el punto y "+ points[i].y);
			ydata[i] = points[i].y;

		}

		FourierTransform ft0 = new FourierTransform(ydata);

		ft0.setDeltaT(deltaT);

		ft0.setData(ydata);

		// ft0.powerSpectrum();

		// Descomentar la linea de abajo si se quiere ver el grafico de
		// frecuencias
		//ft0.plotPowerSpectrum();
		
		//Este metodo realiza las transformadas
		ft0.transform();

		// Obtain the transformed data como datos alternados
		//double[] transformedData = ft0.getTransformedDataAsAlternate();
		
		//Obtiene la transformada como datos complejos
          Complex[] complexTransformedData = ft0.getTransformedDataAsComplex();

		for (int i = 0; i < complexTransformedData.length; i++) {

			System.out.println("Transformada en del Punto: " + i
					+ "  Numero Complejo: " + complexTransformedData[i]);

		}

	}

	public Point[] ordenar() {

		int p1;
		int p2;
		int py1;
		int py2;
		int x = 0;
		boolean bandera = true;
		int elementos = points.length;
		while ((elementos > 1) && (bandera == true)) {
			x = 0;
			p1 = points[x].x;
			p2 = points[x + 1].x;
			py1 = points[x].y;
			py2 = points[x + 1].y;
			elementos--;
			bandera = false;
			for (int i = 0; i < elementos; i++) {
				if (p1 > p2) {
					points[i].x = p2;
					points[i + 1].x = p1;
					points[i].y = py2;
					points[i + 1].y = py1;
					bandera = true;
				}// fin de si

				x++;
				if (x < elementos) {
					p1 = points[x].x;
					p2 = points[x + 1].x;
					py1 = points[x].y;
					py2 = points[x + 1].y;
				}
			}
		}
		return points;
	}
/*	public void generarArchivo(){
		
        try {
			fichero = new FileWriter("Datos.txt",true);
			pw = new PrintWriter(fichero);

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	

		
	}*/
	
	public Point[] transformadaFourier2(String nombre) {
		FileWriter fichero = null;
	    PrintWriter pw = null;
		   try {
				fichero = new FileWriter("DatosFin.txt",true);
				pw = new PrintWriter(fichero);

			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		//generarArchivo();
		 String datos=nombre;	
		 
		 
		 Point[] salida=new  Point[points.length];
		 Point transformedPoint = null;
		 int N = points.length;
		 for (int i = 0; i < N; i++) {
			
			 points[i].x=points[i].x-puntoCentro.x;
			 points[i].y=points[i].y-puntoCentro.y;
			
		 }
		 for (int k = 0; k < N; k++) {
		        double u = 0;
		        double v = 0;
		        int n = 0;
		        for(int j = 0; j < N; j++) {
		            u += points[j].x * Math.cos((2*Math.PI*k*n)/N) + points[j].y * Math.sin((2*Math.PI*k*n)/N);
		            v += points[j].y * Math.cos((2*Math.PI*k*n)/N) - points[j].x * Math.sin((2*Math.PI*k*n)/N);
		            n++;
		        }
		        transformedPoint = new Point((int)Math.round(u), (int)Math.round(v));
		        salida[k]=transformedPoint;
		       // System.out.print("+"+transformedPoint.x+"+"+transformedPoint.y);
		        datos=datos+","+transformedPoint.x+","+transformedPoint.y;
		    }
		 	if (!sigue)
		 	pw.println(datos);
		 	sigue=false;
		 	pw.close();
		    return salida;
	}
	
 
   public Point[] procesarLinea(String linea,int j){
	   Point[] distancia =new  Point[points.length];
	   String comienzo=linea;
	   String fin=linea;
	   for (int i=0;i<512;i++){
	   linea=fin;
	   int cant=linea.indexOf(",");
	   comienzo=linea.substring(0, cant);
	   fin=linea.substring(cant+1);
	  // System.out.println(comienzo);
	   matriz[j][i]=comienzo;
	   }
	   
	   return distancia;
   }

	public void procesarArchivo(){
	      File archivo = null;
	      FileReader fr = null;
	      BufferedReader br = null;
	      Point[] distancia =new  Point[points.length];
	      String linea="";
	      
	      try {
	         // Apertura del fichero y creacion de BufferedReader para poder
	         // hacer una lectura comoda (disponer del metodo readLine()).
	         archivo = new File ("DatosFin.txt");
	         fr = new FileReader (archivo);
	         br = new BufferedReader(fr);

	         // Lectura del fichero
	         
	         while((linea=br.readLine())!=null){
	           // System.out.println(linea);
	            procesarLinea(linea,j);
	            j=j+1;
	         }
	      }
	      catch(Exception e){
	         e.printStackTrace();
	      }finally{
	         // En el finally cerramos el fichero, para asegurarnos
	         // que se cierra tanto si todo va bien como si salta 
	         // una excepcion.
	         try{                    
	            if( null != fr ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
	       
		
	}
	
	public void distanciaEuclidea(String nombre){
		sigue=true;
		Point[] distanciaEuclidea =transformadaFourier2(nombre);
		procesarArchivo();
		Point[] resultado= new Point[points.length];
		Double[] resultadoDistancia=new Double[distanciaEuclidea.length-1];
		int p=0;
		for (int k=0;k<j;k++){
			
		
			if (matriz[k][0].equals(nombre)){
				
			}
			else{
				int t=1;
	/*		for (p=0;p<points.length;p++){
				System.out.println(k + " " + p + "  "+ t);
				System.out.println(matriz[k][t] + "    " + matriz[k][t+1]);
				resultado[p+1].x=Integer.parseInt(matriz[k][t]);
				System.out.println(Integer.parseInt("73"));
				//resultado[p].x=Integer.parseInt("73");
				System.out.println("PASO");
				//resultado[p].y=Integer.parseInt(matriz[k][t+1]);
				t=t+2;
				
			
				
			}*/
				double suma=0;
			int N = distanciaEuclidea.length;
			 for (int i = 0; i < N-1; i++) {
				
				 resultadoDistancia[i]=Math.sqrt(Math.pow(distanciaEuclidea[i].x-Integer.parseInt(matriz[k][t]), 2) + Math.pow(distanciaEuclidea[i].y-Integer.parseInt(matriz[k][t+1]), 2));
				 //System.out.println("Este es el i " + i + " este es el resultado " +resultadoDistancia[i]);
				 t=t+2;
				 suma=suma+resultadoDistancia[i];
			
			}
			 System.out.println("La Distancia entre la imagen " + nombre + " y la imagen " + matriz[k][0] + " es " + suma/254 );
		}
		//Point[] distancia2=procesarLinea(linea);
	/*	Double[] resultado=new Double[distanciaEuclidea.length-1];*/
		 	 
			
		 }
	}

	public void applyFilter(CustomFilters filter2) {
		BufferedImageChanges.getInstance().changeImage(bi);
		bi = filter2.filter.filter(bi, null);

	}

}