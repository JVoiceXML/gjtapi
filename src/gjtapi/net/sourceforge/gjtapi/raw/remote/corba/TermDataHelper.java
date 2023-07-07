package net.sourceforge.gjtapi.raw.remote.corba;

/**
* com/uforce/jtapi/generic/raw/remote/corba/TermDataHelper.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from CorbaProvider.idl
* Thursday, November 16, 2000 1:38:18 o'clock PM EST
*/

abstract public class TermDataHelper
{
  private static String  _id = "IDL:com/uforce/jtapi/generic/raw/remote/corba/TermData:1.0";

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  public static net.sourceforge.gjtapi.raw.remote.corba.TermData extract (org.omg.CORBA.Any a)
  {
	return read (a.create_input_stream ());
  }        
  public static String id ()
  {
	return _id;
  }      
  public static void insert (org.omg.CORBA.Any a, net.sourceforge.gjtapi.raw.remote.corba.TermData that)
  {
	org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
	a.type (type ());
	write (out, that);
	a.read_value (out.create_input_stream (), type ());
  }        
  public static net.sourceforge.gjtapi.raw.remote.corba.TermData read (org.omg.CORBA.portable.InputStream istream)
  {
	net.sourceforge.gjtapi.raw.remote.corba.TermData value = new net.sourceforge.gjtapi.raw.remote.corba.TermData ();
	value.terminal = istream.read_string ();
	value.isMedia = istream.read_boolean ();
	return value;
  }        
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
	if (__typeCode == null)
	{
	  synchronized (org.omg.CORBA.TypeCode.class)
	  {
		if (__typeCode == null)
		{
		  if (__active)
		  {
			return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
		  }
		  __active = true;
		  org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [2];
		  org.omg.CORBA.TypeCode _tcOf_members0 = null;
		  _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
		  _members0[0] = new org.omg.CORBA.StructMember (
			"terminal",
			_tcOf_members0,
			null);
		  _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_boolean);
		  _members0[1] = new org.omg.CORBA.StructMember (
			"isMedia",
			_tcOf_members0,
			null);
		  __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (net.sourceforge.gjtapi.raw.remote.corba.TermDataHelper.id (), "TermData", _members0);
		  __active = false;
		}
	  }
	}
	return __typeCode;
  }        
  public static void write (org.omg.CORBA.portable.OutputStream ostream, net.sourceforge.gjtapi.raw.remote.corba.TermData value)
  {
	ostream.write_string (value.terminal);
	ostream.write_boolean (value.isMedia);
  }        
}
