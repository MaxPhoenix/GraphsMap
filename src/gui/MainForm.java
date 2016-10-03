package gui;


import org.openstreetmap.gui.jmapviewer.JMapViewer;

import javax.swing.*;
import java.awt.*;

public class MainForm {
	private static FileManager m;
	private JFrame frame;
	private JMapViewer miMapa;


	public MainForm() {

		m = new FileManager("instancia1.json");
		if (m.getCordinates().size() > 2 && m.getCordinates() != null) {
			m.getCordinates().forEach(System.out::println);
			//ACA CRASHEA SI NO RECIBE NADA DEL ARCHIVO
		}
		//ACA NO CRASHEA!!!, AUN SI NO RECIBIO NADA DEL ARCHIVO!!!!,aca debe quedar
		initialize();
	}


	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initialize() {
		GrafoJmap JGrafo = new GrafoJmap(m);

		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		miMapa = new JMapViewer();
		miMapa.setZoomContolsVisible(false);
		miMapa.setDisplayPositionByLatLon(-34.521, -58.7008, 11);


		JGrafo.render(miMapa);

		frame.setContentPane(miMapa);
	}
}
