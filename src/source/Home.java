/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author FAHIM
 */
public class Home extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    java.sql.Date date;
    String time;
    int updatekey = 0;
    String customerid = null;
    int visitconfigId = 0;
    int giftvisitid = 0;
    boolean giftServe = false;
    int cardupdatekey = 0;
    boolean cardValidity;

    /**
     * Creates new form Home
     *
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public Home() throws IOException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        intilize();
        currentDate();
        CurrentTime();
        CustomerList();
        CustomerVisitList();
        CustomerSearch();
        GifitList();
        getTotalBirthDay();
        GetBirthdayList();
        GiftList();
        CardNumberList();
        CustomerCardSearch();
        LoginPanel.setVisible(true);
        MainPanel.setVisible(false);
        jDesktopPane1.setBorder(null);
        customersavebtn.setText("Save");
        itembox.setEnabled(false);
        discountbox.setEnabled(false);

        //birthday
        jLabel66.show(false);
        birthdayustomertext.show(false);
        birthdayustomertext.setText(null);

        //gift
        jPanel49.hide();
        giftvisitid = 0;
        homefoodservtext.setText(null);
        Giftdate.setText(null);

    }
//https://www.youtube.com/watch?v=YqUsC0MrK2k

    private void intilize() {

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("company.png")));
    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        currentdateetxt.setDate(date);

    }

    private void CurrentTime() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleformat = new SimpleDateFormat("hh:mm a");
        time = simpleformat.format(cal.getTime());

    }

    private void LoginProcess() throws IOException {
        if (UserName.getText().isEmpty() || Password.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Please Enter User Name/password");
        } else {
            String sql = "Select * from admin where UserName=? and Password=?";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, UserName.getText());
                pst.setString(2, Password.getText());
                rs = pst.executeQuery();
                if (rs.next() == true) {
                    UserName.setText(null);
                    Password.setText(null);
                    LoginPanel.setVisible(false);
                    MainPanel.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(null, "Username and password not Correct");

                }

            } catch (SQLException | HeadlessException e) {

                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    private void CustomerCardSearch() {
        final JTextField textfield = (JTextField) codetext.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        codetext.removeAllItems();
                    } else {
                        combocardFilterItemSeach(textfield.getText());
                    }
                });
            }

        });
    }

    public void combocardFilterItemSeach(String enteredText) {
        codetext.removeAllItems();
        codetext.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;
        try {
            String sql = "Select  cardnumber from loyalty_card_number WHERE lower(cardnumber)  LIKE '" + enteredText + "%' AND status=0 ORDER BY cardnumber ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("cardnumber");
                filterArray.add(str1);
                codetext.addItem(str1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        if (filterArray.size() > 0) {
            codetext.setSelectedItem(enteredText);
            codetext.showPopup();
        } else {
            codetext.hidePopup();

        }
    }

    private void NewCustomer() throws SQLException {
        try {
            String sql = "Insert into customerinfo("
                    + "customerid,"
                    + "customername,"
                    + "address,"
                    + "poboxno,"
                    + "city,"
                    + "country,"
                    + "vatReNo,"
                    + "remark,"
                    + "status,"
                    + "customerType,"
                    + "balanceType,"
                    + "OpeningDate,"
                    + "OpenigBalance,"
                    + "DipositAmt,"
                    + "creditAmnt,"
                    + "cashamt,"
                    + "saleamount,"
                    + "paidamount,"
                    + "totaldiscount,"
                    + "Balancedue,"
                    + "contactname,"
                    + "contactdesignation,"
                    + "TelephoneNo,"
                    + "ContactNo,"
                    + "inputuser,"
                    + "birthday) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) codetext.getSelectedItem());
            pst.setString(2, snametext.getText());
            pst.setString(3, addresstext.getText());
            pst.setString(4, "3100");
            pst.setString(5, "Sylhet");
            pst.setString(6, "Bangladesh");
            pst.setString(7, "");
            pst.setString(8, "");
            pst.setString(9, (String) statusbox.getSelectedItem());
            pst.setString(10, "");
            pst.setString(11, "");
            pst.setDate(12, date);
            pst.setDouble(13, 0);
            pst.setDouble(14, 0);
            pst.setDouble(15, 0);
            pst.setDouble(16, 0);
            pst.setDouble(17, 0);
            pst.setDouble(18, 0);
            pst.setDouble(19, 0);
            pst.setDouble(20, 0);
            pst.setString(21, "");
            pst.setString(22, "");
            pst.setString(23, "");
            pst.setString(24, contactnotext.getText());
            pst.setInt(25, 1);
            pst.setString(26, ((JTextField) birthdaytext.getDateEditor().getUiComponent()).getText());
            pst.execute();
            UpdateCardumber();
            JOptionPane.showMessageDialog(null, "New Customer Data Saved");
            Customerclear();
            CustomerList();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void UpdateCardumber() {
        try {

            String id = (String) codetext.getSelectedItem();

            String sql = "Update loyalty_card_number set status=1 where cardnumber='" + id + "'";
            pst = conn.prepareStatement(sql);
            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private boolean IsValid() {
        String cardNumber = (String) codetext.getSelectedItem();
        if (CardValidityCheck(cardNumber) == false) {
            JOptionPane.showMessageDialog(null, "Customer Code Require");
            return false;
        }

        if (snametext.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Customer Name Require");
            return false;
        }

        if (contactnotext.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Customer Mobile Number Require");
            return false;
        }

        if (((JTextField) birthdaytext.getDateEditor().getUiComponent()).getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Customer Birthday Require");
            return false;
        }

        return true;

    }

    private boolean CardValidityCheck(String cardNumber) {
        try {
            String sql = "Select cardnumber from loyalty_card_number where cardnumber='" + cardNumber + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return false;

    }

    private void CustomerList() throws SQLException {

        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) customerlistTable.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select "
                    + "customerid,"
                    + "customername,"
                    + "address,"
                    + "ContactNo,"
                    + "status,"
                    + "birthday"
                    + " from customerInfo cif";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String id = rs.getString("customerid");
                String name = rs.getString("customername");
                String address = rs.getString("address");
                String mobileno = rs.getString("ContactNo");
                String status = rs.getString("status");
                String birthday = rs.getString("birthday");

                tree++;
                model2.addRow(new Object[]{tree, id, name, address, mobileno, birthday, 12, status});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        try {
            String sql = "Select count(id) as 'totalcustomer' from customerinfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void Customerclear() throws SQLException {
        updatekey = 0;
        codetext.removeAllItems();
        snametext.setText(null);
        addresstext.setText(null);
        contactnotext.setText(null);
        customervisitimetext.setText(null);
        statusbox.setSelectedIndex(0);
        ((JTextField) birthdaytext.getDateEditor().getUiComponent()).setText(null);
        customersavebtn.setText("Save");

        visitcustomertext.setText(null);
        visitcustomeraddress.setText(null);
        visicustomerMobileText.setText(null);
        visitCustomerBirthDaytext.setText(null);
        visitCustomerTotalisit.setText(null);
        customerbox.removeAllItems();
        currentDate();
        if (giftServe == false) {
            homefoodservtext.setText(null);
            Giftdate.setText(null);
            cardnumbertext.setText(null);
            homecusnametext.setText(null);
            homeaddresstext.setText(null);
            homemobiletext.setText(null);
            homebirthdaytext.setText(null);
            birthdayustomertext.setText(null);
        }
        CardNumberList();
    }

    private void CustomerUpdate() throws SQLException {
        try {

            String id = (String) codetext.getSelectedItem();
            String sql = "Update customerInfo set "
                    + "customername='" + snametext.getText() + "',"
                    + "address='" + addresstext.getText() + "',"
                    + "ContactNo='" + contactnotext.getText() + "',"
                    + "status='" + statusbox.getSelectedItem() + "',"
                    + "birthday='" + ((JTextField) birthdaytext.getDateEditor().getUiComponent()).getText() + "'"
                    + " where customerid='" + id + "' ";
            pst = conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Customer Data Update");
            Customerclear();
            CustomerList();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
//Visit

    private void CustomerSearch() {
        final JTextField textfield = (JTextField) customerbox.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        customerbox.removeAllItems();
                    } else {
                        comboFilterItemSeach(textfield.getText());
                    }
                });
            }

        });
    }

    public void comboFilterItemSeach(String enteredText) {
        customerbox.removeAllItems();
        customerbox.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;
        try {
            String sql = "Select  customerid from customerinfo WHERE lower(customerid)  LIKE '" + enteredText + "%' ORDER BY id ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("customerid");
                filterArray.add(str1);
                customerbox.addItem(str1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        if (filterArray.size() > 0) {
            customerbox.setSelectedItem(enteredText);
            customerbox.showPopup();
        } else {
            checkvisibox.hidePopup();
            visitcustomertext.setText(null);
            visitcustomeraddress.setText(null);
            visicustomerMobileText.setText(null);
            visitCustomerBirthDaytext.setText(null);
            visitCustomerTotalisit.setText(null);
        }
    }

    private void NewVisitEntry() throws SQLException {
        try {
            String sql = "Insert into customer_visit("
                    + "customerid,"
                    + "date,"
                    + "Time) values(?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, customerid);
            pst.setString(2, ((JTextField) currentdateetxt.getDateEditor().getUiComponent()).getText());
            pst.setString(3, time);
            pst.execute();
            JOptionPane.showMessageDialog(null, "New Visit Entry Successfuly");
            Customerclear();
            CustomerVisitList();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void GiftServe() {
        int visittime = Integer.parseInt(homevisittimetext.getText());
        try {
            String sql = "Insert into gift("
                    + "customerid,"
                    + "date,"
                    + "Time,"
                    + "giftid,"
                    + "VisitTime,"
                    + "status) values(?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, customerid);
            pst.setString(2, ((JTextField) currentdateetxt.getDateEditor().getUiComponent()).getText());
            pst.setString(3, time);
            pst.setInt(4, giftvisitid);
            pst.setString(5, homevisittimetext.getText());
            pst.setInt(6, 1);
            pst.execute();

            //  Customerclear();
            jButton8.setEnabled(false);
            checkGiftStatus(customerid, visittime, giftvisitid);
            GifitList();
            giftServe = false;
            giftvisitid = 0;

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void CustomerVisitList() throws SQLException {

        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) customervisitTable.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select "
                    + "id,"
                    + "customerid,"
                    + "date,"
                    + "time,"
                    + "(select customername ci from customerInfo ci where ci.customerid =cif.customerid) as 'customername',"
                    + "(select ContactNo ci from customerInfo ci where ci.customerid=cif.customerid) as 'mobile'"
                    + " from customer_visit cif";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String custid = rs.getString("customerid");
                String visitdate = rs.getString("date");
                String visittime = rs.getString("time");
                String name = rs.getString("customername");
                String mobileno = rs.getString("mobile");

                tree++;
                model2.addRow(new Object[]{tree, id, visitdate, visittime, custid, name, mobileno});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        try {
            String sql = "Select count(id) as 'totalvisit' from customer_visit";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void GiftList() {

        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) giftListTable.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select "
                    + "id,"
                    + "customerid,"
                    + "date,"
                    + "time,"
                    + "status,"
                    + "(select customername ci from customerInfo ci where ci.customerid =cif.customerid) as 'customername',"
                    + "(select Food_Serve ci from visitconfig ci where ci.id=cif.giftid) as 'Food_Serve',"
                    + "(select Discount ci from visitconfig ci where ci.id=cif.giftid) as 'Discount',"
                    + "VisitTime as 'visittime'"
                    + " from gift cif";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String sts;
                String giftdate = rs.getString("date");
                String customerno = rs.getString("customerid");
                String name = rs.getString("customername");
                String visittime = rs.getString("visittime");
                String Food_Serve = rs.getString("Food_Serve");
                String Discount = rs.getString("Discount");
                int status = rs.getInt("status");
                if (status == 1) {
                    sts = "Complited";
                } else {
                    sts = "waiting";
                }
                tree++;
                model2.addRow(new Object[]{tree, giftdate, customerno, name, visittime, Food_Serve, Discount, sts});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        try {
            String sql = "Select count(id) as 'totalvisit' from customer_visit";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private int CustomerTotalVisit(String CusId) {

        try {
            String sql = "Select count(id) as 'totalvisit' from customer_visit where customerid='" + CusId + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                int totalvisit = rs.getInt("totalvisit");

                return totalvisit;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return 0;

    }
//visi Config

    private void NewVisitConfig() throws SQLException {
        String VisitTime = visitimetext.getText();
        if (CheckVisitGift(VisitTime)) {
            JOptionPane.showMessageDialog(null, "This Gift Config already exist");
        } else {

            String Food_Serve = "";
            String Discount = "";
            if (foodservicecheckbox.isSelected() == true) {
                Food_Serve = (String) itembox.getSelectedItem();

            } else {
                Food_Serve = "";
            }
            if (checkdiscountbox.isSelected() == true) {

                Discount = discountbox.getText();
            } else {
                Discount = "";
            }
            try {
                String sql = "Insert into visitconfig("
                        + "visittime,"
                        + "Food_Serve,"
                        + "Discount) values(?,?,?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, visitimetext.getText());
                pst.setString(2, Food_Serve);
                pst.setString(3, Discount);
                pst.execute();
                JOptionPane.showMessageDialog(null, "New Gift Config Save Successfuly");
                visitimetext.setText(null);
                foodservicecheckbox.setSelected(false);
                itembox.removeAllItems();
                itembox.setEnabled(false);
                checkdiscountbox.setSelected(false);
                discountbox.setEnabled(false);
                discountbox.setText(null);
                visitconfigId = 0;
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            GifitList();
        }

    }

    private boolean CheckVisitGift(String Visittime) {

        try {
            String sql = "Select id from visitconfig where visittime='" + Visittime + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        return true;
    }

    private void GifitList() throws SQLException {
        checkvisibox.removeAllItems();
        checkvisibox.addItem("Select");
        checkvisibox.setSelectedIndex(0);
        try {
            String sql = "Select visittime from visitconfig";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String name = rs.getString("visittime");
                checkvisibox.addItem(name);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void getVisitConfig(String visitTime) {

        try {
            String sql = "Select id,visittime,Food_Serve,Discount from visitconfig where visittime='" + visitTime + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                visitconfigId = rs.getInt("id");
                String vtime = rs.getString("visittime");
                visitimetext.setText(vtime);
                String Food_Serve = rs.getString("Food_Serve");
                String Discount = rs.getString("Discount");

                if ("".equals(Food_Serve)) {
                    foodservicecheckbox.setSelected(true);
                    itembox.setEnabled(true);
                    ItemSearch();
                    itembox.setSelectedItem(Food_Serve);
                } else {
                    foodservicecheckbox.setSelected(false);
                    itembox.setEnabled(false);
                    itembox.removeAllItems();
                }
                if ("".equals(Discount)) {
                    checkdiscountbox.setSelected(true);
                    discountbox.setEnabled(true);
                    discountbox.setText(Discount);
                } else {
                    checkdiscountbox.setSelected(false);
                    discountbox.setEnabled(false);
                    discountbox.setText(null);
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void UpdateVisitConfig() throws SQLException {
        String VisitTime = visitimetext.getText();
        String Food_Serve = "";
        String Discount = "";
        if (foodservicecheckbox.isSelected() == true) {
            Food_Serve = (String) itembox.getSelectedItem();

        } else {
            Food_Serve = "";
        }
        if (checkdiscountbox.isSelected() == true) {

            Discount = discountbox.getText();
        } else {
            Discount = "";
        }
        try {
            String sql = "Update visitconfig set "
                    + "visittime='" + VisitTime + "',"
                    + "Food_Serve='" + Food_Serve + "',"
                    + "Discount='" + Discount + "'"
                    + " where id='" + visitconfigId + "' ";

            pst = conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Gift Config Update Successfuly");
            visitimetext.setText(null);
            foodservicecheckbox.setSelected(false);
            itembox.removeAllItems();
            itembox.setEnabled(false);
            checkdiscountbox.setSelected(false);
            discountbox.setEnabled(false);
            discountbox.setText(null);
            visitconfigId = 0;
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        GifitList();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel42 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jPanel66 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jPanel68 = new javax.swing.JPanel();
        totalbtext = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jPanel73 = new javax.swing.JPanel();
        jLabel81 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        Dashboard = new javax.swing.JInternalFrame();
        jPanel67 = new javax.swing.JPanel();
        jPanel69 = new javax.swing.JPanel();
        jLabel79 = new javax.swing.JLabel();
        Layalitycheck = new javax.swing.JInternalFrame();
        jPanel7 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        visicustomerMobileText = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        visitcustomeraddress = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        visitCustomerTotalisit = new javax.swing.JTextField();
        jPanel36 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        visitCustomerBirthDaytext = new javax.swing.JTextField();
        jPanel37 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        currentdateetxt = new com.toedter.calendar.JDateChooser();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel47 = new javax.swing.JPanel();
        customerbox = new javax.swing.JComboBox<>();
        jLabel59 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        visitcustomertext = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        visitbtn = new javax.swing.JButton();
        jPanel49 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        homevisittimetext = new javax.swing.JLabel();
        jPanel52 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        homefoodservtext = new javax.swing.JLabel();
        jPanel53 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        Giftdate = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jPanel64 = new javax.swing.JPanel();
        jLabel72 = new javax.swing.JLabel();
        homeDiscounttext = new javax.swing.JLabel();
        jPanel65 = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        giftstatus = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jPanel48 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jPanel56 = new javax.swing.JPanel();
        birthdaypanel = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        birthdayustomertext = new javax.swing.JLabel();
        jPanel58 = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        cardnumbertext = new javax.swing.JLabel();
        jPanel59 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        homecusnametext = new javax.swing.JLabel();
        jPanel60 = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        homeaddresstext = new javax.swing.JLabel();
        jPanel61 = new javax.swing.JPanel();
        jLabel70 = new javax.swing.JLabel();
        homemobiletext = new javax.swing.JLabel();
        jPanel62 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        homebirthdaytext = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        loaylty = new javax.swing.JInternalFrame();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel63 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        giftListTable = new javax.swing.JTable();
        customer = new javax.swing.JInternalFrame();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        snametext = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        codetext = new javax.swing.JComboBox<>();
        jPanel15 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        statusbox = new javax.swing.JComboBox<>();
        jPanel16 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        customersavebtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        addresstext = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        birthdaytext = new com.toedter.calendar.JDateChooser();
        jPanel32 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        customervisitimetext = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        contactnotext1 = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        contactnotext = new javax.swing.JTextField();
        jPanel35 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        contactnotext3 = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        customerlistTable = new javax.swing.JTable();
        visitFrame = new javax.swing.JInternalFrame();
        jPanel22 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jPanel39 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        customervisitTable = new javax.swing.JTable();
        config = new javax.swing.JInternalFrame();
        jPanel40 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        checkdiscountbox = new javax.swing.JCheckBox();
        jLabel56 = new javax.swing.JLabel();
        itembox = new javax.swing.JComboBox<>();
        foodservicecheckbox = new javax.swing.JCheckBox();
        jLabel57 = new javax.swing.JLabel();
        discountbox = new javax.swing.JTextField();
        jPanel44 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel45 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        visitimetext = new javax.swing.JTextField();
        jPanel46 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        customersavebtn1 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        checkvisibox = new javax.swing.JComboBox<>();
        birthday = new javax.swing.JInternalFrame();
        jPanel54 = new javax.swing.JPanel();
        jPanel55 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jPanel57 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        birthdaylistTable = new javax.swing.JTable();
        CardNumberEntry = new javax.swing.JInternalFrame();
        jPanel70 = new javax.swing.JPanel();
        jPanel71 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jPanel72 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        jPanel74 = new javax.swing.JPanel();
        jLabel82 = new javax.swing.JLabel();
        loyaltycardnumbertext = new javax.swing.JTextField();
        jPanel76 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        customersavebtn2 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel75 = new javax.swing.JPanel();
        jLabel85 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jPanel77 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        cardnumberTable = new javax.swing.JTable();
        LoginPanel = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        UserName = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        Password = new javax.swing.JPasswordField();
        jButton3 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Customer Loyalty");
        setResizable(false);

        MainPanel.setBackground(new java.awt.Color(255, 255, 255));
        MainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(51, 0, 51));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 0, 40));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel2MouseEntered(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(102, 0, 51));
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 10, 60));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Gift LIst");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 100, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/source/images/loylty.png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));

        jPanel4.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 160, 60));

        jPanel3.setBackground(new java.awt.Color(51, 0, 40));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel3MouseEntered(evt);
            }
        });
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setBackground(new java.awt.Color(102, 0, 51));
        jLabel3.setOpaque(true);
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 10, 60));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Home");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 100, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/source/images/home.png"))); // NOI18N
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));

        jPanel4.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 160, 60));

        jPanel5.setBackground(new java.awt.Color(51, 0, 40));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel5MouseEntered(evt);
            }
        });
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setBackground(new java.awt.Color(102, 0, 51));
        jPanel5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 10, 60));

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Logout");
        jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 100, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/source/images/logout.png"))); // NOI18N
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, 40));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 560, 160, 60));

        jPanel6.setBackground(new java.awt.Color(51, 0, 40));
        jPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel6MouseEntered(evt);
            }
        });
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setBackground(new java.awt.Color(102, 0, 51));
        jPanel6.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 10, 60));

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Customer");
        jPanel6.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 100, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/source/images/customer.png"))); // NOI18N
        jPanel6.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));

        jPanel4.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 160, 60));

        jLabel13.setFont(new java.awt.Font("sansserif", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Customer Loyalty");
        jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 20, -1, -1));

        jLabel14.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Version 1.0.1");
        jPanel4.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 45, -1, -1));

        jPanel21.setBackground(new java.awt.Color(51, 0, 40));
        jPanel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel21MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel21MouseEntered(evt);
            }
        });
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setBackground(new java.awt.Color(102, 0, 51));
        jPanel21.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 10, 60));

        jLabel28.setBackground(new java.awt.Color(255, 255, 255));
        jLabel28.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Config");
        jPanel21.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 100, -1));

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/source/images/config.png"))); // NOI18N
        jPanel21.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));

        jPanel4.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 160, 60));

        jPanel42.setBackground(new java.awt.Color(51, 0, 40));
        jPanel42.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel42MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel42MouseEntered(evt);
            }
        });
        jPanel42.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel51.setBackground(new java.awt.Color(102, 0, 51));
        jPanel42.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 10, 60));

        jLabel52.setBackground(new java.awt.Color(255, 255, 255));
        jLabel52.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setText("Visit");
        jPanel42.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 100, -1));

        jLabel53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/source/images/visit.png"))); // NOI18N
        jPanel42.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));

        jPanel4.add(jPanel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 160, 60));

        jPanel66.setBackground(new java.awt.Color(51, 0, 40));
        jPanel66.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel66MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel66MouseEntered(evt);
            }
        });
        jPanel66.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel75.setBackground(new java.awt.Color(102, 0, 51));
        jPanel66.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 10, 60));

        jLabel76.setBackground(new java.awt.Color(255, 255, 255));
        jLabel76.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(255, 255, 255));
        jLabel76.setText("Loyality");
        jPanel66.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 100, -1));

        jLabel77.setIcon(new javax.swing.ImageIcon(getClass().getResource("/source/images/loay1.png"))); // NOI18N
        jPanel66.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, -1));

        jPanel4.add(jPanel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 160, 60));

        jPanel68.setBackground(new java.awt.Color(153, 0, 51));
        jPanel68.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel68.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel68MouseClicked(evt);
            }
        });
        jPanel68.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        totalbtext.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        totalbtext.setForeground(new java.awt.Color(255, 255, 255));
        totalbtext.setText("10");
        jPanel68.add(totalbtext, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 50, 30));

        jLabel78.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(255, 255, 255));
        jLabel78.setText("Today Birthday:");
        jPanel68.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 90, 30));

        jPanel4.add(jPanel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 160, 30));

        jPanel73.setBackground(new java.awt.Color(51, 0, 40));
        jPanel73.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel73MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel73MouseEntered(evt);
            }
        });
        jPanel73.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel81.setBackground(new java.awt.Color(102, 0, 51));
        jPanel73.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 10, 60));

        jLabel83.setBackground(new java.awt.Color(255, 255, 255));
        jLabel83.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(255, 255, 255));
        jLabel83.setText("Card setting");
        jPanel73.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 25, 100, -1));

        jLabel84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/source/images/card.png"))); // NOI18N
        jPanel73.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, -1, 40));

        jPanel4.add(jPanel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 160, -1));

        MainPanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 160, 690));

        jDesktopPane1.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPane1.setBorder(null);
        jDesktopPane1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Dashboard.setBackground(new java.awt.Color(255, 255, 255));
        Dashboard.setBorder(null);
        Dashboard.setVisible(true);
        Dashboard.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel67.setBackground(new java.awt.Color(255, 255, 255));
        jPanel67.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel69.setBackground(new java.awt.Color(255, 255, 255));
        jPanel69.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel79.setIcon(new javax.swing.ImageIcon(getClass().getResource("/source/images/logo-deshboard.png"))); // NOI18N
        jPanel69.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 60, 320, 390));

        jPanel67.add(jPanel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 890, 600));

        Dashboard.getContentPane().add(jPanel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 890, 710));

        jDesktopPane1.add(Dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, -40, 920, 740));

        Layalitycheck.setBackground(new java.awt.Color(255, 255, 255));
        Layalitycheck.setBorder(null);
        Layalitycheck.setVisible(true);
        Layalitycheck.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel39.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(51, 0, 51));
        jLabel39.setText("Moible");
        jPanel28.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        visicustomerMobileText.setEditable(false);
        visicustomerMobileText.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        visicustomerMobileText.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 51), 1, true));
        visicustomerMobileText.setOpaque(true);
        jPanel28.add(visicustomerMobileText, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 240, 40));

        jPanel25.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 260, 80));

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel40.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(51, 0, 51));
        jLabel40.setText("Address");
        jPanel30.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        visitcustomeraddress.setEditable(false);
        visitcustomeraddress.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        visitcustomeraddress.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 51), 1, true));
        visitcustomeraddress.setOpaque(true);
        jPanel30.add(visitcustomeraddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 240, 40));

        jPanel25.add(jPanel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 260, 80));

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));
        jPanel31.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel41.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(51, 0, 51));
        jLabel41.setText("Total Visit");
        jPanel31.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        visitCustomerTotalisit.setEditable(false);
        visitCustomerTotalisit.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        visitCustomerTotalisit.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 51), 1, true));
        visitCustomerTotalisit.setOpaque(true);
        jPanel31.add(visitCustomerTotalisit, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 240, 40));

        jPanel25.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 260, 80));

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));
        jPanel36.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel46.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(51, 0, 51));
        jLabel46.setText("Birth Day");
        jPanel36.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        visitCustomerBirthDaytext.setEditable(false);
        visitCustomerBirthDaytext.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        visitCustomerBirthDaytext.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 51), 1, true));
        visitCustomerBirthDaytext.setOpaque(true);
        jPanel36.add(visitCustomerBirthDaytext, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 240, 40));

        jPanel25.add(jPanel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 260, 80));

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel47.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(51, 0, 51));
        jLabel47.setText("Current Date");
        jPanel37.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        currentdateetxt.setBackground(new java.awt.Color(255, 255, 255));
        currentdateetxt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 51), 1, true));
        currentdateetxt.setDateFormatString("yyyy-MM-dd");
        jPanel37.add(currentdateetxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 30, 242, 45));

        jPanel25.add(jPanel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 260, 80));
        jPanel25.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 450, 280, -1));

        jPanel47.setBackground(new java.awt.Color(255, 255, 255));
        jPanel47.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        customerbox.setBackground(new java.awt.Color(255, 255, 255));
        customerbox.setEditable(true);
        customerbox.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        customerbox.setBorder(null);
        customerbox.setOpaque(true);
        customerbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                customerboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        jPanel47.add(customerbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 30, 250, 40));

        jLabel59.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(51, 0, 51));
        jLabel59.setText("Loyalty Card Number");
        jPanel47.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        jPanel25.add(jPanel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 260, 70));

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel37.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(51, 0, 51));
        jLabel37.setText("Customer Name");
        jPanel26.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        visitcustomertext.setEditable(false);
        visitcustomertext.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        visitcustomertext.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 51), 1, true));
        visitcustomertext.setOpaque(true);
        jPanel26.add(visitcustomertext, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 240, 40));

        jPanel25.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 260, 80));

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        visitbtn.setBackground(new java.awt.Color(51, 0, 51));
        visitbtn.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        visitbtn.setForeground(new java.awt.Color(255, 255, 255));
        visitbtn.setText("Visit");
        visitbtn.setBorder(null);
        visitbtn.setOpaque(true);
        visitbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visitbtnActionPerformed(evt);
            }
        });
        jPanel29.add(visitbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 240, 50));

        jPanel25.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, 240, 70));

        jPanel7.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 280, 620));

        jPanel49.setBackground(new java.awt.Color(255, 255, 255));
        jPanel49.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 51)));
        jPanel49.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel51.setBackground(new java.awt.Color(255, 255, 255));
        jPanel51.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 0, 51));
        jLabel16.setText("Total Visit Time:");
        jPanel51.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        homevisittimetext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel51.add(homevisittimetext, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 320, 27));

        jPanel49.add(jPanel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 550, -1));

        jPanel52.setBackground(new java.awt.Color(255, 255, 255));
        jPanel52.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel62.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(51, 0, 51));
        jLabel62.setText("Food Serve:");
        jPanel52.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        homefoodservtext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel52.add(homefoodservtext, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 320, 27));

        jPanel49.add(jPanel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 550, -1));

        jPanel53.setBackground(new java.awt.Color(255, 255, 255));
        jPanel53.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel63.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(51, 0, 51));
        jLabel63.setText("Gift Date:");
        jPanel53.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        Giftdate.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel53.add(Giftdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 320, 27));

        jPanel49.add(jPanel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 550, -1));

        jLabel64.setBackground(new java.awt.Color(51, 0, 51));
        jLabel64.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 255, 255));
        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel64.setText("Gift");
        jLabel64.setOpaque(true);
        jPanel49.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 30));

        jPanel64.setBackground(new java.awt.Color(255, 255, 255));
        jPanel64.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel72.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(51, 0, 51));
        jLabel72.setText("Descount:");
        jPanel64.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        homeDiscounttext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel64.add(homeDiscounttext, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 320, 27));

        jPanel49.add(jPanel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 550, -1));

        jPanel65.setBackground(new java.awt.Color(255, 255, 255));
        jPanel65.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel74.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(51, 0, 51));
        jLabel74.setText("Status:");
        jPanel65.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        giftstatus.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel65.add(giftstatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 320, 27));

        jPanel49.add(jPanel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 550, -1));

        jButton8.setBackground(new java.awt.Color(102, 0, 102));
        jButton8.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Serve");
        jButton8.setOpaque(true);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel49.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(407, 230, 150, 40));

        jPanel7.add(jPanel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 340, 570, 280));

        jPanel48.setBackground(new java.awt.Color(51, 0, 51));
        jPanel48.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel60.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setText("Loyalty Check");
        jPanel48.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jPanel7.add(jPanel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 12, 890, 60));

        jPanel56.setBackground(new java.awt.Color(255, 255, 255));
        jPanel56.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 51)));
        jPanel56.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        birthdaypanel.setBackground(new java.awt.Color(255, 255, 255));
        birthdaypanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel66.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(255, 51, 51));
        jLabel66.setText("Happy Birthday");
        birthdaypanel.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 190, 50));

        birthdayustomertext.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        birthdayustomertext.setForeground(new java.awt.Color(255, 51, 51));
        birthdaypanel.add(birthdayustomertext, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 320, 50));

        jPanel56.add(birthdaypanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 550, 50));

        jPanel58.setBackground(new java.awt.Color(255, 255, 255));
        jPanel58.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel67.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(51, 0, 51));
        jLabel67.setText("Customer Card Number:");
        jPanel58.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        cardnumbertext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel58.add(cardnumbertext, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 320, 28));

        jPanel56.add(jPanel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 550, -1));

        jPanel59.setBackground(new java.awt.Color(255, 255, 255));
        jPanel59.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel68.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(51, 0, 51));
        jLabel68.setText("Customer Name:");
        jPanel59.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        homecusnametext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel59.add(homecusnametext, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 320, 28));

        jPanel56.add(jPanel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 550, -1));

        jPanel60.setBackground(new java.awt.Color(255, 255, 255));
        jPanel60.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel69.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(51, 0, 51));
        jLabel69.setText("Address:");
        jPanel60.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        homeaddresstext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel60.add(homeaddresstext, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 320, 28));

        jPanel56.add(jPanel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 550, -1));

        jPanel61.setBackground(new java.awt.Color(255, 255, 255));
        jPanel61.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel70.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(51, 0, 51));
        jLabel70.setText("Mobile:");
        jPanel61.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        homemobiletext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel61.add(homemobiletext, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 320, 28));

        jPanel56.add(jPanel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 550, -1));

        jPanel62.setBackground(new java.awt.Color(255, 255, 255));
        jPanel62.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel71.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(51, 0, 51));
        jLabel71.setText("Birthday:");
        jPanel62.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        homebirthdaytext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel62.add(homebirthdaytext, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 320, 28));

        jPanel56.add(jPanel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 550, -1));

        jLabel15.setBackground(new java.awt.Color(51, 0, 51));
        jLabel15.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Customer Information");
        jLabel15.setOpaque(true);
        jPanel56.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 30));

        jPanel7.add(jPanel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, 570, 250));

        Layalitycheck.getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 880, 710));

        jDesktopPane1.add(Layalitycheck, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, -40, 910, 750));

        loaylty.setBackground(new java.awt.Color(255, 255, 255));
        loaylty.setBorder(null);
        loaylty.setVisible(true);
        loaylty.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(51, 0, 51));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Gift List");
        jPanel9.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel18.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(204, 204, 204));
        jLabel18.setText("Home/Gift List");
        jPanel9.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 20, -1, -1));

        jPanel8.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 12, 890, 60));

        jPanel63.setBackground(new java.awt.Color(255, 255, 255));
        jPanel63.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane4.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane4.setBorder(null);

        giftListTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        giftListTable.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        giftListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "Date", "Customer No", "Name", "Visit Time", "Food Serve", "Discount", "Status"
            }
        ));
        giftListTable.setGridColor(new java.awt.Color(255, 255, 255));
        giftListTable.setOpaque(false);
        giftListTable.setRowHeight(25);
        giftListTable.setShowHorizontalLines(true);
        giftListTable.setShowVerticalLines(true);
        giftListTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                giftListTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(giftListTable);
        if (giftListTable.getColumnModel().getColumnCount() > 0) {
            giftListTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        }

        jPanel63.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 850, 580));

        jPanel8.add(jPanel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 890, 600));

        loaylty.getContentPane().add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 890, 700));

        jDesktopPane1.add(loaylty, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, -40, 920, 740));

        customer.setBackground(new java.awt.Color(255, 255, 255));
        customer.setBorder(null);
        customer.setVisible(true);
        customer.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(new java.awt.Color(51, 0, 51));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Customer Information");
        jPanel11.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel20.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(204, 204, 204));
        jLabel20.setText("Home/Customer Information");
        jPanel11.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, -1, -1));

        jPanel10.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 12, 910, 60));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 0, 51));
        jLabel21.setText("Customer Name");
        jPanel13.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        snametext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 51)));
        snametext.setOpaque(true);
        jPanel13.add(snametext, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 240, 40));

        jPanel12.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 95, 260, 70));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 0, 51));
        jLabel22.setText("Loyalty Card Number");
        jPanel14.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        codetext.setBackground(new java.awt.Color(255, 255, 255));
        codetext.setEditable(true);
        codetext.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        codetext.setBorder(null);
        codetext.setOpaque(true);
        codetext.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                codetextPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        jPanel14.add(codetext, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 30, 250, 40));

        jPanel12.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 260, 80));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 0, 51));
        jLabel23.setText("Status");
        jPanel15.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        statusbox.setBackground(new java.awt.Color(255, 255, 255));
        statusbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Inactive" }));
        statusbox.setOpaque(true);
        jPanel15.add(statusbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 35, 240, 40));

        jPanel12.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 325, 260, 80));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setBackground(new java.awt.Color(102, 0, 0));
        jButton1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Delete");
        jButton1.setBorder(null);
        jButton1.setOpaque(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel16.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 80, 50));

        customersavebtn.setBackground(new java.awt.Color(51, 0, 51));
        customersavebtn.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        customersavebtn.setForeground(new java.awt.Color(255, 255, 255));
        customersavebtn.setText("Save");
        customersavebtn.setBorder(null);
        customersavebtn.setOpaque(true);
        customersavebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customersavebtnActionPerformed(evt);
            }
        });
        jPanel16.add(customersavebtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 80, 50));

        jButton2.setBackground(new java.awt.Color(0, 51, 51));
        jButton2.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Reset");
        jButton2.setOpaque(true);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel16.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, -1, 50));

        jPanel12.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 570, 260, 70));

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(51, 0, 51));
        jLabel25.setText("Address");
        jPanel17.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        addresstext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 51)));
        addresstext.setOpaque(true);
        jPanel17.add(addresstext, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 240, 40));

        jPanel12.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 260, 70));

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel26.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(51, 0, 51));
        jLabel26.setText("Birth Day");
        jPanel18.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        birthdaytext.setBackground(new java.awt.Color(255, 255, 255));
        birthdaytext.setBorder(null);
        birthdaytext.setDateFormatString("yyyy-MM-dd");
        jPanel18.add(birthdaytext, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 30, 242, 45));

        jPanel12.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 405, 260, 80));

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel42.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(51, 0, 51));
        jLabel42.setText("Total Visit");
        jPanel32.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        customervisitimetext.setEditable(false);
        customervisitimetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 51)));
        customervisitimetext.setOpaque(true);
        jPanel32.add(customervisitimetext, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 240, 40));

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));
        jPanel33.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel43.setText("Moible");
        jPanel33.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        contactnotext1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        contactnotext1.setOpaque(true);
        jPanel33.add(contactnotext1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 240, 40));

        jPanel32.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 260, 80));

        jPanel12.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 260, 70));

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));
        jPanel34.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel44.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(51, 0, 51));
        jLabel44.setText("Moible");
        jPanel34.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        contactnotext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 51)));
        contactnotext.setOpaque(true);
        jPanel34.add(contactnotext, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 240, 40));

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));
        jPanel35.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel45.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel45.setText("Moible");
        jPanel35.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        contactnotext3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        contactnotext3.setOpaque(true);
        jPanel35.add(contactnotext3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 240, 40));

        jPanel34.add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 260, 80));

        jPanel12.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 260, 70));

        jPanel10.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 280, 650));

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel24.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel24.setText("Search");
        jPanel20.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, -1, -1));

        jTextField4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel20.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 260, 40));

        jPanel10.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 70, 593, 60));

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);

        customerlistTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        customerlistTable.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        customerlistTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "No", "Name", "Address", "Mobile", "Birth Day", "Visit Time", "Status"
            }
        ));
        customerlistTable.setGridColor(new java.awt.Color(255, 255, 255));
        customerlistTable.setOpaque(false);
        customerlistTable.setRowHeight(25);
        customerlistTable.setShowHorizontalLines(true);
        customerlistTable.setShowVerticalLines(true);
        customerlistTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customerlistTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(customerlistTable);
        if (customerlistTable.getColumnModel().getColumnCount() > 0) {
            customerlistTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            customerlistTable.getColumnModel().getColumn(3).setHeaderValue("");
            customerlistTable.getColumnModel().getColumn(4).setHeaderValue("Mobile");
            customerlistTable.getColumnModel().getColumn(5).setHeaderValue("Birth Day");
            customerlistTable.getColumnModel().getColumn(6).setHeaderValue("Visit Time");
        }

        jPanel19.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 593, 580));

        jPanel10.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 120, 610, 600));

        customer.getContentPane().add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 890, 710));

        jDesktopPane1.add(customer, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, -40, 910, 740));

        visitFrame.setBackground(new java.awt.Color(255, 255, 255));
        visitFrame.setBorder(null);
        visitFrame.setVisible(true);
        visitFrame.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel24.setBackground(new java.awt.Color(51, 0, 51));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel35.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Customer Visit");
        jPanel24.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel36.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(204, 204, 204));
        jLabel36.setText("Home/Customeer Visit");
        jPanel24.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 20, -1, -1));

        jPanel22.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 12, 890, 60));

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));
        jPanel38.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel38.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel48.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel48.setText("Search");
        jPanel38.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, -1, -1));

        jTextField5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel38.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 260, 40));

        jPanel22.add(jPanel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 70, 870, 60));

        jPanel39.setBackground(new java.awt.Color(255, 255, 255));
        jPanel39.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(null);

        customervisitTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        customervisitTable.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        customervisitTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "Visit Id", "Date", "Time", "Customer No", "Name", "Mobile"
            }
        ));
        customervisitTable.setGridColor(new java.awt.Color(255, 255, 255));
        customervisitTable.setOpaque(false);
        customervisitTable.setRowHeight(25);
        customervisitTable.setShowHorizontalLines(true);
        customervisitTable.setShowVerticalLines(true);
        customervisitTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customervisitTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(customervisitTable);
        if (customervisitTable.getColumnModel().getColumnCount() > 0) {
            customervisitTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        }

        jPanel39.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 870, 580));

        jPanel22.add(jPanel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 890, 600));

        visitFrame.getContentPane().add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 890, 720));

        jDesktopPane1.add(visitFrame, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, -40, 910, 740));

        config.setBackground(new java.awt.Color(255, 255, 255));
        config.setBorder(null);
        config.setVisible(true);
        config.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel40.setBackground(new java.awt.Color(255, 255, 255));
        jPanel40.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel41.setBackground(new java.awt.Color(51, 0, 51));
        jPanel41.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel49.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("Gift Configuration");
        jPanel41.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel50.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(204, 204, 204));
        jLabel50.setText("Home/configuration");
        jPanel41.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 20, -1, -1));

        jPanel40.add(jPanel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 12, 890, 60));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 102)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel43.setBackground(new java.awt.Color(255, 255, 255));
        jPanel43.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel54.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(51, 0, 51));
        jLabel54.setText("Gift Description");
        jPanel43.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 280, -1));

        checkdiscountbox.setText("Discount");
        checkdiscountbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkdiscountboxMouseClicked(evt);
            }
        });
        checkdiscountbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkdiscountboxActionPerformed(evt);
            }
        });
        jPanel43.add(checkdiscountbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));

        jLabel56.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(51, 0, 51));
        jLabel56.setText("Choice Item ");
        jPanel43.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        itembox.setBackground(new java.awt.Color(255, 255, 255));
        itembox.setEditable(true);
        itembox.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        itembox.setBorder(null);
        itembox.setOpaque(true);
        itembox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        jPanel43.add(itembox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 280, 40));

        foodservicecheckbox.setText("Food Serve");
        foodservicecheckbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                foodservicecheckboxMouseClicked(evt);
            }
        });
        foodservicecheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foodservicecheckboxActionPerformed(evt);
            }
        });
        jPanel43.add(foodservicecheckbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel57.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(51, 0, 51));
        jLabel57.setText("Discount Amount");
        jPanel43.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, -1));

        discountbox.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        discountbox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 51)));
        discountbox.setOpaque(true);
        jPanel43.add(discountbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 200, 40));

        jPanel1.add(jPanel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 300, 270));

        jPanel44.setBackground(new java.awt.Color(255, 255, 255));
        jPanel44.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel55.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(51, 0, 51));
        jLabel55.setText("new Config");
        jPanel44.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 2, -1, -1));

        jSeparator3.setBackground(new java.awt.Color(51, 0, 51));
        jSeparator3.setForeground(new java.awt.Color(102, 0, 102));
        jPanel44.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 25, 336, -1));

        jPanel1.add(jPanel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 3, 335, 30));

        jPanel45.setBackground(new java.awt.Color(255, 255, 255));
        jPanel45.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel58.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(51, 0, 51));
        jLabel58.setText("Visit Time");
        jPanel45.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, -1, -1));

        visitimetext.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        visitimetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 51)));
        visitimetext.setOpaque(true);
        jPanel45.add(visitimetext, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, 40));

        jPanel1.add(jPanel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 300, 90));

        jPanel46.setBackground(new java.awt.Color(255, 255, 255));
        jPanel46.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton4.setBackground(new java.awt.Color(102, 0, 0));
        jButton4.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Delete");
        jButton4.setBorder(null);
        jButton4.setOpaque(true);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel46.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 15, 80, 50));

        customersavebtn1.setBackground(new java.awt.Color(51, 0, 51));
        customersavebtn1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        customersavebtn1.setForeground(new java.awt.Color(255, 255, 255));
        customersavebtn1.setText("Save");
        customersavebtn1.setBorder(null);
        customersavebtn1.setOpaque(true);
        customersavebtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customersavebtn1ActionPerformed(evt);
            }
        });
        jPanel46.add(customersavebtn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 15, 80, 50));

        jButton6.setBackground(new java.awt.Color(0, 51, 51));
        jButton6.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Reset");
        jButton6.setOpaque(true);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel46.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 15, -1, 50));

        jPanel1.add(jPanel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 300, 70));

        jPanel40.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 200, 340, 480));

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 102)));
        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel38.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(51, 0, 51));
        jLabel38.setText("Check Visit");
        jPanel27.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 5, -1, -1));

        checkvisibox.setBackground(new java.awt.Color(255, 255, 255));
        checkvisibox.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        checkvisibox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        checkvisibox.setBorder(null);
        checkvisibox.setOpaque(true);
        checkvisibox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                checkvisiboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        jPanel27.add(checkvisibox, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 30, 240, 40));

        jPanel40.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 115, 340, 80));

        config.getContentPane().add(jPanel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 890, 700));

        jDesktopPane1.add(config, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, -40, 920, 740));

        birthday.setBackground(new java.awt.Color(255, 255, 255));
        birthday.setBorder(null);
        birthday.setVisible(true);
        birthday.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel54.setBackground(new java.awt.Color(255, 255, 255));
        jPanel54.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel55.setBackground(new java.awt.Color(51, 0, 51));
        jPanel55.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel61.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("Today Birthday");
        jPanel55.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel65.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(204, 204, 204));
        jLabel65.setText("Home/Today Birthday");
        jPanel55.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 20, -1, -1));

        jPanel54.add(jPanel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 12, 890, 60));

        jPanel57.setBackground(new java.awt.Color(255, 255, 255));
        jPanel57.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setBorder(null);

        birthdaylistTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        birthdaylistTable.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        birthdaylistTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "No", "Name", "Mobile", "Birth Day"
            }
        ));
        birthdaylistTable.setGridColor(new java.awt.Color(255, 255, 255));
        birthdaylistTable.setOpaque(false);
        birthdaylistTable.setRowHeight(30);
        birthdaylistTable.setShowHorizontalLines(true);
        birthdaylistTable.setShowVerticalLines(true);
        birthdaylistTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                birthdaylistTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(birthdaylistTable);
        if (birthdaylistTable.getColumnModel().getColumnCount() > 0) {
            birthdaylistTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            birthdaylistTable.getColumnModel().getColumn(3).setHeaderValue("Mobile");
        }

        jPanel57.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 850, 580));

        jPanel54.add(jPanel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 890, 590));

        birthday.getContentPane().add(jPanel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 890, 700));

        jDesktopPane1.add(birthday, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, -40, 920, 740));

        CardNumberEntry.setBackground(new java.awt.Color(255, 255, 255));
        CardNumberEntry.setBorder(null);
        CardNumberEntry.setVisible(true);
        CardNumberEntry.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel70.setBackground(new java.awt.Color(255, 255, 255));
        jPanel70.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel71.setBackground(new java.awt.Color(51, 0, 51));
        jPanel71.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel73.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setText("Loyalty Card Number Entry");
        jPanel71.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel80.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(204, 204, 204));
        jLabel80.setText("Home/Loyalty Card");
        jPanel71.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 20, -1, -1));

        jPanel70.add(jPanel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 12, 890, 60));

        jPanel72.setBackground(new java.awt.Color(255, 255, 255));
        jPanel72.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel50.setBackground(new java.awt.Color(255, 255, 255));
        jPanel50.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel50.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel74.setBackground(new java.awt.Color(255, 255, 255));
        jPanel74.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel82.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(51, 0, 51));
        jLabel82.setText("New Loyalty Card Number");
        jPanel74.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 5, -1, -1));

        loyaltycardnumbertext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 51)));
        loyaltycardnumbertext.setOpaque(true);
        jPanel74.add(loyaltycardnumbertext, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 30, 240, 40));

        jPanel50.add(jPanel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 260, 70));

        jPanel76.setBackground(new java.awt.Color(255, 255, 255));
        jPanel76.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton5.setBackground(new java.awt.Color(102, 0, 0));
        jButton5.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Delete");
        jButton5.setBorder(null);
        jButton5.setOpaque(true);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel76.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 80, 50));

        customersavebtn2.setBackground(new java.awt.Color(51, 0, 51));
        customersavebtn2.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        customersavebtn2.setForeground(new java.awt.Color(255, 255, 255));
        customersavebtn2.setText("Save");
        customersavebtn2.setBorder(null);
        customersavebtn2.setOpaque(true);
        customersavebtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customersavebtn2ActionPerformed(evt);
            }
        });
        jPanel76.add(customersavebtn2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 80, 50));

        jButton7.setBackground(new java.awt.Color(0, 51, 51));
        jButton7.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Reset");
        jButton7.setOpaque(true);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel76.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, -1, 50));

        jPanel50.add(jPanel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 260, 70));

        jPanel72.add(jPanel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, 290, 180));

        jPanel75.setBackground(new java.awt.Color(255, 255, 255));
        jPanel75.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel75.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel85.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel85.setText("Search");
        jPanel75.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, -1, -1));

        jTextField6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel75.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, 260, 40));

        jPanel72.add(jPanel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 860, 60));

        jPanel77.setBackground(new java.awt.Color(255, 255, 255));
        jPanel77.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane5.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane5.setBorder(null);

        cardnumberTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        cardnumberTable.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        cardnumberTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "Card Number", "Customer Name", "Status"
            }
        ));
        cardnumberTable.setGridColor(new java.awt.Color(255, 255, 255));
        cardnumberTable.setOpaque(false);
        cardnumberTable.setRowHeight(25);
        cardnumberTable.setShowHorizontalLines(true);
        cardnumberTable.setShowVerticalLines(true);
        cardnumberTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardnumberTableMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(cardnumberTable);
        if (cardnumberTable.getColumnModel().getColumnCount() > 0) {
            cardnumberTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        }

        jPanel77.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 350));

        jPanel72.add(jPanel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 880, 360));

        jPanel70.add(jPanel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 890, 640));

        CardNumberEntry.getContentPane().add(jPanel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 890, 700));

        jDesktopPane1.add(CardNumberEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, -40, 920, 740));

        MainPanel.add(jDesktopPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 0, 880, 690));

        LoginPanel.setBackground(new java.awt.Color(51, 0, 51));
        LoginPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel23.setBackground(new java.awt.Color(51, 0, 45));
        jPanel23.setBorder(null);
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel30.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Username");
        jPanel23.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, -1, -1));

        UserName.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        UserName.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        UserName.setOpaque(true);
        jPanel23.add(UserName, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 300, 40));

        jLabel31.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Password");
        jPanel23.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 150, -1, -1));

        Password.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        Password.setBorder(null);
        Password.setOpaque(true);
        Password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PasswordKeyPressed(evt);
            }
        });
        jPanel23.add(Password, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 190, 300, 40));

        jButton3.setBackground(new java.awt.Color(153, 0, 0));
        jButton3.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Login");
        jButton3.setBorder(null);
        jButton3.setOpaque(true);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel23.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 240, 300, 50));

        LoginPanel.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 150, 700, 370));

        jLabel32.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Customer Loyalty");
        LoginPanel.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, -1, -1));

        jLabel33.setFont(new java.awt.Font("sansserif", 2, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Powerd By Code2Creation.com");
        LoginPanel.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 660, -1, 20));

        jLabel34.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Version 1.0.1");
        LoginPanel.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 70, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(LoginPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(LoginPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseClicked
        FrameNavigation(Dashboard, jLabel3);
    }//GEN-LAST:event_jPanel3MouseClicked

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        FrameNavigation(loaylty, jLabel1);
    }//GEN-LAST:event_jPanel2MouseClicked

    private void jPanel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseClicked
        FrameNavigation(customer, jLabel10);
    }//GEN-LAST:event_jPanel6MouseClicked

    private void jPanel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseEntered
        //jPanel2.setBackground(Color.decode("#565255"));

    }//GEN-LAST:event_jPanel2MouseEntered

    private void jPanel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseEntered
        //jPanel3.setBackground(Color.decode("#565255"));

    }//GEN-LAST:event_jPanel3MouseEntered

    private void jPanel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseEntered


    }//GEN-LAST:event_jPanel6MouseEntered

    private void jPanel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseEntered
        // jPanel5.setBackground(Color.decode("#565255"));


    }//GEN-LAST:event_jPanel5MouseEntered

    private void jPanel21MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel21MouseEntered

    }//GEN-LAST:event_jPanel21MouseEntered

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            LoginProcess();

        } catch (IOException ex) {
            Logger.getLogger(Home.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jPanel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel21MouseClicked
        FrameNavigation(config, jLabel27);
    }//GEN-LAST:event_jPanel21MouseClicked

    private void PasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PasswordKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            try {
                LoginProcess();

            } catch (IOException ex) {
                Logger.getLogger(Home.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_PasswordKeyPressed

    private void customersavebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customersavebtnActionPerformed
        if (IsValid()) {
            if (updatekey == 0) {
                try {
                    NewCustomer();
                } catch (SQLException ex) {
                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    CustomerUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }//GEN-LAST:event_customersavebtnActionPerformed

    private void customerlistTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customerlistTableMouseClicked
        updatekey = 1;
        customersavebtn.setText("Update");
        int row = customerlistTable.getSelectedRow();
        String customerCode = (customerlistTable.getModel().getValueAt(row, 1).toString());
        codetext.setSelectedItem(customerCode);
        String name = (customerlistTable.getModel().getValueAt(row, 2).toString());
        snametext.setText(name);
        String address = (customerlistTable.getModel().getValueAt(row, 3).toString());
        addresstext.setText(address);
        String mobile = (customerlistTable.getModel().getValueAt(row, 4).toString());
        contactnotext.setText(mobile);
        String birthday = (customerlistTable.getModel().getValueAt(row, 5).toString());
        if (birthday == null) {
            ((JTextField) birthdaytext.getDateEditor().getUiComponent()).setText(null);
        } else {
            java.sql.Date dates = java.sql.Date.valueOf(birthday);
            birthdaytext.setDate(dates);

        }
        String visitime = (customerlistTable.getModel().getValueAt(row, 6).toString());
        customervisitimetext.setText(visitime);
        String status = (customerlistTable.getModel().getValueAt(row, 7).toString());
        statusbox.setSelectedItem(status);

        int totalvisit = CustomerTotalVisit(customerCode);
        customervisitimetext.setText(String.valueOf(totalvisit));
    }//GEN-LAST:event_customerlistTableMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            Customerclear();
        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (updatekey == 1) {
            try {
                int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
                if (p == 0) {
                    try {
                        String sql = "Delete from customerInfo where customerid=?";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, (String) codetext.getSelectedItem());
                        pst.execute();
                        JOptionPane.showMessageDialog(null, "Data Deleted");

                    } catch (SQLException | HeadlessException e) {
                        JOptionPane.showMessageDialog(null, e);

                    }

                }
                Customerclear();
                CustomerList();
            } catch (SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void customervisitTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customervisitTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_customervisitTableMouseClicked

    private void checkvisiboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_checkvisiboxPopupMenuWillBecomeInvisible
        if (checkvisibox.getSelectedIndex() > 0) {
            String VisitTime = (String) checkvisibox.getSelectedItem();
            getVisitConfig(VisitTime);
        } else {
            checkdiscountbox.setSelected(false);
            foodservicecheckbox.setSelected(false);
            discountbox.setEnabled(false);
            itembox.removeAllItems();
            itembox.setEnabled(false);
            discountbox.setText(null);
            visitimetext.setText(null);
            visitconfigId = 0;
        }
    }//GEN-LAST:event_checkvisiboxPopupMenuWillBecomeInvisible

    private void visitbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visitbtnActionPerformed
        if (customerid == null) {

        } else {
            try {
                CurrentTime();
                NewVisitEntry();
            } catch (SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_visitbtnActionPerformed

    private void jPanel42MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel42MouseClicked
        FrameNavigation(visitFrame, jLabel51);
    }//GEN-LAST:event_jPanel42MouseClicked

    private void jPanel42MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel42MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel42MouseEntered

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
        LoginPanel.setVisible(true);
        MainPanel.setVisible(false);
    }//GEN-LAST:event_jPanel5MouseClicked

    private void checkdiscountboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkdiscountboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkdiscountboxActionPerformed

    private void itemboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemboxPopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_itemboxPopupMenuWillBecomeInvisible

    private void foodservicecheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foodservicecheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_foodservicecheckboxActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (visitconfigId > 0) {
            try {
                int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
                if (p == 0) {
                    try {
                        String sql = "Delete from visitconfig where id=?";
                        pst = conn.prepareStatement(sql);
                        pst.setInt(1, visitconfigId);
                        pst.execute();
                        JOptionPane.showMessageDialog(null, "Data Deleted");

                    } catch (SQLException | HeadlessException e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                    visitimetext.setText(null);
                    foodservicecheckbox.setSelected(false);
                    itembox.removeAllItems();
                    itembox.setEnabled(false);
                    checkdiscountbox.setSelected(false);
                    discountbox.setEnabled(false);
                    discountbox.setText(null);
                    visitconfigId = 0;
                }
                GifitList();
            } catch (SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void customersavebtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customersavebtn1ActionPerformed
        if (visitimetext.getText().isEmpty() || visitimetext.getText().matches("^[a-zA-Z]+$")) {
            JOptionPane.showMessageDialog(null, "Time Should not empty or charecter");
        } else {
            if (visitconfigId == 0) {
                try {
                    NewVisitConfig();
                } catch (SQLException ex) {
                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    UpdateVisitConfig();
                } catch (SQLException ex) {
                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }//GEN-LAST:event_customersavebtn1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        visitimetext.setText(null);
        foodservicecheckbox.setSelected(false);
        itembox.removeAllItems();
        itembox.setEnabled(false);
        checkdiscountbox.setSelected(false);
        discountbox.setEnabled(false);
        discountbox.setText(null);
        visitconfigId = 0;
        try {
            GifitList();
        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void foodservicecheckboxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_foodservicecheckboxMouseClicked
        itembox.removeAllItems();
        if (foodservicecheckbox.isSelected() == true) {
            //  checkdiscountbox.setSelected(false);
            itembox.setEnabled(true);
            // discountbox.setEnabled(false);
            ItemSearch();
        } else {
            itembox.setEnabled(false);

        }

    }//GEN-LAST:event_foodservicecheckboxMouseClicked
    private void ItemSearch() {
        final JTextField textfield = (JTextField) itembox.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        itembox.removeAllItems();
                    } else {
                        FilterItemSeach(textfield.getText());
                    }
                });
            }

        });
    }

    public void FilterItemSeach(String enteredText) {
        itembox.removeAllItems();
        itembox.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;
        try {
            String sql = "Select  itemName from item WHERE lower(itemName)  LIKE '" + enteredText + "%' ORDER BY itemName ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("itemName");
                filterArray.add(str1);
                itembox.addItem(str1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        if (filterArray.size() > 0) {
            itembox.setSelectedItem(enteredText);
            itembox.showPopup();
        } else {
            itembox.hidePopup();
        }
    }
    private void checkdiscountboxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkdiscountboxMouseClicked
        if (checkdiscountbox.isSelected() == true) {
            // foodservicecheckbox.setSelected(false);
            // itembox.setEnabled(false);
            discountbox.setEnabled(true);
        } else {
            discountbox.setEnabled(false);
        }
    }//GEN-LAST:event_checkdiscountboxMouseClicked

    private void customerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_customerboxPopupMenuWillBecomeInvisible
        try {

            String cusid = (String) customerbox.getSelectedItem();

            String sql = "Select "
                    + "customerid,"
                    + "customername,"
                    + "address,"
                    + "ContactNo,"
                    + "birthday"
                    + " from customerInfo where customerid='" + cusid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                customerid = rs.getString("customerid");
                cardnumbertext.setText(customerid);
                String sname = rs.getString("customername");
                visitcustomertext.setText(sname);
                homecusnametext.setText(sname);

                String address = rs.getString("address");
                visitcustomeraddress.setText(address);
                homeaddresstext.setText(address);

                String contctno = rs.getString("ContactNo");
                visicustomerMobileText.setText(contctno);
                homemobiletext.setText(contctno);

                String birthdays = rs.getString("birthday");
                visitCustomerBirthDaytext.setText(birthdays);
                homebirthdaytext.setText(birthdays);
                CheckBirthday(birthdays, sname);
            } else {
                jLabel66.show(false);
                birthdayustomertext.show(false);
                birthdayustomertext.setText(null);

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        int totalvisit = CustomerTotalVisit(customerid);
        visitCustomerTotalisit.setText(String.valueOf(totalvisit));
        homevisittimetext.setText(String.valueOf(totalvisit));
        GiftDescription(totalvisit);

    }//GEN-LAST:event_customerboxPopupMenuWillBecomeInvisible

    private void birthdaylistTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_birthdaylistTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_birthdaylistTableMouseClicked

    private void giftListTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_giftListTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_giftListTableMouseClicked

    private void jPanel66MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel66MouseClicked
        FrameNavigation(Layalitycheck, jLabel75);
    }//GEN-LAST:event_jPanel66MouseClicked

    private void jPanel66MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel66MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel66MouseEntered

    private void jPanel68MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel68MouseClicked
        FrameNavigation(birthday, jLabel3);
    }//GEN-LAST:event_jPanel68MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (cardupdatekey == 1) {
            if (CheckCardCustomer()) {
                JOptionPane.showMessageDialog(null, "Fail to Delete!this Card Number already Activate");
            } else {

                int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
                if (p == 0) {
                    try {
                        String sql = "Delete from loyalty_card_number where cardnumber=?";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, loyaltycardnumbertext.getText());
                        pst.execute();
                        cardupdatekey = 0;
                        loyaltycardnumbertext.setText(null);
                        JOptionPane.showMessageDialog(null, "Data Deleted");

                    } catch (SQLException | HeadlessException e) {
                        JOptionPane.showMessageDialog(null, e);

                    }

                }
                CardNumberList();
            }

        }
    }//GEN-LAST:event_jButton5ActionPerformed
    private boolean CheckCardCustomer() {
        try {
            String sql = "Select id from customerinfo where customerid='" + loyaltycardnumbertext.getText() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return false;

    }
    private void customersavebtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customersavebtn2ActionPerformed
        if (loyaltycardnumbertext.getText().isEmpty()) {

        } else {
            try {
                NewCardEntry();
            } catch (SQLException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_customersavebtn2ActionPerformed
    private void NewCardEntry() throws SQLException {
        String cardnumber = loyaltycardnumbertext.getText();
        if (CheckardExists(cardnumber)) {
            JOptionPane.showMessageDialog(null, "This number already exixts");
        } else {
            try {
                String sql = "Insert into loyalty_card_number("
                        + "cardnumber,status) values(?,?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, loyaltycardnumbertext.getText());
                pst.setString(2, "0");
                pst.execute();
                JOptionPane.showMessageDialog(null, "New ard Number Entry Successfuly");
                CardNumberList();
                loyaltycardnumbertext.setText(null);
                cardupdatekey = 0;

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }

    }

    private boolean CheckardExists(String cardNumber) {
        try {
            String sql = "Select cardnumber from loyalty_card_number where cardnumber='" + cardNumber + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return false;

    }

    private void CardNumberList() {
        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) cardnumberTable.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select "
                    + "cardnumber,"
                    + "status,"
                    + "(select customername ci from customerInfo ci where ci.customerid =cif.cardnumber) as 'customername'"
                    + " from loyalty_card_number cif";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String Sts;
                String cardnumber = rs.getString("cardnumber");
                String name = rs.getString("customername");
                int status = rs.getInt("status");
                if (status == 0) {
                    Sts = "Diactivate";
                } else {
                    Sts = "Activate";
                }
                tree++;
                model2.addRow(new Object[]{tree, cardnumber, name, Sts});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        loyaltycardnumbertext.setText(null);
        cardupdatekey = 0;
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jPanel73MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel73MouseClicked
        FrameNavigation(CardNumberEntry, jLabel81);
    }//GEN-LAST:event_jPanel73MouseClicked

    private void jPanel73MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel73MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel73MouseEntered

    private void cardnumberTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardnumberTableMouseClicked
        cardupdatekey = 1;

        int row = cardnumberTable.getSelectedRow();
        String customerCode = (cardnumberTable.getModel().getValueAt(row, 1).toString());
        loyaltycardnumbertext.setText(customerCode);

    }//GEN-LAST:event_cardnumberTableMouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (giftServe) {
            GiftServe();
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void codetextPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_codetextPopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_codetextPopupMenuWillBecomeInvisible
    private void CheckBirthday(String birthday, String Name) {
        java.sql.Date bdate = java.sql.Date.valueOf(birthday);
        Calendar cal = Calendar.getInstance();
        //birthday
        cal.setTime(bdate);
        int bmonth = cal.get(Calendar.MONTH);
        int bday = cal.get(Calendar.DAY_OF_MONTH);

        // int byear = cal.get(Calendar.YEAR);
        //currentdate
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        int cmonth = cal1.get(Calendar.MONTH);
        int cday = cal1.get(Calendar.DAY_OF_MONTH);
        // int cyear = cal1.get(Calendar.YEAR);
        if (cmonth == bmonth && cday == bday) {
            jLabel66.show(true);
            birthdayustomertext.show(true);
            birthdayustomertext.setText(Name);
        } else {
            jLabel66.show(false);
            birthdayustomertext.show(false);
            birthdayustomertext.setText(null);
        }
    }

    private void getTotalBirthDay() {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        int cmonth = cal1.get(Calendar.MONTH) + 1;

        int cday = cal1.get(Calendar.DAY_OF_MONTH);

        try {
            String sql = "Select count(id) as 'totalbday' from customerinfo where MONTH(birthday)='" + cmonth + "' AND DAY(birthday)='" + cday + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                int totalbirthday = rs.getInt("totalbday");
                totalbtext.setText(String.valueOf(totalbirthday));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void GetBirthdayList() {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        int cmonth = cal1.get(Calendar.MONTH) + 1;

        int cday = cal1.get(Calendar.DAY_OF_MONTH);
        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) birthdaylistTable.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select "
                    + "customerid,"
                    + "customername,"
                    + "ContactNo,"
                    + "birthday"
                    + " from customerInfo where MONTH(birthday)='" + cmonth + "' AND DAY(birthday)='" + cday + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String id = rs.getString("customerid");
                String name = rs.getString("customername");
                String mobileno = rs.getString("ContactNo");
                String birthdays = rs.getString("birthday");
                tree++;
                model2.addRow(new Object[]{tree, id, name, mobileno, birthdays});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        /*
        try {
            String sql = "Select count(id) as 'totalcustomer' from customerinfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            
        }
         */

    }

    private void GiftDescription(int visittime) {
        try {
            String sql = "Select id,"
                    + "Food_Serve,"
                    + "Discount"
                    + " from visitconfig where visittime='" + visittime + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                jPanel49.show();
                giftvisitid = rs.getInt("id");
                String Food_Serve = rs.getString("Food_Serve");
                homefoodservtext.setText(Food_Serve);
                String Discount = rs.getString("Discount");
                homeDiscounttext.setText(Discount);
                checkGiftStatus(customerid, visittime, giftvisitid);
            } else {
                jPanel49.hide();
                giftvisitid = 0;
                homefoodservtext.setText(null);
                homeDiscounttext.setText(null);
                Giftdate.setText(null);
                visitbtn.setText("Visit");
                giftServe = false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void checkGiftStatus(String customerid, int visitTime, int configid) {
        String sts;
        try {
            String sql = "Select "
                    + "date,"
                    + "status"
                    + " from gift cif where cif.customerid='" + customerid + "' AND giftid='" + configid + "' AND VisitTime='" + visitTime + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String giftdate = rs.getString("date");
                Giftdate.setText(giftdate);
                sts = "Complited";
                giftstatus.setText(sts);
                giftServe = false;
                jButton8.setEnabled(false);
                giftServe = false;
            } else {
                sts = "Waiting For Serve";
                giftstatus.setText(sts);
                giftServe = true;
                jButton8.setEnabled(true);
                giftServe = true;
                Giftdate.setText(null);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    public void bar(JLabel lab) {
        jLabel1.setOpaque(false);
        jLabel10.setOpaque(false);
        jLabel3.setOpaque(false);
        jLabel27.setOpaque(false);
        jLabel51.setOpaque(false);
        jLabel75.setOpaque(false);
        jLabel81.setOpaque(false);
        lab.setOpaque(true);
        jPanel4.repaint();
    }

    private void FrameNavigation(JInternalFrame Frame, JLabel lab) {
        Dashboard.setVisible(false);
        Layalitycheck.setVisible(false);
        loaylty.setVisible(false);
        customer.setVisible(false);
        visitFrame.setVisible(false);
        config.setVisible(false);
        birthday.setVisible(false);
        CardNumberEntry.setEnabled(false);

        bar(lab);
        Frame.setVisible(true);
    }

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new Home().setVisible(true);

            } catch (IOException | SQLException ex) {
                Logger.getLogger(Home.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JInternalFrame CardNumberEntry;
    private javax.swing.JInternalFrame Dashboard;
    private javax.swing.JLabel Giftdate;
    private javax.swing.JInternalFrame Layalitycheck;
    private javax.swing.JPanel LoginPanel;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JPasswordField Password;
    private javax.swing.JTextField UserName;
    private javax.swing.JTextField addresstext;
    private javax.swing.JInternalFrame birthday;
    private javax.swing.JTable birthdaylistTable;
    private javax.swing.JPanel birthdaypanel;
    private com.toedter.calendar.JDateChooser birthdaytext;
    private javax.swing.JLabel birthdayustomertext;
    private javax.swing.JTable cardnumberTable;
    private javax.swing.JLabel cardnumbertext;
    private javax.swing.JCheckBox checkdiscountbox;
    private javax.swing.JComboBox<String> checkvisibox;
    private javax.swing.JComboBox<String> codetext;
    private javax.swing.JInternalFrame config;
    private javax.swing.JTextField contactnotext;
    private javax.swing.JTextField contactnotext1;
    private javax.swing.JTextField contactnotext3;
    private com.toedter.calendar.JDateChooser currentdateetxt;
    private javax.swing.JInternalFrame customer;
    private javax.swing.JComboBox<String> customerbox;
    private javax.swing.JTable customerlistTable;
    private javax.swing.JButton customersavebtn;
    private javax.swing.JButton customersavebtn1;
    private javax.swing.JButton customersavebtn2;
    private javax.swing.JTable customervisitTable;
    private javax.swing.JTextField customervisitimetext;
    private javax.swing.JTextField discountbox;
    private javax.swing.JCheckBox foodservicecheckbox;
    private javax.swing.JTable giftListTable;
    private javax.swing.JLabel giftstatus;
    private javax.swing.JLabel homeDiscounttext;
    private javax.swing.JLabel homeaddresstext;
    private javax.swing.JLabel homebirthdaytext;
    private javax.swing.JLabel homecusnametext;
    private javax.swing.JLabel homefoodservtext;
    private javax.swing.JLabel homemobiletext;
    private javax.swing.JLabel homevisittimetext;
    private javax.swing.JComboBox<String> itembox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanel74;
    private javax.swing.JPanel jPanel75;
    private javax.swing.JPanel jPanel76;
    private javax.swing.JPanel jPanel77;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JInternalFrame loaylty;
    private javax.swing.JTextField loyaltycardnumbertext;
    private javax.swing.JTextField snametext;
    private javax.swing.JComboBox<String> statusbox;
    private javax.swing.JLabel totalbtext;
    private javax.swing.JTextField visicustomerMobileText;
    private javax.swing.JTextField visitCustomerBirthDaytext;
    private javax.swing.JTextField visitCustomerTotalisit;
    private javax.swing.JInternalFrame visitFrame;
    private javax.swing.JButton visitbtn;
    private javax.swing.JTextField visitcustomeraddress;
    private javax.swing.JTextField visitcustomertext;
    private javax.swing.JTextField visitimetext;
    // End of variables declaration//GEN-END:variables
}
