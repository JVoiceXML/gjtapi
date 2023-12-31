/*
	Copyright (c) 2005 Serban Iordache 

	All rights reserved. 
	
	Permission is hereby granted, free of charge, to any person obtaining a 
	copy of this software and associated documentation files (the 
	"Software"), to deal in the Software without restriction, including 
	without limitation the rights to use, copy, modify, merge, publish, 
	distribute, and/or sell copies of the Software, and to permit persons 
	to whom the Software is furnished to do so, provided that the above 
	copyright notice(s) and this permission notice appear in all copies of 
	the Software and that both the above copyright notice(s) and this 
	permission notice appear in supporting documentation. 
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS 
	OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT 
	OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR 
	HOLDERS INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL 
	INDIRECT OR CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING 
	FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, 
	NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION 
	WITH THE USE OR PERFORMANCE OF THIS SOFTWARE. 
	
	Except as contained in this notice, the name of a copyright holder 
	shall not be used in advertising or otherwise to promote the sale, use 
	or other dealings in this Software without prior written authorization 
	of the copyright holder.
*/
package net.sourceforge.gjtapi.raw.tapi3;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.*;

import javax.telephony.Event;
import javax.telephony.InvalidArgumentException;
import javax.telephony.InvalidPartyException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.PrivilegeViolationException;
import javax.telephony.ProviderUnavailableException;
import javax.telephony.ResourceUnavailableException;
import javax.telephony.TerminalConnection;
import javax.telephony.media.MediaResourceException;
import javax.telephony.media.MediaRuntimeException;
import javax.telephony.media.RTC;
import javax.telephony.media.SignalConstants;
import javax.telephony.media.Symbol;

import net.sourceforge.gjtapi.CallId;
import net.sourceforge.gjtapi.RawSigDetectEvent;
import net.sourceforge.gjtapi.RawStateException;
import net.sourceforge.gjtapi.TelephonyListener;
import net.sourceforge.gjtapi.TermData;
import net.sourceforge.gjtapi.capabilities.Capabilities;
import net.sourceforge.gjtapi.raw.CCTpi;
import net.sourceforge.gjtapi.raw.MediaTpi;
import net.sourceforge.gjtapi.raw.PrivateDataTpi;
import net.sourceforge.gjtapi.raw.tapi3.logging.Logger;
import net.sourceforge.gjtapi.raw.tapi3.logging.PrintStreamLogger;

/**
 * An implementation of a Jtapi provider which uses Microsoft TAPI 3.0
 * @author Serban Iordache
 */
@SuppressWarnings("serial")
public class Tapi3Provider implements CCTpi, MediaTpi, PrivateDataTpi {
    private static Logger logger; // = new ConsoleLogger(); // new NullLogger();

    /**
     * Method identifier for <i>addressPrivateData</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_ADDRESS_PRIVATE_DATA = 1;
    /**
     * Method identifier for <i>callActive</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_CALL_ACTIVE = 2;
    /**
     * Method identifier for <i>callInvalid</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_CALL_INVALID = 3;
    /**
     * Method identifier for <i>callPrivateData</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_CALL_PRIVATE_DATA = 4;
    /**
     * Method identifier for <i>connectionAlerting</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_CONNECTION_ALERTING = 5;
    /**
     * Method identifier for <i>connectionConnected</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_CONNECTION_CONNECTED = 6;
    /**
     * Method identifier for <i>connectionDisconnected</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_CONNECTION_DISCONNECTED = 7;
    /**
     * Method identifier for <i>connectionFailed</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_CONNECTION_FAILED = 8;
    /**
     * Method identifier for <i>connectionInProgress</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_CONNECTION_IN_PROGRESS = 9;
    /**
     * Method identifier for <i>providerPrivateData</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_PROVIDER_PRIVATE_DATA = 10;
    /**
     * Method identifier for <i>terminalConnectionCreated</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_TERMINAL_CONNECTION_CREATED = 11;
    /**
     * Method identifier for <i>terminalConnectionDropped</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_TERMINAL_CONNECTION_DROPPED = 12;
    /**
     * Method identifier for <i>terminalConnectionHeld</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_TERMINAL_CONNECTION_HELD = 13;
    /**
     * Method identifier for <i>terminalConnectionRinging</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_TERMINAL_CONNECTION_RINGING = 14;
    /**
     * Method identifier for <i>terminalConnectionTalking</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_TERMINAL_CONNECTION_TALKING = 15;
    /**
     * Method identifier for <i>terminalPrivateData</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_TERMINAL_PRIVATE_DATA = 16;
    /**
     * Method identifier for <i>mediaDigitReceived</i>; used as methodID parameter for the {@link #callback} method.  
     */
    public static final int METHOD_MEDIA_DIGIT_RECEIVED = 17;

    /**
     * Array of friendly names for the <i>METHOD_XXX</i> values
     */
    private static final String[] METHOD_NAMES = {
            "METHOD_NONE",
            "METHOD_ADDRESS_PRIVATE_DATA",
            "METHOD_CALL_ACTIVE",
            "METHOD_CALL_INVALID",
            "METHOD_CALL_PRIVATE_DATA",
            "METHOD_CONNECTION_ALERTING",
            "METHOD_CONNECTION_CONNECTED",
            "METHOD_CONNECTION_DISCONNECTED",
            "METHOD_CONNECTION_FAILED",
            "METHOD_CONNECTION_IN_PROGRESS",
            "METHOD_PROVIDER_PRIVATE_DATA",
            "METHOD_TERMINAL_CONNECTION_CREATED",
            "METHOD_TERMINAL_CONNECTION_DROPPED",
            "METHOD_TERMINAL_CONNECTION_HELD",
            "METHODTERMINAL_CONNECTION_RINGING",
            "METHOD_TERMINAL_CONNECTION_TALKING",
            "METHOD_TERMINAL_PRIVATE_DATA",
            "METHOD_MEDIA_DIGIT_RECEIVED",
    };

