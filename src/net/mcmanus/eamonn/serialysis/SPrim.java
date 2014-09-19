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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * A representation of a serialized primitive object such as an int or
 * boolean.
 */
public class SPrim extends SEntity {

    private final Object value;

    /**
     * Create a representation of the given wrapped primitive object.
     *
     * @param x a wrapped primitive object, for example an Integer if
     * the represented primitive is an int.
     */
    SPrim(Object x) {
        super(wrappedClassToPrimName.get(x.getClass()));
        this.value = x;
    }

    String kind() {
        return "SPrim";
    }

    String contents() {
        return value.toString();
    }

    /**
     * The value of the primitive object, wrapped in the corresponding
     * wrapper type, for example Integer if the primitive is an int.
     */
    public Object getValue() {
        return value;
    }
    
    private static final Map<Class<?>, String>
            wrappedClassToPrimName = new HashMap<Class<?>, String>();
    static {
        for (Class<?> c : new Class<?>[] {
            Boolean.class, Byte.class, Character.class, Double.class,
            Float.class, Integer.class, Long.class, Short.class
        }) {
            try {
                Field type = c.getField("TYPE");
                Class<?> prim = (Class<?>) type.get(null);
                wrappedClassToPrimName.put(c, prim.getName());
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }
    }
}
