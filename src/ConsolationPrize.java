import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//implements interface per project reqs
public class ConsolationPrize implements Award{

    //if player hits X points, displays a JOptionPane with their consolation prize
    public int updatePoints(Player player, Boolean hasPoints){
        if(player.getPoints() > 0){
            Image temp;
            ImageIcon grandP = new ImageIcon();
            BufferedImage img;
            try{
                img = ImageIO.read(new File("src//Images//potato.png"));//use of image per project reqs
                temp = img.getScaledInstance(300, 189, Image.SCALE_DEFAULT);
                grandP = new ImageIcon(temp);
            }catch(IOException e){
                JOptionPane.showMessageDialog(null, "Oops something went wrong");
            }
            JOptionPane.showMessageDialog(null, player.getFirstName() +
                    ", Here's what you've won for playing", "Grand Prize",
                    JOptionPane.INFORMATION_MESSAGE, grandP);
        }
        return 0;
    }
}
