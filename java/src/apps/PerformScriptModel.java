// PerformScriptModel.java
package apps;

/**
 * A PerformScriptModel object runs a script when the program is started.
 * <P>
 * @author	Bob Jacobsen Copyright 2003
 * @version $Revision$
 * @see PerformScriptPanel
 */
public class PerformScriptModel implements StartupModel {

    public PerformScriptModel() {
        fileName = null;
    }

    String fileName;

    @Override
    public String getName() {
        return this.getFileName();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String n) {
        fileName = n;
    }
}
