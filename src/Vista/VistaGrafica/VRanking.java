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
    private JPanel panel;
    private JButton volver;
    private VistaGrafica vista_padre;
    private JScrollPane scroll;

    public VRanking (VistaGrafica vista_padre){
        inicializar(vista_padre);
    }
    private void inicializar(VistaGrafica vista_padre){
        setTitle("Ranking historico de ganadores");
        this.vista_padre = vista_padre;
        panel = new JPanel(new BorderLayout());
        volver = new JButton("Volver");
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
        volver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vista_padre.mostrar_menu_principal();
                setVisible(false);
            }
        });
        scroll.setViewportView(tabla);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(volver, BorderLayout.SOUTH);
        setContentPane(panel);
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
}
