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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>A representation of a general Java object appearing in a serial stream.
 * This class is used for all objects except arrays, primitives, and strings,
 * which are represented respectively by {@link SArray}, {@link SPrim},
 * and {@link SString}.</p>
 *
 * <p>The fields accessed by the {@link #getField getField} and {@link #setField
 * setField} methods are serial fields.  Usually these are actual fields in
 * the original Java class of the object, but a class that has a
 * {@code serialPersistentFields} definition, as described in the
 * <a href="http://java.sun.com/javase/6/docs/platform/serialization/spec/class.html">
 * serialization specification</a> can have serial fields that are unrelated
 * to its Java fields.</p>
 *
 * <p>A serialized enumeration constant is represented as an SObject
 * with a synthetic field called {@code "<name>"} of type {@link SString}.</p>
 */
public class SObject extends SEntity {

    SObject(String type) {
        super(type);
    }

    /**
     * Change the representation of the serial field with the given name
     * in this serialized object.
     */
    void setField(String name, SEntity value) {
        fields.put(name, value);
    }

    void addAnnotation(SEntity annot) {
        annots.add(annot);
    }

    /**
     * Get the representation of the serial field with the given name in
     * this serialized object.
     */
    public SEntity getField(String name) {
        return fields.get(name);
    }

    /**
     * Get the names of the serial fields in this serialized object.
     */
    public Set<String> getFieldNames() {
        return fields.keySet();
    }

    /**
     * Get any additional data written to the serial stream by a
     * writeObject method in the class or any of its ancestors.
     */
    @SuppressWarnings("unchecked")
    public List<SEntity> getAnnotations() {
        return (List<SEntity>) annots.clone();
    }

    String kind() {
        return "SObject";
    }

    String contents() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (Map.Entry<String, SEntity> entry : fields.entrySet()) {
            indent(sb);
            sb.append(entry.getKey()).append(" = ")
            .append(entry.getValue()).append("\n");
        }
        if (annots.size() > 0) {
            indent(sb);
            sb.append("-- data written by class's writeObject:\n");
            for (SEntity annot : annots) {
                indent(sb);
                sb.append(annot + "\n");
            }
        }
        return sb.toString();
    }

    public String getType() {
        return super.getType();
    }

    private final Map<String, SEntity> fields = new LinkedHashMap<String, SEntity>();
    private final ArrayList<SEntity> annots = new ArrayList<SEntity>();
}
