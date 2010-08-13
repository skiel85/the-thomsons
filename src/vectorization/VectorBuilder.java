
package vectorization;

import imageProcessing.Face;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class VectorBuilder {

    private List<Point> vectorUp = new ArrayList<Point>();
    private List<Point> vectorCenter = new ArrayList<Point>();
    private List<Point> vectorDown = new ArrayList<Point>();
    private List<Point> signatureImage = new ArrayList<Point>();
    private BufferedImage image = null;

    public List<Point> getVectorCenter()
    {
        return vectorCenter;
    }

    public void setImage(BufferedImage bufferedImage) {
        this.image = bufferedImage;
    }

    public BufferedImage getImage(){
        return this.image;
    }
    
    public List<Point> getVectorUp()
    {
        return vectorUp;
    }

    public List<Point> getVectorDown()
    {
        return vectorDown;
    }
    
    public VectorBuilder(BufferedImage image, Point[] points){
    	this.image = image;
    	for (int i=0; i<points.length; i++){
    		vectorCenter.add(points[i]);
    	}
    }

    public VectorBuilder(BufferedImage image, Face face){
        this.image = image;

        double step = face.getRegion().getHeight() * 0.25;

        int centerX = face.getRegion().getX();
        
        int topY = face.getRegion().getY() - (face.getRegion().getHeight() / 2);
        //int centerX = (int) (image.getWidth() * 0.5);
        //int centerY = (int) (image.getHeight() * rango );
        topY += step;
        getPoints(image,(int)centerX,(int)topY,this.vectorDown);
        topY += step;
        
        getPoints(image,(int)centerX,(int)topY,this.vectorCenter);
        topY += step;        
        getPoints(image,(int)centerX,(int)topY,this.vectorUp);

    }

    

    private void getPoints(BufferedImage image,int centerX,int centerY, List<Point> vector)
    {
        int angle = 0;
        int radio = 1;
        boolean notChange = true;
        Point point = null;
        boolean first = true;
        int prevColor = 0;

        for(angle=0; angle<=360; angle++)
        {
            prevColor= 0;
            radio = 1;
            notChange=true;
            first = true;
            while(notChange){
               //System.out.println("Radio:"+radio+";Angulo:"+angle);
               point = Point.getPoint(radio, angle);
               point.setX(point.getX() + centerX);
               point.setY( point.getY() + centerY);

               if((point.getX() >= image.getWidth()) || (point.getY() >= image.getHeight()) || (point.getX()<0) || (point.getY()<0))
               {

                   notChange = false;
               }
               else{
                   
                   int actualColor = (image.getRGB((int)point.getX(), (int)point.getY()));
                   //System.out.println("X" + point.getX() + ";Y" + point.getY());
                   if((prevColor != actualColor) && (!first) ) //is black and different from the prevColor
                    {
                        notChange = false;
                        vector.add(point);
                    }
                    if (first)
                        first = false;
                     prevColor = actualColor;
                     radio++;
               }
            }
        }

    }
    
    public void DrawVectorOnImage(){
        DrawVector(this.vectorUp,0);
        DrawVector(this.vectorCenter,1);
        DrawVector(this.vectorDown,2);
        
    }
    
    private void DrawVector(List<Point> vectorDraw,int colorElect){

        Graphics g2 = image.getGraphics();
        if (colorElect == 0) g2.setColor(Color.RED);
        if (colorElect == 1) g2.setColor(Color.GREEN);
        if (colorElect == 2) g2.setColor(Color.BLUE);

        Point startPoint = vectorDraw.get(0);
        Point endPoint =  null;

        for( int pointIndex = 1; pointIndex < vectorDraw.size() - 1 ; pointIndex++ ){
            endPoint = vectorDraw.get(pointIndex);
            g2.drawLine((int)startPoint.getX(), (int)startPoint.getY(),
                        (int)endPoint.getX(),(int) endPoint.getY());
            startPoint = endPoint;
        }
            g2.drawLine((int)startPoint.getX(),(int) startPoint.getY(),
                        (int)vectorDraw.get(0).getX(),(int) vectorDraw.get(0).getY());
            startPoint = endPoint;
        
        //g2.dispose();

    }



}