import javax.swing.JPanel;
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
import javax.swing.JOptionPane;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import java.io.*;
import java.net.*;
import java.awt.*;

import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ClientScreen extends JPanel implements ActionListener, MouseListener {
    private PrintWriter out;
    private BufferedReader in;

    private BufferedImage background, logo;
    private DLList<Card> myHand;
    private Card cardInPlay, cardSelected;

    private String hostName, username, playerName, playerNameOneCard, playerNameWon;
    private String currentPlayerName;
    private boolean myTurn, gameStarted, displayRules, connectionScreenShown, endGameScreenShown;
    private boolean skipNotification, reverseNotification, oneCardNotification, wonAnnouncement;
    private boolean startCheckingForUnoOrDoubleUno;
    private boolean canStack;
    private int threadID;
    private boolean thisClientWon;

    private JButton startGameButton, submitButton, rulesButton, doneButton, restartButton;
    private JTextField ipAddressField, usernameField;


    private Color Yellow = new Color(255, 225, 0);
    private Color Blue = new Color(0, 76, 255);
    private Color Red = new Color(255, 4, 0);
    private Color Green = new Color(72, 255, 0);
    private Color Black = new Color(0, 0, 0);
    private DLList<Card> twoCardsToAdd;
    private DLList<Card> fourCardsToAdd;

    public ClientScreen() {
        setLayout(null);
        this.setFocusable(true);
        addMouseListener(this);

        //Instantiate this client's hand
        myHand = new DLList<Card>();
        cardInPlay = null; cardSelected = null; 
        myTurn = false;
        gameStarted = false;
        displayRules = false;
        endGameScreenShown = false;
        thisClientWon = false;
        canStack = false;

        skipNotification = false;
        reverseNotification = false;
        oneCardNotification = false;
        wonAnnouncement = false;
        connectionScreenShown = true;
        startCheckingForUnoOrDoubleUno = false;

        twoCardsToAdd = new DLList<Card>();
        fourCardsToAdd = new DLList<Card>();
        
        startGameButton = new JButton();
        startGameButton.setFont(new Font("Arial", Font.BOLD, 16));
        startGameButton.setHorizontalAlignment(SwingConstants.CENTER);
        startGameButton.setText("PLAY");
        startGameButton.setBounds(350, 500, 100, 50);
        this.add(startGameButton);
        startGameButton.addActionListener(this);
        startGameButton.setVisible(true);

        restartButton = new JButton();
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.setHorizontalAlignment(SwingConstants.CENTER);
        restartButton.setText("Restart");
        restartButton.setBounds(320, 450, 100, 50);
        this.add(restartButton);
        restartButton.addActionListener(this);
        restartButton.setVisible(false);

        submitButton = new JButton();
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setHorizontalAlignment(SwingConstants.CENTER);
        submitButton.setText("Submit");
        submitButton.setBounds(350, 450, 100, 50);
        this.add(submitButton);
        submitButton.addActionListener(this);
        submitButton.setVisible(false);

        rulesButton = new JButton();
        rulesButton.setFont(new Font("Arial", Font.BOLD, 16));
        rulesButton.setHorizontalAlignment(SwingConstants.CENTER);
        rulesButton.setText("Rules");
        rulesButton.setBounds(10, 540, 100, 50); 
        this.add(rulesButton);
        rulesButton.addActionListener(this);
        rulesButton.setVisible(true);

        doneButton = new JButton(); 
        doneButton.setFont(new Font("Arial", Font.BOLD, 16));
        doneButton.setHorizontalAlignment(SwingConstants.CENTER);
        doneButton.setText("Done");
        doneButton.setBounds(350, 500, 100, 50);
        this.add(doneButton);
        doneButton.addActionListener(this);
        doneButton.setVisible(false); 

        ipAddressField = new JTextField();
        ipAddressField.setBounds(400, 285, 100, 30);
        ipAddressField.setText("localhost");
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startGameButton) {
            startGameButton.setVisible(false);
            ipAddressField.setVisible(true);
            usernameField.setVisible(true);
            submitButton.setVisible(true);
        } else if (e.getSource() == submitButton) {
            this.hostName = ipAddressField.getText();
            this.username = usernameField.getText();
            this.playerName = usernameField.getText();
            this.gameStarted = true;
        } else if (e.getSource() == rulesButton) {
            this.displayRules = true;
        } else if (e.getSource() == doneButton) {
            this.displayRules = false;
            doneButton.setVisible(false);
        } else if (e.getSource() == restartButton) {
            restartButton.setVisible(false);
            cardInPlay = null; cardSelected = null; 
            myTurn = false;
            gameStarted = true;
            displayRules = false;
            endGameScreenShown = false;

            skipNotification = false;
            reverseNotification = false;
            oneCardNotification = false;
            wonAnnouncement = false;
            connectionScreenShown = false; // connecting screen is only shown before the first round
            startCheckingForUnoOrDoubleUno = false;
            thisClientWon = false;

            // Clear myHand
            for (int i = 0; i < myHand.size(); i++) {
                myHand.remove(i);
            }
            out.println(username);
            out.println("play");
        }
        repaint();
    }
    private void iWent() {
        out.println("Done" + threadID);
    }
    public void connect() throws IOException {
        int portNumber = 1024;
        Socket serverSocket = new Socket(hostName, portNumber);
        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        out = new PrintWriter(serverSocket.getOutputStream(), true);
        String msg = "";
        try {
            out.println(username);   //sends username to ServerThread
            out.println("play");   //this message is sent to ServerThread in order to call manager.startGame
            connectionScreenShown = false;
            while (true) {
                msg = in.readLine();
                // System.out.println("Message from Server\n" + msg);
                if(gameStarted) {
                    processServerMessage(msg); //See processServerNessage private method
                    if (startCheckingForUnoOrDoubleUno) {
                        if (myHand.size() == 1) { 
                            out.println("OneCardWarning" + this.playerName);
                        } else if (myHand.size() == 0) {
                            System.out.println("This player won!");
                            out.println("WonWarning" + this.playerName);
                            thisClientWon = true;
                        }
                    }
                }
                repaint();
            }
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
    private void processServerMessage(String msg) {
        if (msg.startsWith("ID")) {
            threadID = Integer.parseInt(msg.substring(2));
        } else if (msg.substring(0, 4).equals("Hand")) { 
            myHand = transformHand(msg.substring(4));
        } else if (msg.substring(0, 3).equals("Top")) {
            cardInPlay = transformCard(msg.substring(3));
        } else if (msg.equals("Your Turn")) {
            this.myTurn = true;
            out.println("ThisIsMyTurn" + username); 
        } else if (msg.startsWith("Username")) {
            this.playerName = msg.substring(8);
        } else if (msg.startsWith("FromDraw")) {
            myHand.add(transformCard(msg.substring(8)));
        } else if (msg.startsWith("NextClient")) {
            int id = Integer.parseInt(msg.substring(10));
            if(id == this.threadID) {
                myTurn = true;
                out.println("ThisIsMyTurn" + username); // send out that it's this client's turn
                skipNotification = false;
                reverseNotification = false;
                oneCardNotification = false;
                wonAnnouncement = false;
            } else {
                myTurn = false;
            }
        } else if (msg.startsWith("Top")) {
            cardInPlay = transformCard(msg.substring(3));
        } else if (msg.startsWith("MustDrawTwo")) {
            //Client is only given 2 cards if they DO NOT have a Draw 2 in their hand
            for (int i = 0; i< myHand.size(); i++) {
                if(myHand.get(i).getValue().equals("DrawTwo")) {
                    canStack = true;
                }
            }
            if(!canStack) {
                System.out.println("this shouldn't be printing");
                twoCardsToAdd = transformHand(msg.substring(11));
                myHand.add(twoCardsToAdd.get(0)); myHand.add(twoCardsToAdd.get(1));
                twoCardsToAdd.clear();
                iWent();
            } else {
                System.out.println("Client #" + threadID + " can stack plus 2s");
            }
            
        } else if (msg.startsWith("MustDrawFour")) {
            fourCardsToAdd = transformHand(msg.substring(12));
            myHand.add(fourCardsToAdd.get(0)); myHand.add(fourCardsToAdd.get(1)); 
            myHand.add(fourCardsToAdd.get(2)); myHand.add(fourCardsToAdd.get(3));
            fourCardsToAdd.clear();
            iWent();
        } else if(msg.startsWith("2sStacked")){
            fourCardsToAdd = transformHand(msg.substring(9));
            System.out.println("Client #" + threadID + " has received stacked 2s");
            myHand.add(fourCardsToAdd.get(0)); myHand.add(fourCardsToAdd.get(1)); 
            myHand.add(fourCardsToAdd.get(2)); myHand.add(fourCardsToAdd.get(3));
            fourCardsToAdd.clear();
        }else if(msg.equals("Skipped")) {
            skipNotification = true;
            oneCardNotification = false;
            reverseNotification = false;
        } else if(msg.equals("OrderReversed")) {
            reverseNotification = true;
            skipNotification = false;
            oneCardNotification = false;
        } else if(msg.startsWith("GlobalOneCardWarning")) {
            playerNameOneCard = msg.substring(20);
            oneCardNotification = true;
            skipNotification = false;
            reverseNotification = false;
        } else if (msg.startsWith("WonAnnouncement")) {
            playerNameWon = msg.substring(15);
            wonAnnouncement = true;
            oneCardNotification = false;
            skipNotification = false;
            reverseNotification = false;
        } else if (msg.equals("CardsWereDealt")) {
            startCheckingForUnoOrDoubleUno = true;
        } else if (msg.startsWith("CurrentPlayerName")) {
            currentPlayerName = msg.substring(17);
        }
        repaint();
    }
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);


        if (gameStarted) {
            submitButton.setVisible(false);
            ipAddressField.setVisible(false);
            usernameField.setVisible(false);
            if (this.playerName != null && this.currentPlayerName != null) {
                g.setColor(Color.WHITE);
                g.drawString("Player: " + this.playerName, 30, 30);
                g.drawString("Current Player: " + this.currentPlayerName, 30, 50);
                //g.drawString("Thread #" + this.threadID, 30, 50);
            }
            if (cardInPlay != null) {
                g.setColor(Color.BLACK);
                g.fillRect(450, 100, 100, 150);
                
                g.setColor(Color.WHITE);
                g.drawString("Draw Pile", 480, 280);
                g.drawString("Play Pile", 300, 280);
                g.drawRect(450, 100, 100, 150);
                g.drawImage(logo, 465, 130, 70, 70, null);
                drawCard(g, cardInPlay, 270, 100);

                if(myHand.size() >= 0) {
                    for (int i = 0; i<myHand.size();i++) {
                        drawCard(g, myHand.get(i), 100 + i*55, 400);
                    }
                }

                
            }
            if (skipNotification) {
                g.drawString("You have been skipped!", 280, 370);
            }
            if(reverseNotification) {
                g.drawString("Order Reversed", 280, 370);
            }
            if (canStack) {
                g.drawString("You can only play a Draw 2 Card", 200, 370);
            }
            if (oneCardNotification && playerNameOneCard != null) {
                g.drawString("Player " + playerNameOneCard + " has one card left!", 280, 370);
            } if (wonAnnouncement && playerNameWon != null) {
                gameStarted = false;
                endGameScreenShown = true;
            }
            oneCardNotification = false;
        } else if (connectionScreenShown) {
            if (!startGameButton.isVisible()) {
                g.setColor(Color.WHITE);
                g.drawString("IP address of server:", 220, 300);
                g.drawString("Your Player Name: ", 220, 350);
            } else {
                int scaledWidth = (int) (logo.getWidth() * 0.2); 
                int scaledHeight = (int) (logo.getHeight() * 0.2);
                g.drawImage(logo, 270, 100, scaledWidth, scaledHeight, null);
            }
        } else if (endGameScreenShown) { // End Screen
            g.setColor(new Color(129, 137, 219));
            g.fillRect(0, 0, 800, 600);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("Game Ended", 200, 200);
            g.setFont(new Font("Arial", Font.PLAIN, 30));
            g.drawString(playerNameWon + " won!", 300, 300);
            restartButton.setVisible(true);
            if (thisClientWon) { // if this client won - > play win sound
                this.playWinningSound();
            } else { // if this client lost - > play loosing sound
                this.playLosingSound();
            }
        }
        if (displayRules) {
            drawRulebook(g);
            doneButton.setVisible(true);
            startGameButton.setVisible(false);
            
        }
    }
    private void playWinningSound() {
        try {
            URL url = this.getClass().getClassLoader().getResource("won.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            clip.start();
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }
    private void playLosingSound() {
        try {
            URL url = this.getClass().getClassLoader().getResource("lost.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            clip.start();
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }
    public void mousePressed(MouseEvent e) {        
        if (myTurn) {
            // out.println("ThisIsMyTurn" + username); // send out that it's this client's turn
            if(myHand.size() >= 0) {
                if (myHand.size() > 20) {
                    // actually kick them out of this round
                    System.out.println("You have more than 20 cards! You are being kicked out of the game.");
                    return;
                }
                for (int i = 0; i<myHand.size();i++) {
                    if (e.getY() > 400 && e.getY() < 550) {
                        int last = myHand.size()-1;
                        if (i == last) {
                            if (e.getX() >= 100 +i*55 && e.getX() < 200 + i*55) {
                                if(!canStack) {
                                    cardSelected = myHand.get(i);
                                } else {
                                    if(myHand.get(i).getValue().equals("DrawTwo")) {
                                        cardSelected = myHand.get(i);
                                        System.out.println("Sending msg to server saying stack");
                                        //Send a message to server saying that this client is stacking
                                        out.println("Stack");
                                        canStack = false;
                                    }
                                }                                
                            }    
                        } else {
                            if (e.getX() >= 100 +i*55 && e.getX() < 155 + i*55) {
                                if(!canStack) {
                                    cardSelected = myHand.get(i);
                                } else {
                                    if(myHand.get(i).getValue().equals("DrawTwo")) {
                                        cardSelected = myHand.get(i);
                                        System.out.println("Sending msg to server saying stack");
                                        //Send a message to server saying that this client is stacking
                                        out.println("Stack");
                                        canStack = false;
                                    }
                                }      
                            }
                        }
                    }
                }
            }
            // If clicked on draw pile
            if (e.getX() >= 450 && e.getX() <= 550 && e.getY() >= 100 && e.getY() <= 250) {
                out.println("DrawCard");
            }
            if (cardSelected != null) {
                if (canPlayCard(cardSelected, cardInPlay)) {
                    playCardFromHand(cardSelected); // Plays the card

                    // All the different fancy card effects of being played are handeled below
                    // Remember: Now cardSelected = cardInPlay!!
                    if (cardInPlay.getColor().equals("Black")) {
                        if (cardInPlay.getValue().equals("DrawFourWild")) {
                            // pop up field for client to enter chosen color
                            // msg that this card was played PLUS chosen color gets send to serverThread
                            JTextField chosenColor = new JTextField();
                            Object[] mes = {
                                "What Color Do You Choose?", chosenColor
                            };
                            int option = JOptionPane.showConfirmDialog(null, mes, "Draw 4 Wild Card", JOptionPane.OK_CANCEL_OPTION);
                            if (option == JOptionPane.OK_OPTION) {
                                String color = chosenColor.getText();
                                cardInPlay.setColor(color);
                                out.println("DrawFourWildWasPlayed" + color);
                            }

                        } else if (cardInPlay.getValue().equals("WildCard")) {
                            // pop up field for client to enter chosen color
                            // msg that this card was played PLUS chosen color gets send to serverThread
                            JTextField chosenColor = new JTextField();
                            Object[] mes = {
                                "What Color Do You Choose?", chosenColor
                            };
                            int option = JOptionPane.showConfirmDialog(null, mes, "Wild Card", JOptionPane.OK_CANCEL_OPTION);
                            if (option == JOptionPane.OK_OPTION) {
                                String color = chosenColor.getText();
                                cardInPlay.setColor(color);
                                out.println("WildCardWasPlayed" + color);
                            }
                        }
                    } else if (cardInPlay.getValue().equals("Reverse")) {
                        out.println("ReverseCardWasPlayed");
                    } else if (cardInPlay.getValue().equals("Skip")) {
                        out.println("SkipCardWasPlayed");
                    } else if (cardInPlay.getValue().equals("DrawTwo")) {
                        out.println("DrawTwoCardWasPlayed");
                    } 
                }
            }
            out.println("Update"+cardInPlay.toString());
            myTurn = false;
            iWent();
        }
        repaint();
        
    }
    //Private Methods to Play a card from hand and replace cardInPlay
    private boolean canPlayCard(Card selected, Card inPlay) {
        boolean canPlay = false;
        if (!selected.getColor().equals("Black") // if NOT a wild card OR reverse/skip/draw2 value 
        && !selected.getValue().equals("Reverse") 
        && !selected.getValue().equals("Skip") 
        && !selected.getValue().equals("DrawTwo") ) { 
            System.out.println("Regular Card");
            canPlay = selected.getColor().equals(inPlay.getColor()) || selected.getValue().equals(inPlay.getValue());
        } else if (selected.getColor().equals("Black")) { // if a wild card - > can always place this on cardInPlay!
            System.out.println("Black Card");
            canPlay = true;
        } else if (selected.getValue().equals("Reverse")) { 
            System.out.println("Reverse Card");
            // Reverse can be played on matching color or on another Reverse card
            canPlay = selected.getColor().equals(inPlay.getColor()) || inPlay.getValue().equals("Reverse");
        } else if (selected.getValue().equals("Skip")) { 
            System.out.println("Skip Card");
            // Skip can be played on matching color or on another Skip Card
            canPlay = selected.getColor().equals(inPlay.getColor()) || inPlay.getValue().equals("Skip");
        } else if (selected.getValue().equals("DrawTwo")) {
            System.out.println("Draw 2 Card");
            // Draw 2 can be played on matching color or on another Draw 2 Card
            canPlay = selected.getColor().equals(inPlay.getColor()) || inPlay.getValue().equals("DrawTwo");
        }
        return canPlay;
    }
    private void playCardFromHand(Card selected) {
        this.cardInPlay = selected;
        //Send cardInPlay to server thread
        out.println("ChangeCard" + cardInPlay.toString());
        myHand.remove(selected);
        cardSelected = null;
        
        repaint();
    }

    public boolean getGameStarted() {
        return this.gameStarted;
    }

    
    //Draws card Gaphically
    private void drawCard(Graphics g, Card card, int x, int y) {
        //take in the value and color of the card
        //draws card graphically
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
        } else {
            System.out.println("This color does not exist");
        }
        g.fillRect(x, y, 100, 150);
        g.setColor(Color.WHITE);
        if(card.getValue().equals("DrawTwo")) {
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("+2", x+5, y+20);
        } else if(card.getValue().equals("Skip")) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Skip",  x+5, y+20);
        } else if(card.getValue().equals("Reverse")) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Reverse",  x+5, y+20);
        } else if(card.getValue().equals("DrawFourWild")) {
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("4+",  x+5, y+20);
        } else if(card.getValue().equals("WildCard")) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Wild Card!",  x+5, y+20);
        } else {
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString(card.getValue() + "", x+5, y+20);
            g.drawString(card.getValue() + "", x+80, y+140);
        }
        
        g.drawRect(x, y, 100, 150);
        g.drawImage(logo, x+15, y+30, 70, 70, null);
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
        String[] nS = s.split(" ");
        String color = nS[0];
        String value = nS[1];
        Card card = new Card(color, value);
        return card;
    }


    private void drawRulebook(Graphics g) {
        g.setColor(new Color(34, 82, 52));
        g.fillRect(50, 50, 700, 500);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Rulebook", 350, 100);

        String[] rules = {
            "1. Each player is dealt 7 cards.",
            "2. The top card of the deck is placed face up to start the discard pile.",
            "3. Players take turns matching a card from their hand with the current card.",
            "4. Cards can be matched by number, color, or symbol.",
            "5. Special cards can change the flow of the game.",
            "6. If a player cannot match a card, they must draw a card from the deck.",
            "7. The first player to get rid of all their cards wins.",
            "8. You can only stack Draw Two's once."
        };

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        int yPosition = 150;
        for (String rule : rules) {
            g.drawString(rule, 100, yPosition);
            yPosition += 30;
        }
    }
    
    public void mouseReleased(MouseEvent e) {   }
    public void mouseEntered(MouseEvent e) {   }
    public void mouseExited(MouseEvent e) {   }
    public void mouseClicked(MouseEvent e) {   }
}
