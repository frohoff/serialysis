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
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteObject;
import java.util.List;
import junit.framework.*;
import net.mcmanus.eamonn.serialysis.SBlockData;
import net.mcmanus.eamonn.serialysis.SObject;
import net.mcmanus.eamonn.serialysis.SEntity;
import net.mcmanus.eamonn.serialysis.SerialScan;

public class AnonymousRegistryTest extends TestCase {

    public AnonymousRegistryTest(String testName) {
        super(testName);
    }

    /*
     * Test that we can determine what port an RMI Registry is running on
     * when it was created using an anonymous port number (port 0).
     * The serialized form of an RMI registry stub is described at
     * http://java.sun.com/javase/6/docs/api/serialized-form.html#java.rmi.server.RemoteObject
     * We don't have any socket factories, so we expect it to use the
     * documented UnicastRef form.
     */
    public void testAnonymousRegistry() throws Exception {
        // Make the registry
        Registry reg = LocateRegistry.createRegistry(0);
        
        // Convert it to a stub
        Remote stub = RemoteObject.toStub(reg);

        // Analyze the serial form of the stub
        SObject sstub = (SObject) SerialScan.examine(stub); // (SObject) ss.readObject();
        System.out.println(sstub);

        // The interesting data is in the "annotations", i.e. the data
        // written by the writeObject method as documented.
        List<SEntity> annots = sstub.getAnnotations();
        assertEquals(1, annots.size());
        SBlockData sdata = (SBlockData) annots.get(0);
        DataInputStream din = sdata.getDataInputStream();

        // Read the UnicastRef encoding

        // Type string
        String unicastref = din.readUTF();
        assertEquals("UnicastRef", unicastref);

        // Host address
        String host = din.readUTF();
        // Test that this is indeed a local address by creating a ServerSocket.
        // That will fail if this is not a valid address or if it is remote.
        InetAddress addr = InetAddress.getByName(host);
        ServerSocket socket = new ServerSocket(0, 0, addr);
        socket.close();

        // Port (this is what we would be after if we were using this to
        // discover the port number assigned by the kernel).
        int port = din.readInt();
        assertTrue(port != 0);
        System.out.println("The port is " + port);

        // ObjID, should be the registry id.
        ObjectInput idin = new DataObjectInput(din);
        ObjID objID = ObjID.read(idin);
        assertEquals(new ObjID(ObjID.REGISTRY_ID), objID);

        // Boolean value false
        boolean b = din.readBoolean();
        assertFalse(b);

        // Check we've exhausted the data
        assertEquals(-1, din.read());
    }

    /* Annoyingly, ObjID.read takes an ObjectInput parameter even though
     * it never calls ObjectInput.readObject().  It could just as easily
     * declare the parent interface DataInput, and we wouldn't be obliged
     * to have the hack below.
     */
    private static class DataObjectInput extends DataInputStream
            implements ObjectInput {
        DataObjectInput(InputStream in) {
            super(in);
        }

        public Object readObject() {
            throw new UnsupportedOperationException();
        }
    }
}
