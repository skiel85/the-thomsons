package model.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;




public class Selector extends JPanel implements ItemListener{

	public Selector(String origin, String destiny) {
					

      //  JButton okButton = new JBut
        
        
		
		
		File myDir = new File(origin);
		if( myDir.exists() && myDir.isDirectory()){
			File[] files = myDir.listFiles();
			for(int i=0; i < files.length; i++){
				if (!files[i].isDirectory()){
					BufferedImage img = this.loadImage (files[i]);
					if (img != null) {
						int width = img.getWidth();
						int height = img.getHeight();
						double ratio = (double)width / (double)height;
						//TODO poner todas las imagenes en una ventana seleccionables
						//String newImgPath = cataloguer.getBetterFitFolder(files[i],width,height,ratio);
						//cataloguer.moveImageTo(files[i],newImgPath);
					}
				}
			}
		}
	}

		

	private String getNewName (File imgFile, String folder) {
			
		File fileFolder = new File(folder);
		String destPath = fileFolder.getAbsolutePath()+ File.separator+imgFile.getName();
		return destPath;
	}



	private void moveImageTo(File origImgFile, String newImgPath)  {

		File out = new File (newImgPath);
		
        try {
        	if (origImgFile.renameTo(out)){
	        	System.out.println("Se movio a: "+newImgPath);
        	}
        	else if (out.isFile()) {
        		out.delete();
        		origImgFile.renameTo(out);
	        	System.out.println("Ya existia y se borro: "+newImgPath);
        	}
        } 
        catch (Exception e) {
			System.out.println("Error al copiar: "+origImgFile.getName()+" en "+newImgPath);
        }
	}


	public BufferedImage loadImage(File file) {

		BufferedImage img = null;
		try {
		    img = ImageIO.read(file);
		} catch (Exception e) {
			System.out.println("No es imagen: "+file.getName());
			img = null;
		}
		return img;
	}

	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}


