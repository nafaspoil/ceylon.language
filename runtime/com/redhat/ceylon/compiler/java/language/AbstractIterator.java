package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Iterator;
import ceylon.language.Iterator$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

public abstract class AbstractIterator<Element> implements Iterator<Element>, ReifiedType {
    
    @Ignore
    private TypeDescriptor $reifiedElement;

    public AbstractIterator(TypeDescriptor $reifiedElement) {
        this.$reifiedElement = $reifiedElement;
    }
    
    // our subclasses are generated classes for comprehensions that never need their superinterface because
    // it's not visible to user code
    @Ignore
    @Override
    public Iterator$impl<Element> $ceylon$language$Iterator$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AbstractIterator.class, $reifiedElement);
    }
}
