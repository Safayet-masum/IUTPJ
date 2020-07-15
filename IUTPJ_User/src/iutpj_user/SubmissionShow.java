/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iutpj_user;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import newproblem.NewProblem;
import newsubmission.NewSubmission;

/**
 *
 * @author KAWSAR
 */
public class SubmissionShow extends javax.swing.JFrame {

    
    UserSocket usersocket;
    UserDashboard temporary;
    private Object problemID;
    public SubmissionShow(UserSocket usersocket, UserDashboard dashboard) {
        initComponents();

        this.usersocket = usersocket;
        this.temporary = dashboard;
        SubDetailsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 20));
        SubDetailsTable.setRowHeight(25);

        this.setVisible(rootPaneCheckingEnabled);
        
        SubDetailsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1 && !evt.isConsumed()) {
                    evt.consume();
                    int row = SubDetailsTable.rowAtPoint(evt.getPoint());
                    int col = SubDetailsTable.columnAtPoint(evt.getPoint());
                    if (row >= 0 && col == 2) {
                        usersocket.sendData("ProbFile[" + problemID.toString() + "]");
                        NewProblem problem = usersocket.getProblem();
                        try {
                            FileOutputStream fos = new FileOutputStream(problemID.toString() + ".pdf");
                            fos.write(problem.getProb());
                            fos.close();
                        } catch (FileNotFoundException ex) {
                            System.out.println("At probshow problem write Err: " + ex.getMessage());
                        } catch (IOException ex) {
                            System.out.println("At probshow problem write Err: " + ex.getMessage());
                        }
                        ProblemShow problemshow = new ProblemShow(temporary, problem.getProblemName(), problem.getTimeLimit(),problem.getMemoryLimit());
                        problemshow.viewPdf(new File(problemID.toString() + ".pdf"),problemID.toString());
                    }
                }
            }
        });
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SubDetailsScrollPane = new javax.swing.JScrollPane();
        SubDetailsTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        CopyButton = new javax.swing.JButton();
        SourceCodeLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        SourceCodeTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setSize(new java.awt.Dimension(1920, 1440));

        SubDetailsTable.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        SubDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Author", "Problem ID", "Lang", "Verdict", "Time", "Submitted"
            }
        ));
        SubDetailsTable.setFocusable(false);
        SubDetailsTable.setRequestFocusEnabled(false);
        SubDetailsTable.setRowHeight(25);
        SubDetailsTable.setRowSelectionAllowed(false);
        SubDetailsTable.setSelectionBackground(new java.awt.Color(0, 181, 204));
        SubDetailsTable.setShowHorizontalLines(false);
        SubDetailsScrollPane.setViewportView(SubDetailsTable);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        CopyButton.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        CopyButton.setText("Copy");
        CopyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CopyButtonActionPerformed(evt);
            }
        });

        SourceCodeLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        SourceCodeLabel.setForeground(new java.awt.Color(54, 33, 89));
        SourceCodeLabel.setText("Source Code");

        SourceCodeTextArea.setEditable(false);
        SourceCodeTextArea.setBackground(new java.awt.Color(239, 240, 242));
        SourceCodeTextArea.setColumns(20);
        SourceCodeTextArea.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        SourceCodeTextArea.setForeground(new java.awt.Color(0, 51, 51));
        SourceCodeTextArea.setRows(5);
        jScrollPane2.setViewportView(SourceCodeTextArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1260, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(SourceCodeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CopyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SourceCodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CopyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SubDetailsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1280, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(SubDetailsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CopyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CopyButtonActionPerformed
        try {
            SourceCodeTextArea.getToolkit().getSystemClipboard().setContents(new StringSelection(SourceCodeTextArea.getText()), null);
            Image img = Toolkit.getDefaultToolkit().createImage("icon.png");
            SystemTray tray = SystemTray.getSystemTray();
            TrayIcon trayicon = new TrayIcon(img);
            tray.add(trayicon);
            trayicon.displayMessage("Source Code", "Code Copied", TrayIcon.MessageType.INFO);
        } catch (AWTException ex) {
            Logger.getLogger(SubmissionShow.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_CopyButtonActionPerformed

    public void setSubDetailsTable(Object subID, Object author, Object problem, Object lang, Object verdict, Object time, Object submitted, Object PID) {
        problemID = PID;
        Object[][] table = {{subID, author, problem, lang, verdict, time, submitted}};
        Object[] columns = { "#", "Author", "Problem Name", "Lang", "Verdict", "Time", "Submitted"};
        DefaultTableModel tablemodel = new DefaultTableModel(table, columns){

            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
        SubDetailsTable.setModel(tablemodel);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );

        SubDetailsTable.setDefaultRenderer(Object.class, centerRenderer);
        JTableHeader subdetailstableheader = SubDetailsTable.getTableHeader();
        ((DefaultTableCellRenderer)subdetailstableheader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER); 
    }
    
    public void setSourceCode(NewSubmission submission){
        SourceCodeTextArea.setTabSize(4);
        SourceCodeTextArea.setText(new String(submission.getCodeF()));
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CopyButton;
    private javax.swing.JLabel SourceCodeLabel;
    private javax.swing.JTextArea SourceCodeTextArea;
    private javax.swing.JScrollPane SubDetailsScrollPane;
    private javax.swing.JTable SubDetailsTable;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
