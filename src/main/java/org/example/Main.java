package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static final String file = "ListTarget.txt";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int radysGrypirov;
        int vvod;
        List<RaionZasechek> zasechekList;

        System.out.println("<<< Анализ засечек >>>");
        do {
            System.out.print("Ввести радиус групирования: ");
            radysGrypirov = scanner.nextInt();
            scanner.nextLine();
            List<Double[]> list = new ArrayList<>();
            System.out.println("Файл забит в ручную в формате Х У - 1, скопированные сообщения из чата - 0: ");
            vvod = scanner.nextInt();
            scanner.nextLine();
            if (vvod == 1) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    String[] mas;
                    while ((line = reader.readLine()) != null) {
                        mas = line.split("[^A-Za-z0-9]+");
                        list.add(new Double[]{Double.parseDouble(mas[0]), Double.parseDouble(mas[1])});
                    }
                } catch (Exception e) {
                    Logger.getLogger(Main.class.getName()).info("Шляпа: при чтении файла!!!");
                }
            } else {
                Zasechka zasechka = new Zasechka();
                list = zasechka.getXY();
            }
            //выводим список районов
            zasechekList = getListArea(list, radysGrypirov);
            for (RaionZasechek raionZasechek : zasechekList) {
                System.out.println(raionZasechek);
            }
            System.out.print("Создать файл kml - 1, НЕТ - 0:");
            vvod = scanner.nextInt();
            if (vvod == 1) {
                GenerateKML generateKML = new GenerateKML(zasechekList);
                generateKML.generate();
            }
            System.out.print("Прокрутить ещё раз - 1, НЕТ - 0:");
            vvod = scanner.nextInt();
        } while (vvod == 1);
    }

    public static List<RaionZasechek> getListArea(List<Double[]> list0, int radysGrypirov) {
        List<RaionZasechek> list = new ArrayList<>();
        List<Double[]> listPromeg = new ArrayList<>();
        double x;
        double x1;
        double y;
        double y1;
        double raznX;
        double raznY;
        for (int i = 0; i < list0.size(); i++) {
            x = list0.get(i)[0];
            y = list0.get(i)[1];
            for (int j = i + 1; j < list0.size(); j++) {
                x1 = list0.get(j)[0];
                y1 = list0.get(j)[1];
                raznX = Math.abs(x - x1);
                raznY = Math.abs(y - y1);
                if (raznX <= radysGrypirov && raznY <= radysGrypirov) {
                    listPromeg.add(new Double[]{x1, y1});
                    list0.remove(j);
                    i--;
                    j--;
                }
            }
            if (!listPromeg.isEmpty()) {
                listPromeg.add(new Double[]{x, y});
                if (listPromeg.size() >= 3) list.add(getYsrZnach(listPromeg));
                listPromeg.clear();
            }
        }
        if (list.isEmpty()) System.out.println("По указаным засечкам сгрупированных районов нет!!!");
        return list;
    }

    public static RaionZasechek getYsrZnach(List<Double[]> listPromeg) {
        double xSym = 0.0;
        double ySym = 0.0;
        for (Double[] doubles : listPromeg) {
            xSym += doubles[0];
            ySym += doubles[1];
        }
        double xSr = xSym / listPromeg.size();
        double ySr = ySym / listPromeg.size();
        return new RaionZasechek(Math.round(xSr), Math.round(ySr), listPromeg.size());
    }
}
