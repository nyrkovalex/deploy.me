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
package com.github.nyrkovalex.deploy.me.parsing;

import com.github.nyrkovalex.deploy.me.descriptor.Fileset;
import com.github.nyrkovalex.deploy.me.descriptor.Server;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ServerParserTest extends JsonTest {

    private static final String SERVER_JSON = "{\n"
            + "    \"address\": \"192.168.1.42\",\n"
            + "    \"user\": \"dude\","
            + "    \"filesets\": [ \"development\" ]\n"
            + "}";

    @Mock private Fileset fileset;
    private Server parsed;

    @Before
    public void setUp() {
        ServerParser parser = new ServerParser();
        parsed = parser.parse(
                "test",
                ImmutableMap.of("development", fileset),
                readJson(SERVER_JSON));
    }

    @Test
    public void testShouldUseNameProvided() {
        assertThat(parsed.name(), is("test"));
    }

    @Test
    public void testShouldParseAddress() {
        assertThat(parsed.address(), is("192.168.1.42"));
    }

    @Test
    public void testShouldExtractFilesets() {
        assertThat(parsed.filesets(), is(ImmutableSet.of(fileset)));
    }

    @Test
    public void testShouldParseUser() {
        assertThat(parsed.user(), is("dude"));
    }
}
