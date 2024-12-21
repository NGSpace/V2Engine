package io.github.ngspace.v2engine.v2runtime.values.operations;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import io.github.ngspace.v2engine.utils.CompileException;
import io.github.ngspace.v2engine.utils.Utils;
import io.github.ngspace.v2engine.utils.ValueGetter;
import io.github.ngspace.v2engine.v2runtime.AV2Compiler;
import io.github.ngspace.v2engine.v2runtime.V2Runtime;
import io.github.ngspace.v2engine.v2runtime.values.AV2Value;

public class V2ClassPropertyCall extends AV2Value {
	
	private AV2Value classobj;
	private boolean isFunctionCall;
	private AV2Value[] functionCallArgs;
	private String funcName = "";
	private String fieldName = "";

	public V2ClassPropertyCall(int line, int charpos, String value, AV2Compiler c, V2Runtime runtime,
			AV2Value classobj, String prop) throws CompileException {
		super(line, charpos, value, c);
		this.classobj = classobj;
		if (!prop.startsWith("(")&&prop.endsWith(")")) {
			int argStart = prop.indexOf("(");
			if (argStart!=-1) {
				this.funcName = prop.substring(0, argStart);
				if (funcName.matches("^[a-zA-Z0-9_-]*$")) {
					
					String parametersString = prop.substring(argStart+1, prop.length()-1);
					
					String[] tokenizedArgs = Utils.processParemeters(parametersString);
					functionCallArgs = new AV2Value[tokenizedArgs.length];
					
					for (int i=0;i<functionCallArgs.length;i++) 
						functionCallArgs[i] = c.getV2Value(runtime, tokenizedArgs[i], line, charpos);
					
					this.isFunctionCall = true;
				}
			}
		}
		if (!isFunctionCall) fieldName = prop;
	}
	@Override public Object get() throws CompileException {
		Object obj = smartGet();
		if (obj instanceof Set<?> r) return r.toArray();
		return obj;
	}

	public Object smartGet() throws CompileException {
		
		Object objValue = classobj.get();
		if (V2Runtime.VOID.equals(objValue))
			throw new CompileException("Can't read \"" + funcName+fieldName + "\" because \"" + classobj.value
					+ "\" returns void!", line, charpos);
		Class<?> objClass = objValue.getClass();
		
		if (objClass.isPrimitive())
			throw new CompileException("Can not read properties of Numbers, Booleans and Chars : "+classobj.value,line,charpos);
		
		if (isFunctionCall) {
			Object[] parameters = new Object[functionCallArgs.length];
			Class<?>[] classes = new Class<?>[functionCallArgs.length];
			for (int i=0;i<functionCallArgs.length;i++) {
				parameters[i] = functionCallArgs[i].get();
				classes[i] = parameters[i].getClass();
			}
			Object[] finalParameters = Arrays.copyOf(parameters, parameters.length);
			Method finalmethod = null;
			for (Method method : objClass.getMethods()) {
				if (!funcName.equals(method.getName())||method.getParameterCount()!=classes.length
						||!isAccessible(method)) continue;
				boolean isCompatible = true;
				Class<?>[] v = method.getParameterTypes();
				
				for (int i=0;i<v.length;i++) {
					if (v[i].isPrimitive()&&!v[i].isInstance(char.class)&&!v[i].isInstance(boolean.class)) {
						if (parameters[i] instanceof Number num) {
							if      (v[i].isAssignableFrom(int.class   )) finalParameters[i] = num.intValue   ();
							else if (v[i].isAssignableFrom(float.class )) finalParameters[i] = num.floatValue ();
							else if (v[i].isAssignableFrom(double.class)) finalParameters[i] = num.doubleValue();
							else if (v[i].isAssignableFrom(long.class  )) finalParameters[i] = num.longValue  ();
							else if (v[i].isAssignableFrom(byte.class  )) finalParameters[i] = num.byteValue  ();
							else if (v[i].isAssignableFrom(short.class )) finalParameters[i] = num.shortValue ();
							else isCompatible = false;
						} else isCompatible = false;
					} else if (!v[i].isAssignableFrom(classes[i])) isCompatible = false;
				}
				if (isCompatible) finalmethod = method;
			}
			if (finalmethod==null)
				throw new CompileException("No function named \""+getCallSign(classes)+"\" in type \"" +objClass.getSimpleName()+'"',line,charpos);
			
			try {
				return finalmethod.invoke(objValue, finalParameters);
			} catch (InvocationTargetException e) {
				e.getTargetException().printStackTrace();
				throw new CompileException(e.getTargetException().getMessage(), line, charpos, e.getTargetException());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new CompileException(e.getMessage(), line, charpos, e);
			}
		}
		
		try {
			Field f = objClass.getDeclaredField(fieldName);
			if (!isAccessible(f)) throw new CompileException("No property named \""+fieldName+"\" in type \"" +objClass.getSimpleName()+'"',line,charpos);
			return f.get(objValue);
		} catch (NoSuchFieldException e) {
			if (objValue instanceof ValueGetter getter) return getter.get(fieldName);
			e.printStackTrace();
			throw new CompileException("No property named \""+fieldName+'"',line,charpos);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			throw new CompileException("Failed Reflective Operation property named \""+fieldName+'"',line,charpos);
		}
	}
	
	

	private String getCallSign(Class<?>[] classes) {
		String res = funcName + "(";
		for (int i = 0;i<classes.length;i++) res+=classes[i].getSimpleName()+(classes.length==i+1?"":", ");
		return res + ")";
	}
	private boolean isAccessible(Member method) {
		return method.accessFlags().contains(AccessFlag.PUBLIC);
	}
	
	

	@Override public void setValue(AV2Compiler compiler, Object value) throws CompileException {
		throw new CompileException("Can't change the value of a ClassPropertyCall", line, charpos);
		
	}
	
	@Override public boolean isConstant() throws CompileException {return false;}
}
