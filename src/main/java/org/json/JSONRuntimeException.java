package org.json;

/**
 * The JSONRuntimeException is thrown by the JSON.org classes when things are amiss.
 * <p>
 * Updated to Runtime exception
 * 
 * @author JSON.org
 * @version 2008-09-18
 */
public class JSONRuntimeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 0;
	private Throwable cause;

	/**
	 * Constructs a JSONException with an explanatory message.
	 * 
	 * @param message
	 *            Detail about the reason for the exception.
	 */
	public JSONRuntimeException(String message) {
		super(message);
	}

	public JSONRuntimeException(Throwable t) {
		super(t.getMessage());
		this.cause = t;
	}

	public Throwable getCause() {
		return this.cause;
	}
}
