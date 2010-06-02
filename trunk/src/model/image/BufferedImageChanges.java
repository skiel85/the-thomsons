package model.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BufferedImageChanges {

	private static boolean binarized;
	private static List<BufferedImage> changes;
    private static BufferedImageChanges instance = null;
    private static final Integer MAX_LIST_SIZE = 5;
    
    private BufferedImageChanges() {}
 
    private static void createInstance() {
        if (instance == null) { 
            instance = new BufferedImageChanges();
            changes = new ArrayList<BufferedImage>();
        }
    }
 
    public static BufferedImageChanges getInstance() {
        if (instance == null) createInstance();
        return instance;
    }
    
    public void changeImage(BufferedImage bi) {
    	if(changes.size()==MAX_LIST_SIZE) {
    		changes.remove(0);
    	}
    	changes.add(bi);
    }
    
    public BufferedImage undo() {
    	if(changes.size()>1) {
    		BufferedImage bi = changes.get(changes.size()-1);
	    	changes.remove(changes.size()-1);
	    	return bi;
    	}
    	return changes.get(0);
    }
    
    public void empty() {
    	changes.clear();
    }

	public void binarize(boolean b) {
		binarized = b;
	}
	
	public boolean lastImageBinarized() {
		return binarized;
	}

	public BufferedImage getCurrentImage() {
		return changes.get(changes.size()-1);
	}
}
