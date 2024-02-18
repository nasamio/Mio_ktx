/**
 * 
 */
package net.sourceforge.capcode.S57Library.files;

/**
 * @author cyrille
 *
 */
@SuppressWarnings("serial")
public class S57ReadException extends Exception {

	/**
	 * 
	 */
	public S57ReadException() {
		super();
	}

	/**
	 * @param message
	 */
	public S57ReadException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public S57ReadException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public S57ReadException(String message, Throwable cause) {
		super(message, cause);
	}

}
