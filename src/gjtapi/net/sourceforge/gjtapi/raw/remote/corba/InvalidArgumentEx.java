package net.sourceforge.gjtapi.raw.remote.corba;

/**
* com/uforce/jtapi/generic/raw/remote/corba/InvalidArgumentEx.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from CorbaProvider.idl
* Thursday, November 16, 2000 1:38:18 o'clock PM EST
*/

public final class InvalidArgumentEx extends org.omg.CORBA.UserException implements org.omg.CORBA.portable.IDLEntity
{
  public String reason = null;

  public InvalidArgumentEx ()
  {
  } // ctor      
  public InvalidArgumentEx (String _reason)
  {
	reason = _reason;
  } // ctor      
} // class InvalidArgumentEx
