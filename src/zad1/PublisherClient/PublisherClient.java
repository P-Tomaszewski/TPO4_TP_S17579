package zad1.PublisherClient;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JComboBox;

public class PublisherClient extends JDialog
{
    private String serverHost = "localhost";
    private int serverPort = 6154;
    private final JPanel contentPanel = new JPanel();
    JLabel lblTematNowegoNewsa;
    JComboBox<String> comboBoxToAdd;
    JTextPane textPane;
    JPanel buttonPane;
    JButton okButton;
    JButton cancelButton;
    String[] topicsArray;
    public PublisherClient(String[] topicsArray)
    {
      this.topicsArray = topicsArray;
      setBounds(100, 100, 450, 300);
      getContentPane().setLayout(new BorderLayout());
      contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
      getContentPane().add(contentPanel, BorderLayout.CENTER);
      contentPanel.setLayout(null);
      {
        lblTematNowegoNewsa = new JLabel("Temat nowego newsa");
        lblTematNowegoNewsa.setBounds(78, 11, 141, 16);
        lblTematNowegoNewsa.setFont(new Font("Tahoma", Font.BOLD, 13));
        contentPanel.add(lblTematNowegoNewsa);
      }
      {
        comboBoxToAdd = new JComboBox();
        comboBoxToAdd.setBounds(229, 10, 151, 20);
        comboBoxToAdd.setModel(new DefaultComboBoxModel<String>(topicsArray));
        contentPanel.add(comboBoxToAdd);
      }
      {
        textPane = new JTextPane();
        textPane.setBounds(32, 35, 352, 182);
        contentPanel.add(textPane);
      }
      {
        buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        {
          okButton = new JButton("OK");
          okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0)
            {
              try
              {
                InetSocketAddress hostAddress = new InetSocketAddress(serverHost, serverPort);
                SocketChannel sc = SocketChannel.open(hostAddress);
                writeMessage(sc, "modifyNews_add_"+(String)comboBoxToAdd.getSelectedItem()+
                        "_"+textPane.getText());
                dispose();
              } catch (IOException e)
              {
                e.printStackTrace();
              }
            }
          });
          okButton.setActionCommand("OK");
          buttonPane.add(okButton);
          getRootPane().setDefaultButton(okButton);
        }
        {
          cancelButton = new JButton("Cancel");
          cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
              dispose();
            }
          });
          cancelButton.setActionCommand("Cancel");
          buttonPane.add(cancelButton);
        }
      }
    }
    private void writeMessage(SocketChannel sc, String addMsg) throws IOException
    {
      ByteBuffer buffer = ByteBuffer.allocate(1024);
      buffer.put(addMsg.getBytes());
      buffer.flip();
      sc.write(buffer);
    }
  }




