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
import org.junit.Test;
import com.github.nyrkovalex.seed.test.Seed;
import org.mockito.Mock;

public class RunSpecTest extends Seed.Test {
    
    @Mock Application app;
    @Mock Server server;

    @Test
    public void testShouldAlwaysReturnTrueForEmptySpec() {
        RunSpec spec = RunSpec.empty();
        assertThat(spec.shouldRun(app));
        assertThat(spec.shouldRun(server));
    }
    
    @Test
    public void testShouldRejectApplication() {
        RunSpec spec = RunSpec.forSpec("foo");
        when(app.name()).thenReturn("bar");
        assertThat(!spec.shouldRun(app));
    }
    
    @Test
    public void testShouldAllowApplication() {
        RunSpec spec = RunSpec.forSpec("foo");
        when(app.name()).thenReturn("foo");
        assertThat(spec.shouldRun(app));
    }
    
    @Test
    public void testShouldRejectServer() {
        RunSpec spec = RunSpec.forSpec("foo:bar");
        when(app.name()).thenReturn("foo");
        when(server.name()).thenReturn("fizz");
        assertThat(!spec.shouldRun(server));
    }
    
    @Test
    public void testShouldAllowServer() {
        RunSpec spec = RunSpec.forSpec("foo:bar");
        when(app.name()).thenReturn("foo");
        when(server.name()).thenReturn("bar");
        assertThat(spec.shouldRun(server));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShouldThrowOnTooManySpecParts() {
        RunSpec.forSpec("i:am:long");
    }
}