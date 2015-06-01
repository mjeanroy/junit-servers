/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 <mickael.jeanroy@gmail.com>
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

import java.util.HashMap;
import java.util.Map;

import com.github.mjeanroy.junit.servers.servers.configuration.AbstractConfiguration;

/**
 * Partial implementation of an embedded server.
 * Subclasses should implement {@link #doStart()} and {@link #doStop()} methods.
 * Synchronization is already managed by this abstract implementation.
 */
public abstract class AbstractEmbeddedServer<S extends Object, T extends AbstractConfiguration>
    implements EmbeddedServer<T> {

    /**
     * Server configuration.
     */
    protected final T configuration;

    /**
     * Flag to keep server status.
     * Server can be started if and only if status is equal to {@link ServerStatus#STOPPED}.
     * Server can be stopped if and only if status is equal to {@link ServerStatus#STARTED}.
     */
    private volatile ServerStatus status;

    /**
     * Old properties used to restore initial environment properties values when server stops.
     * It can be used to set a spring profile property or anything else.
     */
    private final Map<String, String> oldProperties;

    // Lock used to synchronize start and stop tasks
    private final Object lock = new Object();

    /**
     * Build default embedded server.
     *
     * @param configuration Server configuration.
     */
    protected AbstractEmbeddedServer(final T configuration) {

        this.status = ServerStatus.STOPPED;
        this.configuration = configuration;
        this.oldProperties = new HashMap<>();
    }

    @Override
    public void start() {

        if (this.status != ServerStatus.STARTED) {
            synchronized (this.lock) {
                if (this.status != ServerStatus.STARTED) {
                    this.status = ServerStatus.STARTING;
                    initEnvironment();
                    execHooks(true);
                    doStart();
                    this.status = ServerStatus.STARTED;

                    // Server is fully initialized
                    onStarted();
                }
            }
        }
    }

    @Override
    public void stop() {

        if (this.status != ServerStatus.STOPPED) {
            synchronized (this.lock) {
                if (this.status != ServerStatus.STOPPED) {
                    this.status = ServerStatus.STOPPING;
                    execHooks(false);
                    doStop();
                    destroyEnvironment();
                    this.status = ServerStatus.STOPPED;
                }
            }
        }
    }

    @Override
    public boolean isStarted() {

        return this.status == ServerStatus.STARTED;
    }

    @Override
    public void restart() {

        stop();
        start();
    }

    @Override
    public String getPath() {

        return this.configuration.getPath();
    }

    /**
     * Add custom environment properties.
     * Initial property value will be store in {@link #oldProperties} map
     * and will be restore later.
     */
    private void initEnvironment() {

        for (final Map.Entry<String, String> property : this.configuration.getEnvProperties().entrySet()) {
            final String name = property.getKey();
            final String newValue = property.getValue();

            final String oldValue = System.getProperty(property.getKey());
            this.oldProperties.put(name, oldValue);

            System.setProperty(name, newValue);
        }
    }

    /**
     * Reset custom environment properties.
     * Initial values stored in {@link #oldProperties} will be restored
     * or clear.
     */
    private void destroyEnvironment() {

        for (final Map.Entry<String, String> property : this.configuration.getEnvProperties().entrySet()) {
            final String name = property.getKey();

            final String oldValue = this.oldProperties.get(name);
            this.oldProperties.remove(name);

            if (oldValue == null) {
                System.clearProperty(name);
            } else {
                System.setProperty(name, oldValue);
            }
        }
    }

    /**
     * Exec hooks phase.
     *
     * @param pre Phase to execute (true => pre ; false => post).
     */
    private void execHooks(
        final boolean pre) {

        for (final Hook hook : this.configuration.getHooks()) {
            if (pre) {
                hook.pre(this);
            } else {
                hook.post(this);
            }
        }
    }

    private void onStarted() {

        for (final Hook hook : this.configuration.getHooks()) {
            hook.onStarted(this, getServletContext());
        }
    }

    @Override
    public String getUrl() {

        final int port = getPort();
        String path = getPath();
        if (!path.isEmpty() && path.charAt(0) != '/') {
            path = "/" + path;
        }

        return String.format("http://localhost:%s%s", port, path);
    }

    @Override
    public T getConfiguration() {

        return this.configuration;
    }

    /**
     * Get internal server implementation.
     * Note that this method should not be used to start
     * or stop internal server, use dedicated method instead.
     *
     * This method can be used to do some custom configuration
     * on original implementation.
     *
     * @return Original server implementation.
     */
    public abstract S getDelegate();

    /**
     * Start embedded server.
     * Must block until server is fully started.
     */
    protected abstract void doStart();

    /**
     * Stop embedded server.
     * Must block until server is fully stopped.
     */
    protected abstract void doStop();
}
