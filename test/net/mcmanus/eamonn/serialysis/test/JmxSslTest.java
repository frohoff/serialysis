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

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnection;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.management.remote.rmi.RMIJRMPServerImpl;
import javax.management.remote.rmi.RMIServer;
import javax.management.remote.rmi.RMIServerImpl;
import javax.management.remote.rmi.RMIServerImpl_Stub;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import javax.security.auth.Subject;
import junit.framework.*;
import net.mcmanus.eamonn.serialysis.SBlockData;
import net.mcmanus.eamonn.serialysis.SObject;
import net.mcmanus.eamonn.serialysis.SEntity;
import net.mcmanus.eamonn.serialysis.SerialScan;

/**
 *
 * @author emcmanus
 */
public class JmxSslTest extends TestCase {
    // This is the Duke example certificate from
    // http://java.sun.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html
    // with a validity of 1000000 days from when I generated it on 8 June 2007.

    // This is a keystore containing the Duke certificate
    // http://java.sun.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html
    // with a validity of 1000000 days from when I generated it on 8 June 2007.
    private static String keyStoreHex =
        "feedfeed000000020000000100000001000464756b65000001130bfab3360000" +
        "02bb308202b7300e060a2b060104012a021101010500048202a3c7c040c49527" +
        "6b597e984075e475cf0b821d61dee82a44a5b2b3a59183cb127cb2bb622bc0c8" +
        "4a259354261165f09c099d0bc76b3f5a48a3b0376e9a686986907eb49d90d8fe" +
        "0fa4372276e5d66a49227986f3a7fd37eebefe7be9ef985aaa40ab7e0a4804c5" +
        "36aa0a62a248e792ea15f74a6f15f17b3a56d80c1db53aa73da0336a6116e2f3" +
        "de6b5f1683620477974f4f3406b967e41e8decf97092980a6f36990a074b65f0" +
        "75428eff37a102ecbda3295238ccfa793441633ac71066bae972b41e61bd4cef" +
        "e9e8d59ca32ba1e6c8291b1cb0af0d81eca437880ac38fcf83d002c5136143fa" +
        "97a3ba6841d1446233646ec61f9c73dc3d4d5a5e729b213204f1274448e04607" +
        "2c3d629f6cdfb50e2ba7a9397d91fb413a3da97b9ecef2216fa3c52a2d6dc0ac" +
        "501b343630b7d50f4e3ff0b283759d1e32bbf9057922901c42ee020165e3e3c7" +
        "5e3a7c203cb9b2d53d098a07f3695214342387c03e20d8ca0bf29e60a052e563" +
        "e45206eb0d969e8984fbb955db9bae39974323d8a8af64042a200b2ddfaf4bb5" +
        "d897c78680b6371e815f6d379cd3b832ef6749e10056e689c9f06d333f615cc2" +
        "0ee0e86147608569aefc867dfce6dad0b8422c7f03b74db46e8704caf66524d8" +
        "2a182f0bbb8c68f6aa89dc4b1769b150d30d05561a9d9d9d31300cdf192206dc" +
        "98869cfe2dfa241c122f7d4017bd2ad0184bd32acbd09e1de1a258e6a02de984" +
        "55caeafc5e66e729f6ead26ff6838614eeaa0f0e690b203760ea8808041cd9b1" +
        "d43e895bc50af64a642a0c8699231fded4611fc39aa99aa9277da52250995c79" +
        "73a96febebeb8ec5f1d163f7c0ad6aee33d39aa00b0fbc5ed2f2e665a0c82503" +
        "19f8443f76a8c6404df25cc0035e87640d53f1cff2557b94b41b18dcbfc02661" +
        "648fc2b5b699593653bc2092c41f1762ec406c3916507d4baa42f068c0000000" +
        "010005582e3530390000026930820265308201cea00302010202044669777830" +
        "0d06092a864886f70d01010505003076310b3009060355040613025553310b30" +
        "09060355040813024341311230100603550407130950616c6f20416c746f311f" +
        "301d060355040a131653756e204d6963726f73797374656d732c20496e632e31" +
        "163014060355040b130d4a61766120536f667477617265310d300b0603550403" +
        "130444756b653020170d3037303630383135333632345a180f34373435303530" +
        "353135333632345a3076310b3009060355040613025553310b30090603550408" +
        "13024341311230100603550407130950616c6f20416c746f311f301d06035504" +
        "0a131653756e204d6963726f73797374656d732c20496e632e31163014060355" +
        "040b130d4a61766120536f667477617265310d300b0603550403130444756b65" +
        "30819f300d06092a864886f70d010101050003818d0030818902818100eff982" +
        "8aab6e728535df5b9c74ba6e7e0daf7a9489775bcf098cad008d57acc342ca26" +
        "847295973678f8eaf31cf4943d1ff1d98deca449024f1a890b4871952ad6ce4f" +
        "92b0b9e135337a5e1246a65bbf1afa9adf6fedcaca0e07fbda6e385cb8dfe6a9" +
        "9afb112ea9ad0f7f187f90b3bb7734781700ef555f0c44ed7ce2d9459d020301" +
        "0001300d06092a864886f70d0101050500038181004b4569fb7104b731b02267" +
        "2b403438971e30893e8e7a530c6d5e5b5f95e4a4be97d07f4ff0536e74ad381b" +
        "04c81b348eaaed44722247c8d4448bff206ab3fd85832044275e83f1260737ef" +
        "e7b020f8700018f54a56617808ea30c8597efb593c1a5f38b1961ea173fc8afc" +
        "4ea9fb643a2c9d5c6799d0239c534852fec5564c5763f769d70048df3dc7e810" +
        "438fb9c1e228e83d1a";

