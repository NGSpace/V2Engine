package io.github.ngspace.v2engine.utils;

import io.github.ngspace.v2engine.v2runtime.V2Runtime;

public class CompileState {
	
	public boolean hasBroken = false;
	public Object returnValue = V2Runtime.VOID;
	public boolean hasReturned;
	
	public void setReturnValue(Object value) {hasReturned = true;returnValue = value;}
}
