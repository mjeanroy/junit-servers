/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2018 <mickael.jeanroy@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.mjeanroy.junit.servers.servers;

import com.github.mjeanroy.junit.servers.utils.impl.FakeEmbeddedServer;

import java.util.concurrent.CountDownLatch;

/**
 * Worker that is used to test thread safety of {@link AbstractEmbeddedServer} class.
 */
class FakeWorker implements Runnable {

	private static final Operation START_OP = new Operation() {
		@Override
		public void apply(FakeEmbeddedServer server) {
			server.start();
		}
	};

	private static final Operation STOP_OP = new Operation() {
		@Override
		public void apply(FakeEmbeddedServer server) {
			server.stop();
		}
	};

	/**
	 * Create worker that will start {@code server} (i.e call {@code server.start()}.
	 *
	 * @param server The server.
	 * @param startSignal The signal that will let {@code start} method to be called.
	 * @param doneSignal The signal that will be released when {@code start} has been called.
	 * @return The worker.
	 */
	static FakeWorker startWorker(FakeEmbeddedServer server, CountDownLatch startSignal, CountDownLatch doneSignal) {
		return new FakeWorker(START_OP, server, startSignal, doneSignal);
	}

	/**
	 * Create worker that will stop {@code server} (i.e call {@code server.stop()}.
	 *
	 * @param server The server.
	 * @param startSignal The signal that will let {@code stop} method to be called.
	 * @param doneSignal The signal that will be released when {@code stop} has been called.
	 * @return The worker.
	 */
	static FakeWorker stopWorker(FakeEmbeddedServer server, CountDownLatch startSignal, CountDownLatch doneSignal) {
		return new FakeWorker(STOP_OP, server, startSignal, doneSignal);
	}

	/**
	 * The server.
	 */
	private final FakeEmbeddedServer server;

	/**
	 * The signal that will let {@link #operation} method to be called.
	 */
	private final CountDownLatch startSignal;

	/**
	 * The signal that will be called once {@link #operation} has been called.
	 */
	private final CountDownLatch doneSignal;

	/**
	 * The server operation.
	 */
	private final Operation operation;

	// Create the worker.
	private FakeWorker(Operation operation, FakeEmbeddedServer server, CountDownLatch startSignal, CountDownLatch doneSignal) {
		this.server = server;
		this.operation = operation;
		this.startSignal = startSignal;
		this.doneSignal = doneSignal;
	}

	@Override
	public void run() {
		try {
			startSignal.await();
			operation.apply(server);
			doneSignal.countDown();
		} catch (InterruptedException ex) {
			throw new AssertionError(ex);
		}
	}

	private interface Operation {
		void apply(FakeEmbeddedServer server);
	}
}
