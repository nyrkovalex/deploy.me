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
import com.github.nyrkovalex.deploy.me.descriptor.DeploymentDescriptor;
import com.github.nyrkovalex.deploy.me.descriptor.Fileset;
import com.github.nyrkovalex.deploy.me.descriptor.Named;
import com.github.nyrkovalex.deploy.me.descriptor.Script;
import com.github.nyrkovalex.deploy.me.descriptor.Server;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

public class Parser {

    private final DeploymentDescriptorParser deploymentDescriptorParser;

    private Parser() {
        ScriptParser scriptParser = new ScriptParser();
        ScriptSetParser scriptSetParser = new ScriptSetParser(scriptParser);
        FilesetParser filesetParser = new FilesetParser(scriptSetParser);
        ServerParser serverParser = new ServerParser();
        ServerSetParser serverSetParser = new ServerSetParser(serverParser);
        FilesetSetParser filesetSetParser = new FilesetSetParser(filesetParser);
        ApplicationParser applicationParser = new ApplicationParser(serverSetParser, filesetSetParser);
        ApplicationSetParser applicationSetParser = new ApplicationSetParser(applicationParser);
        this.deploymentDescriptorParser = new DeploymentDescriptorParser(applicationSetParser);
    }

    private DeploymentDescriptor parse(String source) {
        return deploymentDescriptorParser.parse(new JsonParser().parse(source));
    }
    
    
    public static DeploymentDescriptor read(String source) {
        return new Parser().parse(source);
    }
}

class DeploymentDescriptorParser {

    private final ApplicationSetParser applicationParser;

    DeploymentDescriptorParser(ApplicationSetParser applicationParser) {
        this.applicationParser = applicationParser;
    }

    DeploymentDescriptor parse(JsonElement element) {
        JsonObject jsonObject = element.getAsJsonObject();
        Set<Application> applications = applicationParser.parse(jsonObject);
        return new DeploymentDescriptor(applications);
    }
}

class ApplicationSetParser extends SetParser<Application> {
    public ApplicationSetParser(ApplicationParser applicationParser) {
        super(applicationParser);
    }
}

class ApplicationParser implements BasicParser<Application> {

    private final ServerSetParser serverSetParser;
    private final FilesetSetParser filesetSetParser;

    public ApplicationParser(ServerSetParser serverParser, FilesetSetParser filesetSetParser) {
        this.serverSetParser = serverParser;
        this.filesetSetParser = filesetSetParser;
    }

    @Override
    public Application parse(String name, JsonElement element) {
        JsonObject jsonObject = element.getAsJsonObject();
        Set<Fileset> filesets = filesetSetParser.parse(jsonObject.getAsJsonObject("filesets"));
        Set<Server> servers = serverSetParser.parse(jsonObject.getAsJsonObject("servers"), filesets);
        return new Application(name, servers);
    }

}

class ServerParser {

    Server parse(String name, Map<String, Fileset> filesetIndex, JsonElement element) {
        JsonObject jsonObject = element.getAsJsonObject();
        JsonArray filesetsArray = jsonObject.get("filesets").getAsJsonArray();
        Set<Fileset> filesets = FluentIterable.from(filesetsArray)
                .transform(JsonElement::getAsString)
                .transform(filesetIndex::get)
                .toSet();
        return new Server(
                name,
                jsonObject.get("user").getAsString(),
                jsonObject.get("address").getAsString(),
                filesets);
    }
}

class ServerSetParser {

    private final ServerParser serverParser;

    ServerSetParser(ServerParser serverParser) {
        this.serverParser = serverParser;
    }

    Set<Server> parse(JsonObject jsonObject, Iterable<Fileset> filesets) {
        Map<String, Fileset> filesetIndex = Maps.uniqueIndex(filesets, Named::name);
        return jsonObject.entrySet().stream()
                .map(entry -> serverParser.parse(entry.getKey(), filesetIndex, entry.getValue()))
                .collect(toSet());
    }
}

class FilesetSetParser extends SetParser<Fileset> {
    FilesetSetParser(FilesetParser filesetParser) {
        super(filesetParser);
    }
}

class FilesetParser implements BasicParser<Fileset> {

    private final ScriptSetParser scriptParser;

    FilesetParser(ScriptSetParser scriptParser) {
        this.scriptParser = scriptParser;
    }

    @Override
    public Fileset parse(String name, JsonElement element) {
        JsonObject jsonObject = element.getAsJsonObject();
        Set<Script> scripts = scriptParser.parse(jsonObject);
        return new Fileset(name, scripts);
    }
}

class ScriptSetParser extends SetParser<Script> {
    public ScriptSetParser(ScriptParser scriptParser) {
        super(scriptParser);
    }
}

class ScriptParser implements BasicParser<Script> {
    @Override
    public Script parse(String path, JsonElement locations) {
        Iterable<String> targets = locations.isJsonArray()
                ? FluentIterable.from(locations.getAsJsonArray())
                .transform(item -> item.getAsString())
                .toSet()
                : ImmutableSet.of(locations.getAsString());
        return new Script(path, targets);
    }
}

abstract class SetParser<T> {

    private final BasicParser<T> basicParser;

    public SetParser(BasicParser<T> basicParser) {
        this.basicParser = basicParser;
    }

    public Set<T> parse(JsonObject jsonObject) {
        return jsonObject.entrySet().stream()
                .map(entry -> basicParser.parse(entry.getKey(), entry.getValue()))
                .collect(toSet());
    }
}

interface BasicParser<T> {
    T parse(String name, JsonElement content);
}
