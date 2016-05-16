/* 
 * The MIT License
 *
 * Copyright 2016 Jan Zípek <jan at zipek.cz>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cz.zipek.minicloud;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author Jan Zípek
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
