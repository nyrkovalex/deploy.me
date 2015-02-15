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

import com.github.nyrkovalex.deploy.me.descriptor.DeploymentDescriptor;
import com.github.nyrkovalex.deploy.me.scp.ScpFile;
import com.github.nyrkovalex.deploy.me.scp.ScpTemplate;
import com.github.nyrkovalex.seed.core.Seed;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

public final class TemplateReader {

    private TemplateReader() {
    }

    static Set<ScpTemplate> fromDescriptor(DeploymentDescriptor dd, RunSpec runSpec) {
        return dd.applications().stream()
                .filter(runSpec::shouldRun)
                .flatMap(app -> app.servers().stream()
                        .filter(runSpec::shouldRun)
                        .map(server -> new ScpTemplate(server.address(), server.user(), server.filesets().stream()
                                        .flatMap(fileset -> fileset.scripts().stream()
                                                .flatMap(script -> script.targets().stream()
                                                        .map(target -> new ScpFile(
                                                                        Seed.Strings.join(
                                                                                "/", app.name(), server.name(), script.source()),
                                                                        Seed.Strings.join("/", target, script.source())))))
                                        .collect(toSet())))
                ).collect(toSet());
    }
}
