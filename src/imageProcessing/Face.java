package imageProcessing;

import de.offis.faint.controller.MainController;
import de.offis.faint.model.ImageModel;
import de.offis.faint.model.Region;
import java.awt.BasicStroke;
import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;


public class Face {

    private static int CORRECTION = 25;



    private Region region;
    private double scaleFactor;
    private int correction;

    public Face( Region region){
        this.region = region;
        this.scaleFactor = 1;
    }

    public Face( Region region, double scaleFactor ){
        this.scaleFactor = scaleFactor;
        this.region = region;
        this.correction = 0;
        Scale();
        


    }

    public static Face DetectFace(String imagePath, double scaleFactor) throws Exception {

            MainController controller = MainController.getInstance();
            controller.setScanWindowSize(1);
            ImageModel imageModel = new ImageModel(imagePath);
            
            Region[] regions = controller.detectFaces(imageModel, false);
            if (regions != null) {
                //select bigger region (maybe an eye is detected as a region)
                Region selectedRegion = selectRegion(regions);
                
                return new Face(selectedRegion, scaleFactor);
            }
            else
                throw new Exception("Error - La imagen: " + imagePath + " no tiene caras.");
    }

    private void Scale() {
        region.setX((int) (region.getX() * scaleFactor));
        region.setY((int) (region.getY() * scaleFactor));
        region.setWidth((int) (region.getWidth() * scaleFactor + correction));
        region.setHeight((int) (region.getHeight() * scaleFactor + correction));
    }

    public Region getUnscaledRegion(){
        Region unscaledRegion = new Region(0,0,0,0,0,"");
        unscaledRegion.setX((int) (region.getX() / scaleFactor));
        unscaledRegion.setY((int) (region.getY() / scaleFactor));
        unscaledRegion.setWidth((int) ( (region.getWidth() - correction) / scaleFactor ));
        unscaledRegion.setHeight((int) ( (region.getHeight() - correction) / scaleFactor ));
        return unscaledRegion;
    }

    private static Region selectRegion(Region[] regions) {
        Region selectedRegion = regions[0];
        for( Region region : regions ){
            if( region.getWidth() > selectedRegion.getWidth() &&
                    region.getHeight() > selectedRegion.getHeight() )
                selectedRegion = region;
        }
        return selectedRegion;
    }
    

    public void drawOnImage(BufferedImage image){
        Graphics2D g = (Graphics2D) image.getGraphics();

        Stroke stroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,  new float[] { 3, 1 }, 0);
        
        g.setStroke(stroke);
        g.setColor(Color.ORANGE);

        int xDeviation = getRegion().getWidth() / 2;
        int yDeviation = getRegion().getHeight() / 2;

        g.drawRect(getRegion().getX() - xDeviation, getRegion().getY() - yDeviation, getRegion().getWidth(), getRegion().getHeight());
        
    }

    /**
     * @return the region
     */
    public Region getRegion() {
        return region;
    }



   
}