    // This is the corresponding truststore encoded in hex.
    private static final String trustStoreHex =
        "feedfeed000000020000000100000002000864756b6563657274000001130c1e" +
        "b54b0005582e3530390000026930820265308201cea003020102020446697778" +
        "300d06092a864886f70d01010505003076310b3009060355040613025553310b" +
        "3009060355040813024341311230100603550407130950616c6f20416c746f31" +
        "1f301d060355040a131653756e204d6963726f73797374656d732c20496e632e" +
        "31163014060355040b130d4a61766120536f667477617265310d300b06035504" +
        "03130444756b653020170d3037303630383135333632345a180f343734353035" +
        "30353135333632345a3076310b3009060355040613025553310b300906035504" +
        "0813024341311230100603550407130950616c6f20416c746f311f301d060355" +
        "040a131653756e204d6963726f73797374656d732c20496e632e311630140603" +
        "55040b130d4a61766120536f667477617265310d300b0603550403130444756b" +
        "6530819f300d06092a864886f70d010101050003818d0030818902818100eff9" +
        "828aab6e728535df5b9c74ba6e7e0daf7a9489775bcf098cad008d57acc342ca" +
        "26847295973678f8eaf31cf4943d1ff1d98deca449024f1a890b4871952ad6ce" +
        "4f92b0b9e135337a5e1246a65bbf1afa9adf6fedcaca0e07fbda6e385cb8dfe6" +
        "a99afb112ea9ad0f7f187f90b3bb7734781700ef555f0c44ed7ce2d9459d0203" +
        "010001300d06092a864886f70d0101050500038181004b4569fb7104b731b022" +
        "672b403438971e30893e8e7a530c6d5e5b5f95e4a4be97d07f4ff0536e74ad38" +
        "1b04c81b348eaaed44722247c8d4448bff206ab3fd85832044275e83f1260737" +
        "efe7b020f8700018f54a56617808ea30c8597efb593c1a5f38b1961ea173fc8a" +
        "fc4ea9fb643a2c9d5c6799d0239c534852fec5564c5781f4468f2384f2dc9bb0" +
        "4eb719381d87946582ac";


