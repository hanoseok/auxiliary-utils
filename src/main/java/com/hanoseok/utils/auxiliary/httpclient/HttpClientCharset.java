package com.hanoseok.utils.auxiliary.httpclient;

public enum HttpClientCharset {

	UTF_8 ("UTF-8"),
	EUC_KR ("EUC-KR")
	;
	
	private String charset;
	private HttpClientCharset(String charset) {
		this.charset = charset;
	}
	public String getCharset() {
		return this.charset;
	}
	
}
