// AlignTableFrame.java

package jmri.jmrix.rps.aligntable;

import java.awt.*;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * Frame for user configuration of RPS alignment.
 * <p>
 * We only allow one of these right now, and so don't dispose on close
 * 
 * @see AlignTableAction
 * 
 * @author	Bob Jacobsen   Copyright (C) 2008
 * @version	$Revision: 1.5 $
 */
public class AlignTableFrame extends jmri.util.JmriJFrame {

    ResourceBundle rb = ResourceBundle.getBundle("jmri.jmrix.rps.aligntable.AlignTableBundle");
		
    /**
     * Constructor method
     */
    public AlignTableFrame() {
    	super();
    }

    AlignTablePane p;
    
    /** 
     *  Initialize the window
     */
    public void initComponents() {
        setTitle(rb.getString("WindowTitle"));
	
	    setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	    
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
			

        // add table
        p = new AlignTablePane(this);
        p.initComponents();
        contentPane.add(p);
        
        // add help menu to window
    	addHelpMenu("package.jmri.jmrix.rps.aligntable.AlignTableFrame", true);

        // check at shutdown
        setShutDownTask();
        
        // pack for display
        pack();
    }

    protected void storeValues() {
        p.storeDefault();
        setModifiedFlag(false);
    }
    
}
