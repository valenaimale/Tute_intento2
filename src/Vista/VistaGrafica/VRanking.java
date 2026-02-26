package Vista.VistaGrafica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class VRanking extends JFrame{
    private JTable tabla;
    private DefaultTableModel modelo_tabla;
    private JPanel panel_principal;
    private JPanel panel_botones;
    private JButton volver_final;
    private JButton volver_comienzo;
    private VistaGrafica vista_padre;
    private JScrollPane scroll;

    public VRanking (VistaGrafica vista_padre){
        inicializar(vista_padre);
    }
    private void inicializar(VistaGrafica vista_padre){
        setTitle("Ranking historico de ganadores");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//que pasa al cerrar la ventana
        this.vista_padre = vista_padre;
        panel_principal = new JPanel(new BorderLayout());
        volver_comienzo = new JButton("Volver");
        volver_final = new JButton("Volver");
        volver_final.setVisible(false);
        volver_comienzo.setVisible(false);
        panel_botones=new JPanel(new FlowLayout());
        panel_botones.add(volver_comienzo, SwingConstants.CENTER);
        panel_botones.add(volver_final, SwingConstants.CENTER);
        setBounds(100, 100, 500, 500);
        modelo_tabla=new DefaultTableModel();
        tabla=new JTable(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        scroll = new JScrollPane();
        tabla.setModel(modelo_tabla);
        volver_comienzo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volver_comienzo.setVisible(false);
                vista_padre.mostrar_menu_principal();
                setVisible(false);
            }
        });
        volver_final.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volver_final.setVisible(false);
                vista_padre.mostrar_mano_visible();
                setVisible(false);
            }
        });
        scroll.setViewportView(tabla);
        panel_principal.add(scroll, BorderLayout.CENTER);
        panel_principal.add(panel_botones, BorderLayout.SOUTH);
        setContentPane(panel_principal);
        setSize(900, 500);
        setLocationRelativeTo(null);
    }
    public void cargarDatos(Object[][] datosRanking){
        String[] columnas = {"Nombre", "Puntaje ganador","Fecha"};
        modelo_tabla.setDataVector(datosRanking, columnas);

        //tabla.setModel(modelo_tabla);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo_tabla); //ordena la tabla
        tabla.setRowSorter(sorter); //asocia el ordenador a la tabla
        sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(1, SortOrder.ASCENDING))); //manera en la que se ordena automaticamente(por puntaje, de menor a mayor)
        System.out.println("datos ranking:\n");
        int filas = tabla.getRowCount();
        int columnas1 = tabla.getColumnCount();

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas1; j++) {
                System.out.print(tabla.getValueAt(i, j) + " ");
            }
            System.out.println();
        }
    }
    public void mostrar_boton_volver_comienzo(){
        volver_comienzo.setVisible(true);
    }
    public void mostrar_boton_volver_final(){
        volver_final.setVisible(true);
    }
}
