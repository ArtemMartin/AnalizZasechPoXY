package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author user
 */
public class Zasechka {
    static final String file = "ListTarget.txt";
    //исчем X, Y
    public List<Double[]> getXY() {
        List<String> strList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String[] mas;
            while ((line = reader.readLine()) != null) {
                mas = line.split("[ .,/\n=]");
                Collections.addAll(strList, mas);
            }
        } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).info("Шляпа: при чтении файла!!!");
        }

        double x;
        double y;
        List<Double[]> lisObjXY = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\d");//шаблон для поиска цифр
        Matcher matcher;

        int i;
        for (i = 0; i < strList.size(); i++) {
            matcher = pattern.matcher(strList.get(i));//Создаем объект Matcher для работы со строкой
            String strChis = "";
            while (matcher.find()) {
                strChis = strChis + matcher.group();
            }
            if (strChis.length() >= 7) {
                x = Double.parseDouble(strChis);
                for (i++; i < strList.size(); i++) {
                    matcher = pattern.matcher(strList.get(i));//Создаем объект Matcher для работы со строкой
                    strChis = "";
                    while (matcher.find()) {
                        strChis = strChis + matcher.group();
                    }
                    if (strChis.length() >= 7) {
                        y = Double.parseDouble(strChis);
                        lisObjXY.add(new Double[]{x, y});
                        i++;
                        break;
                    }
                }
            }
        }
        return lisObjXY;
    }
}

