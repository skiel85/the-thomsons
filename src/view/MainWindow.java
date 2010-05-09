package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

import model.image.JPanelWithFilters;

@SuppressWarnings("serial")
public class MainWindow extends JFrame{

	private JPanelWithFilters jContentPane = null;
	private JTextField jTextFieldFoto = null;
	private JLabel jLabelFoto = null;
	private JButton jButtonAbrir = null;
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
	private String nombre,nombreOriginal="";
	


	public static void main(	String[] args	){
		
		//Create the frame.
		MainWindow frame = new MainWindow();
		
		//What happens when it closes
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		//Show it
		frame.setVisible(true);
		
		frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);
	
	}

	public MainWindow(){
		
		super();
		initialize();
	
	}

	
	private void initialize(){
		
		this.setSize( 1000, 750 );
		
		this.setContentPane(getJContentPane());
		
		this.setTitle("Reconocedor de Rostros v0.2");
	
	}

	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			
			jLabelFoto = new JLabel();
			jLabelFoto.setBounds(new Rectangle(41, 21, 228, 22));
			jLabelFoto.setText("Ingrese la foto (CON EXTENSION)");
			jContentPane = new JPanelWithFilters();
			jContentPane.setLayout(null);

//			jContentPane.add(getImagesCombo());
			jContentPane.add(getJTextFieldFoto(), null);
			jContentPane.add(jLabelFoto, null);
			jContentPane.add(getJSliderBinarizar(), null);
			jContentPane.add(getJButtonAbrir(), null);
			jContentPane.add(getJButtonReset(), null);
			jContentPane.add(getJButtonFiltro1(), null);
			jContentPane.add(getJButtonFiltro2(), null);
			jContentPane.add(getJButtonFiltro3(), null);
			jContentPane.add(getJButtonDeshacer(), null);
			jContentPane.add(getJButtonAlmacenar(), null);
			jContentPane.add(getJButtonSimilar(), null);
			jContentPane.add(getJButtonBinarizar(), null);
			jContentPane.add(getJButtonDarken(), null);
			jContentPane.add(getJButtonDeMedia(), null);
			jContentPane.add(getJButtonGaussLowV3(), null);
			jContentPane.add(getJButtonBlancoNegro(), null);
			jContentPane.add(getJButtonSharpenV2(), null);
			
			// Initialize with a img
			jContentPane.loadImage( "7_117_P.jpg" ); 
			nombreOriginal="7_117_P.jpg";
			nombre=nombreOriginal;
			
		}
		
		return jContentPane;

	}


	
	
	private Component getImagesCombo() {
		File folder = new File("C:\\");
	    File[] listOfFiles = folder.listFiles();
	    String fotos[] = new String[200];
	    
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        System.out.println("File " + listOfFiles[i].getName());
	      } else if (listOfFiles[i].isDirectory()) {
	        fotos[i] = listOfFiles[i].getName();
	      }
	    }
		//String fotos[] = {"Nico","Gaby"};
		final JComboBox combo = new JComboBox(fotos);
		combo.setLocation(new Point(39, 54));
		combo.setSize(new Dimension(120, 26));
