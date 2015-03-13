package danielbuecheler.sporttage;

public class TableNotExistentException extends Exception {

	private static final long serialVersionUID = -8513222687598696335L;

	public TableNotExistentException(String message) {
		super(message);
	}

	public TableNotExistentException(Throwable cause) {
		super(cause);
	}

	public TableNotExistentException(String message, Throwable cause) {
		super(message, cause);
	}

	public TableNotExistentException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
