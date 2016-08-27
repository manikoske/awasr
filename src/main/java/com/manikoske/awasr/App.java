package com.manikoske.awasr;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import de.micromata.opengis.kml.v_2_2_0.*;

import javax.print.Doc;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{

    public static final String IMAGE_PATH = "placemark.png";
    public static final String ICON_STYLE_NAME = "style_icon";
    public static final String BASE_INPUT_PATH = "kmz/input/";

    public static final String DATA_NAME_FIBRE_HUB = "fibreHub";
    public static final String DATA_NAME_NETWORK_ACCESS_POINT = "networkAccessPoint";
    public static final String DATA_NAME_SERIAL = "serial";
    public static final String DATA_NAME_TAG_ID = "tagId";



    public static void main( String[] args ) throws FileNotFoundException, JAXBException {

        Marshaller m =  JAXBContext.newInstance(new Class[] { Kml.class }).createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true );
        m.setProperty(Marshaller.JAXB_FRAGMENT, false );
        m.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8");
        NamespacePrefixMapper mapper = new NamespacePrefixMapper() {
            @Override
            public String getPreferredPrefix(String s, String s1, boolean b) {
                if (s.matches("http://www.opengis.net/kml/2.2")) {
                    return "";
                }
                return null;
            }
        };
        m.setProperty("com.sun.xml.bind.namespacePrefixMapper", mapper);

        Map<String, List<String>> resultMapping = new HashMap<>();
//        resultMapping.put("Al Seeb", Arrays.asList("alSharadi.kml", "alSeebJadida.kml", "surElHadid.kml"));
        resultMapping.put("Al Khod", Arrays.asList("alKhaud.kml"));

        String resultName;
        List<String> inputFiles;
        Kml inputKml;
        Document inputDocument;
        Folder inputFolder;
        Placemark inputPlacemark;
        Point inputPoint;
        Coordinate inputCoordinate;

        Kml outputKml;
        Document outputDocument;
        Placemark outputPlacemark;

        StringBuilder sb;

        for (Map.Entry<String, List<String>> entry : resultMapping.entrySet()) {
            resultName = entry.getKey();
            inputFiles = entry.getValue();

            outputKml = new Kml();
            outputDocument = outputKml.createAndSetDocument().withName(resultName);
            Style outputStyle = outputDocument.createAndAddStyle();
            Icon icon = new Icon().withHref(IMAGE_PATH);
            outputStyle.withId(ICON_STYLE_NAME).createAndSetIconStyle().withScale(2).withIcon(icon);

            for (String inputFileName : inputFiles) {
                inputKml = Kml.unmarshal(new File(BASE_INPUT_PATH + inputFileName));
                inputDocument = (Document) inputKml.getFeature();
                for (Feature folderFeature : inputDocument.getFeature()) {
                    inputFolder = (Folder) folderFeature;
                    for (Feature placemarkFeature : inputFolder.getFeature()) {
                        inputPlacemark = (Placemark) placemarkFeature;
                        inputPoint = (Point) inputPlacemark.getGeometry();
                        inputCoordinate = inputPoint.getCoordinates().iterator().next();

                        sb = new StringBuilder();

                        for (SimpleData simpleData : inputPlacemark.getExtendedData().getSchemaData().iterator().next().getSimpleData()) {
                            switch (simpleData.getName()) {
                                case "FDH":
                                case "NAP":
                                case "TENANCIES":
                                    sb.append(simpleData.getValue());
                                    sb.append("~");
                                    break;
                            }
                        }
                        sb.append(inputPlacemark.getName());
                        sb.append("~");
                        sb.append(inputCoordinate.getLongitude());
                        sb.append("~");
                        sb.append(inputCoordinate.getLatitude());

                        outputPlacemark = outputDocument.createAndAddPlacemark();
                        outputPlacemark.withName(inputPlacemark.getName()).withStyleUrl("#" + ICON_STYLE_NAME);
                        outputPlacemark.createAndSetPoint().addToCoordinates(inputCoordinate.getLongitude(), inputCoordinate.getLatitude(), 0);
                        outputPlacemark.setDescription(sb.toString());
                    }
                }
            }
            m.marshal(outputKml, new FileOutputStream(new File(resultName + ".kml")));
        }


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

    }
}
