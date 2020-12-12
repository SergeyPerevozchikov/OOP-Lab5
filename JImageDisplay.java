import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.Dimension;

public class JImageDisplay extends JComponent
{
	public BufferedImage buffImg;
	
	public JImageDisplay(int width, int height)
	{
		this.buffImg = new BufferedImage(width, height, buffImg.TYPE_INT_RGB);
		Dimension dim = new Dimension(width, height);
		this.setPreferredSize(dim);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(this.buffImg, 0, 0, this.buffImg.getWidth(), this.buffImg.getHeight(), null);
	}
	
	public void clearImage()
	{
		for(int x = 0; x < this.buffImg.getWidth(); x++)
		{
			for(int y = 0; y < this.buffImg.getHeight(); y++)
			{
				this.buffImg.setRGB(x, y, 0);
			}
		}
		
	}
	
	public void drawPixel (int x, int y, int rgbColor)
	{
		this.buffImg.setRGB(x, y, rgbColor);
	}
	
}