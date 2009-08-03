// SpecificSensorManager.java

package jmri.jmrix.powerline.cp290;

import jmri.Sensor;
import jmri.jmrix.powerline.X10Sequence;
import jmri.jmrix.powerline.SerialReply;
//import jmri.jmrix.powerline.SerialAddress;
//import jmri.jmrix.powerline.cp290.Constants;

/**
 * Manage the system-specific Sensor implementation.
 * <P>
 * System names are "PSann", where a is the unit id, nn is the unit number without padding.
 * <P>
 * Sensors are not created automatically as there are frequently other X10 codes
 * seen on the wire that you don't want in your panels.
 * <P>
 * Created from the cm11 version
 * <P>
 * @author			Bob Jacobsen Copyright (C) 2003, 2006, 2007, 2008
 * @author			Ken Cameron, (C) 2009, sensors from poll replies
 * @version			$Revision: 1.1 $
 */
public class SpecificSensorManager extends jmri.jmrix.powerline.SerialSensorManager {

    public SpecificSensorManager() {
        super();
    }
    
    /**
     *  Process a reply to a poll of Sensors of one node
     */
    public synchronized void reply(SerialReply r) {
        // process for updates
    	processForPollReq(r);
    }
    
    private void processForPollReq(SerialReply m) {
    	boolean goodSync = true;
    	boolean goodCheckSum = true;
    	int sum = 0;
    	String newHouseCode = null;
    	int newCmdCode = -1;
    	int newAddrCode = -1;
    	Sensor sensor = null;
    	if (m.getNumDataElements() == 12) {
	    	for (int i = 0; i < 6; i++) {
	    		if ((m.getElement(i) & 0xFF) != 0xFF) {
	    			goodSync = false;
	    		}
	    	}
	    	for (int i = 7; i < 11; i++) {
	    		sum = (sum + (m.getElement(i) &0xFF)) & 0xFF;
	    	}
	    	if (sum != (m.getElement(11) & 0xFF)) {
	    		goodCheckSum = false;
	    	}
	    	newCmdCode = m.getElement(7) & 0x0F;
	    	newHouseCode = X10Sequence.houseCodeToText((m.getElement(7) >> 4) & 0x0F);
	    	newAddrCode = (m.getElement(8) & 0x00FF) + ((m.getElement(9) & 0x00FF) << 8);
	    	if (goodSync && goodCheckSum && newHouseCode != "" && newAddrCode != -1 && newCmdCode != -1) {
	    		int unitMask = 1 << 16;
	    		int unitCnt = 0;
	    		while (unitMask > 0 ) {
	    			unitMask = unitMask >> 1;
	    			unitCnt++;
	    			int hCode = newAddrCode & unitMask;
	            	if (newCmdCode != -1 && newHouseCode != null && hCode != 0) {
	            		String sysName = systemLetter() + "S" + newHouseCode + unitCnt;
	            		sensor = provideSensor(sysName);
	            		// see if sensor exists, comment out for production, I'm using it for testing
	//		            		if (sensor == null) {
	//		                    	sensor = newSensor(sysName.toUpperCase(), null); 
	//		            		}
	            		if (sensor != null) {
	            			if (newCmdCode == X10Sequence.FUNCTION_ON || newCmdCode == X10Sequence.FUNCTION_BRIGHT || newCmdCode == X10Sequence.FUNCTION_STATUS_ON) {
	            				try {				
	            					sensor.setKnownState(Sensor.ACTIVE);
	            				} catch (jmri.JmriException e) {
	            					log.error("Exception setting " + sysName + " sensor ACTIVE: " + e);
	            				}
	            			}
	            			if (newCmdCode == X10Sequence.FUNCTION_OFF || newCmdCode == X10Sequence.FUNCTION_DIM || newCmdCode == X10Sequence.FUNCTION_STATUS_OFF) {
	            				try {				
	            					sensor.setKnownState(Sensor.INACTIVE);
	            				} catch (jmri.JmriException e) {
	            					log.error("Exception setting " + sysName + " sensor INACTIVE: " + e);
	            				}
	            			}
	            		}
	        		}
	        	}
	    	}
    	}
    }
    
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SpecificSensorManager.class.getName());
}

/* @(#)SpecificSensorManager.java */
