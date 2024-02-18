/**
 * 
 */
package net.sourceforge.capcode.S57Library.objects;

/**
 * @author cyrille
 *
 */
@SuppressWarnings("serial")
public class S57WrongTypeException extends Exception {

	/**
	 * 
	 */
	public S57WrongTypeException() {
		super();
	}

	/**
	 * @param message
	 */
	public S57WrongTypeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public S57WrongTypeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public S57WrongTypeException(String message, Throwable cause) {
		super(message, cause);
	}

}
