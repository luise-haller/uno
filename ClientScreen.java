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
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JButton;

import java.io.*;
import java.net.*;



public class ClientScreen extends JPanel implements ActionListener {
    private BufferedReader in;
    private PrintWriter out;

    private BufferedImage background;
    private DLList<Card> myHand;

    private boolean myTurn;

    private Color Yellow = new Color(255, 225, 0);
    private Color Blue = new Color(0, 76, 255);
    private Color Red = new Color(255, 4, 0);
    private Color Green = new Color(72, 255, 0);
    private Color Black = new Color(0, 0, 0);

    public ClientScreen() {
        setLayout(null);
        this.setFocusable(true);

        myHand = new DLList<Card>();

        myTurn = false;

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

        for (int i = 0; i<myHand.size();i++) {
            if(myHand.get(i).getColor().equals("Yellow")) {
                g.setColor(Yellow);
            } else if(myHand.get(i).getColor().equals("Blue")) {
                g.setColor(Blue);
            } else if(myHand.get(i).getColor().equals("Red")) {
                g.setColor(Red);
            } else if(myHand.get(i).getColor().equals("Green")) {
                g.setColor(Green);
            } else if(myHand.get(i).getColor().equals("Black")) {
                g.setColor(Black);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        
    }

    public void connect() throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter ip address: ");
        String hostName = sc.nextLine();
        int portNumber = 1024;

        System.out.print("Enter username (no spaces): ");
        String username = sc.next();

        Socket serverSocket = new Socket(hostName, portNumber);
        out = new PrintWriter(serverSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

        try {
            out.println(username);

            while(true) {
                String msg = in.readLine();
                // Check if it's the client's turn
                if (msg.equals("Your turn")) {
                    myTurn = true;
                } else {
                    myTurn = false;
                }
                // add other messages (if needed)
                repaint();
            }
        } catch(IOException e) {
			System.err.println("Couldn't get I/O for the connection");
			System.exit(1);
		}

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
