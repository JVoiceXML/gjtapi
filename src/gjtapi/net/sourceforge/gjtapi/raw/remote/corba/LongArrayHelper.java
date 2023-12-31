package net.sourceforge.gjtapi.raw.remote.corba;

/**
* com/uforce/jtapi/generic/raw/remote/corba/LongArrayHelper.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from CorbaProvider.idl
* Thursday, November 16, 2000 1:38:18 o'clock PM EST
*/

abstract public class LongArrayHelper
{
  private static String  _id = "IDL:com/uforce/jtapi/generic/raw/remote/corba/LongArray:1.0";

  private static org.omg.CORBA.TypeCode __typeCode = null;
  public static int[] extract (org.omg.CORBA.Any a)
  {
	return read (a.create_input_stream ());
  }      
  public static String id ()
  {
	return _id;
  }      
  public static void insert (org.omg.CORBA.Any a, int[] that)
  {
	org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
	a.type (type ());
	write (out, that);
	a.read_value (out.create_input_stream (), type ());
  }      
  public static int[] read (org.omg.CORBA.portable.InputStream istream)
  {
	int value[] = null;
	int _len0 = istream.read_long ();
	value = new int[_len0];
	istream.read_long_array (value, 0, _len0);
	return value;
  }      
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
	if (__typeCode == null)
	{
	  __typeCode = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
	  __typeCode = org.omg.CORBA.ORB.init ().create_sequence_tc (0, __typeCode);
	  __typeCode = org.omg.CORBA.ORB.init ().create_alias_tc (net.sourceforge.gjtapi.raw.remote.corba.LongArrayHelper.id (), "LongArray", __typeCode);
	}
	return __typeCode;
  }        
  public static void write (org.omg.CORBA.portable.OutputStream ostream, int[] value)
  {
	ostream.write_long (value.length);
	ostream.write_long_array (value, 0, value.length);
  }      
}
