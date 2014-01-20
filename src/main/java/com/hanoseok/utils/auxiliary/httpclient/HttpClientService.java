package com.hanoseok.utils.auxiliary.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;


public class HttpClientService {
	
	private NameValuePair [] nameValuePairs;
	private HttpURL httpURL;
	private int timeout = 5 * 1000;
	
	private int statusCode;
	private String data;
	private HttpClientCharset httpClientCharset = HttpClientCharset.UTF_8;
	
	private InputStream inputStream;
	
	public HttpClientService(String url) throws URIException{
		httpURL = new HttpURL(url);
		setParameter(httpURL.getQuery());
	}

	public HttpClientService(String url, int timeout) throws URIException{
		httpURL = new HttpURL(url);
		setParameter(httpURL.getQuery());
		this.timeout = timeout;
	}
	
	public HttpClientService(String url, HttpClientCharset httpClientCharset) throws URIException{
		httpURL = new HttpURL(url);
		setParameter(httpURL.getQuery());
		this.httpClientCharset = httpClientCharset;
	}
	
	public HttpClientService(String url, HttpClientCharset httpClientCharset, int timeout) throws URIException{
		httpURL = new HttpURL(url);
		this.httpClientCharset = httpClientCharset;
		this.timeout = timeout;
	}
	
	public HttpClientService(String host, String path) throws URIException{
		httpURL = new HttpURL(host, 80, path);
	}
	
	public HttpClientService(String host, String path, int timeout) throws URIException{
		httpURL = new HttpURL(host, 80, path);
		this.timeout = timeout;
	}
	
	public HttpClientService(String host, String path, HttpClientCharset httpClientCharset) throws URIException{
		httpURL = new HttpURL(host, 80, path);
		this.httpClientCharset = httpClientCharset;
	}
	
	public HttpClientService(String host, String path, HttpClientCharset httpClientCharset, int timeout) throws URIException{
		httpURL = new HttpURL(host, 80, path);
		this.httpClientCharset = httpClientCharset;
		this.timeout = timeout;
	}
	
	public HttpClientService(String host, short port) throws URIException{
		httpURL = new HttpURL(host, port, null);
	}
	
	public HttpClientService(String host, short port, HttpClientCharset httpClientCharset) throws URIException{
		httpURL = new HttpURL(host, port, null);
		this.httpClientCharset = httpClientCharset;
	}
	
	public HttpClientService(String host, short port, HttpClientCharset httpClientCharset, int timeout) throws URIException{
		httpURL = new HttpURL(host, port, null);
		this.httpClientCharset = httpClientCharset;
		this.timeout = timeout;
	}
	
	public HttpClientService(String host, short port, String path) throws URIException{
		httpURL = new HttpURL(host, port, path);
	}
	
	public HttpClientService(String host, short port, String path, int timeout) throws URIException{
		httpURL = new HttpURL(host, port, path);
		this.timeout = timeout;
	}
	
	public HttpClientService(String host, short port, String path, HttpClientCharset httpClientCharset) throws URIException{
		httpURL = new HttpURL(host, port, path);
		this.httpClientCharset = httpClientCharset;
	}
	public HttpClientService(String host, short port, String path, HttpClientCharset httpClientCharset, int timeout) throws URIException{
		httpURL = new HttpURL(host, port, path);
		this.httpClientCharset = httpClientCharset;
		this.timeout = timeout;
	}	
	
	public void setPort(short port) throws URIException {
		this.httpURL = new HttpURL(this.httpURL.getHost(), port, this.httpURL.getPath());
	}
	public void setUrl(String url) throws URIException {
		this.httpURL = new HttpURL(url);
	}
	public void setPath(String path) throws URIException {
		this.httpURL.setPath(path);
	}
	public void setHttpClientCharset(HttpClientCharset httpClientCharset){
		this.httpClientCharset = httpClientCharset;
	}
	public void setTimeout(int timeout){
		this.timeout = timeout;
	}
		
	public void addParameter(String key, String value){
		if(nameValuePairs == null){
			this.nameValuePairs = new NameValuePair[0];
		}
		int originSize = this.nameValuePairs.length;
		Arrays.copyOf(this.nameValuePairs, originSize + 1);
		this.nameValuePairs[originSize] = new NameValuePair(key, value);
	}
	
	public void setParameter(String query) {
		if(query != null){
			String [] querys = query.split("&");
			this.nameValuePairs = new NameValuePair[querys.length];
			int i = 0;
			for (String s : querys) {
				String [] q = s.split("=");
				this.nameValuePairs[i++] = new NameValuePair(q[0], q[1]);
			}
		}
	}
	
	public void setParameter(NameValuePair [] nameValuePairs){
		this.nameValuePairs = nameValuePairs;
	}
	
	public void setParameter(Map<String, String> map){
		int i = 0;
		NameValuePair [] nameValuePairs = new NameValuePair[map.size()];
		for (Entry<String, String> entry : map.entrySet()) {
			nameValuePairs[i++] = new NameValuePair(entry.getKey(), entry.getValue());
		}
		this.nameValuePairs = nameValuePairs;
	}
	
	private void execute(HttpMethod method) {
		HttpClient client = new HttpClient();
		
		try {
			String url = "http://" + this.httpURL.getHost() + ":" + this.httpURL.getPort() + this.httpURL.getPath();
			client.getHttpConnectionManager().getParams().setConnectionTimeout((int) this.timeout);
			method.setURI(new URI(url, true, this.httpClientCharset.getCharset()));
			if(this.nameValuePairs != null){
				method.setQueryString(this.nameValuePairs);
			}
			this.statusCode = client.executeMethod(method);
			this.inputStream = method.getResponseBodyAsStream();
			this.data = method.getResponseBodyAsString();		
			
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public int get(){
		execute(new GetMethod());
		return this.statusCode;
	}

	public int post(){
		execute(new PostMethod());
		return this.statusCode;
	}

	public String getData() {
		return data;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
	
	public void close(){
		
	}
	
	
}