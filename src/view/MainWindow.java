package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;

import model.filters.AbstractBufferedImageOp;
import model.filters.CustomFilters;
import model.filters.Parameter;
import model.image.BufferedImageChanges;
import model.image.JPanelWithFilters;
import model.image.Selector;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {


	private JPanelWithFilters jContentPane = null;
	private JLabel jLabelFoto = null;

	private JButton jButtonReset = null;
	private JButton jButtonDeshacer = null;
	private JButton jButtonAlmacenar = null;
	private JButton jButtonSimilar = null;
	private JButton jButtonBinarizar = null;
	private JButton jButtonDarken = null;
	private JButton jButtonGrays = null;
	private JButton jButtonReversa = null;
	private JSlider jSliderBinarizar = null;
	//private String nombre, nombreOriginal = "";
	private JButton jButtonMultiProcess = null;
	
	private Buttoner buttoner;

	private JLabel filterNameLabel = null;
	private JButton jButtonFiltro1 = null;
	private static final int MAX_PARAMS = AbstractBufferedImageOp.MAX_PARAMETERS;
	private JSlider filterParamSlides[] = new JSlider[MAX_PARAMS];
	private JCheckBox filterParamCheckBoxes[] = new JCheckBox[MAX_PARAMS];
	private JSpinner filterParamSpinner[] = new JSpinner[MAX_PARAMS];
	private JLabel filterParamLabels[] = new JLabel[MAX_PARAMS];
	
	
//	@SuppressWarnings("static-access")
	public static void main(String[] args) {

		// Create the frame.
		MainWindow frame = new MainWindow();

		// What happens when it closes
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Show it
		frame.setVisible(true);

		frame.setExtendedState(frame.getExtendedState());

	}

	public MainWindow() {

		super();
		initialize();

	}

	private void initialize() {

		this.setSize(1000, 750);

		this.setContentPane(getJContentPane());

		this.setTitle("Reconocedor de Rostros v0.6");

	}

	public JPanelWithFilters getJContentPane() {
		if (jContentPane == null) {

			buttoner = new Buttoner(this);
			
			jLabelFoto = new JLabel();
			jLabelFoto.setBounds(new Rectangle(41, 21, 228, 22));
			jLabelFoto.setText("Elija la foto:");
			
			

			jContentPane = new JPanelWithFilters();
			jContentPane.setLayout(null);

			jContentPane.add(getImagesCombo());
			jContentPane.add(jLabelFoto, null);
			
			jContentPane.add(getJButtonMultiProcess(),null);

			//Binarizar
			jContentPane.add(getJButtonBinarizar(), null);
			jContentPane.add(getJSliderBinarizar(), null);

			//Acciones
			jContentPane.add(getJButtonReset(), null);
			jContentPane.add(getJButtonDeshacer(), null);
			jContentPane.add(getJButtonAlmacenar(), null);
			jContentPane.add(getJButtonSimilar(), null);

			//Filtros
			jContentPane.add(getFilterCombo());
			jContentPane.add(getJButtonDarken(), null);
			jContentPane.add(getJButtonBlancoNegro(), null);

		}

		return jContentPane;

	}

	private Component getFilterCombo() {

		List<CustomFilters> filList = new ArrayList<CustomFilters>();
		CustomFilters[] allFilters = CustomFilters.values();
		for (int i=0; i< allFilters.length; i++){
			filList.add(allFilters[i]);
		}
		Object filtros[] = filList.toArray();

		
		final int initialX = 700;
		final int initialY = 200;
		final JComboBox combo = new JComboBox(filtros);
		combo.setLocation(new Point(initialX,initialY));
		combo.setSize(new Dimension(240+30, 26));
		
		filterNameLabel = Buttoner.getLabel("", 200, 50, initialX,initialY+20);
		jContentPane.add(filterNameLabel);
		
		combo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				JComboBox cm = (JComboBox) ie.getSource();	
				CustomFilters customFilter = (CustomFilters) cm.getSelectedItem();
				System.out.println("Se selecciono: " + customFilter);
				
				jContentPane.repaint();
				jContentPane.validate();
				
				if (customFilter.filter == null) return;

				sacarComponentesViejos();
		
				filterNameLabel.setText(customFilter.name + ":");
				jButtonFiltro1 = buttoner.getButtonApplyFilter(jButtonFiltro1,120,25,initialX+75,initialY+60,customFilter);
				jContentPane.add(jButtonFiltro1);
				
				Parameter[] parameters = customFilter.filter.getParameters();

				for (int i=0; i< customFilter.filter.getParameterCount(); i++){
					if (parameters[i] == null) return;
					int paramY = initialY+95;
					if (parameters[i].isType(Parameter.FLOAT)){
						filterParamSlides[i] = buttoner.getSlider(filterParamSlides[i],200,50,initialX+75,paramY+i*50,customFilter,parameters[i],i);
						jContentPane.add(filterParamSlides[i]);
					}
					if (parameters[i].isType(Parameter.BOOLEAN)){
						filterParamCheckBoxes[i] = buttoner.getCheckbox(filterParamCheckBoxes[i],200,50,initialX+75+50,paramY+i*50,customFilter,parameters[i],i);
						jContentPane.add(filterParamCheckBoxes[i]);
					}
					if (parameters[i].isType(Parameter.INTEGER)){
						filterParamSpinner[i] = buttoner.getSpinner(filterParamSpinner[i],40,40,initialX+75+50,paramY+i*50,customFilter,parameters[i],i);
						jContentPane.add(filterParamSpinner[i]);
					}
					filterParamLabels[i] = Buttoner.getLabel(parameters[i].getName() + ":",200,20,initialX+5,paramY+i*50);
					jContentPane.add(filterParamLabels[i]);
					jContentPane.repaint();
					jContentPane.validate();
				}
			}
		});
		return combo;
	}

	private void sacarComponentesViejos() {
		if (jButtonFiltro1 != null){
			jContentPane.remove(jButtonFiltro1);
		}		
		jButtonFiltro1 = null;
		
		for (int i=0; i<MAX_PARAMS;i++){
			if (filterParamSlides[i] != null){
				jContentPane.remove(filterParamSlides[i]);
			}		
			filterParamSlides[i] = null;	
			
			if (filterParamLabels[i] != null){
				jContentPane.remove(filterParamLabels[i]);
			}		
			filterParamLabels[i] = null;	
			
			if (filterParamCheckBoxes[i] != null){
				jContentPane.remove(filterParamCheckBoxes[i]);
			}		
			filterParamCheckBoxes[i] = null;
			
			if (filterParamSpinner[i] != null){
				jContentPane.remove(filterParamSpinner[i]);
			}
			filterParamSpinner[i] = null;	
		}
		
		jContentPane.validate();
	}
	
	private Component getImagesCombo() {
		File folder = new File(System.getProperty("user.dir"));
		File[] listOfFiles = folder.listFiles();
		List<String> imgList = new ArrayList<String>();

		System.out.println(System.getProperty("user.dir"));
		for (int i = 0; i < listOfFiles.length; i++) {
			String fileName = listOfFiles[i].getName();
			if (listOfFiles[i].isFile()
					&& isCorrectFormat(listOfFiles[i].getName())) {
				imgList.add(fileName);
			}
		}
		Object fotos[] = imgList.toArray();

		final JComboBox combo = new JComboBox(fotos);
		combo.setLocation(new Point(39, 54));
		combo.setSize(new Dimension(300, 26));
		combo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) combo.getSelectedItem();

				BufferedImageChanges.getInstance().empty();
				jContentPane.loadImage(str);
				buttoner.setNombreOriginal (str.substring(0,str.length()-4));
				System.out.println("Este es el nombre " + buttoner.getNombreOriginal());
				buttoner.setNombre(buttoner.getNombreOriginal());
				jContentPane.repaint();

			}
		});

		return combo;
	}

	private boolean isCorrectFormat(String fileName) {
		String extension = fileName.substring(fileName.length() - 4,fileName.length()).toLowerCase();
		return extension.equals(".jpg") || extension.equals(".png") || extension.equals(".gif");
	}


