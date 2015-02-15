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
import com.github.nyrkovalex.deploy.me.parsing.Parser;
import com.github.nyrkovalex.deploy.me.scp.ScpTemplate;
import com.github.nyrkovalex.seed.core.Seed;
import com.github.nyrkovalex.seed.ssh.ScpCommand;
import com.github.nyrkovalex.seed.ssh.Ssh;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;

public class DeployMe {
    private static final Logger LOG = Logger.getLogger(DeployMe.class.getName());

    public static void main(String[] args) throws Exception {    
        initLogging(args);
        Config cfg = parseArgs(args);
        String str = Seed.Files.readToString(cfg.descriptorPath());
        DeploymentDescriptor dd = Parser.read(str);
        Set<ScpTemplate> templates = TemplateReader.fromDescriptor(dd, cfg.runSpec());
        
        for (ScpTemplate t : templates) {
            ScpCommand scpCommand = Ssh.scpTo(t.server());
            t.scripts().forEach(s -> {
                LOG.fine(() -> String.format(
                        "Sending %s to %s on %s", s.localPath(), s.remotePath(), t.server()));
                scpCommand.file(
                    Seed.Strings.join("/", cfg.workingDir(), s.localPath()),
                    s.remotePath());
            });
            scpCommand.asUser(t.user());
            scpCommand.run();
        }
    }

    private static Config parseArgs(String[] args) {
        Preconditions.checkArgument(args.length >= 1);
        String cwd = args[0];
        if (args.length == 1) {
            LOG.fine(() -> "Running with no runspec provided");
            return new Config(cwd);
        }
        LOG.fine(() -> "Running with runspec " + args[1]);
        return new Config(cwd, args[1]);
    }

    private static void initLogging(String[] args) {
        Seed.Logging.init(Arrays.asList(args).contains("-v"), DeployMe.class);
        LOG.fine(() -> "Running in verbose mode, debug output enabled");
    }
}