    /**
     * Cause identifier for <i>CAUSE_UNKNOWN</i>; used as jniCause parameter for the {@link #callback} method.  
     */
    public static final int JNI_CAUSE_UNKNOWN = -1;
    /**
     * Cause identifier for <i>CAUSE_NORMAL</i>; used as jniCause parameter for the {@link #callback} method.  
     */
    public static final int JNI_CAUSE_NORMAL = 1;
    /**
     * Cause identifier for <i>CAUSE_NEW_CALL</i>; used as jniCause parameter for the {@link #callback} method.  
     */
    public static final int JNI_CAUSE_NEW_CALL = 2;
    /**
     * Cause identifier for <i>CAUSE_SNAPSHOT</i>; used as jniCause parameter for the {@link #callback} method.  
     */
    public static final int JNI_CAUSE_SNAPSHOT = 3;
    /**
     * Cause identifier for <i>CAUSE_DEST_NOT_OBTAINABLE</i>; used as jniCause parameter for the {@link #callback} method.  
     */
    public static final int JNI_CAUSE_DEST_NOT_OBTAINABLE = 4;

    /**
     * The concrete Tapi3Native implementation
     */
    private Tapi3Native tapi3Native;
    
    /**
     * The array of addresses
     */
    private String[] addresses = new String[0];
    /**
     * The array of terminals
     */
    private TermData[] terminals = new TermData[0];
    /**
     * indicates whether the termhack is activated
     */
    private boolean termHack = false;
    /**
     * Mapping of addresses to terminals (for the termHack)
     */
    private Map<String, ArrayList<String>> termAddrMap = new HashMap<String, ArrayList<String>>();
    /**
     * Mapping of terminal string to termData
     */
    private Map<String, TermData> termTermDataMap = new HashMap<String, TermData>();
    /**
     * flag for callControl (transfer, conference)
     */
    private int TAPICALLCONTROLMODE_NONE = 0;
    /**
     * The list of TelephonyListeners
     */
    private final ArrayList<TelephonyListener> listenerList = new ArrayList<TelephonyListener>();

    /**
     * The list of DigitListeners 
     */
    private final ArrayList<DigitListener> digitListenerList = new ArrayList<DigitListener>();

    /**
     * Map used to store information to allow transfers and conferences
     */
    private HashMap<CcInfoKey, PrivateTransferConferenceInfo> callControlInfoMap = new HashMap<CcInfoKey, PrivateTransferConferenceInfo>();
    
    private static interface DigitListener {
        public void receivedDigit(String terminal, char ch);
    }

