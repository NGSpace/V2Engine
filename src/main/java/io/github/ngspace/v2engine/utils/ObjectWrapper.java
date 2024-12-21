package io.github.ngspace.v2engine.utils;

/**
 * A wrapper to an Object with functions to convert to each type
 */
public interface ObjectWrapper {
	/**
	 * Return the current value of the Object
	 * @return an Object of any kind.
	 * @throws CompileException - failed to get value of object
	 */
	public Object get() throws CompileException;
	
	
	
	/**
	 * Returns the current value of the Object in the form of a String
	 * @return The object as a String.
	 * @throws CompileException - if failed to get value or convert it to string.
	 */
	public String asString() throws CompileException;
	
	
	/**
	 * Returns the current value of the Object in the form of a Double
	 * @return The Object as a Double.
	 * @throws CompileException - if failed to get value or convert it to Double.
	 */
	public double asDouble() throws CompileException;
	
	
	/**
	 * Returns the current value of the Object in the form of an Object array
	 * @return The Object as an Object array.
	 * @throws CompileException - if failed to get value or convert it to an Object array.
	 */
	public Object[] asArray() throws CompileException;
	
	
	/**
	 * Returns the current value of the Object in the form of a Boolean
	 * @return The Object as a Boolean.
	 * @throws CompileException - if failed to get value or convert it to Boolean.
	 */
	public boolean asBoolean() throws CompileException;
	
	public <T> T asType(Class<T> clazz) throws CompileException;
	
	
	
	

	/**
	 * Returns the current value of the Object in the form of a Float
	 * @return The Object as a Float.
	 * @throws CompileException - if failed to get value or convert it to Float.
	 */
	public default float asFloat() throws CompileException {return (float) asDouble();}
	
	
	/**
	 * Returns the current value of the Object in the form of a Integer
	 * @return The Object as a Integer.
	 * @throws CompileException - if failed to get value or convert it to Integer.
	 */
	public default int asInt() throws CompileException {return (int) asDouble();}
	
	
	/**
	 * Returns the current value of the Object in the form of a Long
	 * @return The Object as a Long.
	 * @throws CompileException - if failed to get value or convert it to Long.
	 */
	public default long asLong() throws CompileException {return (long) asDouble();}
	
	
	
	public abstract String toString();
}
