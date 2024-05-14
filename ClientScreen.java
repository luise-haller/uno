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
import java.awt.*;


public class ClientScreen extends JPanel implements ActionListener {
    private BufferedReader in;
    private PrintWriter out;

    private BufferedImage background, logo;
    private DLList<Card> myHand;

    private boolean myTurn, gameStarted;
    private String hostName, username;

    private JButton startGameButton, submitButton;
    private JTextField ipAddressField, usernameField;

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
        gameStarted = false;

        startGameButton = new JButton();
        startGameButton.setFont(new Font("Arial", Font.BOLD, 16));
        startGameButton.setHorizontalAlignment(SwingConstants.CENTER);
        startGameButton.setText("PLAY");
        startGameButton.setBounds(350, 500, 100, 50);
        this.add(startGameButton);
        startGameButton.addActionListener(this);
        startGameButton.setVisible(true);

        submitButton = new JButton();
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setHorizontalAlignment(SwingConstants.CENTER);
        submitButton.setText("Submit");
        submitButton.setBounds(350, 450, 100, 50);
        this.add(submitButton);
        submitButton.addActionListener(this);
        submitButton.setVisible(false);

        ipAddressField = new JTextField();
        ipAddressField.setBounds(400, 285, 100, 30);
        add(ipAddressField);
        ipAddressField.setVisible(false);

        usernameField = new JTextField();
        usernameField.setBounds(400, 335, 100, 30);
        add(usernameField);
        usernameField.setVisible(false);

        try {
			background = ImageIO.read(new File("Background.png"));
            logo = ImageIO.read(new File("unoLogo.png"));
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
        g.setColor(Color.WHITE);

        if (gameStarted) {
            g.setColor(Color.gray);
            g.fillRect(200, 200, 50, 140);
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
        } else {
            if (!startGameButton.isVisible()) {
                g.drawString("IP address of server:", 220, 300);
                g.drawString("Your Player Name ", 220, 350);
            } else {
                int scaledWidth = (int) (logo.getWidth() * 0.2); 
                int scaledHeight = (int) (logo.getHeight() * 0.2);
                g.drawImage(logo, 250, 100, scaledWidth, scaledHeight, null);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startGameButton) {
            startGameButton.setVisible(false);
            ipAddressField.setVisible(true);
            usernameField.setVisible(true);
            submitButton.setVisible(true);
            this.hostName = ipAddressField.getText();
            this.username = usernameField.getText();
        }
        else if (e.getSource() == submitButton) {
            gameStarted = true;
            // System.out.println("Submit button pressed. gameStarted = " + gameStarted);
            
        }
        repaint();
    }
    public boolean getGameStarted() {
        return this.gameStarted;
    }

    public void connect() throws IOException {
        Scanner sc = new Scanner(System.in);

        // When client clicks play, they are asked to enter id address of server & their player name
        // Then will be connected & "enter" the game
        // System.out.println("Connecting");
        int portNumber = 1024;
        Socket serverSocket = new Socket(hostName, portNumber);
        out = new PrintWriter(serverSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

        try {
            out.println(username);

            while(true) {
                String msg = in.readLine();
                myHand = transformHand(msg);
                System.out.println(myHand);

                

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
       
    }
    private DLList<Card> transformHand(String s) {
        String[] array = s.split(",");
        DLList<Card> hand = new DLList<Card>();
        for (int i = 0; i< array.length; i++) {
            String[] split = array[i].split(" ");
            String color = split[0];
            String value = split[1];
            hand.add(new Card(color, value));
        }
        return hand;
    }

}
