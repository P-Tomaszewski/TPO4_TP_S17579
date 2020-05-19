package zad1.ProxyServer;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

public class ProxyServer {




    private static Pattern pattern = Pattern.compile("_");
    private static Charset charset = Charset.forName("ISO-8859-2");
    private static final int BSIZE = 1024;
    private ServerSocketChannel serverSocketChannel = null;
    private Selector selector = null;
    private String host = "localhost";
    private int port = 6154;
    HashMap<String, Topic> topicMap;

    public ProxyServer()
    {
      try
      {
        topicMap = new HashMap<>();
        topicMap.put("politic", new Topic("politic"));
        topicMap.put("sport", new Topic("sport"));
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(host, port));
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

      } catch (Exception exc)
      {
        exc.printStackTrace();
      }
      System.out.println("Server started");
      serviceConnections();
    }

    private void serviceConnections()
    {
      while (true)
      {
        try
        {
          selector.select();

          Set<SelectionKey> keys = selector.selectedKeys();
          Iterator<SelectionKey> iterator = keys.iterator();
          while (iterator.hasNext())
          {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isAcceptable())
            {
              SocketChannel sc = serverSocketChannel.accept();
              sc.configureBlocking(false);
              sc.register(selector, SelectionKey.OP_READ);
              continue;
            }
            if (key.isReadable())
            {
              SocketChannel sc = (SocketChannel) key.channel();
              serviceRequest(sc);
              continue;
            }
          }
        } catch (IOException e)
        {
          System.out.println("Problem with selector");
          e.printStackTrace();
          continue;
        }
      }

    }

    private void serviceRequest(SocketChannel sc) throws IOException
    {
      if (!sc.isOpen())
        return;
      String reqString = readData(sc);
      System.out.println("Got: " + reqString);
      String[] reqStringTab = pattern.split(reqString);
      if (reqStringTab.length < 2)
        writeResp(sc, null);
      else
      {
        if (reqStringTab[0].equals("subscribe"))
        {

        }
        if (reqStringTab[0].equals("unsubscribe"))
        {

        }
        if (reqStringTab[0].equals("askingForData"))
        {
          if (reqStringTab[1].equals("topics"))
          {
            String tmp = "";
            for (String key : topicMap.keySet())
            {
              tmp += key + "_";
            }
            tmp = removeLastChar(tmp);
            writeResp(sc, tmp);
          }
        }
        if (reqStringTab[0].equals("modifyTopic"))
        {
          if (reqStringTab[1].equals("add"))
          {
            String topicNameToAdd =reqStringTab[2];
            topicMap.put(topicNameToAdd, new Topic(topicNameToAdd));
          }
          else if(reqStringTab[1].equals("delete"))
          {
            String topicNameToDelete =reqStringTab[2];
            topicMap.remove(topicNameToDelete);
          }
        }
        if (reqStringTab[0].equals("modifyNews"))
        {
          if (reqStringTab[1].equals("add"))
          {
            String topic = reqStringTab[2];
            String text = reqStringTab[3];
            Topic topicToAddNews = topicMap.get(topic);
            topicToAddNews.addNews(text);
            ///Dodac wysylke do subskrybetnwo

            if(topicToAddNews.listOfNews.size() > 0)
            writeResp(sc, topicToAddNews.listOfNews.get(0));

          }
        }
      }
      sc.close();
    }

    public String removeLastChar(String s)
    {
      if (s == null || s.length() == 0)
      {
        return s;
      }
      return s.substring(0, s.length() - 1);
    }

    private void writeResp(SocketChannel sc, String addMsg) throws IOException
    {
      StringBuffer remsg = new StringBuffer();
      if (addMsg != null)
        remsg.append(addMsg);
      else
        remsg.append("null");
      ByteBuffer buf = charset.encode(CharBuffer.wrap(remsg));
      sc.write(buf);
    }

  private void sendMesagesToClients(String mesage){

    try {
      selector.select();
      Set keys = selector.selectedKeys();
      Iterator iter = keys.iterator();
      while (iter.hasNext()) {
        SelectionKey selK = (SelectionKey) iter.next();
        if(selK.isWritable()) {
          SocketChannel chanel = (SocketChannel) selK.channel();
         // ReadWriteNIO.write(mesage, chanel);
        }
      }
    } catch (IOException e) {
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



