package net.sourceforge.gjtapi.raw.remote.corba;

/**
* com/uforce/jtapi/generic/raw/remote/corba/RawStateEx.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from CorbaProvider.idl
* Thursday, November 16, 2000 1:38:18 o'clock PM EST
*/

public final class RawStateEx extends org.omg.CORBA.UserException implements org.omg.CORBA.portable.IDLEntity
{
  public int callId = (int)0;
  public String address = null;
  public String terminal = null;
  public int type = (int)0;
  public int state = (int)0;
  public String info = null;

  public RawStateEx ()
  {
  } // ctor      
  public RawStateEx (int _callId, String _address, String _terminal, int _type, int _state, String _info)
  {
	callId = _callId;
	address = _address;
	terminal = _terminal;
	type = _type;
	state = _state;
	info = _info;
  } // ctor      
} // class RawStateEx
