package gui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Max on 10/2/2016.
 */
public class Menu extends JMapViewer implements ActionListener, ChangeListener{

    private  JFrame frame;
    private JComboBox fileCombo;
   // private final ButtonGroup grupo = new ButtonGroup();
    private JCheckBox agmCheck,completeCheck,clusterCheck;
    private JButton start, exit, createInstance, saveInstance;
    private JMapViewer miMapa;
    private FileManager m;
    private boolean running = false;
    private Thread thread;
    private File userFolder, projectDirectory;
    private int width = 1280, height = width / 12 * 9;
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
        frame.setLocationRelativeTo(null);
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

        agmCheck=new JCheckBox("AGM");
        agmCheck.setBounds(300,10,100,30);
        agmCheck.addChangeListener(this);
        frame.add(agmCheck);
        agmCheck.setVisible(false);

        clusterCheck=new JCheckBox("Cluster");
        clusterCheck.setBounds(400,10,100,30);
        clusterCheck.addChangeListener(this);
        frame.add(clusterCheck);
        clusterCheck.setVisible(false);

        completeCheck=new JCheckBox("Completo");
        completeCheck.setBounds(500,10,100,30);
        completeCheck.addChangeListener(this);
        frame.add(completeCheck);
        completeCheck.setVisible(false);


        // grupo.add(agmCheck);
        // grupo.add(clusterCheck);
       // grupo.add(completeCheck);

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
    public void stateChanged(ChangeEvent e) {
    	JGrafo.changeMode(GrafoJmap.GraphType.NINGUNA);
    	if(agmCheck.isSelected() == true){
            miMapa.removeAllMapMarkers();
            miMapa.removeAllMapPolygons();
            JGrafo.changeMode(GrafoJmap.GraphType.AGM);
      
        }
        else if(completeCheck.isSelected() == true){
            miMapa.removeAllMapMarkers();
            miMapa.removeAllMapPolygons();
            JGrafo.changeMode(GrafoJmap.GraphType.COMPLETO);
          
        }
        else if(clusterCheck.isSelected() == true){
            miMapa.removeAllMapMarkers();
            miMapa.removeAllMapPolygons();
            JGrafo.changeMode(GrafoJmap.GraphType.CLUSTERS);
       
        }
        
        
        
      
        
        	JGrafo.render(miMapa);
       
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == start){
            if(loadMap()){
                turnInvisible(start, createInstance, fileCombo);
                turnVisible(agmCheck,completeCheck,clusterCheck);
            }
        }
        if(e.getSource() == createInstance){
            if(createMap()){
                turnInvisible(start, createInstance, fileCombo);
                turnVisible(agmCheck,completeCheck,clusterCheck);
            }
        }

        if(e.getSource()==exit){
            miMapa.removeAllMapMarkers();
            miMapa.removeAllMapPolygons();
            turnVisible(start,createInstance, fileCombo);
            turnInvisible(agmCheck,completeCheck,clusterCheck);
        }

    }

    private boolean createMap() {
        //cartel para ingreso de coordenadas
        menu = false;
        Integer cantCoordenadas = 0;
        String nombre = JOptionPane.showInputDialog(this,"nombre del archivo? se guardara en "+ userFolder.getAbsolutePath())+".json";

        if(nombre.equals("null.json")  || nombre.equals(".json")){
            JOptionPane.showMessageDialog(this, "Ingrese un nombre valido de archivo", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else {
            String cant = (JOptionPane.showInputDialog(this, "cuantas ingresas?"));

            if(cant == null || !isInteger(cant)){
                JOptionPane.showMessageDialog(this, "Ingrese un numero valido de coordenadas, debe ser entero y positivo", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            cantCoordenadas = Integer.parseInt(cant);

            if(cantCoordenadas <= 0)
                return false;
            //si el ususario ingreso 1 o mas coordenadas, se cargan
            else {
                ArrayList<Coordenada> userCoordinates = new ArrayList<Coordenada>(cantCoordenadas);
                for (int i = 0; i < cantCoordenadas; i++) {
                    Coordenada c = getUserCoordinate();
                    if(c == null) {
                        while (c == null) {
                            JOptionPane.showMessageDialog(this, "Solo puede ingresar numeros como coordenadas", "Error", JOptionPane.ERROR_MESSAGE);
                            c = getUserCoordinate();
                        }
                    }
                    if(c.equals(new Coordenada())) {
                        JOptionPane.showMessageDialog(this, "cancelo la operacion vuelva a crear archivo", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }


                    userCoordinates.add(c);
                }
                //arreglo de coordinates para guradar
                fileCoordinates = new ArrayList<Coordinate>(cantCoordenadas);
                for (Coordenada cor : userCoordinates)
                    fileCoordinates.add(new Coordinate(cor.getLat(), cor.getLon()));

                //crea un filemanager que guarda como coordinates el arreglo de coordenada creado arriba
                m = new FileManager(userFolder.getAbsolutePath() + "\\" + nombre);
                System.out.println(userFolder.getAbsolutePath() + "\\" + nombre);
                m.setCordinates(fileCoordinates);
                //cambie la funcion store coordinates para que guarde un directorio
                m.storeCoordinates();

                m.setCordinates(m.retrieveCoordinates());
                JGrafo = new GrafoJmap(m);
                JGrafo.render(miMapa);
                miMapa.setDisplayPositionByLatLon(m.getCordinates().get(0).getLat(),m.getCordinates().get(0).getLon(), 11);
                //agmCheck.setSelected(true);

            }
        }
        return true;
    }


    private boolean loadMap() {

        String opcion = (String) fileCombo.getSelectedItem();
        int opIndex= fileCombo.getSelectedIndex();
        String fileDir ="";
        if(opIndex <= 0) {
            JOptionPane.showMessageDialog(this, "No se ha cargado ningun archivo", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(opIndex < projectDirectorySize+1) {
            File dir = new File(projectDirectory.getAbsolutePath());
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
        JGrafo.render(miMapa);
        miMapa.setDisplayPositionByLatLon(m.getCordinates().get(0).getLat(),m.getCordinates().get(0).getLon(), 11);
        //agmCheck.setSelected(true);

        return true;
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

    private void turnVisible(JCheckBox a, JCheckBox b,JCheckBox c){
        a.setVisible(true);
        b.setVisible(true);
        c.setVisible(true);
    }

    private void turnInvisible(JCheckBox a, JCheckBox b,JCheckBox c){
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

            if (!onlyNumbers(value1) || !onlyNumbers(value2) || value1.equals("") || value2.equals("")) {
                return null;
            }
            else{
                double lat = Double.parseDouble(value1);
                double lon = Double.parseDouble(value2);
                return new Coordenada(lat, lon);
            }
        }
        else if(option == JOptionPane.CANCEL_OPTION){
            return new Coordenada();
        }

        return null;
    }


    private boolean isInteger(String s){
        String integer = "-+0123456789";
        for(int i = 0 ; i < s.length(); i++){
            char c = s.charAt(i);
            if(integer.indexOf(c) == -1)
                return false;
        }
        return true;
    }

    private static boolean onlyNumbers(String s){
        String numbers = "-+.0123456789";
        for(int i = 0 ; i < s.length(); i++){
            char c = s.charAt(i);
            if(numbers.indexOf(c) == -1)
                return false;
        }
        return true;
    }


}
