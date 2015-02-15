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
package com.github.nyrkovalex.deploy.me.descriptor;

import java.util.Objects;
import java.util.Set;

public class Server extends Named {
    private final String address;
    private final Set<Fileset> filesets;
    private final String user;

    public Server(String name, String user, String address, Set<Fileset> filesets) {
        super(name);
        this.user = user;
        this.address = address;
        this.filesets = filesets;
    }

    public Set<Fileset> filesets() {
        return filesets;
    }   
    
    public String address() {
        return address;
    }
    
    public Set<Script> scripts() {
        return null;
    }

    public String user() {
        return user;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.address(), this.filesets(), this.name());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Server other = (Server) obj;
        return Objects.equals(this.address(), other.address()) 
                && Objects.equals(this.filesets(), other.filesets())
                && Objects.equals(this.name(), other.name());
    }    
}
