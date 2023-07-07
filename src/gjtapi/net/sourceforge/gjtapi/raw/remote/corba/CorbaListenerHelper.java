package net.sourceforge.gjtapi.raw.remote.corba;

/**
* com/uforce/jtapi/generic/raw/remote/corba/CorbaListenerHelper.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from CorbaProvider.idl
* Thursday, November 16, 2000 1:10:55 o'clock PM EST
*/

abstract public class CorbaListenerHelper
{
  private static String  _id = "IDL:com/uforce/jtapi/generic/raw/remote/corba/CorbaListener:1.0";

  private static org.omg.CORBA.TypeCode __typeCode = null;
  public static net.sourceforge.gjtapi.raw.remote.corba.CorbaListener extract (org.omg.CORBA.Any a)
  {
	return read (a.create_input_stream ());
  }        
  public static String id ()
  {
	return _id;
  }      
  public static void insert (org.omg.CORBA.Any a, net.sourceforge.gjtapi.raw.remote.corba.CorbaListener that)
  {
	org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
	a.type (type ());
	write (out, that);
	a.read_value (out.create_input_stream (), type ());
  }        
  public static net.sourceforge.gjtapi.raw.remote.corba.CorbaListener narrow (org.omg.CORBA.Object obj)
  {
	if (obj == null)
	  return null;
	else if (obj instanceof net.sourceforge.gjtapi.raw.remote.corba.CorbaListener)
	  return (net.sourceforge.gjtapi.raw.remote.corba.CorbaListener)obj;
	else if (!obj._is_a (id ()))
	  throw new org.omg.CORBA.BAD_PARAM ();
	else
	{
	  org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
	  return new net.sourceforge.gjtapi.raw.remote.corba._CorbaListenerStub (delegate);
	}
  }        
  public static net.sourceforge.gjtapi.raw.remote.corba.CorbaListener read (org.omg.CORBA.portable.InputStream istream)
  {
	return narrow (istream.read_Object (_CorbaListenerStub.class));
  }        
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
	if (__typeCode == null)
	{
	  __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (net.sourceforge.gjtapi.raw.remote.corba.CorbaListenerHelper.id (), "CorbaListener");
	}
	return __typeCode;
  }        
  public static void write (org.omg.CORBA.portable.OutputStream ostream, net.sourceforge.gjtapi.raw.remote.corba.CorbaListener value)
  {
	ostream.write_Object ((org.omg.CORBA.Object) value);
  }        
}
