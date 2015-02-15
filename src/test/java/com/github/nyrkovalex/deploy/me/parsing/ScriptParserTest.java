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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Map;
import org.junit.Test;

public class ScriptParserTest extends JsonTest {

    private static final String SIMPLE_JSON = "{\n"
            + "        \"config.json\": \"/home/user/deployment\"\n"
            + "    }";
    
    private static final String ARRAY_TARGETS_JSON = "{\n"
            + "        \"config.json\": [ \"/home/user/deployment\", \"/home/test\" ]\n"
            + "    }";
    
    private Script parse(String what) throws JsonSyntaxException {
        JsonElement parsed = readJson(what);
        JsonObject parsedObject = parsed.getAsJsonObject();
        Map.Entry<String, JsonElement> entry = parsedObject.entrySet().iterator().next();
        return new ScriptParser().parse(entry.getKey(), entry.getValue());
    }

    @Test
    public void testShouldReadScritSource() {
        Script script = parse(SIMPLE_JSON);
        assertThat(script.source(), is("config.json"));
    }
    
    @Test
    public void testShouldReadScriptTargetFromSimpleEntry() {
        Script script = parse(SIMPLE_JSON);
        assertThat(script.targets(), is(ImmutableSet.of("/home/user/deployment")));
    }
    
    @Test
    public void testShouldReadScriptWithTargetArray() {
        Script script = parse(ARRAY_TARGETS_JSON);
        assertThat(script.targets(), is(ImmutableSet.of(
                "/home/user/deployment", "/home/test"
        )));
    }
     

}