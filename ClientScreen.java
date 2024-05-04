import java.io.IOException;

import javax.swing.JPanel;

public class ClientScreen extends JPanel{

    public void connect() throws IOException{
        
        try {

            repaint();
            
        } catch(IOException e) {
			System.err.println("Couldn't get I/O for the connection");
			System.exit(1);
		}
    }
}