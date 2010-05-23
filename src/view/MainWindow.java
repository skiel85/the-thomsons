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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import com.jhlabs.image.BlurFilter;

import model.filters.AbstractFilter;
import model.filters.GaussLowV3Filter;
import model.filters.LowFilter;
import model.filters.SharpenV2Filter;
import model.filters.SharpenV3Filter;
import model.filters.SmoothFilter;
import model.image.BufferedImageChanges;
import model.image.JPanelWithFilters;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	private JPanelWithFilters jContentPane = null;
	private JLabel jLabelFoto = null;

	private JButton jButtonReset = null;
	private JButton jButtonFiltro1 = null;
	private JButton jButtonFiltro2 = null;
	private JButton jButtonFiltro3 = null;
	private JButton jButtonDeshacer = null;
	private JButton jButtonAlmacenar = null;
	private JButton jButtonSimilar = null;
	private JButton jButtonBinarizar = null;
	private JButton jButtonDarken = null;
	private JButton jButtonDeMedia = null;
	private JButton jButtonGaussLowV3 = null;
	private JButton jButtonGrays = null;
	private JButton jButtonSharpenV2 = null;
	private JSlider jSliderBinarizar = null;
	private String nombre, nombreOriginal = "";

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

		this.setTitle("Reconocedor de Rostros v0.3");

	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {

			jLabelFoto = new JLabel();
			jLabelFoto.setBounds(new Rectangle(41, 21, 228, 22));
			jLabelFoto.setText("Elija la foto:");
			
			

			jContentPane = new JPanelWithFilters();
			jContentPane.setLayout(null);

			jContentPane.add(getImagesCombo());
			jContentPane.add(jLabelFoto, null);

			//Binarizar
			jContentPane.add(getJButtonBinarizar(), null);
			jContentPane.add(getJSliderBinarizar(), null);

			//Acciones
			jContentPane.add(getJButtonReset(), null);
			jContentPane.add(getJButtonDeshacer(), null);
			jContentPane.add(getJButtonAlmacenar(), null);
			jContentPane.add(getJButtonSimilar(), null);

			//Filtros
			jContentPane.add(getJButtonSharpenV3(), null);
			jContentPane.add(getJButtonLowFilter(), null);
			jContentPane.add(getJButtonSmooth(), null);
			jContentPane.add(getJButtonDarken(), null);
			jContentPane.add(getJButtonDeMedia(), null);
			jContentPane.add(getJButtonGaussLowV3(), null);
			jContentPane.add(getJButtonBlancoNegro(), null);
			jContentPane.add(getJButtonSharpenV2(), null);

		}

		return jContentPane;

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
				nombreOriginal = str.substring(0,str.length()-4);
				System.out.println("Este es el nombre " + nombreOriginal);
				nombre = nombreOriginal;
				jContentPane.repaint();

			}
		});

		return combo;
	}

	private boolean isCorrectFormat(String fileName) {
		String extension = fileName.substring(fileName.length() - 4,fileName.length()).toLowerCase();
		return extension.equals(".jpg") || extension.equals(".png") || extension.equals(".gif");
	}

	private JButton getJButtonReset() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				BufferedImageChanges.getInstance().empty();
				jContentPane.reset();
				jContentPane.repaint();
			}
		};
		return getButtonGeneric(jButtonReset, "Reset",80,25,380,57,l);
	}

	private JButton getJButtonSharpenV3() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.setFilter(new SharpenV3Filter());
				jContentPane.applyFilter();
				//jContentPane.applyNewFilters(new BlurFilter());
				jContentPane.repaint();
				nombreOriginal = nombre;
				nombre = nombre + "-SV3";

				System.out.println("mouseClicked() on Sharpen");
			}
		};
		return getButtonGeneric(jButtonFiltro1, "SharpenV3",120,25,700,165,l);
	}
	

	private JButton getJButtonSharpenV2() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.setFilter(new SharpenV2Filter());
				jContentPane.applyFilter();
				jContentPane.repaint();
				nombreOriginal = nombre;
				nombre = nombre + "-SV2";

				System.out.println("mouseClicked() on Sharpen");
			}
		};
		return getButtonGeneric(jButtonSharpenV2, "SharpenV2",120,25,700,201,l);
	}

	private JButton getJButtonBlancoNegro() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.toGrayScale();
				jContentPane.repaint();
				nombreOriginal = nombre;
				nombre = nombre + "-BN";

				System.out.println("mouseClicked() on Blanco y Negro");
			}
		};
		return getButtonGeneric(jButtonGrays, "Grays",120,25,850,165,l);
	}

	private JButton getJButtonLowFilter() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.setFilter(new LowFilter());
				jContentPane.applyFilter();
				jContentPane.repaint();
				nombreOriginal = nombre;
				nombre = nombre + "-LF";

				System.out.println("mouseClicked() on Low Filter");
			}
		};
		return getButtonGeneric(jButtonFiltro2, "Low Filter",120,25,850,201,l);
	}

	private JButton getJButtonSmooth() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.setFilter(new SmoothFilter());
				jContentPane.applyFilter();
				jContentPane.repaint();
				nombreOriginal = nombre;
				nombre = nombre + "-Sm";

				System.out.println("mouseClicked() on Smooth");
			}
		};
		return getButtonGeneric(jButtonFiltro3, "Smooth",120,25,700,236,l);
	}

	private JButton getJButtonDarken() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.darkenLUT();					
				jContentPane.applyFilterWithLookUpTable();
				jContentPane.repaint();
				nombreOriginal = nombre;
				nombre = nombre + "-DLUT";

				System.out.println("mouseClicked() on Darken");
			}
		};
		return getButtonGeneric(jButtonDarken, "Darken",120,25,850,236,l);
	}

	private JButton getJButtonDeMedia() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.reverseLUT();
				jContentPane.applyFilterWithLookUpTable();
				jContentPane.repaint();
				nombreOriginal = nombre;
				nombre = nombre + "-RLUT";

				System.out.println("mouseClicked() on Invertir");
			}
		};
		return getButtonGeneric(jButtonDeMedia, "Invertir",120,25,700,270,l);
	}

	private JButton getJButtonGaussLowV3() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.setFilter(new GaussLowV3Filter());
				jContentPane.applyFilter();
				jContentPane.repaint();
				nombreOriginal = nombre;
				nombre = nombre + "-GLV3";

				System.out.println("mouseClicked() on GaussLowV3");
			}
		};
		return getButtonGeneric(jButtonGaussLowV3, "GaussLowV3",120,25,850,270,l);
	}


	private JButton getJButtonBinarizar() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.binarizeLUT(jSliderBinarizar.getValue());
				
				jContentPane.applyFilterWithLookUpTable();
				
				jContentPane.repaint();
				
				nombreOriginal = nombre;
				
				nombre = nombre + "-Bin";

				System.out.println("mouseClicked() on binarizar");

				jContentPane.detectarBorde();
			}
		};
		return getButtonGeneric(jButtonBinarizar, "Discretizar",152,25,785,57,l);
	}


	private JButton getJButtonSimilar() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				System.out.println("mouseClicked()");
			}
		};
		return getButtonGeneric(jButtonSimilar, "Similar",184,28,700,370+170,l);
	}
	
	private JButton getJButtonDeshacer() {
		MouseListener l = new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				jContentPane.undo();
				jContentPane.repaint();
				nombre = nombreOriginal;
				System.out.println("mouseClicked() on deshacer");
			}
		};
	return getButtonGeneric(jButtonDeshacer, "Deshacer",184,28,700,410+170,l);
	}
	
	private JButton getJButtonAlmacenar() {
		MouseListener l = new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					System.out.println("mouseClicked()");
					jContentPane.guardarImagen(nombre);
				}
			};
		return getButtonGeneric(jButtonAlmacenar, "Almacenar",184,28,700,450+170,l);
	}
	
	private JButton getButtonGeneric(JButton button, String name, int xSize, int ySize, int xPos, int yPos,MouseListener l ){
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

}
