package view;

import model.image.*;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Point;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	private JPanelWithFilters jContentPane = null;
	private JTextField jTextFieldFoto = null;
	private JLabel jLabelFoto = null;
	private JButton jButtonAbrir = null;
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

	public static void main(	String[] args	){
		
		//Create the frame.
		MainWindow frame = new MainWindow();
		
		//What happens when it closes
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		//Show it
		frame.setVisible(true);
	
	}

	public MainWindow(){
		
		super();
		initialize();
	
	}

	
	private void initialize(){
		
		this.setSize(1000, 750);
		
		this.setContentPane(getJContentPane());
		
		this.setTitle("Ey ey fierita ete e el proyecto toson");
	
	}

	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			
			jLabelFoto = new JLabel();
			jLabelFoto.setBounds(new Rectangle(41, 21, 228, 22));
			jLabelFoto.setText("Ingrese la foto en jpg a procesar sin la extension");
			jContentPane = new JPanelWithFilters();
			jContentPane.setLayout(null);
			jContentPane.add(getJTextFieldFoto(), null);
			jContentPane.add(jLabelFoto, null);
			jContentPane.add(getJButtonAbrir(), null);
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
			jContentPane.loadImage( "eze_perfil2.png" );
			
		}
		
		return jContentPane;

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
			jButtonAbrir.setBounds(new Rectangle(284, 57, 152, 25));
			jButtonAbrir.setText("Abrir");
			jButtonAbrir.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
				
					String foto=jTextFieldFoto.getText();
					
					jContentPane.loadImage( foto + ".jpg" ); //TODO no funca esto todavia
					
					jContentPane.repaint();
				
				}
			});
		}
		return jButtonAbrir;
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
			jButtonSharpenV2.setLocation(new Point(700, 301));
			jButtonSharpenV2.setSize(new Dimension(120, 26));
			jButtonSharpenV2.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
					jContentPane.sharpenV2();
					jContentPane.applyFilter();
					jContentPane.repaint();
					
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
			jButtonFiltro2.setLocation(new Point(850, 301));
			jButtonFiltro2.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
					jContentPane.lowFilter();
					jContentPane.applyFilter();
					jContentPane.repaint();
					
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
			jButtonFiltro3.setLocation(new Point(789, 441));
			jButtonFiltro3.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
					jContentPane.smooth();
					jContentPane.applyFilter();
					jContentPane.repaint();
					
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
			jButtonDarken.setLocation(new Point(789, 561));
			jButtonDarken.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
										
					jContentPane.darkenLUT();
					jContentPane.applyFilterWithLookUpTable();
					jContentPane.repaint();
					
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
			jButtonDeMedia.setLocation(new Point(700, 661));
			jButtonDeMedia.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
										
					jContentPane.reverseLUT();
					jContentPane.applyFilterWithLookUpTable();
					jContentPane.repaint();
					
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
			jButtonGaussLowV3.setLocation(new Point(850, 661));
			jButtonGaussLowV3.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
										
					jContentPane.gaussLowFilterV3();
					jContentPane.applyFilter();
					jContentPane.repaint();
					
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
			jButtonDeshacer.setSize(new Dimension(152, 25));
			jButtonDeshacer.setLocation(new Point(585, 57));
			jButtonDeshacer.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
					jContentPane.reset();
					jContentPane.repaint();
					
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
					
					jContentPane.binarizeLUT();
					jContentPane.applyFilterWithLookUpTable();
					jContentPane.repaint();
					
					System.out.println("mouseClicked() on binarizar"); 
				
				}
			});
		}
		return jButtonBinarizar;
	}


	
	private JButton getJButtonAlmacenar() {
		if (jButtonAlmacenar == null) {
			jButtonAlmacenar = new JButton();
			jButtonAlmacenar.setText("Almacenar");
			jButtonAlmacenar.setLocation(new Point(97, 677));
			jButtonAlmacenar.setSize(new Dimension(184, 28));
			jButtonAlmacenar.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					System.out.println("mouseClicked()"); 
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
			jButtonSimilar.setLocation(new Point(397, 677));
			jButtonSimilar.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					System.out.println("mouseClicked()"); 
				}
			});
		}
		return jButtonSimilar;
	}
	
	

}  
