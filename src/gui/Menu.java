package gui;

import org.openstreetmap.gui.jmapviewer.JMapViewer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Max on 10/2/2016.
 */
public class Menu extends JFrame implements Runnable , ActionListener{

    private JComboBox files;
    private JButton start, exit, createInstance, saveInstance;
    private JMapViewer miMapa;
    private FileManager m;
    private boolean running = false;
    private Thread thread;
    private File path, directoryProject;
    private int width = 1024, height = width / 12 * 9;
    private boolean menu = true; //para que no sea expandible la ventana
    private GrafoJmap JGrafo;


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
            path = new File(currentUsersHomeDir+separator+"Archivos User");
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

        new JFrame("Soy el mapa");
        setBounds(100, 100, width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        if(menu)
            setResizable(false);
        else
            setResizable(true);

        miMapa = new JMapViewer();
        miMapa.setZoomContolsVisible(false);
        miMapa.setDisplayPositionByLatLon(-34.521, -58.7008, 11);
        setContentPane(miMapa);


        start = new JButton("Start");
        start.setBounds(width-200,height-200,150,50);
        start.addActionListener(this);
        add(start);

        exit= new JButton("Exit");
        exit.setBounds(width-200,height-100,150,50);
        exit.addActionListener(this);
        add(exit);

        createInstance = new JButton("Crear nueva instancia");
        createInstance.setBounds(width /4,height/3,400,50);
        createInstance.addActionListener(this);
        add(createInstance);


        files = new JComboBox();
        files.setBounds(width /4,height/2,400,50);
        files.addActionListener(this);
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


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == start){
            menu = false;
            String opcion = (String) files.getSelectedItem();
            int opIndex= files.getSelectedIndex();
            String fileDir ="";
            if(opIndex > 0 && opIndex < 6) {
                File dir = new File("Archivos");
                if(dir.isDirectory()){
                    for(File f: dir.listFiles()){
                        if(f.getName().equals(opcion))
                            fileDir =  f.getAbsolutePath();
                    }
                    opcion = fileDir;

                }
                m = new FileManager(opcion);
                JGrafo = new GrafoJmap(m);
                start.setVisible(false);
                createInstance.setVisible(false);
                files.setVisible(false);
                JGrafo.render(miMapa);
                setContentPane(miMapa);
            }

        }
    }
}
