package com.manikoske.awasr;

import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws FileNotFoundException {
        /* https://github.com/micromata/javaapiforkml/blob/master/src/test/java/de/micromata/jak/examples/Example3.java */
        /* https://github.com/micromata/javaapiforkml/blob/master/src/test/java/de/micromata/jak/examples/Example1.java */

        Kml kml = Kml.unmarshal(new File("src/main/java/com/manikoske/awasr/kmz/input/alHailAlJanubiyya.kml"));

        Document document = (Document) kml.getFeature();
        Folder folder = (Folder) document.getFeature().get(0);

        final Kml outputKml = new Kml();
        Document outputDocument = outputKml.createAndSetDocument().withName("JAK Example1").withOpen(true);
        Style outputStyle = outputDocument.createAndAddStyle();

        // create a Folder
        Folder outputFolder = outputDocument.createAndAddFolder();
        folder.withName("Continents with Earth's surface").withOpen(true);


//        kml = Kml.unmarshal(new File("src/main/java/com/manikoske/awasr/kmz/input/alHailAlShamaliyya.kml"));
//        kml = Kml.unmarshal(new File("src/main/java/com/manikoske/awasr/kmz/input/alKhaud.kml"));
//        kml = Kml.unmarshal(new File("src/main/java/com/manikoske/awasr/kmz/input/alMabailaAlJanubiyya.kml"));
//        kml = Kml.unmarshal(new File("src/main/java/com/manikoske/awasr/kmz/input/alMabailaAlShamaliyya.kml"));
//        kml = Kml.unmarshal(new File("src/main/java/com/manikoske/awasr/kmz/input/alMawalihAlJanubiyya.kml"));
//        kml = Kml.unmarshal(new File("src/main/java/com/manikoske/awasr/kmz/input/alMawalihAlShamaliyya.kml"));
//        kml = Kml.unmarshal(new File("src/main/java/com/manikoske/awasr/kmz/input/alSeebJadida.kml"));
//        kml = Kml.unmarshal(new File("src/main/java/com/manikoske/awasr/kmz/input/alSharadi.kml"));
//        kml = Kml.unmarshal(new File("src/main/java/com/manikoske/awasr/kmz/input/hayAlSaruj.kml"));
//        kml = Kml.unmarshal(new File("src/main/java/com/manikoske/awasr/kmz/input/surElHadid.kml"));
        kml.marshal(new File("HelloKml.kml"));
    }
}