//--------------- AbstractFilters
/*	private JButton getJButtonSharpenV3() {
		return buttoner.getButtonAbstractFilter(jButtonFiltro1, "SharpenV3",120,25,700,165,new SharpenV3Filter());
	}

	private JButton getJButtonSharpenV2() {
		return buttoner.getButtonAbstractFilter(jButtonSharpenV2, "SharpenV2",120,25,700,236,new SharpenV2Filter());
	}
	
	private JButton getJButtonLowFilter() {
		return buttoner.getButtonAbstractFilter(jButtonFiltro2, "Low Filter",120,25,850,236,new LowFilter());
	}

	private JButton getJButtonSmooth() {
		return buttoner.getButtonAbstractFilter(jButtonFiltro3, "Smooth",120,25,850,165,new SmoothFilter());
	}
	
	private JButton getJButtonGaussLowV3() {
		return buttoner.getButtonAbstractFilter(jButtonGaussLowV3, "GaussLowV3",120,25,850,270, new GaussLowV3Filter());
	}
*/

//--------------- LUT Table Filters
	private JButton getJButtonBlancoNegro() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.toGrayScale();
				jContentPane.repaint();
				buttoner.setNombreOriginal(buttoner.getNombre());
				buttoner.setNombre(buttoner.getNombre() + "-BN");

				System.out.println("mouseClicked() on Blanco y Negro");
			}
		};
		return Buttoner.getButtonGeneric(jButtonGrays, "Grays",120,25,700,165,l);
	}

	private JButton getJButtonDarken() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.darkenLUT();					
				jContentPane.applyFilterWithLookUpTable();
				jContentPane.repaint();
				buttoner.setNombreOriginal(buttoner.getNombre());
				buttoner.setNombre(buttoner.getNombre() + "-DRK");

				System.out.println("mouseClicked() on Darken");
			}
		};
		return Buttoner.getButtonGeneric(jButtonDarken, "Darken",120,25,850,165,l);
	}

	private JButton getJButtonReversa() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.reverseLUT();
				jContentPane.applyFilterWithLookUpTable();
				jContentPane.repaint();
				buttoner.setNombreOriginal(buttoner.getNombre());
				buttoner.setNombre(buttoner.getNombre() + "-REV");

				System.out.println("mouseClicked() on Invertir");
			}
		};
		return Buttoner.getButtonGeneric(jButtonReversa, "Invertir",120,25,700,270,l);
	}


	private JButton getJButtonBinarizar() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.binarizeLUT(jSliderBinarizar.getValue());
				
				jContentPane.applyFilterWithLookUpTable();
				
				jContentPane.repaint();
				
				buttoner.setNombreOriginal(buttoner.getNombre());
				buttoner.setNombre(buttoner.getNombre() + "-Bin");


				System.out.println("mouseClicked() on binarizar");

				jContentPane.detectarBorde();
				
				//jContentPane.transformadaFourier();
				//jContentPane.transformadaFourier2();
				jContentPane.distanciaEuclidea();
			}
		};
		return Buttoner.getButtonGeneric(jButtonBinarizar, "Discretizar",152,25,785,57,l);
	}
	

