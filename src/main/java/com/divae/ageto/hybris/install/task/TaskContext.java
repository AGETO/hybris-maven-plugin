package com.divae.ageto.hybris.install.task;

import com.divae.ageto.hybris.version.HybrisVersion;
import com.google.common.collect.Maps;

import java.io.File;
import java.util.Map;

/**
 * @author Klaus Hauschild
 */
public class TaskContext {

    private final HybrisVersion hybrisVersion;
    private final Map<String, Object> parameters;
    private final File                hybrisDirectory;

    private Throwable                 lastError;

    public TaskContext(final HybrisVersion hybrisVersion, File hybrisDirectory) {
        this.hybrisVersion = hybrisVersion;
        this.hybrisDirectory = hybrisDirectory;
        parameters = Maps.newHashMap();
    }

    public HybrisVersion getHybrisVersion() {
        return hybrisVersion;
    }

    public File getHybrisDirectory() {
        return hybrisDirectory;
    }

    public Object getParameter(final String key) {
        return parameters.get(key);
    }

    public void setParameter(final String key, final Object value) {
        parameters.put(key, value);
    }

    public Throwable getLastError() {
        return lastError;
    }

    public void setLastError(Throwable lastError) {
        this.lastError = lastError;
    }

}
