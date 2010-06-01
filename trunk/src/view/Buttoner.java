package view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import model.filters.*;

public class Buttoner {
	
	private MainWindow mainWindow;
	private String nombreOriginal;
	private String nombre;



	public Buttoner(MainWindow mainWindow){
		this.mainWindow = mainWindow;
	}
	
	public JButton getButtonAbstractFilter(JButton button, int xSize, int ySize, int xPos, int yPos,final CustomFilters filter ){
			
		MouseListener l = new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					try {
						if (filter.filter != null){
							mainWindow.getJContentPane().setFilter(filter.filter);
							mainWindow.getJContentPane().applyFilter(filter);
							mainWindow.getJContentPane().repaint();
							nombreOriginal = nombre;
							nombre = nombre + "-" + filter.shortName;
						}
						System.out.println("mouseClicked() on " + filter.name);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}
			};
		if (filter.filter == null){
			return button;
		}
		return getButtonGeneric(button, filter.name, xSize, ySize, xPos, yPos,l );
	}
	
//	public JButton getButtonAbstractFilter(JButton button, int xSize, int ySize, int xPos, int yPos,final CustomFilters filter ){
		
//	}
	
	public static JButton getButtonGeneric(JButton button, String name, int xSize, int ySize, int xPos, int yPos,MouseListener l ){
		if (button == null) {
			button = new JButton();
			button.setText(name);
			button.setSize(new Dimension(xSize, ySize));
			button.setLocation(new Point(xPos, yPos));
			if (l != null) {
				button.addMouseListener(l);
			}
		}
		return button;
	}
	


	public String getNombreOriginal() {
		return nombreOriginal;
	}

	public void setNombreOriginal(String nombreOriginal) {
		this.nombreOriginal = nombreOriginal;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
