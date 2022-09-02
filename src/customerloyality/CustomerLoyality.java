/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customerloyality;

import java.io.IOException;
import java.sql.SQLException;
import source.Home;

/**
 *
 * @author FAHIM
 */
public class CustomerLoyality {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws IOException, SQLException {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerLoyality.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        //LicenseAccess desboard = new LicenseAccess();
        Home home = new Home();
        home.setVisible(true);
    }

}
