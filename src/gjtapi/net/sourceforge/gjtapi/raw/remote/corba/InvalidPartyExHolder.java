package net.sourceforge.gjtapi.raw.remote.corba;

/**
* com/uforce/jtapi/generic/raw/remote/corba/InvalidPartyExHolder.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from CorbaProvider.idl
* Thursday, November 16, 2000 1:38:18 o'clock PM EST
*/

public final class InvalidPartyExHolder implements org.omg.CORBA.portable.Streamable
{
  public net.sourceforge.gjtapi.raw.remote.corba.InvalidPartyEx value = null;

  public InvalidPartyExHolder ()
  {
  }      
  public InvalidPartyExHolder (net.sourceforge.gjtapi.raw.remote.corba.InvalidPartyEx initialValue)
  {
	value = initialValue;
  }        
  public void _read (org.omg.CORBA.portable.InputStream i)
  {
	value = net.sourceforge.gjtapi.raw.remote.corba.InvalidPartyExHelper.read (i);
  }        
  public org.omg.CORBA.TypeCode _type ()
  {
	return net.sourceforge.gjtapi.raw.remote.corba.InvalidPartyExHelper.type ();
  }        
  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
	net.sourceforge.gjtapi.raw.remote.corba.InvalidPartyExHelper.write (o, value);
  }        
}
