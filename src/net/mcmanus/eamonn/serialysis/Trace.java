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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Stack;
import javax.management.Query;
import javax.management.QueryExp;

class Trace {
    static final boolean TRACE = false;

    static void enter(String what, Object...args) {
        if (!TRACE) return;
        indent();
        StringBuilder sb = new StringBuilder(what).append("(");
        String sep = "";
        for (Object arg : args) {
            sb.append(sep).append(toString(arg));
            sep = ", ";
        }
        sb.append(")");
        System.out.println(sb);
        stack.push(what);
    }

    static void show(String what) {
        if (!TRACE) return;
        indent();
        System.out.println(what);
    }

    static void show(String what, Object x) {
        if (!TRACE) return;
        if (x instanceof byte[])
            show(what, (byte[]) x, 0, ((byte[]) x).length);
        else {
            indent();
            System.out.println(what + ": " + toString(x));
        }
    }

    static void show(String what, byte[] bytes, int off, int len) {
        if (!TRACE) return;
        indent();
        System.out.print(what + ": ");
        for (int i = 0; i < len; i++)
            System.out.printf(" %02x", bytes[off + i] & 255);
        System.out.println();
    }

    static void exit() {
        exit("");
    }

    static void exit(Object ret) {
        if (!TRACE) return;
        String what = stack.pop();
        indent();
        System.out.println(what + " returns " + toString(ret));
    }

    private static String toString(Object x) {
        if (x == null || !x.getClass().isArray())
            return String.valueOf(x);
        String s = Arrays.deepToString(new Object[] {x});
        return s.substring(1, s.length() - 1);
    }

    private static void indent() {
        for (int i = 0; i < stack.size(); i++)
            System.out.print("| ");
    }

    private static Stack<String> stack = new Stack<String>();
}
