package com.khorashadi.quickapp;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.google.gson.Gson;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class StatKeeper {
    private DescriptiveStatistics kryoSerializationTimes = new DescriptiveStatistics();
    private DescriptiveStatistics kryoDeserializationTimes = new DescriptiveStatistics();
    private DescriptiveStatistics gsonSerializationTimes = new DescriptiveStatistics();
    private DescriptiveStatistics gsonDeserializationTimes = new DescriptiveStatistics();

    int kryoSize = 0;
    int gsonSize;
    private int numIterations;
    int myObjectSize;

    Completable runExperiment(final int numIterations) {
        this.numIterations = numIterations;
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Kryo kryo = new Kryo();
                Gson gson = new Gson();
                long start;
                byte[] serializedBytes;
                MyObject myObject = new MyObject();
                myObjectSize = myObject.getNumCreations();
                for (int i = 0; i < numIterations; i++) {
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    Output output = new Output(stream);
                    start = System.nanoTime();
//                    kryo.writeObject(output, this);
//                    output.flush();
//                    serializedBytes = stream.toByteArray();
                    serializedBytes = myObject.serializeWithKryo(kryo);
                    kryoSerializationTimes.addValue((System.nanoTime() - start)/1000);
//                    output.close();
                    kryoSize = serializedBytes.length;

                    start = System.nanoTime();
                    String s = gson.toJson(myObject);
                    gsonSerializationTimes.addValue((System.nanoTime() - start)/1000);
                    gsonSize = s.getBytes().length;

                    MyObject returnedObj;
                    start = System.nanoTime();
                    returnedObj = readMyObject(kryo, serializedBytes);
                    kryoDeserializationTimes.addValue((System.nanoTime() - start)/1000);
                    if (!returnedObj.equals(myObject)) {
                        System.out.println(myObject.printMap());
                        System.out.println(returnedObj.printMap());
                        return;
//                        throw new RuntimeException("Objects not equal after deserialization");
                    }

                    start = System.nanoTime();
                    returnedObj = gson.fromJson(s, MyObject.class);
                    if (!returnedObj.equals(myObject)) {
                        System.out.println(myObject.printMap());
                        System.out.println(returnedObj.printMap());
                        return;
//                        throw new RuntimeException("Objects not equal after deserialization");
                    }
                    gsonDeserializationTimes.addValue((System.nanoTime() - start)/1000);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private MyObject readMyObject(Kryo kryo, byte[] serializedBytes) {
        Input input = new Input(serializedBytes);
        return kryo.readObject(input, MyObject.class);
    }

    @Override
    public String toString() {
        return myObjectSize + " Num Objects: " + numIterations + " Kryo stats " + kryoSerializationTimes.getMean()
                + " " + kryoDeserializationTimes.getMean() + " Gson stats: " + gsonSerializationTimes.getMean() + " " +
                gsonDeserializationTimes.getMean() + " kryo " + kryoSize + " gson size " + gsonSize;
    }
}
