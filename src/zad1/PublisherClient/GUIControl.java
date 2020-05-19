package zad1.PublisherClient;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class GUIControl extends JFrame
{
	private static final int BSIZE = 1024;
	private String serverHost = "localhost";
	private int serverPort = 6154;
	private static Pattern pattern = Pattern.compile("_");
	private JPanel contentPane;
	private JTextField tFAddNewTopic;
	String[] topicsArray;
	JButton buttonNewMessage;
	JLabel lblAddNewTopic;
	JButton buttonAddNewTopic;
	JLabel lblDelete;
	JComboBox<String> comboBoxToDelete;
	JButton buttonToDelete;

	public GUIControl()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 453, 187);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		buttonNewMessage = new JButton("Dodaj nową wiadomość");
		buttonNewMessage.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				PublisherClient addNews = new PublisherClient(topicsArray);
				addNews.setVisible(true);
			}
		});
		buttonNewMessage.setBounds(119, 11, 204, 23);
		contentPane.add(buttonNewMessage);

		lblAddNewTopic = new JLabel("Dodaj nowy temat");
		lblAddNewTopic.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblAddNewTopic.setBounds(10, 56, 126, 14);
		contentPane.add(lblAddNewTopic);

		tFAddNewTopic = new JTextField();
		tFAddNewTopic.setBounds(145, 54, 164, 20);
		contentPane.add(tFAddNewTopic);
		tFAddNewTopic.setColumns(10);

		buttonAddNewTopic = new JButton("Dodaj");
		buttonAddNewTopic.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				String newTopic = tFAddNewTopic.getText();
				SocketChannel sc = getConnection(serverHost, serverPort);
				writeMessage(sc, "modifyTopic_add_"+newTopic);
				comboBoxToDelete.addItem(newTopic);
			}
		});
		buttonAddNewTopic.setBounds(335, 53, 89, 23);
		contentPane.add(buttonAddNewTopic);

		lblDelete = new JLabel("Usuń temat");
		lblDelete.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDelete.setBounds(10, 106, 110, 14);
		contentPane.add(lblDelete);

		comboBoxToDelete = new JComboBox<String>();
		comboBoxToDelete.setBounds(145, 104, 164, 20);
		contentPane.add(comboBoxToDelete);

		buttonToDelete = new JButton("Usuń");
		buttonToDelete.setBounds(335, 103, 89, 23);
		contentPane.add(buttonToDelete);

		//taking data to comboBox
		SocketChannel sc = getConnection(serverHost, serverPort);
		writeMessage(sc, "askingForData_topics");
		String resString = readData(sc);
		topicsArray = pattern.split(resString);
		comboBoxToDelete.setModel(new DefaultComboBoxModel<String>(topicsArray));
	

	}

	private SocketChannel getConnection(String serverHost, int serverPort)
	{
		SocketChannel sc = null;
		try
		{
			InetSocketAddress hostAddress = new InetSocketAddress(serverHost, serverPort);
			sc = SocketChannel.open(hostAddress);
		} catch (IOException e)
		{
			System.out.println("Problem with connection to Server");
			e.printStackTrace();
		}
		return sc;
	}

	private void writeMessage(SocketChannel sc, String addMsg)
	{
		try
		{
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			buffer.put(addMsg.getBytes());
			buffer.flip();
			sc.write(buffer);
		} catch (IOException e)
		{
			System.out.println("Problem with write");
			e.printStackTrace();
		}
	}

	private String readData(SocketChannel sc)
	{
		int numRead = -1;
		try
		{
			ByteBuffer buffer = ByteBuffer.allocate(BSIZE);
			numRead = sc.read(buffer);
			byte[] data = new byte[numRead];
			System.arraycopy(buffer.array(), 0, data, 0, numRead);
			return new String(data);
		} catch (IOException e)
		{
			return null;
		}
	}
}
