/*
 * The MIT License
 *
 * Copyright 2015 Alexander Nyrkov.
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
package com.github.nyrkovalex.deploy.me;

import com.github.nyrkovalex.deploy.me.descriptor.Application;
import com.github.nyrkovalex.deploy.me.descriptor.Server;
import com.google.common.base.Preconditions;
import java.util.Optional;
import java.util.logging.Logger;

public interface RunSpec {
    
    boolean shouldRun(Application app);
    
    boolean shouldRun(Server server);
    
    static RunSpec forSpec(String spec) {
        return new LimitedRunSpec(spec);
    }
    
    static RunSpec empty() {
        return new EmptyRunSpec();
    }
}

class LimitedRunSpec implements RunSpec {
    
    private final static Logger LOG = Logger.getLogger(LimitedRunSpec.class.getName());
    
    private final String appSpec;
    private final Optional<String> serverSpec;
    
    public LimitedRunSpec(String specString) {
        String[] splitted = specString.split(":");
        Preconditions.checkArgument(splitted.length <= 2);
        this.appSpec = splitted[0];
        this.serverSpec = splitted.length == 2 ? Optional.of(splitted[1]) : Optional.empty();
    }
    
    @Override
    public boolean shouldRun(Application app) {
        boolean shouldRun = app.name().equals(appSpec);
        logIfSkipped(shouldRun, "application", app.name());
        return shouldRun;
    }
    
    @Override
    public boolean shouldRun(Server server) {
        boolean shouldRun = serverSpec.isPresent()
                ? server.name().equals(serverSpec.get())
                : true;
        logIfSkipped(shouldRun, "server", server.name());
        return shouldRun;
    }
    
    private void logIfSkipped(boolean shouldRun, String kind, String name) {
        if (!shouldRun) {
            LOG.fine(() -> String.format("%s %s didn't match runspec, skipping", kind, name));
        }
    }
}

class EmptyRunSpec implements RunSpec {
    
    @Override
    public boolean shouldRun(Application app) {
        return true;
    }
    
    @Override
    public boolean shouldRun(Server server) {
        return true;
    }
    
}
