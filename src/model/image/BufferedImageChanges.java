package model.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BufferedImageChanges {

	private static List<BufferedImage> changes;
    private static BufferedImageChanges instance = null;
    
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
}
