
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FractalExplorer extends JFrame
{
	private int size;
	public JImageDisplay imageDisp;
	private FractalGenerator generator;
	private Rectangle2D.Double range;
	
	public FractalExplorer(int size)
	{
		this.size = size;
		this.range = new Rectangle2D.Double();
		this.generator = new Mandelbrot();
		this.generator.getInitialRange(range);
	}
	
	public void createAndShowGUI()
	{
		JFrame frame = new JFrame("Fractal");
        JButton reset = new JButton("Reset");
		JButton save = new JButton("Save Image");
		JPanel buttons = new JPanel();
		buttons.add(save);
		buttons.add(reset);
		
		JComboBox<FractalGenerator> box = new JComboBox<>();
		box.addItem(new Mandelbrot());
		box.addItem(new Tricorn());
		box.addItem(new BurningShip());
		JLabel label = new JLabel("Fractal: ");
		JPanel panel = new JPanel();
		panel.add(label);
		panel.add(box);
		
		imageDisp = new JImageDisplay(size,size);
		
		ResetEvent resetEvent = new ResetEvent();
        MouseHandler mouseHandler = new MouseHandler();
		imageDisp.setLayout(new BorderLayout());
		
		ChangeEvent changeEvent = new ChangeEvent();
		SaveEvent saveEvent = new SaveEvent();
		
		imageDisp.addMouseListener(mouseHandler);
        reset.addActionListener(resetEvent);
		box.addActionListener(changeEvent);
		
		save.addActionListener(saveEvent);
		
		frame.add(imageDisp, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.NORTH);
		frame.add(buttons, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		
	}
	
	private void drawFractal()
	{
		for(int x = 0; x < size; x++)
		{
            for(int y = 0; y < size; y++)
			{
				double xCoord  =  FractalGenerator.getCoord(range.x,  range.x  +  range.width, size, x);
				double yCoord  =  FractalGenerator.getCoord(range.y,  range.y  +  range.height, size, y);
				int iteration = generator.numIterations(xCoord,yCoord);
				if (iteration == -1)
				{
					imageDisp.drawPixel(x,y,0);
				}
				else
				{
					float hue = 0.7f + (float) iteration / 200f; 
					int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
					imageDisp.drawPixel(x,y,rgbColor);
				}
			}
		}
		imageDisp.repaint();
	}
	
	private class ResetEvent implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
            generator.getInitialRange(range);
            drawFractal();
        }
    }
	
	private class ChangeEvent implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
			JComboBox box = (JComboBox)e.getSource();
			String type = box.getSelectedItem().toString();
			if(type == "Mandelbrot")
			{
				generator = new Mandelbrot();
			}
			if(type == "Tricorn")
			{
				generator = new Tricorn();
			}
			if(type == "Burning Ship")
			{
				generator = new BurningShip();
			}
            generator.getInitialRange(range);
            drawFractal();
        }
    }
	
	private class SaveEvent implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser(); 
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png"); 
			chooser.setFileFilter(filter); 
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.showSaveDialog(null);
			try
			{
				ImageIO.write(imageDisp.buffImg, "png", chooser.getSelectedFile());
			}
			catch(IOException io)
			{
				JOptionPane.showMessageDialog(null, io.getMessage(), "Can not Save Image", JOptionPane.ERROR_MESSAGE);
			}
			catch(IllegalArgumentException ilArg)
			{
				
			}
        }
    }
	
	private class MouseHandler extends MouseAdapter{
        
        public void mouseClicked(MouseEvent e)
        {
            int x = e.getX();
            int y = e.getY();
            double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, size, x);
            double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, size, y);
            generator.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }
	
	public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(800);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }
	
}