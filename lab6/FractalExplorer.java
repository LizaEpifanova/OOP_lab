import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import javax.swing.JFileChooser.*;
import javax.swing.filechooser.*;
import javax.imageio.ImageIO.*;
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

private int rowsLost;


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
    myframe.add(imgDisplay, BorderLayout.CENTER);

ButtonHandler resetHandler = new ButtonHandler();
    resetButton.addActionListener(resetHandler);


MouseHandler click = new MouseHandler();
    imgDisplay.addMouseListener(click);

    myframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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

    // create a save button 
    saveButton = new JButton("Save fractal");

    ButtonHandler saveHandler = new ButtonHandler();
    saveButton.addActionListener(saveHandler);

    // create top and bottom panels with buttons and combo-box
    //top panel
    JPanel myBottomPanel = new JPanel();
    myBottomPanel.add(resetButton);
    myBottomPanel.add(saveButton);
    myframe.add(myBottomPanel, BorderLayout.SOUTH);

    // bottom panel
    JPanel myTopPanel = new JPanel();
    JLabel myLabel = new JLabel("Fractal:");
    myTopPanel.add(myLabel);
    myTopPanel.add(comboBox);
    myframe.add(myTopPanel, BorderLayout.NORTH);

    myframe.pack ();
    myframe.setVisible (true);
    myframe.setResizable (false);

}

private void drawFractal(){
    enableUI(false);
    rowsLost = windowSize;

    for (int y=0; y<windowSize; y++){
    FractalWorker drawRow = new FractalWorker(y);
    drawRow.execute();
}


    System.out.println(rowsLost);

}

private void enableUI(boolean val){
    comboBox.setEnabled(val);
    resetButton.setEnabled(val);
    saveButton.setEnabled(val);
}

private class ButtonHandler implements ActionListener{
    public void actionPerformed(ActionEvent e)
{
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
        javax.imageio.ImageIO.write(displayImage, "png", dir);
    }
    catch (Exception exception) {
        JOptionPane.showMessageDialog(imgDisplay, exception.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
    }
}

    else
        return;
}
}
}
private class MouseHandler extends MouseAdapter
{
    @Override
    public void mouseClicked(MouseEvent e){
    if (rowsLost != 0){
        return;
    }

    int x = e.getX();
    double xCoord = fracGenerate.getCoord(range.x, range.x + range.width, windowSize, x);

    int y = e.getY();
    double yCoord = fracGenerate.getCoord(range.y, range.y + range.height, windowSize, y);

    fracGenerate.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

    drawFractal();  
    }
}

public class FractalWorker extends SwingWorker<Object, Object>{
    private int y;
    private int rgbValueArray[];

    public FractalWorker(int row){
    y = row;
    }

@Override
    protected Object doInBackground() throws Exception {
    rgbValueArray = new int[windowSize];
    for (int i = 0; i < rgbValueArray.length; i++){
    double xCoord = fracGenerate.getCoord(range.x, range.x + range.width, windowSize, i);
    double yCoord = fracGenerate.getCoord(range.y, range.y + range.height, windowSize, y);
    int numIters = fracGenerate.numIterations(xCoord, yCoord);
    int rgbColor;
    if (numIters == -1){
    rgbColor = 0;
    }
    else{
        float hue = 0.7f + (float) numIters / 200f;
        rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
    }
        setNewNumberInArr(i, rgbColor);
    }
    return null;
}

private void setNewNumberInArr(int i, int num){
    if (i < rgbValueArray.length){
    rgbValueArray[i] = num;
}

}
@Override
    protected void done() {

    for (int i=0; i < rgbValueArray.length; i++){
        imgDisplay.drawPixel(i, y, rgbValueArray[i]);
}
    imgDisplay.repaint(0, 0, y, windowSize, 1);

    rowsLost--;
    if (rowsLost == 0){
    enableUI(true);
}

}
}
    public static void main(String[] args)
{
    FractalExplorer displayExplorer = new FractalExplorer(600);
    displayExplorer.createAndShowGUI();
    displayExplorer.drawFractal();
}

}
