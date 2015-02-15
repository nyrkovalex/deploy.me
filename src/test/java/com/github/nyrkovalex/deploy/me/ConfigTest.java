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

import com.github.nyrkovalex.seed.test.Seed;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class ConfigTest extends Seed.Test {

    private Config cfg;

    @Before
    public void setUp() {
        cfg = new Config("test");
    }

    @Test
    public void testShouldReturnWorkingDir() {
        assertThat(cfg.workingDir(), is("test/"));
    }

    @Test
    public void testShouldReturnDescriptorPath() {
        assertThat(cfg.descriptorPath(), is("test/" + Config.DD_FILENAME));
    }

    @Test
    public void testShouldUseEmptyRunSpecByDefault() {
        assertThat(cfg.runSpec(), is(instanceOf(EmptyRunSpec.class)));
    }

    @Test
    public void testShouldUseLimitedRunSpecWhenProvidedWIthSpec() {
        cfg = new Config("test", "some:spec");
        assertThat(cfg.runSpec(), is(instanceOf(LimitedRunSpec.class)));
    }

}
