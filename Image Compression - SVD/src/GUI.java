import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.BoxLayout;


@SuppressWarnings("serial")
public class GUI extends JPanel implements ChangeListener{

	private JButton selectButton;
	private JButton saveButton;
	private Image myImage;
	private JLabel picLabel;
	private JLabel textLabel;
	private JLabel creatorLabel;
	
	private JPanel panelFirst;
	private JPanel panelLast;
	
	public GUI() {
		setLayout(new BorderLayout());
		
		selectButton = new JButton("Select Image (Medium Size Prefered)");
	    add(selectButton, BorderLayout.CENTER);
		selectButton.setPreferredSize(new Dimension(70, 30));

		selectButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				selectBtn();
			} 
		} );
		
	}
	
	public void selectBtn()
	{
        String filename = File.separator+"tmp";
		JFileChooser fc = new JFileChooser(new File(filename));

		fc.showOpenDialog(this);
		File selFile = fc.getSelectedFile();
		
		String extension = null;
		
		if(selFile!=null)
		{
			String fileName = selFile.getName();
			
			if(fileName.endsWith("png"))
				extension = "png";
			else if(fileName.endsWith("PNG"))
				extension = "PNG";
			else if(fileName.endsWith("jpg"))
				extension = "jpg";
			else if(fileName.endsWith("JPG"))
				extension = "JPG";
				
			if(extension!=null)
			{
				openImage(selFile.getPath());
				selectButton.setText("Select New Image");
			}

		}
	}

	public void saveBtn()
	{
		JFileChooser jfc = new JFileChooser();
		int retVal = jfc.showSaveDialog(null);
		if(retVal==JFileChooser.APPROVE_OPTION){
			  
			File file = jfc.getSelectedFile();
			
			String fileName = file.getName();
			
			if (fileName.endsWith("png") ||
				fileName.endsWith("PNG") ||
				fileName.endsWith("jpg") ||
				fileName.endsWith("JPG") )
			{
				String ext = "" + fileName.charAt(fileName.length()-3) + fileName.charAt(fileName.length()-2) + fileName.charAt(fileName.length()-1);
			    myImage.saveImage(ext, file);
			}
			
			else
			{
			    File newFile = new File(file.getPath() + ".png");
			    myImage.saveImage("png", newFile);
			}
			
		
		}
	}

    public void stateChanged(ChangeEvent event) {
    	JSlider source = (JSlider) event.getSource();
        if (!source.getValueIsAdjusting()) {
        	textLabel.setText("Singular Values: " + (int) source.getValue());
            myImage.changeSize( (int) source.getValue());
            picLabel.setIcon(new ImageIcon(myImage.getImage()));
        }
    }

	public void openImage(String path)
	{
		removeAll();
		
		myImage = new Image(path);
		picLabel = new JLabel(new ImageIcon(myImage.getImage()));
		
		
		//SLIDER----------------------------------------------
		int height = myImage.getHeight();
		int width = myImage.getWidth();

	    JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, Math.min(width, height), Math.min(width, height));
	    textLabel = new JLabel("Singular Values: " + Math.min(width,height));
	    creatorLabel = new JLabel(" Created by Kounelis Agisilaos");


	    final int step = 25;
	    
        Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
    	table.put(new Integer(1),  new JLabel(Integer.toString(1)));
    	
        for(int i=step; i<=Math.min(width, height); i+=step)
        	table.put(new Integer(i),  new JLabel(Integer.toString(i)));


        slider.setLabelTable(table);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setPaintTrack(true);
        slider.setMajorTickSpacing(step);
        slider.setMinorTickSpacing(5);
        
	    slider.addChangeListener(this);
        //----------------------------------------------------
        
        
	    saveButton = new JButton("Save Image");
		
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveBtn();
			}
		});
		
		
        remove(selectButton);
        
        panelFirst = new JPanel();
        BoxLayout layout2 = new BoxLayout(panelFirst, BoxLayout.X_AXIS);
        panelFirst.setLayout(layout2);
        panelFirst.add(selectButton);
	    panelFirst.add(saveButton);
	    panelFirst.add(creatorLabel);
	    
	    
		panelLast = new JPanel();
        BoxLayout layout1 = new BoxLayout(panelLast, BoxLayout.X_AXIS);
        panelLast.setLayout(layout1);
        panelLast.add(textLabel);
	    panelLast.add(slider);

	    
	    add(panelFirst, BorderLayout.PAGE_START);
	    add(picLabel, BorderLayout.CENTER);
	    add(panelLast, BorderLayout.PAGE_END);
	    
	    
	    Main.packMe();
	    
	}
	
	
}
