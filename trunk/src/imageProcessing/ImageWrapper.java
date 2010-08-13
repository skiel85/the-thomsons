package imageProcessing;

import imageProcessing.Face;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import vectorization.Point;
import vectorization.VectorBuilder;
import java.awt.Image;

public class ImageWrapper {

    private String path;
    private VectorBuilder vectorBuilder;
    private List<List<Point>> finalVector;
    private BufferedImage bufferedImage;
    private Face face;
    private int imageWidth;
	private int imageHeight;
    private int bufimageWidth;
    private int bufimageHeight;
    

    public ImageWrapper(){
        finalVector = new LinkedList<List<Point>>();
        this.vectorBuilder = null;
    }
    
    public void createBufferImage(int width, int height){
		int imageWidth = width;
		int imageHeight = height;
		bufimageWidth = 640;
		if (bufimageWidth > imageWidth)
			bufimageWidth = imageWidth;
		bufimageHeight = 480;
		if (bufimageHeight > imageHeight)
			bufimageHeight = imageHeight;
		
		bufferedImage = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage getBufferedImage(){
        return this.bufferedImage;
    }

    public void setBufferedImage(BufferedImage newImage){
        this.bufferedImage = newImage;
    }

    public List<List<Point>> getFinalVector() {
        return finalVector;
    }

    public void addFourierVector(List<Point> finalVector) {
        this.finalVector.add(finalVector);
    }

    public VectorBuilder getVectorBuilder() {
        return this.vectorBuilder;
    }

    public void setVectorBuilder(VectorBuilder vectorBuilder) {
        this.vectorBuilder = vectorBuilder;
    }

    /**
     * @return the face
     */
    public Face getFace() {
        return face;
    }

    /**
     * @param face the face to set
     */
    public void setFace(Face face) {
        this.face = face;
    }

    public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public int getBufimageWidth() {
		return bufimageWidth;
	}

	public void setBufimageWidth(int bufimageWidth) {
		this.bufimageWidth = bufimageWidth;
	}

	public int getBufimageHeight() {
		return bufimageHeight;
	}

	public void setBufimageHeight(int bufimageHeight) {
		this.bufimageHeight = bufimageHeight;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setFinalVector(List<List<Point>> finalVector) {
		this.finalVector = finalVector;
	}
	
    public String getPath() {
        return path;
    }

}
