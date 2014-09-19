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
import java.io.InputStream;

import static net.mcmanus.eamonn.serialysis.Trace.enter;
import static net.mcmanus.eamonn.serialysis.Trace.exit;
import static net.mcmanus.eamonn.serialysis.Trace.show;

class TraceInputStream extends InputStream {
    TraceInputStream(InputStream in) {
        this.in = in;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int n = in.read(b, off, len);
        if (n < 0)
            show("is.read", n);
        else
            show("is.read", b, off, n);
        return n;
    }

    public int read(byte[] b) throws IOException {
        int n = in.read(b);
        if (n < 0)
            show("is.read", n);
        else
            show("is.read", b, 0, n);
        return n;
    }

    public long skip(long n) throws IOException {
        long ret = in.skip(n);
        show("is.skip(" + n + ") -> " + ret);
        return ret;
    }

    public int read() throws IOException {
        int n = in.read();
        if (n < 0)
            show("is.read", n);
        else
            show("is.read", new byte[] {(byte) n});
        return n;
    }

    private final InputStream in;
}
