package pe.beyond.gls.model;

import java.io.Serializable;

public class Progress implements Serializable
{
	private static final long serialVersionUID = -9069457823383811764L;
	
	private int intPending;
	private int intInProgress;
	private int intExecuted;
	private int intFinished;
	private String strModuleCod;
	private String strModuleDesc;
	private int intSended;
	private int intNotSended;
	
	// Get y Set
	public int getIntPending() 
	{
		return intPending;
	}
	public void setIntPending(int intPending) 
	{
		this.intPending = intPending;
	}
	
	public int getIntInProgress() 
	{
		return intInProgress;
	}
	public void setIntInProgress(int intInProgress) 
	{
		this.intInProgress = intInProgress;
	}
	
	public int getIntExecuted() 
	{
		return intExecuted;
	}
	public void setIntExecuted(int intExecuted) 
	{
		this.intExecuted = intExecuted;
	}
	
	public int getIntFinished() 
	{
		return intFinished;
	}
	public void setIntFinished(int intFinished) 
	{
		this.intFinished = intFinished;
	}
	
	public String getStrModuleCod() 
	{
		return strModuleCod;
	}
	public void setStrModuleCod(String strModuleCod) 
	{
		this.strModuleCod = strModuleCod;
	}
	
	public String getStrModuleDesc() 
	{
		return strModuleDesc;
	}
	public void setStrModuleDesc(String strModuleDesc) 
	{
		this.strModuleDesc = strModuleDesc;
	}
	
	public int getIntSended() {
		return intSended;
	}
	public void setIntSended(int intSended) {
		this.intSended = intSended;
	}
	
	public int getIntNotSended() {
		return intNotSended;
	}
	public void setIntNotSended(int intNotSended) {
		this.intNotSended = intNotSended;
	}
	
	
}
