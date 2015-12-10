package cz.vse.rdf.controller;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AjaxResponse<T> implements Serializable{
	
	public final T content;
	public final Code code;
	
	public AjaxResponse(T content, Code code) {
		super();
		this.content = content;
		this.code = code;
	}
	
	public static <V> AjaxResponse<V> ok(V content) {
		return new AjaxResponse<V>(content, Code.OK);
	}
	
	public static <V> AjaxResponse<V> fail(V content) {
		return new AjaxResponse<V>(content, Code.FAIL);
	}

	public static enum Code {
		OK,
		FAIL;
	}
}
