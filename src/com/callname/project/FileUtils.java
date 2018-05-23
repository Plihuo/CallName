package com.callname.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileUtils {

    public static Map<String, List<String>> parseStudent(File file) throws IOException {
        Map<String, List<String>> students = new HashMap<String, List<String>>();
        String[] strs = null;
        BufferedReader br = null;
        List<String[]> list = new ArrayList<String[]>();
        try {
        	FileInputStream fis = new FileInputStream(file);
        	InputStreamReader isr = new InputStreamReader(fis,"utf-8");
            br = new BufferedReader(isr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        String line = "";
        try {
            while ((line = br.readLine()) != null){
                strs = line.split(",");
                System.out.println(line);
                list.add(strs);
            }
            System.out.println("csv表格中所有行数：" + list.size());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br!=null) {
                br.close();
            }
        }
        List<String> names = null;
        for (String[] ss : list) {
            Iterator<String> keys = students.keySet().iterator();
            if(students.containsKey(ss[1])) {
                System.out.println("containsKey" + ss[0]+","+ss[1]);
                while(keys.hasNext()) {
                    String key = keys.next();
                    if(key.equals(ss[1])) {
                        students.get(key).add(ss[0]);
                    }
                }
            }else {
                names = new ArrayList<String>();
                names.clear();
                names.add(ss[0]);
                students.put(ss[1], names);
            }
        }
        System.out.println("===========" + students);
        return students;
    }
}
