package danielbuecheler.sporttage;

public class TableNotExistentException extends Exception {

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
