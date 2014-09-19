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
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.OutputStream;
import java.io.ObjectOutputStream;

/**
 * <p>A representation of arbitrary binary data included in a serial stream.
 * As well as objects written with {@link ObjectOutputStream#writeObject},
 * a serial stream can contain binary data written with any of the methods
 * that {@link ObjectOutputStream} inherits from {@link OutputStream} and
 * {@link DataOutput}.  The format of such data needs to be agreed on by
 * writer and reader.  Each chunk of such data in the stream is represented
 * by an instance of this class.</p>
 */
public class SBlockData extends SEntity {
    
    SBlockData(byte[] data) {
        super("blockdata");
        this.data = data;
    }
    
    /**
     * Get the binary data.
     */
    public byte[] getValue() {
        return data.clone();
    }
    
    /**
     * Get a DataInputStream that can read the binary data.
     */
    public DataInputStream getDataInputStream() {
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        return new DataInputStream(bin);
    }
    
    String kind() {
        return "SBlockData";
    }
    
    String contents() {
        return data.length + " byte" + (data.length == 1 ? "" : "s") +
                " of binary data";
    }
    
    private final byte[] data;
}
