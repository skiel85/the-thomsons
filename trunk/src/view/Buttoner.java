package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import model.filters.*;

public class Buttoner {
	
	private MainWindow mainWindow;
	private String nombreOriginal;
	private String nombre;



	public Buttoner(MainWindow mainWindow){
		this.mainWindow = mainWindow;
	}
	
	public JButton getButtonApplyFilter(JButton button, int xSize, int ySize, int xPos, int yPos,final CustomFilters filter ){
			
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
		return getButtonGeneric(button, "APPLY", xSize, ySize, xPos, yPos,l );
	}
	

	
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

	public JSlider getSlider(JSlider jSliderSlide1, int xSize, int ySize, int xPos, int yPos,	final CustomFilters customFilter, final int position) {
		if (jSliderSlide1 == null) {
			jSliderSlide1 = new JSlider(JSlider.HORIZONTAL, 0, 256, 150); //TODO rehacer

			// Turn on labels at major tick marks.

			jSliderSlide1.setMajorTickSpacing(64);
			jSliderSlide1.setMinorTickSpacing(1);
			jSliderSlide1.setPaintTicks(true);
			jSliderSlide1.setPaintLabels(true);
			// jFrameBinarizar.setBorder(
			// BorderFactory.createEmptyBorder(0,0,10,0));
			Font font = new Font("Serif", Font.PLAIN, 15);
			jSliderSlide1.setFont(font);
			//jSliderSlide1.set

			jSliderSlide1.setLocation(new Point(xPos, yPos));
			jSliderSlide1.setSize(new Dimension(xSize, ySize));

			jSliderSlide1
					.addChangeListener(new javax.swing.event.ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							JSlider source = (JSlider) e.getSource();
							customFilter.filter.setParameterAt(position,new Float(source.getValue()));
							System.out.println("stateChanged() slider:" + customFilter.filter.getParameterNames()[position] + " a valor:" + source.getValue());
						}
					});

		}
		return jSliderSlide1;
	}
}
