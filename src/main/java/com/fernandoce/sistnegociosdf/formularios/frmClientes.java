/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.fernandoce.sistnegociosdf.formularios;

/**
 *
 * @author lfern
 */
public class frmClientes extends javax.swing.JPanel {

    /**
     * Creates new form frmClientes
     */
    public frmClientes() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnNuevo = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        cbBuscar = new javax.swing.JComboBox<>();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        scrollTblUsuarios = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CLIENTES", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14))); // NOI18N
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNuevo.setBackground(new java.awt.Color(51, 153, 255));
        btnNuevo.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnNuevo.setForeground(new java.awt.Color(255, 255, 255));
        btnNuevo.setText("NUEVO");
        add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 120, 40));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Busqueda"));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel2.setText("BUSCAR: ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 90, 30));

        txtBuscar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtBuscar.setBorder(null);
        jPanel1.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 200, 30));

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 200, 5));

        cbBuscar.setBackground(new java.awt.Color(255, 255, 255));
        cbBuscar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cbBuscar.setForeground(new java.awt.Color(0, 0, 0));
        cbBuscar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NOMBRES", "DOCUMENTO" }));
        jPanel1.add(cbBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 150, 30));

        btnBuscar.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(0, 0, 0));
        btnBuscar.setText("BUSCAR");
        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 130, 30));

        btnLimpiar.setBackground(new java.awt.Color(204, 204, 204));
        btnLimpiar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(0, 0, 0));
        btnLimpiar.setText("LIMPIAR");
        jPanel1.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, 130, 30));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1085, 70));

        scrollTblUsuarios.setOpaque(false);

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblClientes.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblClientes.setOpaque(false);
        scrollTblUsuarios.setViewportView(tblClientes);

        add(scrollTblUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 1085, 480));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnBuscar;
    public javax.swing.JButton btnLimpiar;
    public javax.swing.JButton btnNuevo;
    public javax.swing.JComboBox<String> cbBuscar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    public javax.swing.JScrollPane scrollTblUsuarios;
    public javax.swing.JTable tblClientes;
    public javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
