package gui;

import gui.GrafoJmap.Cluster;
import gui.GrafoJmap.GraphType;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javax.swing.JOptionPane.*;

/**
 * Created by Max on 10/2/2016.
 */
public class Menu extends JMapViewer implements ActionListener, ChangeListener, MouseListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JComboBox<String> fileCombo, modeCombo;
    private int indexCombo;
    // private final ButtonGroup grupo = new ButtonGroup();
    private JCheckBox clusterCheck;
    private JRadioButton intelliRadio, maximoRadio, promedioRadio;
    private static JProgressBar progressBar;
    private JButton start, exit, dictarCoords, nuevaInstancia, aplicarButton, stadistics, edit, noEdit;
    private static JMapViewer miMapa;
    GraphType Modo ;
    private Cluster modoCluster ;
    private File userFolder, projectDirectory;
    private FileManager fileManager;
    private String loadingStatus="";
    private int width = 1280, height = width / 12 * 9;
    private boolean menu = true; //para que no sea expandible la ventana
    private static GrafoJmap JGrafo;
    private ArrayList<Coordinate> fileCoordinates;
    private int projectDirectorySize = 0;
    private boolean edition = false;
    private Integer clusterInput = 0;
    private boolean edited = false;
    private boolean instanciaVacia=false;
    



    


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
            userFolder = new File(currentUsersHomeDir + separator + "Archivos User");
            if (!userFolder.exists()) {
                userFolder.mkdir();   //crea el directorio de archivos
            }

        } catch (Exception e) {
        }
        initialize();
    }

    public void initialize() {

        frame = new JFrame("TPJmapViewer");
        try {
            frame.setIconImage(ImageIO.read(new File("librerias/map.png")));
        }

        catch (IOException exc) {
            exc.printStackTrace();
        }
        frame.setLocationRelativeTo(null);
        frame.setBounds(100, 100, width, height);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);


        miMapa = new JMapViewer();
        miMapa.setZoomContolsVisible(false);
        miMapa.setDisplayPositionByLatLon(-34.521, -58.7008, 11);
        frame.setContentPane(miMapa);


        start = new JButton("Start");
        start.setBounds(205, 0, 100, 30);
        start.addActionListener(this);
        frame.add(start);

        exit = new JButton("Exit");
        exit.setBounds(310, 0, 100, 30);
        exit.addActionListener(this);
        frame.add(exit);

        dictarCoords = new JButton("Dictar Coordenadas");

        dictarCoords.setBounds(0, 70, 200, 30);
        dictarCoords.addActionListener(this);
        frame.add(dictarCoords);


        nuevaInstancia = new JButton("Nueva Instancia");
        nuevaInstancia.setBounds(0, 35, 200, 30);
        nuevaInstancia.addActionListener(this);
        frame.add(nuevaInstancia);
        


        fileCombo = new JComboBox<String>();
        fileCombo.setBounds(0, 0, 200, 30);
        fileCombo.addActionListener(this);
        addFilestoComboBox();
        frame.add(fileCombo);


        //////////////////////////////////

        modeCombo = new JComboBox<String>();
        modeCombo.addItem("Modo: Ninguna");
        modeCombo.addItem("Modo: AGM");
        modeCombo.addItem("Modo: Camino Goloso");
        modeCombo.addItem("Modo: Completo");
        modeCombo.setBounds(0, 0, 200, 30);
        modeCombo.addActionListener(this);
        frame.add(modeCombo);
        modeCombo.setVisible(false);
        modeCombo.setSelectedItem("Modo: Ninguna");


        int offset = width / 10; //45
        clusterCheck = new JCheckBox("Cluster");
        clusterCheck.setBounds(width / 6, 0, 90, 30);
        clusterCheck.addChangeListener(this);
        clusterCheck.setFont(clusterCheck.getFont().deriveFont(1, 15F));
        frame.add(clusterCheck);
        clusterCheck.setVisible(false);
        clusterCheck.setOpaque(false);


        maximoRadio = new JRadioButton("Maximo");
        maximoRadio.setFont(maximoRadio.getFont().deriveFont(1, 15F));
        maximoRadio.setBounds((int) (width / 4.38) + offset, 0, 95, 30);
        maximoRadio.addChangeListener(this);
        frame.add(maximoRadio);
        maximoRadio.setVisible(false);
        maximoRadio.setOpaque(false);


        promedioRadio = new JRadioButton("Promedio");
        promedioRadio.setFont(promedioRadio.getFont().deriveFont(1, 15F));
        promedioRadio.setBounds((int) (width / 3.3) + offset, 0, 110, 30);
        promedioRadio.addChangeListener(this);
        frame.add(promedioRadio);
        promedioRadio.setVisible(false);
        promedioRadio.setOpaque(false);


        intelliRadio = new JRadioButton("Inteligente");
        intelliRadio.setFont(intelliRadio.getFont().deriveFont(1, 15F));
        intelliRadio.setBounds((int) (width / 2.6) + offset, 0, 120, 30);
        intelliRadio.addChangeListener(this);
        frame.add(intelliRadio);
        intelliRadio.setVisible(false);
        intelliRadio.setOpaque(false);



        aplicarButton = new JButton("Aplicar");
        aplicarButton.setBounds((int) (width / 2.1) + offset, 0, 85, 30);
        aplicarButton.addActionListener(this);
        frame.add(aplicarButton);
        aplicarButton.setVisible(false);

        stadistics = new JButton("Mostrar estadisticas");
        stadistics.setBounds((int) (width / 1.5) + offset, 0, 150, 30);
        stadistics.addActionListener(this);
        frame.add(stadistics);
        stadistics.setVisible(false);

        edit = new JButton("Modo edicion");
        edit.setBounds((int) (width / 1.5) + offset, (int)(height/20), 100, 30);
        edit.addActionListener(this);
        frame.add(edit);
        edit.setVisible(false);

        noEdit = new JButton("Fin modo edicion");
        noEdit.setBounds((int) (width / 1.5) + offset, (int)(height/17), 150, 30);
        noEdit.addActionListener(this);
        frame.add( noEdit);
        noEdit.setVisible(false);

        miMapa.addMouseListener(this);
        frame.setVisible(true);
        

        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        progressBar.setBounds((height/2)+30,0, 250, 30);

        Container content = frame.getContentPane();
        content.add(progressBar, BorderLayout.NORTH);
        
  
        

    }

    private void addFilestoComboBox() {
        fileCombo.addItem("Seleccionar");
        for (String f : projectDirectory.list()) {
            fileCombo.addItem(f);
            projectDirectorySize++;
        }
        for (String s : userFolder.list()) {
            fileCombo.addItem(s);
        }
    }

    @SuppressWarnings("unused")
	public static void main(String[] args) {
        Menu menu = new Menu();
    }


    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == clusterCheck) {
            if (clusterCheck.isSelected()) {
                Modo = GraphType.CLUSTERS;
                turnVisible(new Object[]{maximoRadio, promedioRadio, intelliRadio, aplicarButton});
            } else if (!clusterCheck.isSelected()) {
                turnInvisible(new Object[]{maximoRadio, promedioRadio, intelliRadio, aplicarButton});
            }
        }

        if (e.getSource() instanceof JRadioButton) {
            JRadioButton button = (JRadioButton) e.getSource();
            if (((JRadioButton) e.getSource()).isSelected()) {
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

        if(e.getSource()==nuevaInstancia) {
            JGrafo = new GrafoJmap(new ArrayList<Coordinate>(), this);
            turnInvisible(new Object[]{start, dictarCoords, fileCombo, nuevaInstancia });
            turnVisible(new Object[]{modeCombo,stadistics,edit});
            modeCombo.setSelectedIndex(0);
            edition = true;
            JOptionPane.showMessageDialog(null, "Entrando en modo edicion. Haga click donde desee ingresar coordenadas.");
            turnInvisible(new Object[] {edit, modeCombo});
            noEdit.setVisible(true);


        }


        if (menu) {



            if (e.getSource() == start) {
                if (loadMap()) {
                    exit.setText("Cancel");
                	modeCombo.setSelectedIndex(0);
                    menu = false;
                    turnInvisible(new Object[]{start, dictarCoords, fileCombo, nuevaInstancia });
                    turnVisible(new Object[]{modeCombo,stadistics,edit});
                }
            }



            if (e.getSource() == dictarCoords) {
                if (createMap()) {
                    menu = false;
                    frame.setResizable(true);
                    turnInvisible(new Object[]{start, dictarCoords, fileCombo,stadistics});
                    turnVisible(new Object[]{modeCombo,edit});
                }
            }




            if (e.getSource() == exit) {
            	
                System.exit(0);
            }
        }
        else {
            selecciondeModo(e);
            if (e.getSource() == exit) {
                if(edited == true){
                    int saveOption = JOptionPane.showConfirmDialog(this,"Se han detectado cambios en la instancia, desea guardarlos?","Cambios", YES_NO_OPTION);
                    if (saveOption == YES_OPTION) {
                        String name = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo a guardar", "File Name");
                        while (isInvalidName(name)) {
                            JOptionPane.showMessageDialog(this, "no se puede dividir el grafo en mas grupos que aristas", "Error", JOptionPane.ERROR_MESSAGE);
                            name = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo a guardar", "File Name");
                        }
                        saveChanges(name);
                        edited = false;
                        addFilestoComboBox();
                        }
                        else if (saveOption == NO_OPTION || saveOption == CLOSED_OPTION)
                            JOptionPane.showMessageDialog(this, "No se guardaron las instancias", "Error", JOptionPane.ERROR_MESSAGE);
                
                }
                JGrafo.interrupt();
                setProgress("Interrupted",100);
                menu = true;
                Rectangle frameBounds = (frame.getBounds());
                width = frameBounds.width;
                height = frameBounds.height;         
                miMapa.removeAllMapMarkers();
                miMapa.removeAllMapPolygons();
                turnVisible(new Object[]{start, dictarCoords, fileCombo,nuevaInstancia});
                turnInvisible(new Object[]{modeCombo, intelliRadio, maximoRadio, promedioRadio, clusterCheck, aplicarButton,stadistics,edit,noEdit,progressBar});
                clusterInput = 0;
                Modo = GraphType.NINGUNA;
                modoCluster = Cluster.INTELIGENTE;

            }
        }
    }

    private void selecciondeModo(ActionEvent e) {

        if (e.getSource() == modeCombo) {
            miMapa.removeAllMapMarkers();
            miMapa.removeAllMapPolygons();
            if ((modeCombo.getSelectedItem().toString().contains("AGM")) && !clusterCheck.isSelected()) {
                if(!isGraphLoaded()){
                    mostrarMensajeError();
                    modeCombo.setSelectedIndex(0);
                }
                else {

                    clusterCheck.setVisible(true);
                    JGrafo.changeMode(GraphType.AGM);
                    Modo = GraphType.AGM;
                }
            }

            else if (modeCombo.getSelectedItem().toString().contains("Camino Goloso")) {

                if(!isGraphLoaded()){
                    mostrarMensajeError();
                    modeCombo.setSelectedIndex(0);
                }
                else{

                    clusterCheck.setVisible(false);
                    clusterCheck.setSelected(false);
                    Modo = GraphType.CAMINOMINIMO;
                    JGrafo.changeMode(GraphType.CAMINOMINIMO);

            }
            }




            else if (modeCombo.getSelectedItem().toString().contains("Completo")) {
                if(!isGraphLoaded()){
                    mostrarMensajeError();
                    modeCombo.setSelectedIndex(0);
                }
                else{

                    clusterCheck.setVisible(false);
                    clusterCheck.setSelected(false);
                    JGrafo.changeMode(GraphType.COMPLETO);
                    Modo = GraphType.COMPLETO;
                }
            }




            else if (modeCombo.getSelectedItem().toString().contains("Ninguna")) {

                clusterCheck.setVisible(false);
                clusterCheck.setSelected(false);
                JGrafo.changeMode(GraphType.NINGUNA);
                Modo = GraphType.NINGUNA;
            }

        }

        if(e.getSource() == stadistics){
            int cant= 0;
            double pesoTot=0;
            for(Arista ar : JGrafo.getAristasActuales()) {
                cant++;
                pesoTot+=ar.getPeso()*1000;
            }


            JOptionPane.showMessageDialog(null,"Modo : \n"+ Modo + "\nPeso del Grafo:\n"+pesoTot +"\nCantidad de Aristas:\n"+cant+ "\nCantidad de Vertices:\n"+JGrafo.getCoordenadas().size()+
                    "\nCantidad de Aristas:\n"+ JGrafo.getAristasActuales().size() + "\n Cantidad de clusters : \n"+ JGrafo.getCantClusters());
        }

        if (e.getSource() == aplicarButton) {



            if(intelliRadio.isSelected()){
                miMapa.removeAllMapMarkers();
                miMapa.removeAllMapPolygons();
                modoCluster = Cluster.INTELIGENTE;
                JGrafo.changeClusterMode(modoCluster,0);
                JGrafo.changeMode(GraphType.CLUSTERS);
                render();
                return;

            }
            String n = JOptionPane.showInputDialog(this, "Ingrese la cantidad de clusters");
           if(isNumeroValido(n)){

                clusterInput = Integer.parseInt(n);


                if (maximoRadio.isSelected())
                    modoCluster = Cluster.MAXIMO;
                else if (promedioRadio.isSelected())
                    modoCluster = Cluster.PROMEDIO;
                else if(intelliRadio.isSelected()){
                    modoCluster = Cluster.INTELIGENTE;
                }
                else{
                    modoCluster =null;
                }
                try{
                    JGrafo.changeClusterMode(modoCluster, clusterInput);
                    JGrafo.changeMode(GraphType.CLUSTERS);
                }catch (IllegalArgumentException exception){
                    JOptionPane.showMessageDialog(this, "no se puede dividir el grafo en mas grupos que aristas", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

                render();

            return;
        }

        if(e.getSource() == edit) {


            modeCombo.setSelectedIndex(0);
            edition = true;
            JOptionPane.showMessageDialog(null, "Entrando en modo edicion. Haga click donde desee ingresar coordenadas.");
            turnInvisible(new Object[] {edit, modeCombo});
            noEdit.setVisible(true);

        }





        if(e.getSource() == noEdit){
            modeCombo.setSelectedIndex(indexCombo);

            JOptionPane.showMessageDialog(null, "Finalizando modo edicion");
            edition = false;
            turnVisible(new Object[] {edit, modeCombo});
            noEdit.setVisible(false);


            ArrayList<Coordinate> bkp=JGrafo.getCoordenadas();
            JGrafo= new GrafoJmap(bkp,this);
            edited=false;
        }

    }

    private void mostrarMensajeError(){
        JOptionPane.showMessageDialog(this, "no cargo todavia", "Error", JOptionPane.ERROR_MESSAGE);
    }


private void render(){

    if(JGrafo.isInterrupted()) {
        JGrafo.render();

        if (JGrafo.getCoordenadas().size() > 0)
            miMapa.setDisplayPositionByLatLon(JGrafo.getCoordenadas().get(0).getLat(), JGrafo.getCoordenadas().get(0).getLon(), 11);

    }
}

    private boolean isGraphLoaded(){
        boolean condition1 = JGrafo.isAgmLoaded();
        boolean condition2 = true;
        return (condition1 && condition2);
    }






    private boolean isNumeroValido(String n){
        
        if(n == null){
            JOptionPane.showMessageDialog(this, "No ha seleccionado nada", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(!isInteger(n) || n.equals("")){
            JOptionPane.showMessageDialog(this, "Solo se permiten digitos para la creacion de clusters", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (isInteger(n)) {
            Integer input = Integer.parseInt(n);
            if(input <= 1){
                JOptionPane.showMessageDialog(this, "Pueden haber como minimo 2 clusters", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }


    private boolean createMap() {
        //cartel para ingreso de coordenadas
        Integer cantCoordenadas = 0;
        String nombre = showInputDialog(this,"nombre del archivo?, se guardara en "+ userFolder.getAbsolutePath())+".json";

        if(nombre.equals("null.json")  || nombre.equals(".json") || isInvalidName(nombre)){
            JOptionPane.showMessageDialog(this, "Ingrese un nombre valido de archivo", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else {
        	//TODO CREAR UN ARCHIVO VACIO
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
                fileManager = new FileManager(userFolder.getAbsolutePath() + "\\" + nombre);
                fileManager.setCordinates(fileCoordinates);
                //cambie la funcion store coordinates para que guarde un directorio
                fileManager.storeCoordinates();

                fileManager.setCordinates(fileManager.retrieveCoordinates());
                JGrafo = new GrafoJmap(fileManager,this);

                miMapa.setDisplayPositionByLatLon(fileManager.getCordinates().get(0).getLat(), fileManager.getCordinates().get(0).getLon(), 11);
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
        fileManager = new FileManager(opcion);
        fileManager.setCordinates(fileManager.retrieveCoordinates());
        
        if(fileManager.isArchivoCorrupto()){
        	JOptionPane.showMessageDialog(this, "Archivo Corrupto, generando instancia vacia... ", "Error", JOptionPane.ERROR_MESSAGE);
        	edited=true;
        }
        
        JGrafo = new GrafoJmap(fileManager,this);
        


        return true;
    }


    public static JMapViewer getMiMapa() {
        return miMapa;
    }

    public void setProgress(String titulo, int p){
        progressBar.setVisible(true);
        if(p==100){
            progressBar.setVisible(false);
            frame.setTitle("TPJmapViewer:  "+this.fileCombo.getSelectedItem());
            exit.setText("Exit");
            if(!JGrafo.isInterrupted()){
                JGrafo.interrupt();
            }
            if(JGrafo.isInterrupted()){
                render();

            }
            return;
        }
        progressBar.setString(titulo);
        progressBar.setValue(p);
        frame.setTitle("TPJmapViewer:    "+titulo+" "+p+"%");


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
        else if(option == JOptionPane.CLOSED_OPTION)
            return new Coordenada();

        return null;
    }

    private boolean isInteger(String s){
        String integer = "-+0123456789";
        String nonCharacters = "@.,_/(&%$#!|°=\\)¨´^{}";
        for(int i = 0 ; i < s.length(); i++){
            if(i != 0 ) {
                if (s.charAt(i) == '+' || s.charAt(i) == '-')
                    return false;
            }
            char c = s.charAt(i);
            if(integer.indexOf(c) == -1 || nonCharacters.indexOf(c) != -1)
                return false;
        }
        return true;
    }

    private static boolean onlyNumbers(String s){
        String numbers = "-+.0123456789";
        String nonCharacters = "@,_/(&%$#!|°*=\\)¨´^{}";
        for(int i = 0 ; i < s.length(); i++){
            if(i != 0 ) {
                if (s.charAt(i) == '+' || s.charAt(i) == '-')
                    return false;
            }
            char c = s.charAt(i);
            if(numbers.indexOf(c) == -1 || nonCharacters.indexOf(c) != -1)
                return false;
        }
        return true;
    }

    private boolean isInvalidName(String s){
        if(s==null)
            return true;
        String nonCharacters = "@+-.,_/(&%$#!|°*\\=)¨´^{}";
        for(int i = 0; i < s.length()-5;i++ ){
            char c =  s.charAt(i);
            if(nonCharacters.indexOf(c) != -1)
                return true;
        }
        return false;
    }



    private void saveChanges(String nombre){
        //arreglo de coordinates para guradar
        fileCoordinates = JGrafo.getCoordenadas();
        //crea un filemanager que guarda como coordinates el arreglo de coordenada creado arriba
        fileManager = new FileManager(userFolder.getAbsolutePath() + "\\" + nombre);
        fileManager.setCordinates(fileCoordinates);
        //cambie la funcion store coordinates para que guarde un directorio
        fileManager.storeCoordinates();
    }

	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
		//TODO AGREGAR AL GRAFO Cuando se hace click en RECALCULAR
		
		if(!menu){
            if (e.getButton() == 1) {

                if(edition == true) {
                    JGrafo.getCoordenadas().add(miMapa.getPosition(e.getPoint()));
                   render();
                    edited = true;
                }
            }
		}
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
	
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
	
	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
	
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		
	}

    public JProgressBar getBar() {
        return progressBar;}

    public void setCantClusters(int cantClusters) {
        this.clusterInput=-cantClusters;
    }
}
