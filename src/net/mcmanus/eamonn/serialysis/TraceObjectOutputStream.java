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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

import static net.mcmanus.eamonn.serialysis.Trace.enter;
import static net.mcmanus.eamonn.serialysis.Trace.exit;

class TraceObjectOutputStream extends ObjectOutputStream {
    public TraceObjectOutputStream(OutputStream os) throws IOException {
        super(os);
    }

    protected Object replaceObject(Object obj) throws IOException {
        enter("oos.replaceObject", obj);
        Object retValue;

        retValue = super.replaceObject(obj);
        exit(retValue);
        return retValue;
    }

    public boolean equals(Object obj) {
        enter("oos.equals", obj);
        boolean retValue;

        retValue = super.equals(obj);
        exit(retValue);
        return retValue;
    }

    protected void writeClassDescriptor(java.io.ObjectStreamClass desc) throws IOException {
        enter("oos.writeClassDescriptor", desc);
        super.writeClassDescriptor(desc);
        exit();
    }

    protected boolean enableReplaceObject(boolean enable) throws SecurityException {
        enter("oos.enableReplaceObject", enable);
        boolean retValue;

        retValue = super.enableReplaceObject(enable);
        exit(retValue);
        return retValue;
    }

    public void writeBoolean(boolean val) throws IOException {
        enter("oos.writeBoolean", val);
        super.writeBoolean(val);
        exit();
    }

    public void writeDouble(double val) throws IOException {
        enter("oos.writeDouble", val);
        super.writeDouble(val);
        exit();
    }

    public void writeFloat(float val) throws IOException {
        enter("oos.writeFloat", val);
        super.writeFloat(val);
        exit();
    }

    public void writeUTF(String str) throws IOException {
        enter("oos.writeUTF", str);
        super.writeUTF(str);
        exit();
    }

    public void writeBytes(String str) throws IOException {
        enter("oos.writeBytes", str);
        super.writeBytes(str);
        exit();
    }

    public void writeChars(String str) throws IOException {
        enter("oos.writeChars", str);
        super.writeChars(str);
        exit();
    }

    public void write(byte[] buf, int off, int len) throws IOException {
        enter("oos.write", Arrays.toString(buf), off, len);
        super.write(buf, off, len);
        exit();
    }

    public void write(byte[] buf) throws IOException {
        enter("oos.write", Arrays.toString(buf));
        super.write(buf);
        exit();
    }

    public void writeShort(int val) throws IOException {
        enter("oos.writeShort", val);
        super.writeShort(val);
        exit();
    }

    public void writeInt(int val) throws IOException {
        enter("oos.writeInt", val);
        super.writeInt(val);
        exit();
    }

    public void useProtocolVersion(int version) throws IOException {
        enter("oos.useProtocolVersion", version);
        super.useProtocolVersion(version);
        exit();
    }

    public void write(int val) throws IOException {
        enter("oos.write", val);
        super.write(val);
        exit();
    }

    public void writeByte(int val) throws IOException {
        enter("oos.writeByte", val);
        super.writeByte(val);
        exit();
    }

    public void writeChar(int val) throws IOException {
        enter("oos.writeChar", val);
        super.writeChar(val);
        exit();
    }

    public void writeLong(long val) throws IOException {
        enter("oos.writeLong", val);
        super.writeLong(val);
        exit();
    }

    protected void writeStreamHeader() throws IOException {
        enter("oos.writeStreamHeader");
        super.writeStreamHeader();
        exit();
    }

    public void writeFields() throws IOException {
        enter("oos.writeFields");
        super.writeFields();
        exit();
    }

    public java.io.ObjectOutputStream.PutField putFields() throws IOException {
        enter("oos.putFields");
        java.io.ObjectOutputStream.PutField retValue;

        retValue = super.putFields();
        exit(retValue);
        return retValue;
    }

    public int hashCode() {
        enter("oos.hashCode");
        int retValue;

        retValue = super.hashCode();
        exit(retValue);
        return retValue;
    }

    public void flush() throws IOException {
        enter("oos.flush");
        super.flush();
        exit();
    }

    protected void drain() throws IOException {
        enter("oos.drain");
        super.drain();
        exit();
    }

    public void defaultWriteObject() throws IOException {
        enter("oos.defaultWriteObject");
        super.defaultWriteObject();
        exit();
    }

    public void close() throws IOException {
        enter("oos.close");
        super.close();
        exit();
    }

    public void reset() throws IOException {
        enter("oos.reset");
        super.reset();
        exit();
    }

    public String toString() {
        enter("oos.toString");
        String retValue;

        retValue = super.toString();
        exit(retValue);
        return retValue;
    }

    public void writeUnshared(Object obj) throws IOException {
        enter("oos.writeUnshared", obj);
        super.writeUnshared(obj);
        exit();
    }

    protected final void writeObjectOverride(Object obj) throws IOException {
        enter("oos.writeObjectOverride", obj);
        super.writeObjectOverride(obj);
        exit();
    }

}
