package gui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Max on 10/2/2016.
 */
public class Menu extends JMapViewer implements ActionListener{

    private  JFrame frame;
    private JComboBox fileCombo;
    private JButton start, exit, createInstance, saveInstance;
    private JMapViewer miMapa;
    private FileManager m;
    private boolean running = false;
    private Thread thread;
    private File userFolder, projectDirectory;
    private int width = 1024, height = width / 12 * 9;
    private boolean menu = true; //para que no sea expandible la ventana
    private GrafoJmap JGrafo;
    private ArrayList<Coordinate> fileCoordinates;
    private int projectDirectorySize = 0;

    public Menu() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());

            //esta es la carpeta con istancias del proyecto
            projectDirectory = new File("Archivos");
            if (!projectDirectory.exists()) {
                projectDirectory.mkdir();   //crea el directorio de archivos
            }

            //aca se crea una carpeta en el home directory del usuario para guardar sus propias instancias
            String separator = File.separator;
            String currentUsersHomeDir = System.getProperty("user.home"); //C\Users\ "Nombre del usuario
            userFolder = new File(currentUsersHomeDir+separator+"Archivos User");
            if (!userFolder.exists()) {
                userFolder.mkdir();   //crea el directorio de archivos
            }

        } catch (Exception e) {
        }
       initialize();
    }




    public void initialize() {

        frame=new JFrame("Soy el mapa");
        frame.setBounds(100, 100, width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        if(menu)
            frame.setResizable(false);
        else
            frame.setResizable(true);

        miMapa = new JMapViewer();
        miMapa.setZoomContolsVisible(false);
        miMapa.setDisplayPositionByLatLon(-34.521, -58.7008, 11);
        frame.setContentPane(miMapa);


        start = new JButton("Start");
        start.setBounds(width-200,height-200,150,50);
        start.addActionListener(this);
        frame.add(start);

        exit= new JButton("Exit");
        exit.setBounds(width-200,height-100,150,50);
        exit.addActionListener(this);
        frame.add(exit);

        createInstance = new JButton("Crear nueva instancia");
        createInstance.setBounds(width /4,height/3,400,50);
        createInstance.addActionListener(this);
        frame. add(createInstance);


        fileCombo = new JComboBox();
        fileCombo.setBounds(width /4,height/2,400,50);
        fileCombo.addActionListener(this);
        addFilestoComboBox();
        frame.add(fileCombo);

        frame.setVisible(true);




    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawRect(100,100,1000000,100000);
    }

    private void addFilestoComboBox() {
        fileCombo.addItem("(Seleccione una instancia)");
        for (String f : projectDirectory.list()) {
            fileCombo.addItem(f);
            projectDirectorySize++;
        }
        for (String s : userFolder.list()) {
            fileCombo.addItem(s);
        }
    }

    public static void main(String[] args){
        Menu menu = new Menu();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == start){
            loadMap();
        }
        if(e.getSource() == createInstance){
            createMap();
        }

        if(e.getSource()==exit){
            miMapa.removeAllMapMarkers();
            miMapa.removeAllMapPolygons();
            turnVisible(start,createInstance, fileCombo);
        }

    }

    private void createMap() {
        //cartel para ingreso de coordenadas
        String nombre = JOptionPane.showInputDialog(this,"nombre del archivo? se guardara en "+ userFolder.getAbsolutePath())+".json";
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
        m = new FileManager(userFolder.getAbsolutePath()+"\\"+nombre);
        System.out.println(userFolder.getAbsolutePath()+"\\"+nombre);
        m.setCordinates(fileCoordinates);
        //cambie la funcion store coordinates para que guarde un directorio
        m.storeCoordinates();
/*
        //aca se guarda el archivo en el home directory de la computadora del usuario
        String fileDir="", opcion="";
        File dir = new File(userFolder.getAbsolutePath());
        if(dir.isDirectory()){
            for(File f: dir.listFiles()){
                if(f.getName().equals(nombre)){
                    fileDir =  f.getAbsolutePath();

                }
            }
            opcion = fileDir;
        }
        System.out.println(opcion);
        m = new FileManager(opcion);
*/
        m.setCordinates(m.retrieveCoordinates());
        JGrafo = new GrafoJmap(m);
        turnInvisible(start,createInstance, fileCombo);
        JGrafo.render(miMapa);

    }

    private void loadMap() {
        menu = false;
        String opcion = (String) fileCombo.getSelectedItem();
        int opIndex= fileCombo.getSelectedIndex();
        String fileDir ="";
        if(opIndex < projectDirectorySize-1) {
            File dir = new File("Archivos");
            if(dir.isDirectory()){
                for(File f: dir.listFiles()){
                    if(f.getName().equals(opcion))
                        fileDir =  f.getAbsolutePath();
                }
                opcion = fileDir;
            }
        }
        else {
            File dir = new File(userFolder.getAbsolutePath());
            if(dir.isDirectory()){
                for(File f: dir.listFiles()){
                    if(f.getName().equals(opcion)){
                        fileDir =  f.getAbsolutePath();

                    }
                }
                opcion = fileDir;
            }
        }
            m = new FileManager(opcion);
            m.setCordinates(m.retrieveCoordinates());
            JGrafo = new GrafoJmap(m);
            miMapa.setDisplayPositionByLatLon(m.getCordinates().get(0).getLat(),m.getCordinates().get(0).getLon(), 30);
            turnInvisible(start,createInstance, fileCombo);

               JGrafo.render(miMapa);


    }

    private void turnVisible(JButton a, JButton b,JComboBox c){
        a.setVisible(true);
        b.setVisible(true);
        c.setVisible(true);
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
