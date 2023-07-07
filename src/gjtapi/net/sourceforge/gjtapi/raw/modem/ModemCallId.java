package net.sourceforge.gjtapi.raw.modem;
// NAME
//      $RCSfile: ModemCallId.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.2 $
// CREATED
//      $Date: 2005-10-17 05:11:56 $
// COPYRIGHT
//      Westhawk Ltd
// TO DO
//

import net.sourceforge.gjtapi.CallId;

/**
 *
 * @author <a href="mailto:ray@westhawk.co.uk">Ray Tran</a>
 * @version $Revision: 1.2 $ $Date: 2005-10-17 05:11:56 $
 */
public class ModemCallId implements CallId {

    private static int idCounter = 0;
    private int id;

    public ModemCallId() {
        id = idCounter++;
    }
    
    public String toString(){
        return this.getClass().getName() + ": " + id;
    }
    
    public int getId(){
        return id;
    }
    
    public boolean equals(Object obj){
        return (obj instanceof ModemCallId && ((ModemCallId)obj).getId() == this.getId());
    }
    
    public int hashCode(){
        return this.getId();
    }

}