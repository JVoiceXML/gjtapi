package net.sourceforge.gjtapi.raw.remote.corba;

/**
* com/uforce/jtapi/generic/raw/remote/corba/ResourceUnionHelper.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from CorbaProvider.idl
* Thursday, November 16, 2000 1:38:18 o'clock PM EST
*/

abstract public class ResourceUnionHelper
{
  private static String  _id = "IDL:com/uforce/jtapi/generic/raw/remote/corba/ResourceUnion:1.0";

  private static org.omg.CORBA.TypeCode __typeCode = null;
  public static net.sourceforge.gjtapi.raw.remote.corba.ResourceUnion extract (org.omg.CORBA.Any a)
  {
	return read (a.create_input_stream ());
  }        
  public static String id ()
  {
	return _id;
  }      
  public static void insert (org.omg.CORBA.Any a, net.sourceforge.gjtapi.raw.remote.corba.ResourceUnion that)
  {
	org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
	a.type (type ());
	write (out, that);
	a.read_value (out.create_input_stream (), type ());
  }        
  public static net.sourceforge.gjtapi.raw.remote.corba.ResourceUnion read (org.omg.CORBA.portable.InputStream istream)
  {
	net.sourceforge.gjtapi.raw.remote.corba.ResourceUnion value = new net.sourceforge.gjtapi.raw.remote.corba.ResourceUnion ();
	net.sourceforge.gjtapi.raw.remote.corba.ResourceType _dis0 = null;
	_dis0 = net.sourceforge.gjtapi.raw.remote.corba.ResourceTypeHelper.read (istream);
	switch (_dis0.value ())
	{
	  case net.sourceforge.gjtapi.raw.remote.corba.ResourceType._player:
		net.sourceforge.gjtapi.raw.remote.corba.PlayerEvent _playEv = null;
		_playEv = net.sourceforge.gjtapi.raw.remote.corba.PlayerEventHelper.read (istream);
		value.playEv (_playEv);
		break;
	  case net.sourceforge.gjtapi.raw.remote.corba.ResourceType._recorder:
		net.sourceforge.gjtapi.raw.remote.corba.RecorderEvent _recEv = null;
		_recEv = net.sourceforge.gjtapi.raw.remote.corba.RecorderEventHelper.read (istream);
		value.recEv (_recEv);
		break;
	  case net.sourceforge.gjtapi.raw.remote.corba.ResourceType._sigGenerator:
		net.sourceforge.gjtapi.raw.remote.corba.SigDetectorEvent _sdEv = null;
		_sdEv = net.sourceforge.gjtapi.raw.remote.corba.SigDetectorEventHelper.read (istream);
		value.sdEv (_sdEv);
		break;
	  default:
		throw new org.omg.CORBA.BAD_OPERATION ();
	}
	return value;
  }        
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
	if (__typeCode == null)
	{
	  org.omg.CORBA.TypeCode _disTypeCode0;
	  _disTypeCode0 = net.sourceforge.gjtapi.raw.remote.corba.ResourceTypeHelper.type ();
	  org.omg.CORBA.UnionMember[] _members0 = new org.omg.CORBA.UnionMember [3];
	  org.omg.CORBA.TypeCode _tcOf_members0;
	  org.omg.CORBA.Any _anyOf_members0;

	  // Branch for playEv
	  _anyOf_members0 = org.omg.CORBA.ORB.init ().create_any ();
	  net.sourceforge.gjtapi.raw.remote.corba.ResourceTypeHelper.insert (_anyOf_members0, net.sourceforge.gjtapi.raw.remote.corba.ResourceType.player);
	  _tcOf_members0 = net.sourceforge.gjtapi.raw.remote.corba.PlayerEventHelper.type ();
	  _members0[0] = new org.omg.CORBA.UnionMember (
		"playEv",
		_anyOf_members0,
		_tcOf_members0,
		null);

	  // Branch for recEv
	  _anyOf_members0 = org.omg.CORBA.ORB.init ().create_any ();
	  net.sourceforge.gjtapi.raw.remote.corba.ResourceTypeHelper.insert (_anyOf_members0, net.sourceforge.gjtapi.raw.remote.corba.ResourceType.recorder);
	  _tcOf_members0 = net.sourceforge.gjtapi.raw.remote.corba.RecorderEventHelper.type ();
	  _members0[1] = new org.omg.CORBA.UnionMember (
		"recEv",
		_anyOf_members0,
		_tcOf_members0,
		null);

	  // Branch for sdEv
	  _anyOf_members0 = org.omg.CORBA.ORB.init ().create_any ();
	  net.sourceforge.gjtapi.raw.remote.corba.ResourceTypeHelper.insert (_anyOf_members0, net.sourceforge.gjtapi.raw.remote.corba.ResourceType.sigGenerator);
	  _tcOf_members0 = net.sourceforge.gjtapi.raw.remote.corba.SigDetectorEventHelper.type ();
	  _members0[2] = new org.omg.CORBA.UnionMember (
		"sdEv",
		_anyOf_members0,
		_tcOf_members0,
		null);
	  __typeCode = org.omg.CORBA.ORB.init ().create_union_tc (net.sourceforge.gjtapi.raw.remote.corba.ResourceUnionHelper.id (), "ResourceUnion", _disTypeCode0, _members0);
	}
	return __typeCode;
  }        
  public static void write (org.omg.CORBA.portable.OutputStream ostream, net.sourceforge.gjtapi.raw.remote.corba.ResourceUnion value)
  {
	net.sourceforge.gjtapi.raw.remote.corba.ResourceTypeHelper.write (ostream, value.discriminator ());
	switch (value.discriminator ().value ())
	{
	  case net.sourceforge.gjtapi.raw.remote.corba.ResourceType._player:
		net.sourceforge.gjtapi.raw.remote.corba.PlayerEventHelper.write (ostream, value.playEv ());
		break;
	  case net.sourceforge.gjtapi.raw.remote.corba.ResourceType._recorder:
		net.sourceforge.gjtapi.raw.remote.corba.RecorderEventHelper.write (ostream, value.recEv ());
		break;
	  case net.sourceforge.gjtapi.raw.remote.corba.ResourceType._sigGenerator:
		net.sourceforge.gjtapi.raw.remote.corba.SigDetectorEventHelper.write (ostream, value.sdEv ());
		break;
	  default:
		throw new org.omg.CORBA.BAD_OPERATION ();
	}
  }        
}
