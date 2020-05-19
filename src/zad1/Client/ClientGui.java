package zad1.Client;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class ClientGui extends JFrame {
  private JPanel contentPane;
  JComboBox<String> comboBoxChoseIntresting;
  JComboBox comboBoxToDelete;
  JLabel lblChoseIntresting;
  JLabel lblToDelete;
  JTextPane textPane;
  JButton btnDelete;
  JButton btnAdd;

  public ClientGui()

  {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 300);
    contentPane = new JPanel();
    contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
    setContentPane(contentPane);
    contentPane.setLayout(null);
    comboBoxChoseIntresting = new JComboBox();
    comboBoxChoseIntresting.setBounds(208, 11, 117, 31);
    contentPane.add(comboBoxChoseIntresting);

    comboBoxToDelete = new JComboBox(new Object[]{});
    comboBoxToDelete.setBounds(180, 216, 117, 31);
    contentPane.add(comboBoxToDelete);

    lblChoseIntresting = new JLabel("Wybierz interesujace tematy");
    lblChoseIntresting.setFont(new Font("Tahoma", Font.BOLD, 13));
    lblChoseIntresting.setBounds(10, 14, 196, 23);
    contentPane.add(lblChoseIntresting);

    lblToDelete = new JLabel("Usuń tematy");
    lblToDelete.setFont(new Font("Tahoma", Font.BOLD, 13));
    lblToDelete.setBounds(80, 219, 102, 23);
    contentPane.add(lblToDelete);

    textPane = new JTextPane();
    textPane.setBounds(48, 48, 331, 158);
    contentPane.add(textPane);

    btnAdd = new JButton("Dodaj");
    btnAdd.setBounds(335, 11, 89, 31);
    contentPane.add(btnAdd);

    btnDelete = new JButton("Usun");
    btnDelete.setBounds(335, 216, 89, 31);
    contentPane.add(btnDelete);
  }
}
