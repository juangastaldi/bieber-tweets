package org.interview.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

import org.interview.context.AppContext;
import org.interview.exception.InterviewException;
import org.interview.exception.TwitterAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.common.collect.Lists;

@Component
public class ConnectionUtil {

	@Autowired
	private AppContext appContext;

	/**
	 * Connect to the requested path and return a list of String with the content of
	 * the Stream
	 * 
	 * @param httpFactory
	 * @param path
	 * @param parameters
	 * @return List of String
	 * @throws TwitterAuthenticationException
	 * @throws InterviewException
	 */
	public Pair<List<String>, Long> readStream(HttpRequestFactory httpFactory, String path,
			Map<String, String> parameters) throws TwitterAuthenticationException, InterviewException {
		GZIPInputStream inputStream = getStreamBufferedReaderRest(httpFactory, path, parameters);
		BufferedReader reader = getReader(inputStream);
		return readBufferedReader(reader);
	}

	/**
	 * Connect to the request path and return a {@link GZIPInputStream}
	 * 
	 * @param httpFactory
	 * @param path
	 * @param parameters
	 * @return GZIPInputStream
	 * @throws TwitterAuthenticationException
	 * @throws InterviewException
	 */
	public GZIPInputStream getStreamBufferedReaderRest(HttpRequestFactory http, String path,
			Map<String, String> parameters) throws TwitterAuthenticationException, InterviewException {

		GenericUrl url = new GenericUrl(path);
		if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
			parameters.entrySet().stream().forEach(p -> url.set(p.getKey(), p.getValue()));
		}

		HttpRequest httpR;
		try {
			httpR = http.buildGetRequest(url).setConnectTimeout(30000);
			HttpResponse resp = httpR.execute();
			return (GZIPInputStream) resp.getContent();
		} catch (IOException e) {
			throw new InterviewException("Conection error", e);
		}
	}

	/**
	 * Create new {@link BufferedReader} from {@link GZIPInputStream}
	 * 
	 * @param gis
	 * @return
	 * @throws InterviewException
	 */
	public BufferedReader getReader(GZIPInputStream gis) throws InterviewException {
		try {
			return new BufferedReader(new InputStreamReader(gis, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new InterviewException("Error creating BufferedReader", e);
		}
	}

	/**
	 * Return the content of the Stream in a List of String, one Object per line.
	 * Limit the response to {@value #ROW_LIMIT} rows or {@value #TIME_LIMIT}
	 * milliseconds
	 * 
	 * @param reader
	 * @return
	 * @throws InterviewException
	 */
	public Pair<List<String>, Long> readBufferedReader(BufferedReader reader) throws InterviewException {
		List<String> lines = Lists.newArrayList();
		if (Objects.isNull(reader)) {
			return Pair.of(lines, 0L);
		}
		String line;
		int i = 0;
		long start = System.currentTimeMillis();
		long time = 0L;
		try {
			while ((line = reader.readLine()) != null && i < appContext.getFilterRowLimit()
					&& time < appContext.getFilterTimeLimit()) {
				lines.add(line);
				i++;
				time = System.currentTimeMillis() - start;
			}

		} catch (IOException e) {
			throw new InterviewException("Error reading inputStream", e);
		}
		return Pair.of(lines, time);
	}

	public HttpResponse executeGetRequest(HttpRequestFactory http, String path) throws IOException {
		GenericUrl url = new GenericUrl(path);
		HttpRequest httpR = http.buildGetRequest(url);
		return httpR.execute();
	}
}
