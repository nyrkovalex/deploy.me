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

import com.github.nyrkovalex.deploy.me.descriptor.Script;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class SetParserTest extends JsonTest {

    @Mock private ScriptParser scriptParser;
    @Mock private Script script1;
    @Mock private Script script2;
    @InjectMocks private ScriptSetParser parser;

    @Test
    public void testShouldShouldParseScripts() {
        when(scriptParser.parse("foo", jsonElement())).thenReturn(script1);
        when(scriptParser.parse("bar", jsonElement())).thenReturn(script2);
        Set<Script> parsed = parser.parse(jsonObject());
        assertThat(parsed, is(ImmutableSet.of(script1, script2)));
    }
}
