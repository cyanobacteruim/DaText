/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datext;

import datext.util.Formatter;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * This is the abstract superclass for all DaText implementations. It provides 
 * get and set methods for all possible subtypes, even if the underlying data 
 * doesn't match that type (throwing an UnsupportedOperationException). This is 
 * to simplify the use of this class instead of casting every element to the 
 * appropriate subtype.
 * @author Christopher Collin Hall
 */
public abstract class DaTextVariable implements Cloneable{
	/** Lock to synchronize reading and writing */
	protected final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	/** All getter methods must use this lock (or their own Read/Write Lock) to be thread-safe */
	protected final ReadLock readLock = readWriteLock.readLock();
	/** All setter methods must use this lock (or their own Read/Write Lock) to be thread-safe */
	protected final WriteLock writeLock = readWriteLock.writeLock();
	
	
	

	/** Annotation content */
	private String annotation = null;
//	/** Name of this element (null if root) */
//	private String name = null;
	
	
	/**
	 * Returns the annotation for this variable.
	 * <p/>This method is thread-safe.
	 * @return Returns this element's annotation coment, or null if there is 
	 * none.
	 */
	public String getAnnotation(){
		readLock.lock();
		try{
			return annotation;
		}finally {
			readLock.unlock();
		}
	}
	/**
	 * Sets the annotation comment for this element.
	 * <p/>This method is thread-safe.
	 * @param content The comment to go with this element.
	 */
	public void setAnnotation(String content){
		writeLock.lock();
		try{
			annotation = content;
		}finally {
			writeLock.unlock();
		}
	}
//	/**
//	 * Gets the variable name of this element.
//	 * <p/>This method is thread-safe.
//	 * @return Name of this element, or null if this is a root element.
//	 */
//	public String getName(){
//		readLock.lock();
//		try{
//			return name;
//		}finally {
//			readLock.unlock();
//		}
//	}
//	/**
//	 * Sets the variable name of this element.
//	 * <p/>This method is thread-safe.
//	 * @param newName Name of this element, or null if this is a root element.
//	 */
//	public void setName(String newName){
//		writeLock.lock();
//		try{
//			name = newName;
//		}finally {
//			writeLock.unlock();
//		}
//	}
	
	
	// DaText Types:
	//	Binary	<- byte[]
	//	Int	<- int
	//	Long	<- long
	//	Number	<- double
	//	Text	<- String
	//	List	<- List<DaTextVariable>
	//	Object	<- DaTextObject
	
	/**
	 * Gets the value of this variable as a String
	 * @return The value stored in this variable.
	 */
	public abstract String asText();
	
	
	/**
	 * Gets the value of this variable as an integer
	 * @return The value stored in this variable.
	 * @throws UnsupportedOperationException Thrown if the value is not 
	 * a compatible type.
	 * @throws NumberFormatException Thrown if the field could not be parsed as 
	 * a number (may be due to locale mismatch)
	 */
	public abstract int asInt() throws UnsupportedOperationException, NumberFormatException;
	/**
	 * Gets the value of this variable as a 64-bit integer
	 * @return The value stored in this variable.
	 * @throws UnsupportedOperationException Thrown if the value is not 
	 * a compatible type.
	 * @throws NumberFormatException Thrown if the field could not be parsed as 
	 * a number (may be due to locale mismatch)
	 */
	public abstract long asLong() throws UnsupportedOperationException, NumberFormatException;
	/**
	 * Gets the value of this variable as a decimal number
	 * @return The value stored in this variable.
	 * @throws UnsupportedOperationException Thrown if the value is not 
	 * a compatible type.
	 * @throws NumberFormatException Thrown if the field could not be parsed as 
	 * a number (may be due to locale mismatch)
	 */
	public abstract double asNumber() throws UnsupportedOperationException, NumberFormatException;
	/**
	 * Gets the value of this variable as a byte array
	 * @return The value stored in this variable.
	 * @throws UnsupportedOperationException Thrown if the value is not 
	 * a compatible type.
	 */
	public abstract byte[] asBinary() throws UnsupportedOperationException, NumberFormatException;
	/**
	 * Gets the value of this variable as a list of variables
	 * @return The value stored in this variable.
	 * @throws UnsupportedOperationException Thrown if the value is not 
	 * a compatible type.
	 */
	public abstract DaTextList asList() throws UnsupportedOperationException;
	/**
	 * Gets the value of this variable as a DaText object
	 * @return The value stored in this variable.
	 * @throws UnsupportedOperationException Thrown if the value is not 
	 * a compatible type.
	 */
	public DaTextObject asObject() throws UnsupportedOperationException{
		if(this instanceof DaTextObject){
			return (DaTextObject)this;
		} else {
			throw new UnsupportedOperationException(this.getClass().getName() 
					+ " cannot be converted to " + DaTextObject.class.getName()
					+ " (not a valid DaText object definition)");
		}
	}
	
	/**
	 * Sets the value of this variable
	 * @param value The value to store in this variable.
	 */
	public abstract void set(String value);
	/**
	 * Sets the value of this variable
	 * @param value The value to store in this variable.
	 */
	public abstract void set(int value);
	/**
	 * Sets the value of this variable
	 * @param value The value to store in this variable.
	 */
	public abstract void set(long value);
	/**
	 * Sets the value of this variable
	 * @param value The value to store in this variable.
	 */
	public abstract void set(double value);
	/**
	 * Sets the value of this variable
	 * @param value The value to store in this variable.
	 */
	public abstract void set(byte[] value);
	/**
	 * Sets the value of this variable
	 * @param value The value to store in this variable.
	 */
	public abstract void set(DaTextList value);
	/**
	 * Sets the value of this variable
	 * @param value The value to store in this variable.
	 */
	public abstract void set(DaTextObject value);
	/**
	 * Implementations must override the <code>clone()</code> method 
	 * to perform a deep copy, treating the copy at the root of a new 
	 * tree.
	 * @return A copy of this element that is an independant root whose 
	 * children elements and fields are all copies of the those of this element.
	 */
	@Override public abstract DaTextVariable clone();
	
	/**
	 * Converts an array of objects into a list of variables.
	 * @param elements 
	 * @return
	 * @throws IllegalArgumentException 
	 */
	public static List<DaTextVariable> toVariableList(Object... elements) throws IllegalArgumentException{
		List<DaTextVariable> list = new java.util.ArrayList<>();
		for(Object o : elements){
			
				// TODO: add values to list unless nt supported values
			
			
		}
		
		throw new UnsupportedOperationException("Not Done Yet");
	}
	
	
	
	/**
	 * Writes the value of this variable as a string to the given 
	 * writer, applying the appropriate escape charafcters to make the 
	 * output readable by a DaTextParser. 
	 * Usually this is just forwarded to the asText() method, but some 
	 * implementations may override the default behavior for improved 
	 * performance in a streaming environment.
	 * @param outputStream A writer to the stream.
	 * @param doIndent If <code>true</code>, then tab characters will be 
	 * inserted at the beginning of each line to improve human legibility.
	 * @param indent If <code>doIndent</code> is <code>true</code>, then 
	 * this is the number of tabs to indent (typically the toplevel DaText 
	 * object would be written with an indent of 0)
	 * @throws java.io.IOException Thrown if there was an error writing to the 
	 * stream.
	 */
	public void serialize(java.io.Writer outputStream, boolean doIndent, int indent) throws java.io.IOException{
		readLock.lock();
		try {
			outputStream.write(Formatter.escapeText(this.asText()));
		} finally {
			readLock.unlock();
		}
	}
}
