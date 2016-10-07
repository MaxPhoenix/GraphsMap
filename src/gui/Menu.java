package gui;

import gui.GrafoJmap.Cluster;
import gui.GrafoJmap.GraphType;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showInputDialog;

/**
 * Created by Max on 10/2/2016.
 */
public class Menu extends JMapViewer implements ActionListener, ChangeListener{

    private  JFrame frame;
    private JComboBox fileCombo,modeCombo;
   // private final ButtonGroup grupo = new ButtonGroup();
    private JCheckBox clusterCheck;
    private JRadioButton intelliRadio,maximoRadio,promedioRadio;
    private JButton start, exit, createInstance, aplicarButton;
    private JMapViewer miMapa;
    private GraphType Modo= GrafoJmap.GraphType.AGM;
    private Cluster modoCluster=GrafoJmap.Cluster.MAXIMO;
    private File userFolder, projectDirectory;
    private FileManager m;
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

        frame=new JFrame("TPJmapViewer");
        frame.setLocationRelativeTo(null);
        frame.setBounds(100, 100, width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setResizable(false);


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


        //////////////////////////////////

        modeCombo = new JComboBox();
        modeCombo.addItem("Modo: Ninguna");
        modeCombo.addItem("Modo: AGM");
        modeCombo.addItem("Modo: Completo");
        modeCombo.setBounds(0,0,200,30);
        modeCombo.addActionListener(this);
        frame.add(modeCombo);
        modeCombo.setVisible(false);
        modeCombo.setSelectedItem("Modo: Ninguna");

        int offset=width/45;
        clusterCheck=new JCheckBox("Cluster");
        clusterCheck.setBounds(width/6,0,90,30);
        clusterCheck.addChangeListener(this);
        clusterCheck.setFont(clusterCheck.getFont().deriveFont(1,15F));
        frame.add(clusterCheck);
        clusterCheck.setVisible(false);
        clusterCheck.setOpaque(false);

        maximoRadio=new JRadioButton("Maximo");
        maximoRadio.setFont(maximoRadio.getFont().deriveFont(1,15F));
        maximoRadio.setBounds((int)(width/4.38)+offset,0,95,30);
        maximoRadio.addChangeListener(this);
        frame.add( maximoRadio);
        maximoRadio.setVisible(false);
        maximoRadio.setOpaque(false);

        promedioRadio=new JRadioButton("Promedio");
        promedioRadio.setFont(promedioRadio.getFont().deriveFont(1,15F));
        promedioRadio.setBounds((int)(width/3.3)+offset,0,110,30);
        promedioRadio.addChangeListener(this);
        frame.add( promedioRadio);
        promedioRadio.setVisible(false);
        promedioRadio.setOpaque(false);


        intelliRadio =new JRadioButton("Inteligente");
        intelliRadio.setFont(intelliRadio.getFont().deriveFont(1,15F));
        intelliRadio.setBounds((int)(width/2.6)+offset,0,120,30);
        intelliRadio.addChangeListener(this);
        frame.add(intelliRadio);
        intelliRadio.setVisible(false);
        intelliRadio.setOpaque(false);



        aplicarButton = new JButton("Aplicar");
        aplicarButton.setBounds((int)(width/2.1)+offset,0,85,30);
        aplicarButton.addActionListener(this);
        frame.add(aplicarButton);
        aplicarButton.setVisible(false);




        frame.setVisible(true);

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
        if(e.getSource()==clusterCheck){
        if(clusterCheck.isSelected()){
            Modo= GraphType.CLUSTERS;
            turnVisible(new Object[]{maximoRadio,promedioRadio,intelliRadio,aplicarButton});
        }
        else if (!clusterCheck.isSelected()){
            turnInvisible(new Object[]{maximoRadio,promedioRadio,intelliRadio,aplicarButton});
        }}


        if(e.getSource()instanceof JRadioButton){
            JRadioButton button=(JRadioButton) e.getSource();
           if(((JRadioButton) e.getSource()).isSelected()) {
               ArrayList<JRadioButton> link = new ArrayList<>();

               link.add(maximoRadio);

               link.add(promedioRadio);

               link.add(intelliRadio);

               link.remove((button));
               button.setSelected(true);
               for (JRadioButton j : link)
                   j.setSelected(false);


           }



        }


    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if (menu) {

            if (e.getSource() == start) {
                if (loadMap()) {
                    menu = false;
                    frame.setResizable(true);
                    turnInvisible(new Object[]{start, createInstance, fileCombo});
                    turnVisible(new Object[]{modeCombo});
                }
            }
            if (e.getSource() == createInstance) {
                if (createMap()) {
                    menu = false;
                    frame.setResizable(true);
                    turnInvisible(new Object[]{start, createInstance, fileCombo});
                    turnVisible(new Object[]{modeCombo});
                }
            }

            if (e.getSource() == exit) {
                System.exit(0);

            }
        } else {

            selecciondeModo(e);
            if (e.getSource() == exit) {
                menu = true;
                miMapa.removeAllMapMarkers();
                miMapa.removeAllMapPolygons();
                turnVisible(new Object[]{start, createInstance, fileCombo});
                turnInvisible(new Object[]{modeCombo,intelliRadio,maximoRadio,promedioRadio,clusterCheck,aplicarButton});
            }
        }
    }


