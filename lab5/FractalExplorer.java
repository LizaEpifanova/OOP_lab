import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import javax.swing.JFileChooser.*;
import javax.swing.filechooser.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;
import javax.swing.filechooser.FileFilter;


public class FractalExplorer {
    private int windowSize;
    private JImageDisplay imgDisplay;
    private FractalGenerator fracGenerate;
    private Rectangle2D.Double range;

    private JButton resetButton;
    private JButton saveButton;
    private JComboBox comboBox;

    public FractalExplorer(int size){
        windowSize = size;
        fracGenerate = new Mandelbrot();
        range = new Rectangle2D.Double();
        fracGenerate.getInitialRange(range);
        imgDisplay = new JImageDisplay(windowSize, windowSize);
    }

    public void createAndShowGUI(){
        imgDisplay.setLayout(new BorderLayout());
        // create main frame
        JFrame myframe = new JFrame("Fractal Explorer"); 
        myframe.add(imgDisplay, BorderLayout.CENTER);
        // create a reset button
        resetButton = new JButton("Reset Display");
        
        ButtonHandler resetHandler = new ButtonHandler();
        resetButton.addActionListener(resetHandler);

        saveButton = new JButton("Save fractal");
        
        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);
        
        // create a combo-box
        comboBox = new JComboBox();
        FractalGenerator mandelbrot = new Mandelbrot();
        comboBox.addItem(mandelbrot);
        FractalGenerator tricorn = new Tricorn();
        comboBox.addItem(tricorn);
        FractalGenerator burningShip = new BurningShip();
        comboBox.addItem(burningShip);
         // bind chooser with combo-box
         ButtonHandler fractalChooser = new ButtonHandler();
         comboBox.addActionListener(fractalChooser);


        MouseHandler click = new MouseHandler();
        imgDisplay.addMouseListener(click);

        myframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // create top and bottom panels with buttons and combo-box
        //top panel
        JPanel myTopPanel = new JPanel();
        
        JLabel myLabel = new JLabel("Fractal:");
        myTopPanel.add(myLabel);
        myTopPanel.add(comboBox);
        myframe.add(myTopPanel, BorderLayout.NORTH);
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(resetButton);
        
        myBottomPanel.add(saveButton);
        myframe.add(myBottomPanel, BorderLayout.SOUTH);

        myframe.pack ();
        myframe.setVisible (true);
        myframe.setResizable (false);

    }

    public void drawFractal()
	{
		for (int x=0; x<windowSize; ++x){
			for (int y=0; y<windowSize; ++y){        
				double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, windowSize, x);
				double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, windowSize, y);  	
				//System.out.println(xCoord+ " " +yCoord);
				int iter = fracGenerate.numIterations(xCoord, yCoord);
				if (iter < 0) {
					imgDisplay.drawPixel(x, y, 0);
					continue;
				}
				float hue = 0.7f + (float) iter / 200f;
				int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
				imgDisplay.drawPixel(x, y, rgbColor);
			}
		}
		imgDisplay.repaint();
	}
	class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Reset Display")) {
                fracGenerate.getInitialRange(range);
                drawFractal();
            }
            else if (e.getSource() instanceof JComboBox) {
                JComboBox mySource = (JComboBox) e.getSource();
                fracGenerate = (FractalGenerator) mySource.getSelectedItem();
                fracGenerate.getInitialRange(range);
                drawFractal();
            }
            else if(command.equals("Save fractal")){
                JFileChooser fileChooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                fileChooser.setFileFilter(filter);
                fileChooser.setAcceptAllFileFilterUsed(false);

                int userchoise = fileChooser.showSaveDialog(imgDisplay);
                if (userchoise == JFileChooser.APPROVE_OPTION){
                    File dir = fileChooser.getSelectedFile();

                    try {
                        BufferedImage displayImage = imgDisplay.getImage();
                        ImageIO.write(displayImage, "png", dir);
                    }
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(imgDisplay, exception.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
		}	
	}
	class MouseHandler extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			double x = FractalGenerator.getCoord(range.x, range.x+range.width, windowSize, e.getX());
			double y = FractalGenerator.getCoord(range.y, range.y+range.height, windowSize, e.getY());
			fracGenerate.recenterAndZoomRange(range, x, y, 0.5);
			drawFractal();
		}
	}
    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(500);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }

}
