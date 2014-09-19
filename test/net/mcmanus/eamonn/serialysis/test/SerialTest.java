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

package net.mcmanus.eamonn.serialysis.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Collections;
import junit.framework.*;
import net.mcmanus.eamonn.serialysis.SArray;
import net.mcmanus.eamonn.serialysis.SObject;
import net.mcmanus.eamonn.serialysis.SEntity;
import net.mcmanus.eamonn.serialysis.SPrim;
import net.mcmanus.eamonn.serialysis.SString;
import net.mcmanus.eamonn.serialysis.SerialScan;

public class SerialTest extends TestCase {

    public SerialTest(String testName) {
        super(testName);
    }

    public void testInteger() throws Exception {
        SObject so = (SObject) scan(Integer.valueOf(5));
        assertEquals(Integer.class.getName(), so.getType());
        assertEquals(Collections.singleton("value"), so.getFieldNames());
        SPrim value = (SPrim) so.getField("value");
        Integer x = (Integer) value.getValue();
        assertEquals(5, x.intValue());
    }

    public void testString() throws Exception {
        SString so = (SString) scan("noddy");
        assertEquals("noddy", so.getValue());
    }

    public void testLongString() throws Exception {
        StringBuilder sb = new StringBuilder("x");
        for (int i = 0; i < 20; i++)
            sb.append(sb);
        String s = sb.toString();
        assertTrue(s.length() > 1000000);
        SString ss = (SString) scan(s);
        assertEquals(s, ss.getValue());
    }

    public void testIntegerArray() throws Exception {
        SArray so = (SArray) scan(new Integer[] {1, 2, 3});
        SEntity[] a = so.getValue();
        assertEquals(3, a.length);
        for (int i = 0; i < a.length; i++) {
            SObject x = (SObject) a[i];
            SPrim p = (SPrim) x.getField("value");
            assertEquals(i + 1, p.getValue());
        }
    }

    public void testIntArray() throws Exception {
        SArray so = (SArray) scan(new int[] {1, 2, 3});
        SEntity[] a = so.getValue();
        assertEquals(3, a.length);
        for (int i = 0; i < a.length; i++) {
            SPrim p = (SPrim) a[i];
            assertEquals(i + 1, p.getValue());
        }
    }

    public void testStringArray() throws Exception {
        String[] strings = {"seacht", "ocht", "naoi"};
        SArray so = (SArray) scan(strings);
        SEntity[] a = so.getValue();
        assertEquals(strings.length, a.length);
        for (int i = 0; i < a.length; i++) {
            SString s = (SString) a[i];
            assertEquals(strings[i], s.getValue());
        }
    }

    public void testEnum() throws Exception {
        Enum en = ElementType.LOCAL_VARIABLE;  // random enum from Java SE
        SObject so = (SObject) scan(en);
        assertEquals(en.getClass().getName(), so.getType());
        SString name = (SString) so.getField("<name>");
        assertEquals(en.name(), name.getValue());
    }

    public void testSelfRef() throws Exception {
        Holder x = new Holder();
        x.held = x;
        SObject so = (SObject) scan(x);
        assertSame(so.getField("held"), so);
        // Make sure toString() doesn't get into infinite recursion
        System.out.println(so);
    }

    public void testObjectArrayContainingObjectArray() throws Exception {
        Object[] objects = {new Object[0]};
        SArray so = (SArray) scan(objects);
        SEntity[] a = so.getValue();
        assertEquals(objects.length, a.length);
        SArray aa = (SArray) a[0];
        assertEquals(0, aa.getValue().length);
    }

    public void testObjectArrayContainingSelf() throws Exception {
        Object[] objects = new Object[1];
        objects[0] = objects;
        SArray so = (SArray) scan(objects);
        SEntity[] a = so.getValue();
        assertEquals(objects.length, a.length);
        assertSame(so, a[0]);
        // Make sure toString() doesn't get into infinite recursion
        System.out.println(so);
    }

    public void testMultipleObjects() throws Exception {
        Object[] objects = {5, new int[] {5}, new Integer[] {5}, "noddy"};
        SEntity[] sos = scanMultiple(objects);
        SString noddy = (SString) sos[sos.length - 1];
        assertEquals("noddy", noddy.getValue());
    }

    private static class Holder implements Serializable {
        private static final long serialVersionUID = 6922605819566649377L;

        Object held;
    }

    private SEntity scan(Object x) throws IOException {
        return scanMultiple(new Object[] {x})[0];
    }

    private SEntity[] scanMultiple(Object[] xs) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(bout);
        for (Object x : xs)
            oout.writeObject(x);
        oout.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(bout.toByteArray());
        SerialScan ss = new SerialScan(bis);
        SEntity[] sos = new SEntity[xs.length];
        for (int i = 0; i < xs.length; i++)
            sos[i] = ss.readObject();
        assertTrue(bis.read() == -1);  // should have exhausted input stream
        return sos;
    }
}
