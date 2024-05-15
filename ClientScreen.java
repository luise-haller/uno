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
    private DLList<Card> myHand, deck;
    private Card cardInPlay;

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
        deck = new DLList<Card>();
        cardInPlay = null;

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


        if (gameStarted) {
            if (cardInPlay != null) {
                g.setColor(Color.BLACK);
            g.drawString("Draw Pile", 500, 300);
            System.out.println("Card in Play " + cardInPlay.toString());
            
            drawCard(g, cardInPlay, 200, 300);
            }
            

        
        } else {
            if (!startGameButton.isVisible()) {
                g.drawString("IP address of server:", 220, 300);
                g.drawString("Your Player Name ", 220, 350);
            } else {
                int scaledWidth = (int) (logo.getWidth() * 0.2); 
                int scaledHeight = (int) (logo.getHeight() * 0.2);
                g.drawImage(logo, 270, 100, scaledWidth, scaledHeight, null);
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
                System.out.println("Turn? " + myTurn);
                
                String msg2 = in.readLine();
                System.out.println("msg from in.readLine = " + msg2);
                // System.out.println("New Top card: " + transformCard(msg2));
                cardInPlay = transformCard(msg2);
                
                

                
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

    private Card transformCard(String s) {
        String nS = s.substring(1, s.length()-1);
        String[] array = nS.split(" ");
        Card card = new Card(array[0], array[1]);
        return card;
    }
    
    
    private void drawCard(Graphics g, Card card, int x, int y) {
        //take in the value and color of the card
        //draws card graphically
        System.out.println(card.toString());
        if(card.getColor().equals("Yellow")) {
            g.setColor(Yellow);
        } else if(card.getColor().equals("Blue")) {
            g.setColor(Blue);
        } else if(card.getColor().equals("Red")) {
            g.setColor(Red);
        } else if(card.getColor().equals("Green")) {
            g.setColor(Green);
        } else if(card.getColor().equals("Black")) {
            g.setColor(Black);
        }
        g.fillRect(x, y, 120, 200);
        g.setColor(Color.BLACK);
        g.drawString(card.getValue() + "", x+55, y+95);
    }



}