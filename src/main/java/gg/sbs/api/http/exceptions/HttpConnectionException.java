package gg.sbs.api.http.exceptions;

import gg.sbs.api.http.HttpBody;
import gg.sbs.api.http.HttpStatus;
import gg.sbs.api.util.StringUtil;

public class HttpConnectionException extends Exception {

	private final HttpBody body;
	private final HttpStatus status;

	public HttpConnectionException(HttpStatus status, HttpBody body) {
		this(status, body, status.getMessage());
	}

	public HttpConnectionException(Throwable throwable) {
		this(HttpStatus.UNKNOWN_ERROR, throwable);
	}

	public HttpConnectionException(HttpStatus status, Throwable throwable) {
		super(StringUtil.format("{0}: {1}: {2}", status.getMessage(), throwable.getClass().getName(), throwable.getMessage()), throwable);
		this.body = HttpBody.EMPTY;
		this.status = status;
	}

	private HttpConnectionException(HttpStatus status, HttpBody body, String message) {
		super(message);
		this.body = body;
		this.status = status;
	}

	public HttpBody getBody() {
		return this.body;
	}

	public HttpStatus getStatus() {
		return this.status;
	}

}