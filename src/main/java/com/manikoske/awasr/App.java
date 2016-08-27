package com.manikoske.awasr;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import de.micromata.opengis.kml.v_2_2_0.*;

import javax.print.Doc;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Hello world!
 *
 */
public class App 
{

    public static final String IMAGE_PATH = "placemark.png";
    public static final String ICON_STYLE_NAME = "style_icon";
    public static final String BASE_INPUT_PATH = "input/";
    public static final String IMAGE_FILE_PATH = "input/img/placemark.png";
    public static final String BASE_OUTPUT_PATH = "output/result/";
    public static final String BASE_OUTPUT_TMP = "output/result/tmp.kml";

    public static void main( String[] args ) throws IOException, JAXBException {

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

        List<MapEntry> maps = new LinkedList<>();
        maps.add(new MapEntry("Al Seeb", "MBL", Arrays.asList("alSharadi.kml", "alSeebJadida.kml", "surElHadid.kml")));
        maps.add(new MapEntry("Al Mawalih North", "ALK", Arrays.asList("alMawalihAlShamaliyya.kml")));
        maps.add(new MapEntry("Al Hail North", null, Arrays.asList("alHailAlShamaliyya.kml")));
        maps.add(new MapEntry("Al Hail South", null, Arrays.asList("alHailAlJanubiyya.kml")));
        maps.add(new MapEntry("Al Khod", null, Arrays.asList("alKhaud.kml")));
        maps.add(new MapEntry("Al Mawalih South", null, Arrays.asList("alMawalihAlJanubiyya.kml")));
        maps.add(new MapEntry("Al Mabelah", null, Arrays.asList("alMabailaAlShamaliyya.kml", "alMabailaAlJanubiyya.kml")));
        maps.add(new MapEntry("Shatti Al Qurum", null, Arrays.asList("hayAlSaruj.kml")));

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

        for (MapEntry entry : maps) {
            resultName = entry.name;
            inputFiles = entry.inputKmls;

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
                        if (entry.tagRestriction == null || (entry.tagRestriction != null && inputPlacemark.getName().startsWith(entry.tagRestriction))) {
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
                        } else {
                            System.out.println(inputPlacemark.getName());
                        }
                    }
                }
            }
            m.marshal(outputKml, new FileOutputStream(new File(BASE_OUTPUT_TMP)));
            createZip(resultName);
        }
    }

    private static void createZip(String name) throws IOException {

        FileOutputStream fos = new FileOutputStream(BASE_OUTPUT_PATH + name + ".zip");
        ZipOutputStream zos = new ZipOutputStream(fos);

        addToZipFile(BASE_OUTPUT_TMP, "doc.kml",zos);
        addToZipFile(IMAGE_FILE_PATH, "placemark.png",zos);

        zos.close();
        fos.close();
    }

    private static void addToZipFile(String fileName, String pathInZip, ZipOutputStream zos) throws IOException {

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(pathInZip);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }

    public static class MapEntry {

        public String name;
        public String tagRestriction;
        public List<String> inputKmls;

        public MapEntry(String name, String tagRestriction, List<String> inputKmls) {
            this.name = name;
            this.tagRestriction = tagRestriction;
            this.inputKmls = inputKmls;
        }


    }
}
