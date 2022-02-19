import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame(){
        this.add(new GamePanel()); // Instance of game frame is created
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();//takes the j frame and fit it snugly around all the components that we add to the frame
        this.setVisible(true);
        this.setLocationRelativeTo(null); //to make it appear at the middle of the screen
    }
} 