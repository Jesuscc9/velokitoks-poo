import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class InterfazGrafica extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public InterfazGrafica() {
        super("Lectura de Tabla MySQL");

        // Configuración de la interfaz gráfica
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configuración del modelo de la tabla
        model = new DefaultTableModel();
        table = new JTable(model);

        // Agregar la tabla a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Configuración del título
        JLabel titleLabel = new JLabel("Tabla de Posts");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        getContentPane().add(titleLabel, BorderLayout.NORTH);

        // Agregar un botón para agregar elementos
        JButton addButton = new JButton("Agregar");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí puedes agregar la lógica para abrir una nueva ventana o realizar
                // acciones al hacer clic en el botón "Agregar"
                // Por ejemplo, puedes abrir un cuadro de diálogo para ingresar nuevos datos.
            }
        });
        getContentPane().add(addButton, BorderLayout.SOUTH);

        // Obtener datos de la base de datos y mostrar en la tabla
        obtenerDatos();

        // Centrar la ventana en la pantalla
        setLocationRelativeTo(null);

        // Mostrar la interfaz gráfica
        setVisible(true);
    }

    private void obtenerDatos() {
        String url = "jdbc:mysql://localhost/fime-posts";
        String usuario = "root";
        String contraseña = "";

        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña)) {
            String query = "SELECT * FROM posts";
            try (Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query)) {

                // Obtener información de las columnas
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Agregar columnas al modelo de la tabla
                for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
                }

                // Agregar filas al modelo de la tabla
                while (resultSet.next()) {
                    Object[] rowData = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        rowData[i - 1] = resultSet.getObject(i);
                    }
                    model.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazGrafica());
    }
}
