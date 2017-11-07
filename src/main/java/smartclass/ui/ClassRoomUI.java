/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartclass.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import smartclass.SmartClass;
import smartclass.entities.ContextResponses;
import smartclass.entities.ContextResponsesContainer;
import smartclass.entities.SubscribeResponseContainer;
import smartclass.util.NgsiRequest;
import smartclass.util.NioSocketServer;

public class ClassRoomUI extends javax.swing.JFrame {

    private static ClassRoomUI classRoomUI = new ClassRoomUI();
    public String selectedClass = "";

    /**
     * Creates new form ClassRoomUI
     */
    private ClassRoomUI() {
        initComponents();
        jLabel4.setVisible(false);
        jComboBox1.removeAllItems();
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        ContextResponsesContainer crc = getSalas();
        jComboBox1.addItem("");
        for (ContextResponses contextResponse : crc.getContextResponses()) {
            jComboBox1.addItem(contextResponse.getContextElement().getId());
        }
        NioSocketServer server = new NioSocketServer();
    }

    private boolean presence = false;
    private int time = 0;

    public static synchronized ClassRoomUI getInstance() {
        return classRoomUI;
    }

    public void lightOn() {
        System.out.println("lightOn");
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/sala_luz_ligada.png")));
    }

    public void lightOff() {
        System.out.println("lightOff");
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/sala_luz_desligada.png")));
    }

    public void projectorOn() {
        System.out.println("projectorOn");
        presence = true;
        jLabel4.setVisible(true);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/projetor_ligado.png")));
    }

    public void projectorOff() {
        System.out.println("projectorOff");
        presence = false;
        jLabel4.setVisible(false);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/projetor_desligado.png")));
    }

    public void computerOn() {
        System.out.println("computerOn");
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/computador_ligado.png")));
    }

    public void computerOff() {
        System.out.println("computerOff");
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/computador_desligado.png")));
    }

    public void airOn(int temp) {
        System.out.println("airOn - " + temp + "°C");
        jLabel6.setText(temp + "");
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/ar_ligado.png")));
    }

    public void airOff() {
        System.out.println("airOff");
        jLabel6.setText("");
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/ar_desligado.png")));
    }

    public void airTemp(int temp) {
        if (presence) {
            jLabel6.setText(temp + "");
        } else {
            jLabel6.setText("");
        }
    }

    public void theProfessor(String p) {
        switch (p) {
            case "p1":
                jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/professor_1.png")));
                break;
            case "p2":
                jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/professor_2.png")));
                break;
            case "p3":
                jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/professor_3.png")));
                break;
            case "p4":
                jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/professor_4.png")));
                break;
            default:
                break;
        }
    }

    private ContextResponsesContainer getSalas() {
        NgsiRequest ng = new NgsiRequest();
        String bodyQuery = "{\n"
                + "	\"entities\":[\n"
                + "		{\n"
                + "			\"type\": \"Sala\",\n"
                + "			\"isPattern\": \"true\",\n"
                + "			\"id\": \"SalaInteligente.*\"\n"
                + "		}\n"
                + "	]\n"
                + "}";
        String resposta = ng.sendPost("/v1/queryContext", bodyQuery);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(resposta, ContextResponsesContainer.class);
        } catch (IOException ex) {
            Logger.getLogger(SmartClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if ("".equals(jComboBox1.getSelectedItem().toString()) || jComboBox1.getSelectedItem().toString() == null) {
                System.out.println("Nada selecionado.");
            } else {
                this.selectedClass = jComboBox1.getSelectedItem().toString();
                NgsiRequest ng = new NgsiRequest();
                String bodyQuery = "{\n"
                        + "	\"entities\":[\n"
                        + "		{\n"
                        + "			\"type\": \"Sala\",\n"
                        + "			\"isPattern\": \"true\",\n"
                        + "			\"id\": \"" + jComboBox1.getSelectedItem().toString() + "\"\n"
                        + "		}\n"
                        + "	]\n"
                        + "}";
                String resposta = ng.sendPost("/v1/queryContext", bodyQuery);
                ObjectMapper mapper = new ObjectMapper();
                try {
                    ContextResponsesContainer crc = mapper.readValue(resposta, ContextResponsesContainer.class);
                    System.out.println(crc.getContextResponses()[0].getContextElement().getAttributes()[2].getName());
                    String bodySubs = "{\n"
                            + "    \"entities\": [\n"
                            + "        {\n"
                            + "            \"type\": \"Sala\",\n"
                            + "            \"isPattern\": \"false\",\n"
                            + "            \"id\": \"" + crc.getContextResponses()[0].getContextElement().getId() + "\"\n"
                            + "        }\n"
                            + "    ],\n"
                            + "    \"attributes\": [\n"
                            + "        \"presence\",\n"
                            + "        \"brightness\",\n"
                            + "        \"temperature\",\n"
                            + "        \"time\"\n"
                            + "    ],\n"
                            + "    \"reference\": \"http://"+getIpAddress()+":1026\",\n"
                            + "    \"duration\": \"P1M\",\n"
                            + "    \"notifyConditions\": [\n"
                            + "        {\n"
                            + "            \"type\": \"ONCHANGE\",\n"
                            + "            \"condValues\": [\n"
                            + "                \"presence\",\n"
                            + "                \"brightness\",\n"
                            + "                \"temperature\",\n"
                            + "                \"time\"\n"
                            + "            ]\n"
                            + "        }\n"
                            + "    ],\n"
                            + "    \"throttling\": \"PT5S\"\n"
                            + "}";
                    String resposta2 = ng.sendPost("/v1/subscribeContext", bodySubs);
                    SubscribeResponseContainer sr = mapper.readValue(resposta2, SubscribeResponseContainer.class);

                } catch (IOException ex) {
                    Logger.getLogger(ClassRoomUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static String getIpAddress() {
        URL whatismyip;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            return in.readLine();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ClassRoomUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClassRoomUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 0, 200, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/computador_desligado.png"))); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, 250, 150));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/projetor_desligado.png"))); // NOI18N
        jLabel3.setText("jLabel3");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, -50, 250, 220));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/professor_1.png"))); // NOI18N
        jLabel4.setToolTipText("");
        jLabel4.setName(""); // NOI18N
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 200, -1, -1));

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("23");
        jLabel6.setToolTipText("");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/ar_desligado.png"))); // NOI18N
        jLabel5.setToolTipText("");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 0, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/smartclass/ui/imagens/sala_luz_desligada.png"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(1600, 1163));
        jLabel1.setMinimumSize(new java.awt.Dimension(1600, 1163));
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClassRoomUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClassRoomUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClassRoomUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClassRoomUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
