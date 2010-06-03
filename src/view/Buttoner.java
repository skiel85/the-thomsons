package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

import model.filters.*;

public class Buttoner {
	
	private MainWindow mainWindow;
	private String nombreOriginal;
	private String nombre;



	public Buttoner(MainWindow mainWindow){
		this.mainWindow = mainWindow;
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
	

	public JSlider getSlider(JSlider jSliderSlide1, int xSize, int ySize, int xPos, int yPos,	final CustomFilters customFilter, final Parameter parameter, final int index) {
		if (jSliderSlide1 == null) {
			
			int min = ((Integer) parameter.getOthers()[0].getData()).intValue();
			int max = ((Integer) parameter.getOthers()[1].getData()).intValue();
			int data = ((Float) parameter.getData()).intValue();
			final int escalador = 1000;
			jSliderSlide1 = new JSlider(JSlider.HORIZONTAL, min*escalador, max*escalador,data*escalador );

			
			jSliderSlide1.setMajorTickSpacing((int) ((max-min)*0.2*escalador));
			//jSliderSlide1.setMinorTickSpacing(1);
			jSliderSlide1.setPaintTicks(true);

			Font font = new Font("Serif", Font.PLAIN, 15);
			jSliderSlide1.setFont(font);

			//Create the label table.
	        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
	        labelTable.put(new Integer( min*escalador ),
	        				new JLabel(String.valueOf(min)) );
	        labelTable.put(new Integer( (max-min)*escalador/2 ),
	        				new JLabel(String.valueOf((max-min)/(float)2)) );
	        labelTable.put(new Integer( max*escalador),
	                       new JLabel(String.valueOf(max)) );
	        jSliderSlide1.setLabelTable(labelTable);

	        jSliderSlide1.setPaintLabels(true);
	        jSliderSlide1.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));



			jSliderSlide1.setLocation(new Point(xPos, yPos));
			jSliderSlide1.setSize(new Dimension(xSize, ySize));

			jSliderSlide1
					.addChangeListener(new javax.swing.event.ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							JSlider source = (JSlider) e.getSource();
							float valor = ((float) source.getValue())/((float) escalador);
							customFilter.filter.setParameterAt(index,new Float(valor));
							System.out.println("stateChanged() slider:" + customFilter.filter.getParameters()[index].getName() + " a valor:" + valor);
						}
					});

		}
		return jSliderSlide1;
	}

	public static JLabel getLabel(String text,  int xSize, int ySize, int xPos, int yPos) {
		JLabel filterNameLabel = new JLabel();
		filterNameLabel.setLocation(xPos, yPos);
		filterNameLabel.setSize(xSize, ySize);
		filterNameLabel.setText(text);
		return filterNameLabel;
	}

	public JCheckBox getCheckbox(JCheckBox jCheckBox, int xSize, int ySize, int xPos, int yPos, final CustomFilters customFilter, final Parameter parameter, final int index) {
		
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox(parameter.getName(),((Boolean)parameter.getData()).booleanValue());
			jCheckBox.setLocation(new Point(xPos, yPos));
			jCheckBox.setSize(new Dimension(xSize, ySize));
			jCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							JCheckBox source = (JCheckBox) e.getSource();
							customFilter.filter.setParameterAt(index,new Boolean(source.isSelected()));
							System.out.println("stateChanged() checkbox:" + customFilter.filter.getParameters()[index].getName() + " a valor:" + source.isSelected());
						}
					});
		}
		return jCheckBox;
	}

	public JSpinner  getSpinner(JSpinner spinner , int xSize, int ySize, int xPos, int yPos, final CustomFilters customFilter, final Parameter parameter, final int index) {
		
		if (spinner == null){
			int min = ((Integer) parameter.getOthers()[0].getData()).intValue();
			int max = ((Integer) parameter.getOthers()[1].getData()).intValue();
			int data = ((Integer) parameter.getData()).intValue();
			SpinnerModel model = new SpinnerNumberModel(data,min,max,1);
			spinner = new JSpinner(model);
			spinner.setLocation(new Point(xPos, yPos));
			spinner.setSize(new Dimension(xSize, ySize));
			spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));

			spinner.addChangeListener(new javax.swing.event.ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							JSpinner source = (JSpinner) e.getSource();
							String textData = ((JSpinner.DefaultEditor)source.getEditor()).getTextField().getText();
							customFilter.filter.setParameterAt(index,Integer.parseInt(textData));
							System.out.println("stateChanged() spinner:" + customFilter.filter.getParameters()[index].getName() + " a valor:" + textData);
						}
					});
		}
		return spinner;
	}
}
