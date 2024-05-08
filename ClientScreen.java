import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JTextArea;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JButton;

import java.io.*;
import java.net.*;



public class ClientScreen extends JPanel implements ActionListener {
    private PrintWriter out;
    private BufferedImage background;


    public ClientScreen() {
        setLayout(null);

        try {
			background = ImageIO.read(new File("Background.png"));
		} catch (IOException event) {
			event.printStackTrace();
		}
    }
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
    }

    public void actionPerformed(ActionEvent e) {
        
    }

    public void connect() throws IOException {
        String hostName = "localHost";
        int portNumber = 1024;
        Socket serverSocket = new Socket(hostName, portNumber);

        BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        out = new PrintWriter(serverSocket.getOutputStream(), true);

        

        // listens for inputs
        // try {
            
        //     while (true) {
                
        //         repaint();
        //     }
        // } catch (UnknownHostException e) {
        //     System.err.println("Host unkown: " + hostName);
        //     System.exit(1);
        // } catch (IOException e) {
        //     System.err.println("Couldn't get I/O for the connection to " + hostName);
        //     System.exit(1);
        // }
    }

}
