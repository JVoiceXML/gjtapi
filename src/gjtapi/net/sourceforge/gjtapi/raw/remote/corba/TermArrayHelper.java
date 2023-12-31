package net.sourceforge.gjtapi.raw.remote.corba;

/**
* com/uforce/jtapi/generic/raw/remote/corba/TermArrayHelper.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from CorbaProvider.idl
* Thursday, November 16, 2000 1:38:18 o'clock PM EST
*/

abstract public class TermArrayHelper
{
  private static String  _id = "IDL:com/uforce/jtapi/generic/raw/remote/corba/TermArray:1.0";

  private static org.omg.CORBA.TypeCode __typeCode = null;
  public static net.sourceforge.gjtapi.raw.remote.corba.TermData[] extract (org.omg.CORBA.Any a)
  {
	return read (a.create_input_stream ());
  }        
  public static String id ()
  {
	return _id;
  }      
  public static void insert (org.omg.CORBA.Any a, net.sourceforge.gjtapi.raw.remote.corba.TermData[] that)
  {
	org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
	a.type (type ());
	write (out, that);
	a.read_value (out.create_input_stream (), type ());
  }        
  public static net.sourceforge.gjtapi.raw.remote.corba.TermData[] read (org.omg.CORBA.portable.InputStream istream)
  {
	net.sourceforge.gjtapi.raw.remote.corba.TermData value[] = null;
	int _len0 = istream.read_long ();
	value = new net.sourceforge.gjtapi.raw.remote.corba.TermData[_len0];
	for (int _o1 = 0;_o1 < value.length; ++_o1)
	  value[_o1] = net.sourceforge.gjtapi.raw.remote.corba.TermDataHelper.read (istream);
	return value;
  }        
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
	if (__typeCode == null)
	{
	  __typeCode = net.sourceforge.gjtapi.raw.remote.corba.TermDataHelper.type ();
	  __typeCode = org.omg.CORBA.ORB.init ().create_sequence_tc (0, __typeCode);
	  __typeCode = org.omg.CORBA.ORB.init ().create_alias_tc (net.sourceforge.gjtapi.raw.remote.corba.TermArrayHelper.id (), "TermArray", __typeCode);
	}
	return __typeCode;
  }        
  public static void write (org.omg.CORBA.portable.OutputStream ostream, net.sourceforge.gjtapi.raw.remote.corba.TermData[] value)
  {
	ostream.write_long (value.length);
	for (int _i0 = 0;_i0 < value.length; ++_i0)
	  net.sourceforge.gjtapi.raw.remote.corba.TermDataHelper.write (ostream, value[_i0]);
  }        
}
