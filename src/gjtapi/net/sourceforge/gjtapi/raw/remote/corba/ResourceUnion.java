package net.sourceforge.gjtapi.raw.remote.corba;

/**
* com/uforce/jtapi/generic/raw/remote/corba/ResourceUnion.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from CorbaProvider.idl
* Thursday, November 16, 2000 1:38:18 o'clock PM EST
*/

public final class ResourceUnion implements org.omg.CORBA.portable.IDLEntity
{
  private net.sourceforge.gjtapi.raw.remote.corba.PlayerEvent ___playEv;
  private net.sourceforge.gjtapi.raw.remote.corba.RecorderEvent ___recEv;
  private net.sourceforge.gjtapi.raw.remote.corba.SigDetectorEvent ___sdEv;
  private net.sourceforge.gjtapi.raw.remote.corba.ResourceType __discriminator;
  private boolean __uninitialized = true;

  public ResourceUnion ()
  {
  }      
  public void _default ()
  {
	__discriminator = net.sourceforge.gjtapi.raw.remote.corba.ResourceType.sigDetector;
	__uninitialized = false;
  }        
  public net.sourceforge.gjtapi.raw.remote.corba.ResourceType discriminator ()
  {
	if (__uninitialized)
	  throw new org.omg.CORBA.BAD_OPERATION ();
	return __discriminator;
  }        
  public net.sourceforge.gjtapi.raw.remote.corba.PlayerEvent playEv ()
  {
	if (__uninitialized)
	  throw new org.omg.CORBA.BAD_OPERATION ();
	verifyplayEv (__discriminator);
	return ___playEv;
  }        
  public void playEv (net.sourceforge.gjtapi.raw.remote.corba.PlayerEvent value)
  {
	__discriminator = net.sourceforge.gjtapi.raw.remote.corba.ResourceType.player;
	___playEv = value;
	__uninitialized = false;
  }        
  public net.sourceforge.gjtapi.raw.remote.corba.RecorderEvent recEv ()
  {
	if (__uninitialized)
	  throw new org.omg.CORBA.BAD_OPERATION ();
	verifyrecEv (__discriminator);
	return ___recEv;
  }        
  public void recEv (net.sourceforge.gjtapi.raw.remote.corba.RecorderEvent value)
  {
	__discriminator = net.sourceforge.gjtapi.raw.remote.corba.ResourceType.recorder;
	___recEv = value;
	__uninitialized = false;
  }        
  public net.sourceforge.gjtapi.raw.remote.corba.SigDetectorEvent sdEv ()
  {
	if (__uninitialized)
	  throw new org.omg.CORBA.BAD_OPERATION ();
	verifysdEv (__discriminator);
	return ___sdEv;
  }        
  public void sdEv (net.sourceforge.gjtapi.raw.remote.corba.SigDetectorEvent value)
  {
	__discriminator = net.sourceforge.gjtapi.raw.remote.corba.ResourceType.sigGenerator;
	___sdEv = value;
	__uninitialized = false;
  }        
  private void verifyplayEv (net.sourceforge.gjtapi.raw.remote.corba.ResourceType discriminator)
  {
	if (discriminator != net.sourceforge.gjtapi.raw.remote.corba.ResourceType.player)
	  throw new org.omg.CORBA.BAD_OPERATION ();
  }        
  private void verifyrecEv (net.sourceforge.gjtapi.raw.remote.corba.ResourceType discriminator)
  {
	if (discriminator != net.sourceforge.gjtapi.raw.remote.corba.ResourceType.recorder)
	  throw new org.omg.CORBA.BAD_OPERATION ();
  }        
  private void verifysdEv (net.sourceforge.gjtapi.raw.remote.corba.ResourceType discriminator)
  {
	if (discriminator != net.sourceforge.gjtapi.raw.remote.corba.ResourceType.sigGenerator)
	  throw new org.omg.CORBA.BAD_OPERATION ();
  }        
} // class ResourceUnion