    public JmxSslTest(String testName) {
        super(testName);
    }

    public void testSslStub() throws Exception {
        File keyStoreFile = File.createTempFile("keystore", "ks");
        keyStoreFile.deleteOnExit();
        decodeHexToFile(keyStoreFile, keyStoreHex);
        System.setProperty("javax.net.ssl.keyStore", keyStoreFile.getAbsolutePath());
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        File trustStoreFile = File.createTempFile("truststore", "ks");
        trustStoreFile.deleteOnExit();
        decodeHexToFile(trustStoreFile, trustStoreHex);
        System.setProperty("javax.net.ssl.trustStore", trustStoreFile.getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStorePassword", "trustword");
        // System.setProperty("javax.net.debug", "all");

        RMIClientSocketFactory csf = new SslRMIClientSocketFactory();
        RMIServerSocketFactory ssf = new SslRMIServerSocketFactory();
        
        Registry reg = LocateRegistry.createRegistry(0, csf, ssf);
        int port = getRegistryPort(reg);
        System.out.println("Port is " + port);
        
        // Server
        Map<String, Object> env = new HashMap<String, Object>();
        env.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE, csf);
        env.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, ssf);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///");
        RMIServerImpl rmiServerImpl = new RMIJRMPServerImpl(port, csf, ssf, null);
        RMIConnectorServer cs =
                new RMIConnectorServer(url, null, rmiServerImpl, mbs);
        cs.start();
        reg.bind("jmxrmi", rmiServerImpl);

        // Client
        Registry creg = LocateRegistry.getRegistry(
                InetAddress.getLocalHost().getHostAddress(), port, csf);
        RMIServer rmiServerStub = (RMIServer) creg.lookup("jmxrmi");
        assertEquals(RMIServerImpl_Stub.class, rmiServerStub.getClass());
        SObject sstub = (SObject) SerialScan.examine(rmiServerStub);
        List<SEntity> annots = sstub.getAnnotations();
        /* The annotations consist of the data written for a UnicastRef2
         * before the client socket factory; the client socket factory;
         * and the data written after the factory.
         */
        assertEquals(3, annots.size());
        SObject factory = (SObject) annots.get(1);
        assertEquals(SslRMIClientSocketFactory.class.getName(), factory.getType());
    }

    private static void decodeHexToFile(File f, String hex) throws IOException {
        FileOutputStream fout = new FileOutputStream(f);
        BufferedOutputStream bout = new BufferedOutputStream(fout);
        if (hex.length() % 2 == 1)
            throw new IllegalArgumentException("Odd length hex string");
        for (int i = 0; i < hex.length(); i += 2) {
            int x = Integer.parseInt(hex.substring(i, i + 2), 16);
            bout.write(x);
        }
        bout.close();
        fout.close();
    }
    
    private static int getRegistryPort(Registry reg) throws IOException {
        Remote stub = RemoteObject.toStub(reg);
        SObject sstub = (SObject) SerialScan.examine(stub);
        List<SEntity> annots = sstub.getAnnotations();
        SBlockData sdata = (SBlockData) annots.get(0);
        DataInputStream din = sdata.getDataInputStream();
        String type = din.readUTF();
        if (type.equals("UnicastRef"))
            return getRegistryPortUnicastRef(din);
        else if (type.equals("UnicastRef2"))
            return getRegistryPortUnicastRef2(din);
        else
            throw new IOException("Can't handle ref type " + type);
    }
    
    private static int getRegistryPortUnicastRef(DataInputStream din)
    throws IOException {
        String host = din.readUTF();
        return din.readInt();
    }
    
    private static int getRegistryPortUnicastRef2(DataInputStream din)
    throws IOException {
        byte hasCSF = din.readByte();
        String host = din.readUTF();
        return din.readInt();
    }
}