    /**
     * Return the logger
     * @return The logger
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Configure the logger
     * @param props The name value properties map used to configure the logger 
     */
    @SuppressWarnings("unchecked")
	private void configureLogger(Map props) {
        String tapi3LogOut = (String)props.get("tapi3.log.out");
        if(tapi3LogOut != null) {
            if("console".equals(tapi3LogOut)) {
                logger = new PrintStreamLogger(System.err);
            } else {
                try {
                    PrintStream logStream = new PrintStream(new FileOutputStream(tapi3LogOut));
                    logger = new PrintStreamLogger(logStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    logger = new PrintStreamLogger(System.err);
                }
            }
        }
        String tapi3LogClass = (String)props.get("tapi3.log.class");
        if(tapi3LogClass != null) {
            try {
                Class cls = Class.forName(tapi3LogClass);
                logger = (Logger)cls.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                logger = new PrintStreamLogger(System.err);
            }
        }
    }

    /**
     * Configure a concrete implementation of the {@link Tapi3Native} interface
     * @param props The name value properties map used to configure the {@link Tapi3Native} implementation 
     */
    @SuppressWarnings("unchecked")
	private void configureNative(Map props) {
        String tapi3ImplClass = (String)props.get("tapi3.impl.class");
        if(tapi3ImplClass == null) {
            tapi3ImplClass = "net.sourceforge.gjtapi.raw.tapi3.Tapi3NativeImpl";
        }
        logger.debug("Trying to instantiate " + tapi3ImplClass);
        try {
            Class<?> cls = Class.forName(tapi3ImplClass);
            Class<?>[] paramClasses = { String.class };
            Method m = cls.getMethod("getInstance", paramClasses);
            Object[] libraryNameArgs = { props.get("nativeLibraryPath") };
            tapi3Native = (Tapi3Native)m.invoke(null, libraryNameArgs);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
        logger.debug("tapi3Native successfully created.");
    }

    /**
     * Return the friendly name of the parameter methodID  
     * @param methodID The identifier of the method parameter (as used in the {@link #callback} method)
     * @return The friendly name of methodID
     */
    private String getMethodName(int methodID) {
        String name = "<UNKNOWN>";
        if(methodID >= 0 && methodID < METHOD_NAMES.length) {
            name = METHOD_NAMES[methodID];
        }
        return name;
    }

    /**
     * Return the friendly name of the parameter jniCause 
     * @param jniCause The identifier of the cause parameter (as used in the {@link #callback} method)
     * @return The friendly name of jniCause
     */
    private int getEventCause(int jniCause) {
        switch(jniCause) {
            case JNI_CAUSE_NORMAL: return Event.CAUSE_NORMAL;
            case JNI_CAUSE_NEW_CALL: return Event.CAUSE_NEW_CALL;
            case JNI_CAUSE_SNAPSHOT: return Event.CAUSE_SNAPSHOT;
            case JNI_CAUSE_DEST_NOT_OBTAINABLE: return Event.CAUSE_DEST_NOT_OBTAINABLE;
        }
        return Event.CAUSE_UNKNOWN;
    }

    /**
     * Callback method typically called by the concrete implementation of the {@link Tapi3Native} interface
     * @param methodID The identifier of the method (event) that should be notified. Must be one of the <i>METHOD_XXX</i> values.
     * @param callID The identifier for the call
     * @param address The address that defines the call
     * @param jniCause The event cause. Must be one of the <i>JNI_CAUSE_XXX</i> values.
     * @param callInfo Array of 4 elements used to initialize a {@link Tapi3PrivateData}
     */
    public synchronized void callback(int methodID, int callID, String address, int jniCause, String[] callInfo) {
        Tapi3CallID tapi3CallID = new Tapi3CallID(callID);
        String terminal = null;  // !!!
        if(termHack)
          try
          {
            terminal = getTerminals(address)[0].terminal;
          }
          catch (InvalidArgumentException e)
          {
            e.printStackTrace();
          }
        else
          terminal = address;
//        String terminal = getTerminals(address)[0].terminal;
        Tapi3PrivateData privateData = null;
        if(callInfo != null && callInfo.length == 5) {
            privateData = new Tapi3PrivateData(callInfo[0], callInfo[1], callInfo[2], callInfo[3], callInfo[4]);
        }
        int eventCause = getEventCause(jniCause);
        String methodName = getMethodName(methodID);
        logger.info("CALLBACK: " + methodID + " (" + methodName + ") on " + address +
                ": callID=" + tapi3CallID.getCallID() + ", privateData: " + privateData);
        Iterator<TelephonyListener> it = listenerList.iterator();
        boolean validCall = true;	// track if a call is made invalid, so we don't send private data
        while(it.hasNext()) {
            TelephonyListener listener = it.next();
            switch(methodID) {
                case METHOD_ADDRESS_PRIVATE_DATA:
                    listener.addressPrivateData(address, privateData, eventCause);
                    break;
                case METHOD_CALL_ACTIVE:
                    listener.callActive(tapi3CallID, eventCause);
                    break;
                case METHOD_CALL_INVALID:
                    listener.callInvalid(tapi3CallID, eventCause);
                    break;
                case METHOD_CALL_PRIVATE_DATA:
                	// Called at exit
                    // listener.callPrivateData(tapi3CallID, privateData, eventCause);
                    break;
                case METHOD_CONNECTION_ALERTING:
                    listener.connectionAlerting(tapi3CallID, address, eventCause);
                    if(privateData != null) {
                        String callerAddr = privateData.getCallerNumber();
                        if(privateData.getCallerName() != null && privateData.getCallerName().length() > 0) {
                            callerAddr += " (" + privateData.getCallerName() + ")"; 
                        }
                        listener.connectionInProgress(tapi3CallID, callerAddr, Event.CAUSE_NEW_CALL);
                    }
                    break;
                case METHOD_CONNECTION_CONNECTED:
                    listener.connectionConnected(tapi3CallID, address, eventCause);
                    break;
                case METHOD_CONNECTION_DISCONNECTED:
                    listener.connectionDisconnected(tapi3CallID, address, eventCause);
                    // in TAPI land, we are 1st party, and so a disconnected connection causes the call to drop
                    listener.callInvalid(tapi3CallID, eventCause);
                    validCall = false;
                    break;
                case METHOD_CONNECTION_FAILED:
                    listener.connectionFailed(tapi3CallID, address, eventCause);
                    // in TAPI land, we are 1st party, and so a failed connection causes the call to drop
                    listener.callInvalid(tapi3CallID, eventCause);
                    validCall = false;
                    break;
                case METHOD_CONNECTION_IN_PROGRESS:
                    // events added because there was no information about the called number in a jtapi call
                    listener.terminalConnectionCreated(tapi3CallID, address, terminal, eventCause);
                    if(privateData != null)
                      listener.connectionAlerting(tapi3CallID, privateData.getCalledNumber(), eventCause);
                    else //in case there was no callinfo ... not very good this way (should be improved)
                      listener.connectionAlerting(tapi3CallID, address, eventCause);
                    // events add till here
                    listener.connectionInProgress(tapi3CallID, address, eventCause);
                    break;
                case METHOD_PROVIDER_PRIVATE_DATA:
                    listener.providerPrivateData(privateData, eventCause);
                    break;
                case METHOD_TERMINAL_CONNECTION_CREATED:
                    listener.terminalConnectionCreated(tapi3CallID, address, terminal, eventCause);
                    break;
                case METHOD_TERMINAL_CONNECTION_DROPPED:
                    listener.terminalConnectionDropped(tapi3CallID, address, terminal, eventCause);
                    // in TAPI land, we are 1st party, and so a dropped terminal connection causes the call to drop
                    listener.callInvalid(tapi3CallID, eventCause);
                    validCall = false;
                    break;
                case METHOD_TERMINAL_CONNECTION_HELD:
                    listener.terminalConnectionHeld(tapi3CallID, address, terminal, eventCause);
                    break;
                case METHOD_TERMINAL_CONNECTION_RINGING:
                    listener.terminalConnectionRinging(tapi3CallID, address, terminal, eventCause);
                    break;
                case METHOD_TERMINAL_CONNECTION_TALKING:
                	listener.connectionConnected(tapi3CallID, address, eventCause);	// in con_connected not sent
                    listener.terminalConnectionTalking(tapi3CallID, address, terminal, eventCause);
                    break;
                case METHOD_TERMINAL_PRIVATE_DATA:
                    listener.terminalPrivateData(terminal, privateData, eventCause);
                    break;
                case METHOD_MEDIA_DIGIT_RECEIVED:
                    // Nothing to do here
                    break;
                default:
                    logger.error("CALLBACK: Unknown method: " + methodID + ", callID=" + callID +
                            ", address=" + address + ", jniCause=" + jniCause + ", callInfo=" +
                            (callInfo != null ? Arrays.asList(callInfo).toString() : "null"));
                    break;
            }
            // don't send private data to a call we have just invalidated
            if(validCall && (privateData != null)) {
                listener.callPrivateData(tapi3CallID, privateData, eventCause);
            }
        }
        try {
            if(methodID == METHOD_MEDIA_DIGIT_RECEIVED) {
                char ch = (char)jniCause;
                logger.debug("Character " + ch + " received on " + terminal);
                synchronized(digitListenerList) {
                    Iterator<DigitListener> digitItD = digitListenerList.iterator();
                    while(digitItD.hasNext()) {
                        DigitListener listener = digitItD.next();
                        listener.receivedDigit(terminal, ch);
                    }
                }
            }
        } catch(Exception e) {
            logger.error("Failed to receive digit.", e);
        }
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.CoreTpi#initialize(java.util.Map)
     */
    @SuppressWarnings("unchecked")
	public void initialize(Map props) throws ProviderUnavailableException {
    	configureProperties(props);
		  configureLogger(props);
      logger.debug("Tapi3 properties: " + props + " ...");
      configureNative(props);
      logger.debug("Initializing Tapi3 provider...");
      addresses = tapi3Native.tapi3Init(props);
      logger.debug("Registering Tapi3 provider...");
      ((Tapi3NativeImpl)tapi3Native).registerProvider(this);

      logger.debug("Retrieving addresses...");
      if(addresses == null) {
        addresses = new String[0];
      }
      /**
       * termHack enables several addresses for one terminal
       * the decision depends on the addressnames
       */
      if("true".equals(props.get("tapi3.termHack")))
      {
        termHack = true;
        int nGramVal = 3;
        double threshold = 0.75;
        ArrayList<String> termArr = new ArrayList<String>();
        ArrayList<TermData> conTermArr = new ArrayList<TermData>();
        for(int i = 0; i < addresses.length; i++)
        {
          double bestFit = 0;
          int bestFitIndex[] = new int[]{0, 0};
          ArrayList<String> refNGram = _nGramSegmentation(addresses[i], nGramVal);
          for(Iterator j = termArr.iterator(); j.hasNext(); )
          {
            String next = (String)j.next();
            ArrayList<String> curNGram = _nGramSegmentation(next, nGramVal);
            double fit = _nGramRating(refNGram, curNGram);
            if(fit > bestFit)
            {
              bestFit = fit;
              bestFitIndex[1] = bestFitIndex[0];
            }
            bestFitIndex[0]++;
          }
          if(bestFit < threshold)
          {
            termArr.add(addresses[i]);
            TermData tmpTermData = new TermData(addresses[i], false);
            conTermArr.add(tmpTermData);
            termAddrMap.put(addresses[i], new ArrayList<String>());
            termTermDataMap.put(addresses[i], tmpTermData);
          }
          termAddrMap.get(termArr.get(bestFitIndex[1])).add(addresses[i]);

        }
        terminals = (TermData[]) conTermArr.toArray(new TermData[conTermArr.size()]);
      }
      else
      {
        terminals = new TermData[addresses.length];
        for(int i=0; i<terminals.length; i++) {
            logger.info("Address #" + (i+1) + ": " + addresses[i]);
            terminals[i] = new TermData(addresses[i], false);
        }
      }
      logger.debug("Initialized");
    }

    /**
     * Replace ${<i>propertyName</i>} occurences with the actual value of the system property identified by <i>propertyName</i>  
     * @param props The properties map whose values will be modified
     */
    @SuppressWarnings("unchecked")
	public void configureProperties(Map props) {
    	Iterator<Map.Entry<String, String>> it = (Iterator<Map.Entry<String, String>>)props.entrySet().iterator();
    	while(it.hasNext()) {
        	Map.Entry<String, String> entry = it.next();
			StringBuffer sbufVal = new StringBuffer((String)entry.getValue());
        	while(true) {
        		int startPos = sbufVal.indexOf("${");
        		if(startPos < 0) break;
        		int endPos = sbufVal.indexOf("}", startPos+3);
        		if(endPos < 0) break;
        		String sysProp = sbufVal.substring(startPos+2, endPos);
        		String replaceVal = System.getProperty(sysProp);
        		if(replaceVal != null) {
        			sbufVal.replace(startPos, endPos+1, replaceVal); 
        		}
        	}
        	entry.setValue(sbufVal.toString());
    	}
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.CoreTpi#shutdown()
     */
    public void shutdown() {
        logger.debug("Shutting down...");
        int retCode = tapi3Native.tapi3Shutdown();
        Tapi3NativeImpl.releaseInstance();
        /*logger = null;
        tapi3Native = null;*/
        logger.debug("Shut down: retCode=" + retCode);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.CoreTpi#addListener(net.sourceforge.gjtapi.TelephonyListener)
     */
    public void addListener(TelephonyListener ro) {
        logger.debug("addListener(" + ro.getClass().getName() + ")");
        listenerList.add(ro);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.CoreTpi#removeListener(net.sourceforge.gjtapi.TelephonyListener)
     */
    public void removeListener(TelephonyListener ro) {
        logger.debug("removeListener()");
        listenerList.remove(ro);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.CoreTpi#getCapabilities()
     */
    public Properties getCapabilities() {
        logger.debug("getCapabilities()");
        Properties caps = new Properties();
        // mark my differences from the default
//      caps.put(Capabilities.HOLD, "f");
//      caps.put(Capabilities.JOIN, "f");
        caps.put(Capabilities.THROTTLE, "f");
        caps.put(Capabilities.MEDIA, "t");
        caps.put(Capabilities.ALL_MEDIA_TERMINALS, "t");
        caps.put(Capabilities.ALLOCATE_MEDIA, "f");

        return caps;
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.BasicJtapiTpi#getAddresses()
     */
    public String[] getAddresses() throws ResourceUnavailableException {
        logger.debug("getAddresses()");
        return addresses;
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.BasicJtapiTpi#getAddresses(java.lang.String)
     */
    public String[] getAddresses(String terminal) throws InvalidArgumentException {
        logger.debug("getAddresses(" + terminal + ")");
        if(termHack)
        {
          ArrayList<String> arr = termAddrMap.get(terminal);
          String[] adr = arr.toArray(new String[arr.size()]);
          return adr;
        }
        else for(int i=0; i<terminals.length; i++) {
            if(terminals[i].terminal.equals(terminal)) {
                return new String[] { addresses[i] };
            }
        }
        return new String[0];
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.BasicJtapiTpi#getTerminals()
     */
    public TermData[] getTerminals() throws ResourceUnavailableException {
        /*for(int i = 0; i < terminals.length; i++)
          System.out.println("debug test: " + terminals[i].terminal);*/
        logger.debug("getTerminals()");
        return terminals;
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.BasicJtapiTpi#getTerminals(java.lang.String)
     */
    public TermData[] getTerminals(String address) throws InvalidArgumentException {
        logger.debug("getTerminals(" + address + ")");
        if(termHack)
        {
          String found = null;
          Set<String> keySet = termAddrMap.keySet();
          for(Iterator<String> i = keySet.iterator(); i.hasNext(); )
          {
            String key = (String)i.next();
            if((termAddrMap.get(key)).contains(address))
            {
              found = key;
              break ;
            }
          }
          if(found != null)
            return new TermData[]{(TermData)termTermDataMap.get(found)};
          /*List tmpList = (List)termAddrMap.get(terminals[0].terminal);
          int tmpCount = 0;
          for(Iterator i = tmpList.iterator(); i.hasNext(); tmpCount++)
          {
            String next = i.next().toString();
            if(next.equals(address))
              return new TermData[]{(terminals[tmpCount])};
          }*/
        }
        else for(int i=0; i<addresses.length; i++) {
            if(addresses[i].equals(address)) {
                return new TermData[] { terminals[i] };
            }
        }
        return new TermData[0];
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.CoreTpi#reserveCallId(java.lang.String)
     */
    public CallId reserveCallId(String address) throws InvalidArgumentException {
      logger.debug("reserveCallId for adress: " + address);
        int id = tapi3Native.tapi3ReserveCallId(address);
        logger.debug("reservedCallId(" + id + ")");
        return (id < 0) ? null : new Tapi3CallID(id);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.CoreTpi#releaseCallId(net.sourceforge.gjtapi.CallId)
     */
    public void releaseCallId(CallId id) {
        logger.debug("releaseCallId(" + id + ")");
        if(id instanceof Tapi3CallID) {
            Tapi3CallID tapi3CallID = (Tapi3CallID)id;
          int retCode = tapi3Native.tapi3ReleaseCall(tapi3CallID.getCallID());
            logger.debug("tapi3ReleaseCall() returned: 0x" + Integer.toHexString(retCode));
        } else {
            logger.warn("Not a Tapi3CallID: " + id);
        }
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.CoreTpi#createCall(net.sourceforge.gjtapi.CallId, java.lang.String, java.lang.String, java.lang.String)
     */
    public CallId createCall(CallId id, String address, String term, String dest) throws ResourceUnavailableException,
            PrivilegeViolationException, InvalidPartyException, InvalidArgumentException, RawStateException,
            MethodNotSupportedException {
        logger.debug("createCall(" + id + ", " + address + ", " + term + ", " + dest + ")");
        if(id instanceof Tapi3CallID) {
            Tapi3CallID tapi3CallID = (Tapi3CallID)id;
            PrivateTransferConferenceInfo ccInfo = this.getCallControlInfoMap().get(new CcInfoKey(address, term));
            int retCode;
            	// Detect if we are setting up a consultation call
            if(ccInfo != null)
            {
            	// the return code is the new callId
            	retCode = tapi3Native.tapi3ConsultationStart(ccInfo.getPrimaryCallId().getCallID(), address, dest);
            	if(retCode >= 0) {
            		ccInfo.setConsultCallId(retCode);
            	}
            }
            else
              retCode = tapi3Native.tapi3CreateCall(tapi3CallID.getCallID(), address, dest, TAPICALLCONTROLMODE_NONE);

            logger.debug("tapi3CreateCall() returned: " + Integer.toHexString(retCode));
            if(retCode < 0) {
                throw new InvalidPartyException(InvalidPartyException.UNKNOWN_PARTY, "Error code: " + Integer.toHexString(retCode));
            }
            return id;
        } else {
            throw new MethodNotSupportedException("Not a Tapi3CallID: " + id);
        }
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.CoreTpi#answerCall(net.sourceforge.gjtapi.CallId, java.lang.String, java.lang.String)
     */
    public void answerCall(CallId call, String address, String terminal) throws PrivilegeViolationException,
            ResourceUnavailableException, MethodNotSupportedException, RawStateException {
        logger.debug("answerCall(" + call + ", " + address + ", " + terminal + ")");
        if(call instanceof Tapi3CallID) {
            Tapi3CallID tapi3CallID = (Tapi3CallID)call;
            int retCode = tapi3Native.tapi3AnswerCall(tapi3CallID.getCallID());
            logger.debug("tapi3AnswerCall() returned: 0x" + Integer.toHexString(retCode));
            if(retCode != 0) {
                throw new RawStateException(call, TerminalConnection.UNKNOWN);
            }
        } else {
            logger.warn("Not a Tapi3CallID: " + call);
            throw new MethodNotSupportedException("Not a Tapi3CallID: " + call);
        }
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.BasicJtapiTpi#release(java.lang.String, net.sourceforge.gjtapi.CallId)
     */
    public void release(String address, CallId call) throws PrivilegeViolationException, ResourceUnavailableException,
            MethodNotSupportedException, RawStateException {
        logger.debug("release(" + address + ", " + call + ")");
        if(call instanceof Tapi3CallID) {
            Tapi3CallID tapi3CallID = (Tapi3CallID)call;
            int retCode = tapi3Native.tapi3DisconnectCall(tapi3CallID.getCallID());
            logger.debug("tapi3DisconnectCall() returned: 0x" + Integer.toHexString(retCode));
            if(retCode != 0) {
                throw new RawStateException(call, TerminalConnection.UNKNOWN);
            }
        } else {
            logger.warn("Not a Tapi3CallID: " + call);
            throw new MethodNotSupportedException("Not a Tapi3CallID: " + call);
        }
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.CCTpi#hold(net.sourceforge.gjtapi.CallId, java.lang.String, java.lang.String)
     */
    public void hold(CallId call, String address, String terminal) throws RawStateException, MethodNotSupportedException, PrivilegeViolationException, ResourceUnavailableException {
        logger.debug("hold(" + call + ", " + address + ", " + terminal + ")");
        if(call instanceof Tapi3CallID) {
            Tapi3CallID tapi3CallID = (Tapi3CallID)call;
            int retCode = tapi3Native.tapi3Hold(tapi3CallID.getCallID());
            logger.debug("tapi3Hold() returned: 0x" + Integer.toHexString(retCode));
            if(retCode != 0) {
                throw new RawStateException(call, TerminalConnection.UNKNOWN);
            }
        } else {
            logger.warn("Not a Tapi3CallID: " + call);
            throw new MethodNotSupportedException("Not a Tapi3CallID: " + call);
        }
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.CCTpi#join(net.sourceforge.gjtapi.CallId, net.sourceforge.gjtapi.CallId, java.lang.String, java.lang.String)
     */
    public CallId join(CallId call1, CallId call2, String address, String terminal) throws RawStateException, InvalidArgumentException, MethodNotSupportedException, PrivilegeViolationException, ResourceUnavailableException {
        logger.debug("join(" + call1 + ", " + call2 + ", " + address + ", " + terminal + ")");
        PrivateTransferConferenceInfo ccInfo = this.getCallControlInfoMap().get(new CcInfoKey(address, terminal));
        if(ccInfo == null)
        {
            throw new InvalidArgumentException("No or wrong callControlMode set. Call sendPrivateData(call, address, terminal, mode) first!");
        }
        if((call1 instanceof Tapi3CallID) && (ccInfo.getConsultCallId() > 0)) {
            int errCode;
            if(ccInfo.isTransfer()) {
            	errCode = tapi3Native.tapi3AssistedTransferFinish(ccInfo.getConsultCallId());
            } else {
            	errCode = tapi3Native.tapi3ConferenceFinish(ccInfo.getConsultCallId());
            }
            if(errCode == 0) {
                logger.debug("tapi3Join() succeeded.");
                // clean out call control entries
                clearCallControlInfo(ccInfo);

                return new Tapi3CallID(ccInfo.getConsultCallId());
            } else {
                logger.error("Cannot join (errorCode=" + Integer.toHexString(errCode) + ")");
                throw new RawStateException(call1, TerminalConnection.UNKNOWN);
            }
        } else {
           logger.warn("Not a Tapi3CallID: " + call1 + ", " + call2);
           throw new InvalidArgumentException("Not a Tapi3CallID: " + call1 + ", " + call2);
        }
    }

    /* (non-Javadoc)
     * @see net.sourceforge.gjtapi.raw.CCTpi#unHold(net.sourceforge.gjtapi.CallId, java.lang.String, java.lang.String)
     */
    public void unHold(CallId call, String address, String terminal) throws RawStateException, MethodNotSupportedException, PrivilegeViolationException, ResourceUnavailableException {
        logger.debug("unHold(" + call + ", " + address + ", " + terminal + ")");
        if(call instanceof Tapi3CallID) {
            Tapi3CallID tapi3CallID = (Tapi3CallID)call;
            int retCode = tapi3Native.tapi3UnHold(tapi3CallID.getCallID());
            logger.debug("tapi3UnHold() returned: 0x" + Integer.toHexString(retCode));
            if(retCode != 0) {
                throw new RawStateException(call, TerminalConnection.UNKNOWN);
            }
        } else {
            logger.warn("Not a Tapi3CallID: " + call);
            throw new MethodNotSupportedException("Not a Tapi3CallID: " + call);
        }
    }

    // *** MediaTpi ***    
    @SuppressWarnings("unchecked")
	public boolean allocateMedia(String terminal, int type, Dictionary resourceArgs) {
        return false;
    }
    public boolean freeMedia(String terminal, int type) {
        return false;
    }
    public boolean isMediaTerminal(String terminal) {
        return true;
    }
    @SuppressWarnings("unchecked")
	public void play(String terminal, String[] streamIds, int offset, RTC[] rtcs, Dictionary optArgs) throws MediaResourceException {
        throw new MediaResourceException("Not implemented.");
    }
    @SuppressWarnings("unchecked")
	public void record(String terminal, String streamId, RTC[] rtcs, Dictionary optArgs) throws MediaResourceException {
        throw new MediaResourceException("Not implemented.");
    }
    public void stop(String terminal) {
        throw new MediaRuntimeException("Not implemented.") {};
    }
    
    public void triggerRTC(String terminal, Symbol action) {
        throw new MediaRuntimeException("Not implemented.") {};
    }
    @SuppressWarnings("unchecked")
	public RawSigDetectEvent retrieveSignals(final String terminal, final int num, Symbol[] patterns, RTC[] rtcs, Dictionary optArgs) throws MediaResourceException {
        logger.debug("retrieveSignals('" + terminal + "', " + num + ")");
        if(num <= 0) throw new MediaResourceException("Invalid number of signals: " + num);
        
        final Symbol[] symbols = new Symbol[num];
        final int[] digitCount = {0};
        
        DigitListener listener = new DigitListener() {
            public void receivedDigit(String term, char ch) {
                if(term.equals(terminal)) {
                    synchronized(symbols) {
                        symbols[digitCount[0]++] = getSymbol(ch);
                        if(digitCount[0] >= num) {
                            symbols.notifyAll();
                        }
                    }
                }
            }
        };
        synchronized(digitListenerList) {
            digitListenerList.add(listener);
        }
        try {
            synchronized(symbols) {
                while(digitCount[0] < num) {
                    try {
                        symbols.wait();
                    } catch(InterruptedException e) {
                        logger.warn("retrieveSignals() interrupted", e);
                        throw new MediaResourceException("retrieveSignals() interrupted");
                    }
                }
            }
        } finally {
            synchronized(digitListenerList) {
                digitListenerList.remove(listener);
            }
        }
        RawSigDetectEvent event = RawSigDetectEvent.maxDetected(terminal, symbols);
        logger.debug("retrieveSignals() done.");
        return event;
    }
    @SuppressWarnings("unchecked")
	public void sendSignals(String terminal, Symbol[] syms, RTC[] rtcs, Dictionary optArgs) throws MediaResourceException {
        logger.debug("sendSignals(" + terminal + ", " + Arrays.asList(syms) + ")");
        int retCode = tapi3Native.tapi3SendSignals(terminal, getSymbolsAsString(syms));
        logger.debug("sendSignals() returned: 0x" + Integer.toHexString(retCode));
        if(retCode != 0) {
            throw new MediaResourceException("Failed to send DTMF tones: errorCode = 0x" + Integer.toHexString(retCode));
        }
    }
    
	/* (non-Javadoc)
	 * This is used to get any private data
	 * @see net.sourceforge.gjtapi.raw.PrivateDataTpi#getPrivateData(net.sourceforge.gjtapi.CallId, java.lang.String, java.lang.String)
	 */
	public Object getPrivateData(CallId call, String address, String terminal) {
		// Don't have any private data
		return null;
	}
	/* This is used to send in-call dialing information.
	 * @see net.sourceforge.gjtapi.raw.PrivateDataTpi#sendPrivateData(net.sourceforge.gjtapi.CallId, java.lang.String, java.lang.String, java.lang.Object)
	 */
	public Object sendPrivateData(CallId call, String address, String terminal, Object data) {
		// If the data is a PrivateDialCommand, send it to the native level
		if ((call instanceof Tapi3CallID) && (data instanceof PrivateDialCommand))
		{
			PrivateDialCommand dialCommand = (PrivateDialCommand)data;
			int result = tapi3Native.tapi3Dial(((Tapi3CallID)call).getCallID(), dialCommand.getNumberToDial());
			return new Integer(result);
		}
		// The Join commands are for blind transfer, assisted transfer and conference
		else if ((call instanceof Tapi3CallID) && (data instanceof PrivateJoinCommand)) {
			return new Integer(((PrivateJoinCommand)data).perform(tapi3Native, (Tapi3CallID)call));
		}
	    else if (data instanceof byte[] && address != null)
			{
				long result;
	      if(call == null)
	        result = tapi3Native.tapi3LineDevSpecific(-1, address, (byte[])data);
	      else if(call instanceof Tapi3CallID)
	        result = tapi3Native.tapi3LineDevSpecific(((Tapi3CallID)call).getCallID(), address, (byte[])data);
	      else
	        return null;
	      return new Long(result);
			}
	    else if(address == null && terminal == null)
	    {
	      if(data instanceof PrivateTransferConferenceInfo) {
	    	  // store it against both calls
	    	  PrivateTransferConferenceInfo info = (PrivateTransferConferenceInfo)data;
	    	  CcInfoKey key = new CcInfoKey(info.getConsultAddress(), info.getConsultTerminal());
	    	  if(info.getPrimaryCallId() != null) {
	    		  // store the mapping so that the next create Call is a consultation call
		    	  this.getCallControlInfoMap().put(key, info);
		    	  }
	    	  else {
	    		  // we need to clear out the consultation mapping
	    		  this.getCallControlInfoMap().remove(key);
	    	  }
	     	 return "callControl success";
	      }
	    }
		return null;
	}
	/* (non-Javadoc)
	 * @see net.sourceforge.gjtapi.raw.PrivateDataTpi#setPrivateData(net.sourceforge.gjtapi.CallId, java.lang.String, java.lang.String, java.lang.Object)
	 */
	public void setPrivateData(CallId call, String address, String terminal,
			Object data) {
		// Don't do anything with this

	}


    /**
     * Convert an array of {@link javax.telephony.media.Symbol}s to a String of DTMF digits 
     * @param symbols The array of {@link javax.telephony.media.Symbol}s
     * @return A String containing the corresponding DTMF digits
     */
    public String getSymbolsAsString(Symbol[] symbols) {
        StringBuffer sbuf = new StringBuffer(symbols.length);
        for(int i=0; i<symbols.length; i++) {
            char ch = getSymbolAsChar(symbols[i]);
            sbuf.append(ch);
        }
        return sbuf.toString();
    }
    
    /**
     * Convert a {@link javax.telephony.media.Symbol} to a DTMF digit
     * @param symbol The {@link javax.telephony.media.Symbol} to be converted
     * @return The corresponding DTMF digit
     */
    public char getSymbolAsChar(Symbol symbol) {
        if(symbol == SignalConstants.v_DTMF0) return '0';
        if(symbol == SignalConstants.v_DTMF1) return '1';
        if(symbol == SignalConstants.v_DTMF2) return '2';
        if(symbol == SignalConstants.v_DTMF3) return '3';
        if(symbol == SignalConstants.v_DTMF4) return '4';
        if(symbol == SignalConstants.v_DTMF5) return '5';
        if(symbol == SignalConstants.v_DTMF6) return '6';
        if(symbol == SignalConstants.v_DTMF7) return '7';
        if(symbol == SignalConstants.v_DTMF8) return '8';
        if(symbol == SignalConstants.v_DTMF9) return '9';
        if(symbol == SignalConstants.v_DTMFA) return 'A';
        if(symbol == SignalConstants.v_DTMFB) return 'B';
        if(symbol == SignalConstants.v_DTMFC) return 'C';
        if(symbol == SignalConstants.v_DTMFD) return 'D';
        if(symbol == SignalConstants.v_DTMFHash) return '#';
        if(symbol == SignalConstants.v_DTMFStar) return '*';        
        return '\0';
    }
    
    /**
     * Convert a DTMF digit to a {@link javax.telephony.media.Symbol}
     * @param ch The DTMF digit as char
     * @return The corresponding {@link javax.telephony.media.Symbol}
     */
    public Symbol getSymbol(char ch) {
        switch(ch) {
            case '0': return SignalConstants.v_DTMF0;
            case '1': return SignalConstants.v_DTMF1;
            case '2': return SignalConstants.v_DTMF2;
            case '3': return SignalConstants.v_DTMF3;
            case '4': return SignalConstants.v_DTMF4;
            case '5': return SignalConstants.v_DTMF5;
            case '6': return SignalConstants.v_DTMF6;
            case '7': return SignalConstants.v_DTMF7;
            case '8': return SignalConstants.v_DTMF8;
            case '9': return SignalConstants.v_DTMF9;
            case 'A': return SignalConstants.v_DTMFA;
            case 'B': return SignalConstants.v_DTMFB;
            case 'C': return SignalConstants.v_DTMFC;
            case 'D': return SignalConstants.v_DTMFD;
            case '#': return SignalConstants.v_DTMFHash;
            case '*': return SignalConstants.v_DTMFStar;
            default: return null;
        }
    }

    /**
     * segmentates a string into n-grams
     * @param pStr the string to segmentate
     * @param n the value of the n-gram
     * @return the n-gram segmentated from the string
     */
    private ArrayList<String> _nGramSegmentation(String pStr, int n)
    {

      ArrayList<String> arr = new ArrayList<String>();
      if(n > pStr.length())
        arr.add(pStr);
      else
      {
        for(int i = 0; i < pStr.length()-(n-1); i++)
        {
          arr.add(pStr.substring(i, i+n));
        }
      }
      return arr;
    }

   /**
    * rates the similarity
    * @param pRef remains unmodified
    * @param pToRate will be modified
    * @return a rating
    */
    //@SuppressWarnings("unchecked")
	@SuppressWarnings("unchecked")
	private double _nGramRating(ArrayList<String> pRef, ArrayList<String> pToRate)
    {
      ArrayList<String> a;
      ArrayList<String> b;
      if(pToRate.size() > pRef.size())
      {
        a = (ArrayList<String>)pRef.clone();
        b = pToRate;
      }
      else
      {
        a = pToRate;
        b = (ArrayList<String>)pRef.clone();
      }
      int count = a.size() + b.size();
      int hits = 0;
      for(Iterator<String> i = a.iterator(); i.hasNext(); )
      {
        String next = i.next();
        if(b.contains(next))
        {
          b.remove(next);
          i.remove();
          hits++;
        }
      }
      return 2.0*(double)hits/(double)count;
    }

    /**
     * for callcontrol (transfer, conference)
     */
    private void clearCallControlInfo(PrivateTransferConferenceInfo ccInfo)
    {
      this.getCallControlInfoMap().remove(new CcInfoKey(ccInfo.getConsultAddress(), ccInfo.getConsultTerminal()));
    }

    /**
     * Accessor for the map of call ids to transfer/conference instructions
     * @return
     */
	private HashMap<CcInfoKey, PrivateTransferConferenceInfo> getCallControlInfoMap() {
		return callControlInfoMap;
	}
	
	protected class CcInfoKey {
		private String address;
		private String terminal;
		
		/**
		 * Constructor
		 * @param address
		 * @param terminal
		 */
		private CcInfoKey(String address, String terminal) {
			this.address = address;
			this.terminal = terminal;
		}
		
		// Overridden methods
		@Override
		public boolean equals(Object other) {
			if (other instanceof CcInfoKey) {
				CcInfoKey otherKey = (CcInfoKey)other;
				if(otherKey.address.equals(this.address) &&
						otherKey.terminal.equals(this.terminal)) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return this.address.hashCode() + this.terminal.hashCode();
		}
		
		@Override
		public String toString() {
			return "Key for address " + this.address + " and terminal " + terminal;
		}
	}
}
