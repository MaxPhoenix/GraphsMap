package gui;

import org.openstreetmap.gui.jmapviewer.JMapViewer;

import javax.swing.*;
import java.io.File;

/**
 * Created by Max on 10/2/2016.
 */
public class Menu extends JFrame implements Runnable {

    private JComboBox files;
    private JButton start, exit, createInstance, selectInstance, saveInstance;
    private JMapViewer miMapa;
    private FileManager m;
    private boolean running = false;
    private Thread thread;
    private File path, directoryProject;
    private int width = 1024, height = width / 12 * 9;
    private boolean menu = true; //para que no sea expandible la ventana

    public Menu() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());

            //esta es la carpeta con istancias del proyecto
            directoryProject = new File("Archivos");
            if (!directoryProject.exists()) {
                directoryProject.mkdir();   //crea el directorio de archivos
            }

            //aca se crea una carpeta en el home directory del usuario para guardar sus propias instancias
            String separator = File.separator;
            String currentUsersHomeDir = System.getProperty("user.home"); //C\Users\ "Nombre del usuario
            path = new File(currentUsersHomeDir+separator+"Archivos");
            if (!path.exists()) {
                path.mkdir();   //crea el directorio de archivos
            }

        } catch (Exception e) {
        }
        initialize();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }

            if (running)
                render();
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
        stop();
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;

    }

    private synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tick() {
    }

    public void render() {
    }


    public void initialize() {
        //GrafoJmap JGrafo = new GrafoJmap(m);


        new JFrame();
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        if(menu)
            setResizable(false);
        else
            setResizable(true);

        miMapa = new JMapViewer();
        miMapa.setZoomContolsVisible(false);
        miMapa.setDisplayPositionByLatLon(-34.521, -58.7008, 11);


        //JGrafo.render(miMapa);

        setContentPane(miMapa);

        files = new JComboBox();
        files.setBounds(width -500,height-500,200,50);
        
        addFilestoComboBox();
        add(files);

        setVisible(true);

    }

    private void addFilestoComboBox() {
        files.addItem("(Seleccione una instancia)");
        for(String f : directoryProject.list())
            files.addItem(f);
        for(String s: path.list())
            files.addItem(s);
    }

    public static void main(String[] args){
        Menu menu = new Menu();
    }



}