//		combo.addItemListener(new ItemListener() {
//			public void itemStateChanged(ItemEvent ie) {
//				String str = (String) combo.getSelectedItem();
//				txt.setText(str);
//			}
//		});

		return combo;
	}

	private JTextField getJTextFieldFoto() {
		if (jTextFieldFoto == null) {
			jTextFieldFoto = new JTextField();
			jTextFieldFoto.setBounds(new Rectangle(39, 54, 230, 28));
		}
		return jTextFieldFoto;
	}

	
	private JButton getJButtonAbrir() {
		if (jButtonAbrir == null) {
			jButtonAbrir = new JButton();
			jButtonAbrir.setBounds(new Rectangle(284, 57, 80, 25));
			jButtonAbrir.setText("Abrir");
			jButtonAbrir.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
				
					String foto=jTextFieldFoto.getText();
					nombreOriginal=jTextFieldFoto.getText();
					nombre=nombreOriginal;
					
					jContentPane.loadImage( foto  ); //TODO no funca esto todavia
					
					jContentPane.repaint();
				
				}
			});
		}
		return jButtonAbrir;
	}
	
	private JButton getJButtonReset() {
		if (jButtonReset == null) {
			jButtonReset = new JButton();
			jButtonReset.setBounds(new Rectangle(380, 57, 80, 25));
			jButtonReset.setText("Reset");
			jButtonReset.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {

					jContentPane.reset();
					jContentPane.repaint();
				}
			});
		}
		return jButtonReset;
	}

	
	private JButton getJButtonFiltro1() {
		if (jButtonFiltro1 == null) {
			jButtonFiltro1 = new JButton();
			jButtonFiltro1.setText("SharpenV3");
			jButtonFiltro1.setLocation(new Point(700, 165));
			jButtonFiltro1.setSize(new Dimension(120, 26));
			jButtonFiltro1.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
					jContentPane.sharpenV3();
					jContentPane.applyFilter();
					jContentPane.repaint();
					nombreOriginal=nombre;
					nombre=nombre+"-sharpenV3";
					
					System.out.println("mouseClicked() on Sharpen"); 
				
				}
			});
		}
		return jButtonFiltro1;
	}
	
	
	
	
	private JButton getJButtonSharpenV2() {
		if (jButtonSharpenV2 == null) {
			jButtonSharpenV2 = new JButton();
			jButtonSharpenV2.setText("SharpenV2");
			jButtonSharpenV2.setLocation(new Point(700, 201));
			jButtonSharpenV2.setSize(new Dimension(120, 26));
			jButtonSharpenV2.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
					jContentPane.sharpenV2();
					jContentPane.applyFilter();
					jContentPane.repaint();
					nombreOriginal=nombre;
					nombre=nombre+"-sharpenV2";
					
					System.out.println("mouseClicked() on Sharpen"); 
				
				}
			});
		}
		return jButtonSharpenV2;
	}
	
	private JButton getJButtonBlancoNegro() {
		if (jButtonGrays == null) {
			jButtonGrays = new JButton();
			jButtonGrays.setText("Grays");
			jButtonGrays.setLocation(new Point(850, 165));
			jButtonGrays.setSize(new Dimension(120, 26));
			jButtonGrays.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
					jContentPane.toGrayScale();
					
					jContentPane.repaint();
					nombreOriginal=nombre;
					nombre=nombre+"-BlancoNegro";
					
					System.out.println("mouseClicked() on Blanco y Negro"); 
				
				}
			});
		}
		return jButtonGrays;
	}

	
	private JButton getJButtonFiltro2() {
		if (jButtonFiltro2 == null) {
			jButtonFiltro2 = new JButton();
			jButtonFiltro2.setText("Low Filter");
			jButtonFiltro2.setSize(new Dimension(120, 26));
			jButtonFiltro2.setLocation(new Point(850, 201));
			jButtonFiltro2.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
					jContentPane.lowFilter();
					jContentPane.applyFilter();
					jContentPane.repaint();
					nombreOriginal=nombre;
					nombre=nombre+"-lowFilter";
					
					System.out.println("mouseClicked() on Low Filter"); 
				}
			});
		}
		return jButtonFiltro2;
	}

	private JButton getJButtonFiltro3() {
		if (jButtonFiltro3 == null) {
			jButtonFiltro3 = new JButton();
			jButtonFiltro3.setText("Smooth");
			jButtonFiltro3.setSize(new Dimension(120, 26));
			jButtonFiltro3.setLocation(new Point(700, 236));
			jButtonFiltro3.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
					jContentPane.smooth();
					jContentPane.applyFilter();
					jContentPane.repaint();
					nombreOriginal=nombre;
					nombre=nombre+"-smooth";
					
					System.out.println("mouseClicked() on Smooth"); 
				}
			});
		}
		return jButtonFiltro3;
	}

	private JButton getJButtonDarken() {
		if (jButtonDarken == null) {
			jButtonDarken = new JButton();
			jButtonDarken.setText("Darken");
			jButtonDarken.setSize(new Dimension(120, 26));
			jButtonDarken.setLocation(new Point(850, 236));
			jButtonDarken.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
										
					jContentPane.darkenLUT();
					jContentPane.applyFilterWithLookUpTable();
					jContentPane.repaint();
					nombreOriginal=nombre;
					nombre=nombre+"-darkenLUT";
					
					System.out.println("mouseClicked() on Darken"); 
				}
			});
		}
		return jButtonDarken;
	}
	
	
	private JButton getJButtonDeMedia() {
		if (jButtonDeMedia == null) {
			jButtonDeMedia = new JButton();
			jButtonDeMedia.setText("Invertir");
			jButtonDeMedia.setSize(new Dimension(120, 26));
			jButtonDeMedia.setLocation(new Point(700, 270));
			jButtonDeMedia.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
										
					jContentPane.reverseLUT();
					jContentPane.applyFilterWithLookUpTable();
					jContentPane.repaint();
					nombreOriginal=nombre;
					nombre=nombre+"-reverseLUT";
					
					System.out.println("mouseClicked() on Invertir"); 
				}
			});
		}
		return jButtonDeMedia;
	}
	
	private JButton getJButtonGaussLowV3() {
		if (jButtonGaussLowV3 == null) {
			jButtonGaussLowV3 = new JButton();
			jButtonGaussLowV3.setText("GaussLowV3");
			jButtonGaussLowV3.setSize(new Dimension(120, 26));
			jButtonGaussLowV3.setLocation(new Point(850, 270));
			jButtonGaussLowV3.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
										
					jContentPane.gaussLowFilterV3();
					jContentPane.applyFilter();
					jContentPane.repaint();
					nombreOriginal=nombre;
					nombre=nombre+"-gaussLowFilterV3";
					
					System.out.println("mouseClicked() on GaussLowV3"); 
				}
			});
		}
		return jButtonGaussLowV3;
	}

	
	private JButton getJButtonDeshacer() {
		if (jButtonDeshacer == null) {
			jButtonDeshacer = new JButton();
			jButtonDeshacer.setText("Deshacer");
			jButtonDeshacer.setSize(new Dimension(184, 28));
			jButtonDeshacer.setLocation(new Point(700, 410));
			jButtonDeshacer.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
					jContentPane.undo();
					jContentPane.repaint();
					nombre=nombreOriginal;
					
					System.out.println("mouseClicked() on deshacer"); 
				
				}
			});
		}
		return jButtonDeshacer;
	}
	
	private JButton getJButtonBinarizar() {
		if (jButtonBinarizar == null) {
			jButtonBinarizar = new JButton();
			jButtonBinarizar.setText("Binarizar");
			jButtonBinarizar.setSize(new Dimension(152, 25));
			jButtonBinarizar.setLocation(new Point(785, 57));
			jButtonBinarizar.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
					jContentPane.binarizeLUT(150);
					jContentPane.applyFilterWithLookUpTable();
					jContentPane.repaint();
					nombreOriginal=nombre;
					nombre=nombre+"-Binarizar";
					
					System.out.println("mouseClicked() on binarizar"); 
					
					System.out.println( jContentPane.getNose() );
				
				}
			});
		}
		return jButtonBinarizar;
	}


	
	private JButton getJButtonAlmacenar() {
		if (jButtonAlmacenar == null) {
			jButtonAlmacenar = new JButton();
			jButtonAlmacenar.setText("Almacenar");
			jButtonAlmacenar.setLocation(new Point(700, 450));
			jButtonAlmacenar.setSize(new Dimension(184, 28));
			jButtonAlmacenar.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					System.out.println("mouseClicked()"); 
					
					jContentPane.guardarImagen(nombre);
				}
			});
		}
		
		return jButtonAlmacenar;
	}


	private JButton getJButtonSimilar() {
		if (jButtonSimilar == null) {
			jButtonSimilar = new JButton();
			jButtonSimilar.setText("Similar");
			jButtonSimilar.setSize(new Dimension(184, 28));
			jButtonSimilar.setLocation(new Point(700, 370));
			jButtonSimilar.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					System.out.println("mouseClicked()"); 
				}
			});
		}
		return jButtonSimilar;
	}
	
	
	
	
	private JSlider  getJSliderBinarizar() {
		if (jSliderBinarizar == null) {
			jSliderBinarizar = new JSlider (JSlider.HORIZONTAL,
                    0, 256, 150);

	        //Turn on labels at major tick marks.

			jSliderBinarizar.setMajorTickSpacing(64);
			jSliderBinarizar.setMinorTickSpacing(1);
			jSliderBinarizar.setPaintTicks(true);
			jSliderBinarizar.setPaintLabels(true);
			//jFrameBinarizar.setBorder( BorderFactory.createEmptyBorder(0,0,10,0));
	        Font font = new Font("Serif", Font.PLAIN, 15);
	        jSliderBinarizar.setFont(font);

	        
	        jSliderBinarizar.setLocation(new Point(765, 87));
	        jSliderBinarizar.setSize(new Dimension(200, 50));
	        
	        
			jSliderBinarizar.addChangeListener(new javax.swing.event.ChangeListener(){
				public void stateChanged(ChangeEvent e) {
			        JSlider source = (JSlider)e.getSource();
			        if (!source.getValueIsAdjusting()) {
			    
			            int valor = (int)source.getValue();
						jContentPane.binarizeLUT(valor);
						jContentPane.applyFilterWithLookUpTable();
						jContentPane.repaint();
			            System.out.println("stateChanged() slider"); 
			        }
				}
			});

		}
		return jSliderBinarizar;
	}
	
	


	

}  
