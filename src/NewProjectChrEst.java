/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author JiaYing
 */
public class NewProjectChrEst extends javax.swing.JDialog {

    /**
     * Creates new form NewProjectChrEst
     */
    
    public static int numofchromosomes;
    
    public NewProjectChrEst(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setTitle("No. of chromosomes");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Create1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jCheckBox2 = new javax.swing.JCheckBox();

        Create1.setText("Confirm");
        Create1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Create1ActionPerformed(evt);
            }
        });

        jLabel1.setText("No. of chromosomes:");

        jCheckBox2.setText("Estimate the number of chromosomes this genome has.");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(Create1)))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jCheckBox2)
                .addGap(18, 18, 18)
                .addComponent(Create1)
                .addGap(167, 167, 167))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void Create1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Create1ActionPerformed
        numofchromosomes = Integer.parseInt(jTextField1.getText());
    }//GEN-LAST:event_Create1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
//        //Start Here: Job submission
//        JobManager jobManagerChr = new JobManager(ServerConfigurationInputs.serverURL + "/estimate_chr/" + queryfilename + "/ChrEst.txt");
//        //("http://elvis.misc.cranfield.ac.uk/group2vm/estimate_chr/Ridaeus_Ras1_v1.fasta/ChrEst");
//        //estimate_chr/:input_file/:output_file
//
//        try {
//            // Submit a job and get the job ID
//            List<String> jobInfo = jobManagerChr.submitJob(ServerConfigurationInputs.serverURL + "/estimate_chr/" + queryfilename + "/ChrEst.txt");
//            jobIdChr = jobInfo.get(0);
//            submissionDateChr = jobInfo.get(1);
//            System.out.println("Job submitted with ID: " + jobIdChr);
//            JOptionPane.showMessageDialog(null,
//                "Estimation in process. Please wait.",
//                "Chromosome Estimation",
//                JOptionPane.INFORMATION_MESSAGE);
//            jobStatus = jobManagerChr.getJobStatus(jobIdChr);
//            System.out.println(jobStatus);
//            if (jobStatus.equals("\"FINISHED\"")) {
//                String fileURL = ServerConfigurationInputs.serverURL + "/download/ChrEst.txt";
//                System.out.println(fileURL);
//                String saveDir = newprojectpath;
//                System.out.println(saveDir);
//
//                try {
//                    HttpDownloadUtility.downloadFile(fileURL, saveDir);
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }// End Job Submission

        //        String message = null;
        //
        //        try {
            //            BufferedReader bufReader = new BufferedReader(new FileReader("C:\\Users\\JiaYing\\GP\\Anitra_OUT.txt"));
            //            String line;
            //
            //            ArrayList<String> seqid = new ArrayList<String>();
            //            ArrayList<String> seqlen = new ArrayList<String>();
            //
            //            while ((line = bufReader.readLine()) != null) {
                //                seqid.add(line.split("\t")[0]);
                //                seqlen.add(line.split("\t")[1]);
                //            }
            //            bufReader.close();
            //            message = "Estimated number of chromosomes: " + seqid.size() + "\n Sequence IDs: " + seqid + "\n Sequence lengths: " + seqlen;
            //
            //            //System.out.println(message);
            //            JOptionPane.showMessageDialog(null,
                //                    message,
                //                    "Chromosome Estimation",
                //                    JOptionPane.INFORMATION_MESSAGE);
            //
            //            jTextField1.setText(Integer.toString(seqid.size()));
            //
            //        } catch (IOException ex) {
            //            Logger.getLogger(CreateNewProject.class.getName()).log(Level.SEVERE, null, ex);
            //        }
    }//GEN-LAST:event_jCheckBox2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Create1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
