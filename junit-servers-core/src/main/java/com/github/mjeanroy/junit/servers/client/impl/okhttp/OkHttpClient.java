package com.github.mjeanroy.junit.servers.client.impl.okhttp;

import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpMethod;
import com.github.mjeanroy.junit.servers.client.HttpRequest;
import com.github.mjeanroy.junit.servers.client.impl.AbstractHttpClient;
import com.github.mjeanroy.junit.servers.servers.EmbeddedServer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of {@link HttpClient} using OkHttp library from Square.
 * @see <a href="http://square.github.io/okhttp">http://square.github.io/okhttp</a>
 */
public class OkHttpClient extends AbstractHttpClient implements HttpClient {

	/**
	 * Create new http client using internal
	 * http client from ok-http library.
	 *
	 * @param server Embedded server.
	 * @return Http client.
	 */
	public static OkHttpClient defaultOkHttpClient(EmbeddedServer server) {
		return new OkHttpClient(server, new okhttp3.OkHttpClient());
	}

	/**
	 * Create new http client using internal
	 * http client from ok-http library.
	 *
	 * @param server Embedded server.
	 * @param client The custom client.
	 * @return Http client.
	 */
	public static OkHttpClient newOkHttpClient(EmbeddedServer server, okhttp3.OkHttpClient client) {
		return new OkHttpClient(server, client);
	}

	/**
	 * Flag to ensure that the http client has been destroyed or not.
	 */
	private final AtomicBoolean destroyed;

	/**
	 * The native OkHttp client.
	 */
	private final okhttp3.OkHttpClient client;

	/**
	 * Create the client.
	 * @param server The embedded server that will be queried.
	 * @param client The internal client.
	 */
	private OkHttpClient(EmbeddedServer server, okhttp3.OkHttpClient client) {
		super(server);
		this.client = client;
		this.destroyed = new AtomicBoolean(false);
	}

	@Override
	protected HttpRequest buildRequest(HttpMethod httpMethod, String url) {
		return new OkHttpRequest(this.client, httpMethod, url);
	}

	@Override
	public void destroy() {
		if (destroyed.compareAndSet(false, true)) {
			client.dispatcher().executorService().shutdown();
			client.connectionPool().evictAll();
		}
	}

	@Override
	public boolean isDestroyed() {
		return destroyed.get();
	}
}
