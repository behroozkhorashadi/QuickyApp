package com.khorashadi.quickapp;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyObject {

    private final int numCreations;

    private Map<String, String> map = new HashMap<>();
    private List<String> myList = new LinkedList<>();

    public MyObject() {
        Random r = new Random();
        numCreations = r.nextInt(1000);
        for (int i = 0; i < numCreations; i++) {
            map.put(String.valueOf(r.nextFloat()), String.valueOf(r.nextFloat()));
            myList.add(String.valueOf(r.nextDouble()));
        }
    }

    public Map<String, String> getMap() {
        return map;
    }
    public List<String> getMyList() {
        return myList;
    }
    public int getNumCreations() {
        return numCreations;
    }

    public byte[] serializeWithKryo(Kryo kryo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Output output = new Output(stream);

        kryo.writeObject(output, this);
        output.flush();

        byte[] bytes = stream.toByteArray();
        output.close();
        return bytes;
    }

    public String printMap() {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        return sb.toString();

    }

    public boolean equals(Object o) {
        if (!(o instanceof MyObject)) {
            return false;
        }
        MyObject myObject = (MyObject) o;
        for (String key : map.keySet()) {
            if (!myObject.getMap().containsKey(key)) {
                return false;
            }
            if (!myObject.getMap().get(key).equals(map.get(key))) {
                return false;
            }
        }
        for (String val : myList) {
            if (!myObject.myList.contains(val)) {
                return false;
            }
        }
        return true;
    }
}
