package zad1.Client;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.regex.Pattern;


  public class Client
  {
    private String serverHost = "localhost";
    private int serverPort = 6154;
    private static Pattern pattern = Pattern.compile("_");
    private static final int BSIZE = 1024;
    static ClientGui gui = new ClientGui();
    public Client()
    {

      try
      {
        gui.setVisible(true);
        InetSocketAddress hostAddress = new InetSocketAddress(serverHost,serverPort);
        SocketChannel client = SocketChannel.open(hostAddress);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("askingForData_topics".getBytes());
        buffer.flip();
        client.write(buffer);
        String resString = readData(client);
        String[] resStringTab = pattern.split(resString);
        gui.comboBoxChoseIntresting.setModel(new DefaultComboBoxModel<String>(resStringTab));
      } catch (IOException e)
      {
        System.out.println("Problem with sending");
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


