package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import ceylon.language.model.Variable$impl;

import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.loader.model.FieldValue;
import com.redhat.ceylon.compiler.loader.model.JavaBeanValue;
import com.redhat.ceylon.compiler.loader.model.LazyValue;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.NONE),
})
public class AppliedVariable<Type> 
    extends AppliedValue<Type> 
    implements ceylon.language.model.Variable<Type> {

    private MethodHandle setter;

    public AppliedVariable(@Ignore TypeDescriptor $reifiedType, FreeAttribute value, ProducedTypedReference valueTypedReference, Object instance) {
        super($reifiedType, value, valueTypedReference, instance);
    }

    @Override
    protected void initField(com.redhat.ceylon.compiler.typechecker.model.Declaration decl, java.lang.Class<?> javaClass, 
                             java.lang.Class<?> getterReturnType, Object instance, ProducedType valueType) {
        if(decl instanceof JavaBeanValue){
            String setterName = ((JavaBeanValue) decl).getSetterName();
            try {
                Method m = javaClass.getMethod(setterName, getterReturnType);
                m.setAccessible(true);
                setter = MethodHandles.lookup().unreflect(m);
                setter = setter.bindTo(instance);
                setter = setter.asType(MethodType.methodType(void.class, getterReturnType));
                setter = MethodHandleUtil.unboxArguments(setter, 0, 0, new java.lang.Class[]{getterReturnType}, Arrays.asList(valueType));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            } catch (SecurityException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            }
        }else if(decl instanceof LazyValue){
            // FIXME: we should really save the getter name in the LazyDecl
            String setterName = Naming.getSetterName(decl);
            try {
                Method m = javaClass.getMethod(setterName, getterReturnType);
                m.setAccessible(true);
                setter = MethodHandles.lookup().unreflect(m);
                setter = setter.asType(MethodType.methodType(void.class, getterReturnType));
                setter = MethodHandleUtil.unboxArguments(setter, 0, 0, new java.lang.Class[]{getterReturnType}, Arrays.asList(valueType));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            } catch (SecurityException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            }
        }else if(decl instanceof FieldValue){
            String fieldName = ((FieldValue) decl).getRealName();
            try {
                Field f = javaClass.getField(fieldName);
                f.setAccessible(true);
                setter = MethodHandles.lookup().unreflectSetter(f);
                setter = setter.bindTo(instance);
                setter = setter.asType(MethodType.methodType(void.class, getterReturnType));
                setter = MethodHandleUtil.unboxArguments(setter, 0, 0, new java.lang.Class[]{getterReturnType}, Arrays.asList(valueType));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Failed to find field "+fieldName+" for: "+decl, e);
            } catch (SecurityException e) {
                throw new RuntimeException("Failed to find field "+fieldName+" for: "+decl, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to find field "+fieldName+" for: "+decl, e);
            }
        }else
            throw new RuntimeException("Unsupported attribute type: "+decl);
    }

    @Override
    @Ignore
    public Variable$impl<Type> $ceylon$language$model$Variable$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object set(Type value) {
        try {
            setter.invokeExact(value);
            return null;
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke setter for "+declaration.getName(), e);
        }
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedVariable.class, $reifiedType);
    }

}