       private void selecciondeModo(ActionEvent e){

        clusterCheck.setVisible(true);

         if(e.getSource()==modeCombo) {

             if((modeCombo.getSelectedItem().toString().contains("AGM"))&&!clusterCheck.isSelected()){
                 miMapa.removeAllMapMarkers();
                 miMapa.removeAllMapPolygons();
                 clusterCheck.setVisible(true);
                 JGrafo.changeMode(GraphType.AGM);


        }
             else if(modeCombo.getSelectedItem().toString().contains("Completo")){
                 miMapa.removeAllMapMarkers();
                 miMapa.removeAllMapPolygons();
                 clusterCheck.setVisible(false);
                 clusterCheck.setSelected(false);
                 JGrafo.changeMode(GraphType.COMPLETO);
         }
            else if(modeCombo.getSelectedItem().toString().contains("Ninguna")){
                miMapa.removeAllMapMarkers();
                miMapa.removeAllMapPolygons();
                clusterCheck.setVisible(false);
                clusterCheck.setSelected(false);
                 JGrafo.changeMode(GraphType.NINGUNA);
        }
         }

           if(e.getSource()==aplicarButton) {
              // Modo=GraphType.CLUSTERS;
                String n=JOptionPane.showInputDialog(this,"Ingrese la cantidad de clusters");

               if(isInteger(n)){
                 Integer input=Integer.parseInt(n);
                   miMapa.removeAllMapMarkers();
                   miMapa.removeAllMapPolygons();
                   JGrafo.changeClusterMode(modoCluster,input);
                   JGrafo.changeMode(GraphType.CLUSTERS);
               }


           }






           JGrafo.render(miMapa);


    }





    private boolean createMap() {
        //cartel para ingreso de coordenadas
        menu = false;
        Integer cantCoordenadas = 0;
        String nombre = showInputDialog(this,"nombre del archivo? se guardara en "+ userFolder.getAbsolutePath())+".json";

        if(nombre.equals("null.json")  || nombre.equals(".json")){
            JOptionPane.showMessageDialog(this, "Ingrese un nombre valido de archivo", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            else {
                String cant = (showInputDialog(this, "cuantas ingresas?"));

                if(cant == null || !isInteger(cant)){
                JOptionPane.showMessageDialog(this, "Ingrese un numero valido de coordenadas, debe ser entero y positivo", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            cantCoordenadas = Integer.parseInt(cant);

            if(cantCoordenadas <= 0)
                return false;
            //si el ususario ingreso 1 o mas coordenadas, se cargan
            else {
                ArrayList<Coordenada> userCoordinates = new ArrayList<>(cantCoordenadas);
                for (int i = 0; i < cantCoordenadas; i++) {
                    Coordenada c = getUserCoordinate(Integer.toString(cantCoordenadas-i));
                    if(c == null) {
                        while (c == null) {
                            JOptionPane.showMessageDialog(this, "Solo puede ingresar numeros como coordenadas", "Error", JOptionPane.ERROR_MESSAGE);
                            c = getUserCoordinate(Integer.toString(cantCoordenadas-i));
                        }
                    }
                    if(c.equals(new Coordenada())) {
                        JOptionPane.showMessageDialog(this, "Cancelo la operacion vuelva a crear archivo","Error", JOptionPane.ERROR_MESSAGE);
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

    private void turnVisible(Object[] obj){
        for(Object object: obj){
           if (object instanceof JComponent){
               JComponent j=(JComponent)object;
               j.setVisible(true);
           }

        }

    }

    private void turnInvisible(Object [] obj){
        for(Object object: obj){
            if (object instanceof JComponent){
                JComponent j=(JComponent)object;
                j.setVisible(false);
            }
        }


    }

    private Coordenada getUserCoordinate(String cantCords) {
        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        Object[] message = {"Latitud:", field1, "Longitud:", field2};
        String value1 = "" , value2 = "";
        int option = JOptionPane.showConfirmDialog(this, message, "Ingrese los valores ("+cantCords+")", JOptionPane.OK_CANCEL_OPTION);
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
