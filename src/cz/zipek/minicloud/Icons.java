/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zipek.minicloud;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author Kamen
 */
public class Icons {
    
    public static boolean inicialized = false;
    
    public static ImageIcon iconFolder;
    public static ImageIcon iconFile;
    public static ImageIcon iconArchive;
    public static ImageIcon iconDocument;
    public static ImageIcon iconImage;
	
	public static List<Image> logo;
    
	/**
	 * Returns icon appropriate to extension.
	 * 
	 * @param ext file extension
	 * @return extension icon
	 */
    public static ImageIcon getIcon(String ext)
    {
        if (!inicialized) {
			inicialize();
        }
        
        if (ext == null) {
            return iconFolder;
        }
        
        switch(ext.toLowerCase())
        {
            case "rar":
            case "zip":
            case "tg":
            case "7z":
                return iconArchive;
            case "doc":
            case "docx":
            case "txt":
            case "rtf":
            case "odt":
                return iconDocument;
            case "gif":
            case "png":
            case "jpg":
            case "jpeg":
                return iconImage;
        }
        
        return iconFile;
    }
	
	public static List<Image> getLogo()
	{
		if (!inicialized) {
			inicialize();
        }
		
		return logo;
	}
	
	/**
	 * Loads icons from resources.
	 */
	private static void inicialize() {
		Icons ic = new Icons();
		iconFolder = new ImageIcon(ic.getClass().getResource("/cz/zipek/minicloud/res/folder.png"));
		iconFile = new ImageIcon(ic.getClass().getResource("/cz/zipek/minicloud/res/file.png"));
		iconArchive = new ImageIcon(ic.getClass().getResource("/cz/zipek/minicloud/res/archive.png"));
		iconDocument = new ImageIcon(ic.getClass().getResource("/cz/zipek/minicloud/res/document.png"));
		iconImage = new ImageIcon(ic.getClass().getResource("/cz/zipek/minicloud/res/image.png"));
		
		logo = new ArrayList<>();
		logo.add(new ImageIcon(ic.getClass().getResource("/cz/zipek/minicloud/res/logo.png")).getImage());
		logo.add(new ImageIcon(ic.getClass().getResource("/cz/zipek/minicloud/res/logo40.png")).getImage());
		logo.add(new ImageIcon(ic.getClass().getResource("/cz/zipek/minicloud/res/logo32.png")).getImage());
		logo.add(new ImageIcon(ic.getClass().getResource("/cz/zipek/minicloud/res/logo20.png")).getImage());
		
		inicialized = true;
	}
}
