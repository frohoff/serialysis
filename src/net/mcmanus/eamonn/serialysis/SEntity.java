/*
 *                ,
 * Copyright 2007 Eamonn McManus.
 *
 *  This file is part of the Serialysis library.
 *
 *  Serialysis is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Serialysis is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Serialysis; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston,
 *  MA 02110-1301  USA.
 *
 *  Linking this library statically or dynamically with other modules is
 *  making a combined work based on this library.  Thus, the terms and
 *  conditions of the GNU General Public License cover the whole
 *  combination.
 *
 *  As a special exception, the copyright holder of this library gives you
 *  permission to link this library with independent modules to produce an
 *  executable, regardless of the license terms of these independent
 *  modules, and to copy and distribute the resulting executable under
 *  terms of your choice, provided that you also meet, for each linked
 *  independent module, the terms and conditions of the license of that
 *  module.  An independent module is a module which is not derived from
 *  or based on this library.  If you modify this library, you may extend
 *  this exception to your version of the library, but you are not
 *  obligated to do so.  If you do not wish to do so, delete this
 *  exception statement from your version.
 */

package net.mcmanus.eamonn.serialysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * A representation of a serialized object.  Scanning a serial stream
 * produces a sequence of SEntity instances.  The subclass of each depends on the
 * type of the real Java object that would be produced when deserializing
 * the input stream.  Essentially, SEntity and its subclasses define a
 * parallel type hierarchy that represents the original Java type hierarchy.
 * An instance of an SEntity subclass represents an instance of a Java
 * object or primitive.
 */
public abstract class SEntity {

    SEntity(String type) {
        this.type = type;
    }

    abstract String kind();
    abstract String contents();

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(kind()).append("(").append(type).append("){");
        Stack<SEntity> stack = stringThings.get();
        if (stack.contains(this)) {
            sb.append("...");
        } else {
            stack.push(this);
            sb.append(contents());
            stack.pop();
        }
        if (sb.charAt(sb.length() - 1) == '\n')
            indent(sb);
        sb.append("}");
        return sb.toString();
    }

    String getType() {
        return type;
    }

    static void indent(StringBuilder sb) {
        Stack<SEntity> stack = stringThings.get();
        for (int i = stack.size(); i > 0; i--)
            sb.append("  ");
    }

    private final String type;
    private static final ThreadLocal<Stack<SEntity>> stringThings =
            new ThreadLocal<Stack<SEntity>>() {
                protected Stack<SEntity> initialValue() {
                    return new Stack<SEntity>();
                }
            };
}