//--------------- Otros Botones

	private JButton getJButtonSimilar() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				System.out.println("mouseClicked()");
			}
		};
		return Buttoner.getButtonGeneric(jButtonSimilar, "Similar",184,28,700,370+170,l);
	}
	
	private JButton getJButtonDeshacer() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.undo();
				jContentPane.repaint();
				buttoner.setNombre(buttoner.getNombreOriginal());
				System.out.println("mouseClicked() on deshacer");
			}
		};
	return Buttoner.getButtonGeneric(jButtonDeshacer, "Deshacer",184,28,700,410+170,l);
	}
	
	private JButton getJButtonAlmacenar() {
		MouseListener l = new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					System.out.println("mouseClicked()");
					jContentPane.guardarImagen(buttoner.getNombre());
				}
			};
		return Buttoner.getButtonGeneric(jButtonAlmacenar, "Almacenar",184,28,700,450+170,l);
	}
	

	
	private JSlider getJSliderBinarizar() {
		if (jSliderBinarizar == null) {
			jSliderBinarizar = new JSlider(JSlider.HORIZONTAL, 0, 256, 150);

			// Turn on labels at major tick marks.

			jSliderBinarizar.setMajorTickSpacing(64);
			jSliderBinarizar.setMinorTickSpacing(1);
			jSliderBinarizar.setPaintTicks(true);
			jSliderBinarizar.setPaintLabels(true);
			// jFrameBinarizar.setBorder(
			// BorderFactory.createEmptyBorder(0,0,10,0));
			Font font = new Font("Serif", Font.PLAIN, 15);
			jSliderBinarizar.setFont(font);

			jSliderBinarizar.setLocation(new Point(765, 87));
			jSliderBinarizar.setSize(new Dimension(200, 50));

			jSliderBinarizar
					.addChangeListener(new javax.swing.event.ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							JSlider source = (JSlider) e.getSource();
							/*
							if (!source.getValueIsAdjusting()) {

								int valor = (int) source.getValue();
								jContentPane.binarizeLUT(valor);
								jContentPane.applyFilterWithLookUpTable();
								jContentPane.repaint();
								System.out.println("stateChanged() slider");
								
							} */
						}
					});

		}
		return jSliderBinarizar;
	}

	private JButton getJButtonMultiProcess() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				
				//TODO aca viene lo de Ezequiel, yo tomo las imagenes de la carpeta POSSIBLES y las dejo en READY
				
		        JFrame frame = new JFrame("Selector");
		        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		        //Create and set up the content pane.
		        JComponent newContentPane = new Selector("POSSIBLES","READY");
		        newContentPane.setOpaque(true); //content panes must be opaque
		        frame.setContentPane(newContentPane);

		        //Display the window.
		        frame.pack();
		        frame.setVisible(true);
		        
			}
		};
		return Buttoner.getButtonGeneric(jButtonMultiProcess, "Process Multiple",152,25,380+90,57,l);
	}

	private JButton getJButtonReset() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				BufferedImageChanges.getInstance().empty();
				jContentPane.reset();
				jContentPane.repaint();
			}
		};
		return Buttoner.getButtonGeneric(jButtonReset, "Reset",80,25,380,57,l);
	}
}
