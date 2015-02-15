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

import com.github.nyrkovalex.deploy.me.descriptor.Application;
import com.github.nyrkovalex.deploy.me.descriptor.Fileset;
import com.github.nyrkovalex.deploy.me.descriptor.Server;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ApplicationParserTest extends JsonTest {

    @Mock private ServerSetParser serverParser;
    @Mock private FilesetSetParser filesetParser;
    @InjectMocks private ApplicationParser parser;

    private Set<Fileset> filesets;
    private Set<Server> servers;
    
    @Before
    public void setUp() {
        filesets = ImmutableSet.of();
        servers = ImmutableSet.of();
    }
    
    @Test
    public void testShouldSetApplicationName() {
        Application parsed = parser.parse("foo", jsonElement());
        assertThat(parsed.name(), is("foo"));
    }
    
    @Test
    public void testShouldParseServers() {
        when(filesetParser.parse(jsonObject())).thenReturn(filesets);
        when(serverParser.parse(jsonObject(), filesets)).thenReturn(servers);
        Application apps = parser.parse("foo", jsonElement());
        assertThat(apps.servers(), is(servers));
    }
}
