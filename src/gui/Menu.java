package gui;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

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
    private ArrayList<Coordinate> fileCoordinates;

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
                m.setCordinates(m.retrieveCoordinates(opcion));
                JGrafo = new GrafoJmap(m);
                turnInvisible(start,createInstance,files);
                JGrafo.render(miMapa);
                setContentPane(miMapa);
            }
        }
        if(e.getSource() == createInstance){
            //cartel para ingreso de coordenadas
            String nombre = JOptionPane.showInputDialog(this,"nombre del archivo? se guardara en "+ path.getAbsolutePath());
            int cant = Integer.parseInt(JOptionPane.showInputDialog(this,"cuantas ingresas?"));
            ArrayList<Coordenada> userCoordinates = new ArrayList<Coordenada>(cant);
            for(int i = 0; i< cant ; i++){
                Coordenada c = getUserCoordinate();
                while(c == null){
                    JOptionPane.showMessageDialog(this, "Solo puede ingresar numeros como coordenadas", "Error", JOptionPane.ERROR_MESSAGE);
                    c = getUserCoordinate();
                }
                userCoordinates.add(c);
            }

            //arreglo de coordinates para guradar
            fileCoordinates = new ArrayList<Coordinate>(cant);
            for(Coordenada cor: userCoordinates)
                fileCoordinates.add(new Coordinate(cor.getLat(),cor.getLon()));

            //crea un filemanager que guarda como coordinates el arreglo de coordenada creado arriba
            m = new FileManager(path.getAbsolutePath());
            m.setCordinates(fileCoordinates);
            //cambie la funcion store coordinates para que guarde un directorio
            m.storeCoordinates(path.getAbsolutePath(),nombre+".json");

            //aca se guarda el archivo en el home directory de la computadora del usuario
            String fileDir="", opcion="";
            File dir = new File(path.getAbsolutePath());
            if(dir.isDirectory()){
                for(File f: dir.listFiles()){
                    if(f.getName().equals(nombre+".json")){
                        fileDir =  f.getAbsolutePath();
                    }
                }
                opcion = fileDir;
            }

            m = new FileManager(opcion);
            m.setCordinates(m.retrieveCoordinates(opcion));
            JGrafo = new GrafoJmap(m);
            turnInvisible(start,createInstance,files);
            JGrafo.render(miMapa);
            setContentPane(miMapa);
        }
    }

    private void turnInvisible(JButton a, JButton b,JComboBox c){
        a.setVisible(false);
        b.setVisible(false);
        c.setVisible(false);
    }


    private Coordenada getUserCoordinate() {
        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        Object[] message = {"Latitud:", field1, "Longitud:", field2};
        String value1 = "" , value2 = "";
        int option = JOptionPane.showConfirmDialog(this, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            value1 = field1.getText();
            value2 = field2.getText();
            // if(!onlyNumbers(value1) || !onlyNumbers(value2) || value1.equals("") || value2.equals("")){
            //     return null;
        }
        //
        // else{
        //    return null;
        // }
        //boolean numbers1 = onlyNumbers(value1);
        //boolean numbers2 = onlyNumbers(value2);
        //if (numbers1 && numbers2) {
        double lat = Double.parseDouble(value1);
        double lon = Double.parseDouble(value2);
        return new Coordenada(lat, lon);
        // }


    }

    private static boolean onlyNumbers(String s){
        String numbers = "-.123456789";
        for(int i = 0 ; i < s.length(); i++){
            char c = s.charAt(i);
            if(numbers.indexOf(c) == -1){
                System.out.println(s.charAt(i));
                return false;}
        }
        return true;
    }

}
