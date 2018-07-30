import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.ojalgo.matrix.decomposition.SingularValue;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;

import java.awt.Color;

public class Image
{
	private BufferedImage originalImage, tempImage;
	
    private SingularValue<Double> svdRed;
    private SingularValue<Double> svdGreen;
    private SingularValue<Double> svdBlue;
    
    private int height;
    private int width;
    
    public int getHeight()
    {
    	return height;
    }
    public int getWidth()
    {
    	return width;
    }
    
    
    public Image(String path)
    {
		try {
			originalImage = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	tempImage = originalImage;
    	
        height = originalImage.getHeight();     //1080
        width = originalImage.getWidth();       //1920
        
        PrimitiveDenseStore red = PrimitiveDenseStore.FACTORY.makeEye(height, width);
        PrimitiveDenseStore green = PrimitiveDenseStore.FACTORY.makeEye(height, width);
        PrimitiveDenseStore blue = PrimitiveDenseStore.FACTORY.makeEye(height, width);
        
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                Color mycolor = new Color(originalImage.getRGB(j, i));

                red.set(i, j, mycolor.getRed());
                green.set(i, j, mycolor.getGreen());
                blue.set(i, j, mycolor.getBlue());
            }
        
        
		svdRed = SingularValue.make(red);
		svdRed.compute(red);
		
		svdGreen = SingularValue.make(green);
		svdGreen.compute(green);
		
		svdBlue = SingularValue.make(blue);
		svdBlue.compute(blue);
        
		red = null;
		green = null;
		blue = null;
    }
    
    public double[][] getTruncatedSVD(SingularValue<Double> svd, final int k) {
    	
    	MatrixStore<Double> U = svd.getQ1();
        PrimitiveDenseStore truncatedU = PrimitiveDenseStore.FACTORY.makeEye(height, k);
        for(int i=0;i<height;i++)
        	for(int j=0;j<k;j++)
        		truncatedU.set(i, j, U.doubleValue(i, j));
        
    	MatrixStore<Double> S = svd.getD();
        PrimitiveDenseStore truncatedS = PrimitiveDenseStore.FACTORY.makeZero(k, k);
        for(int i=0;i<k;i++)
        	truncatedS.set(i, i, S.doubleValue(i, i));
        
    	MatrixStore<Double> VT = svd.getQ2().transpose();
        PrimitiveDenseStore truncatedVT = PrimitiveDenseStore.FACTORY.makeEye(k, width);
        for(int i=0;i<k;i++)
        	for(int j=0;j<width;j++)
        		truncatedVT.set(i, j, VT.doubleValue(i, j));
                
        MatrixStore<Double> svdMatrix = (truncatedU.multiply(truncatedS)).multiply(truncatedVT);

        return svdMatrix.toRawCopy2D();
    }
    
    public void changeSize(int size){
    	
    	double[][] red = getTruncatedSVD(svdRed, size);
    	double[][] green = getTruncatedSVD(svdGreen, size);
    	double[][] blue = getTruncatedSVD(svdBlue, size);
    	
        
        tempImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0;i<height;i++)
            for(int j = 0;j < width;j++)
            {
            	if((int)red[i][j]>255)	red[i][j] = 255;
            	if((int)green[i][j]>255)	green[i][j] = 255;
            	if((int)blue[i][j]>255)	blue[i][j] = 255;
            	if((int)red[i][j]<0)	red[i][j] = 0;
            	if((int)green[i][j]<0)	green[i][j] = 0;
            	if((int)blue[i][j]<0)	blue[i][j] = 0;
            	
            	Color mycolor = new Color((int)red[i][j],(int)green[i][j],(int)blue[i][j]);

            	tempImage.setRGB(j, i, mycolor.getRGB());
            }
        
        
    }
    
    public void saveImage(String type, File outputfile)
    {
        
        try {
			ImageIO.write(tempImage, type, outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public BufferedImage getImage()
    {
    	return tempImage;
    }
    
}
