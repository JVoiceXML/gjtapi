package net.sourceforge.gjtapi.raw.remote.corba;

/**
* com/uforce/jtapi/generic/raw/remote/corba/CallDataHelper.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from CorbaProvider.idl
* Thursday, November 16, 2000 1:38:18 o'clock PM EST
*/

abstract public class CallDataHelper
{
  private static String  _id = "IDL:com/uforce/jtapi/generic/raw/remote/corba/CallData:1.0";

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  public static net.sourceforge.gjtapi.raw.remote.corba.CallData extract (org.omg.CORBA.Any a)
  {
	return read (a.create_input_stream ());
  }        
  public static String id ()
  {
	return _id;
  }      
  public static void insert (org.omg.CORBA.Any a, net.sourceforge.gjtapi.raw.remote.corba.CallData that)
  {
	org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
	a.type (type ());
	write (out, that);
	a.read_value (out.create_input_stream (), type ());
  }        
  public static net.sourceforge.gjtapi.raw.remote.corba.CallData read (org.omg.CORBA.portable.InputStream istream)
  {
	net.sourceforge.gjtapi.raw.remote.corba.CallData value = new net.sourceforge.gjtapi.raw.remote.corba.CallData ();
	value.callId = istream.read_long ();
	value.state = istream.read_long ();
	int _len0 = istream.read_long ();
	value.connections = new net.sourceforge.gjtapi.raw.remote.corba.ConnectionData[_len0];
	for (int _o1 = 0;_o1 < value.connections.length; ++_o1)
	  value.connections[_o1] = net.sourceforge.gjtapi.raw.remote.corba.ConnectionDataHelper.read (istream);
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
		  org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [3];
		  org.omg.CORBA.TypeCode _tcOf_members0 = null;
		  _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
		  _members0[0] = new org.omg.CORBA.StructMember (
			"callId",
			_tcOf_members0,
			null);
		  _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
		  _members0[1] = new org.omg.CORBA.StructMember (
			"state",
			_tcOf_members0,
			null);
		  _tcOf_members0 = net.sourceforge.gjtapi.raw.remote.corba.ConnectionDataHelper.type ();
		  _tcOf_members0 = org.omg.CORBA.ORB.init ().create_sequence_tc (0, _tcOf_members0);
		  _members0[2] = new org.omg.CORBA.StructMember (
			"connections",
			_tcOf_members0,
			null);
		  __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (net.sourceforge.gjtapi.raw.remote.corba.CallDataHelper.id (), "CallData", _members0);
		  __active = false;
		}
	  }
	}
	return __typeCode;
  }        
  public static void write (org.omg.CORBA.portable.OutputStream ostream, net.sourceforge.gjtapi.raw.remote.corba.CallData value)
  {
	ostream.write_long (value.callId);
	ostream.write_long (value.state);
	ostream.write_long (value.connections.length);
	for (int _i0 = 0;_i0 < value.connections.length; ++_i0)
	  net.sourceforge.gjtapi.raw.remote.corba.ConnectionDataHelper.write (ostream, value.connections[_i0]);
  }        
}
