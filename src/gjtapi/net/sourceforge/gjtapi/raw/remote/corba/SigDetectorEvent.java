package net.sourceforge.gjtapi.raw.remote.corba;

/**
* com/uforce/jtapi/generic/raw/remote/corba/SigDetectorEvent.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from CorbaProvider.idl
* Thursday, November 16, 2000 1:38:18 o'clock PM EST
*/

public final class SigDetectorEvent implements org.omg.CORBA.portable.IDLEntity
{
  public int index = (int)0;
  public int buffer[] = null;

  public SigDetectorEvent ()
  {
  } // ctor      
  public SigDetectorEvent (int _index, int[] _buffer)
  {
	index = _index;
	buffer = _buffer;
  } // ctor      
} // class SigDetectorEvent
