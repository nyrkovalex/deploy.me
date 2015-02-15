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

public class Config {
    static final String DD_FILENAME = "deploy.json";

    private final String workingDir;
    private final RunSpec runSpec;

    Config(String workingDir, String runSpec) {
        this.workingDir = sanitize(workingDir);
        this.runSpec = RunSpec.forSpec(runSpec);
    }

    Config(String workingDir) {
        this.workingDir = sanitize(workingDir);
        this.runSpec = RunSpec.empty(); 
    }

    public String workingDir() {
        return workingDir;
    }
    
    public String descriptorPath() {
        return workingDir + DD_FILENAME;
    }
    
    public RunSpec runSpec() {
        return runSpec;
    }

    private static String sanitize(String workingDir) {
        return workingDir.endsWith("/") ? workingDir : workingDir + "/";
    }
}
