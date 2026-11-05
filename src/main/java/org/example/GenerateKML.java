package org.example;

import org.osgeo.proj4j.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author user
 */
public class GenerateKML {

    private final File filePath;
    List<RaionZasechek> listTocheck;

    public GenerateKML( List<RaionZasechek> listTocheck) {
        this.filePath = new File("D:\\Projekts\\AnalizZasechPoXY\\AnalizZasechPoXY.kml");
        this.listTocheck = listTocheck;
    }

    public static List<Double> refactorXYtoBL(double x, double y) {
        List<Double> list = new ArrayList<>();
        // Создаем исходную и целевую системы координат
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem srcCRS = factory.createFromName("EPSG:28407");
        CoordinateReferenceSystem dstCRS = factory.createFromName("EPSG:4326");

        // Создаем объект для преобразования координат
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = ctFactory.createTransform(srcCRS, dstCRS);

        // Преобразуем координаты
        //сначала вводим долготу потом широту
        ProjCoordinate srcCoord = new ProjCoordinate(y, x);
        ProjCoordinate dstCoord = new ProjCoordinate();
        transform.transform(srcCoord, dstCoord);

        // Выводим результат(наоборот x->y)
        //System.out.println("Преобразованные координаты: " + dstCoord.x + ", " + dstCoord.y);
        //возвращаем масив b, l
        list.add(dstCoord.y);
        list.add(dstCoord.x);
        return list;
    }

    public void generate() {
        List<Double> bl;
        double b;
        double l;

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<kml xmlns=\"http://earth.google.com/kml/2.2\">\n"
                    + "  <Document>\n");
            //<!-- Стиль для синей точки -->
            writer.write("<Style id='bluePoint'>\n"
                    + "<IconStyle>\n"
                    + "<color>FF0000FF</color>\n"          //<!-- Синий, полностью непрозрачный -->
                    + " <scale>1.2</scale> \n"            // <!-- Размер иконки (опционально) -->
                    + "<Icon>\n"
                    + "<href>http://maps.google.com/mapfiles/kml/shapes/placemark_circle.png</href>\n"
                    + "</Icon>\n"
                    + "</IconStyle>\n"
                    + "<LabelStyle>\n"
                    + "<color>FFFFFFFF</color>\n"       //<!-- Белый цвет текста подписи -->
                    + "<scale>0.8</scale>\n"
                    + "</LabelStyle>\n"
                    + "</Style>");

            for (int i = 0; i < listTocheck.size(); i++) {
                bl = refactorXYtoBL(listTocheck.get(i).getX(), listTocheck.get(i).getY());
                b = bl.get(0);
                l = bl.get(1);

                writer.write("    <Placemark>\n");
                writer.write("<name>(Район засечек)</name>\n");
                writer.write("<description>" + "к-во засечек " + listTocheck.get(i).getCount() + "</description>\n");
                writer.write("<styleUrl>#bluePoint</styleUrl>\n"
                        + "      <Point>\n"
                        + "        <extrude>1</extrude>\n");
                writer.write("<coordinates>" + l + "," + b + "," + 0 + "</coordinates>\n");
                writer.write(" </Point>\n"
                        + "  </Placemark>\n");
            }
            writer.write(" </Document>\n"
                    + "</kml>");
            System.out.println("kml файл сгенерирован успешно!!!");
        } catch (IOException e) {
            System.out.println("Шляпа: " + GenerateKML.class.getName() + e.getMessage());
        }
    }
}
