package by.mercom.pstrade.printbonusticket.config;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Loads the configuration of the program and returns parameter values on request
 */

public class Config {
    private  final Logger logger = LoggerFactory.getLogger(Config.class);
    private XMLConfiguration xmlConfig = null;
    private String pathToConfig;

    public Config() {}

    /**
     * set path to file with config
     * @param path
     */
    public void setPathToConfig(String path) {
        this.pathToConfig = path;
    }

    /**
     * Convert to arrays to Map. The key and value are mapped by the array index
     * The maximum value of the index is the minimum length of the array
     * @param arrayKey - array, that uses as keys
     * @param arrayValues - array, that uses as values
     * @return a Map, containing keys and values, that were  added from arrays
     */
    private Map<String, String> getMapByArrays(String[] arrayKey, String[] arrayValues) {
        int minCount = arrayKey.length < arrayValues.length ? arrayKey.length : arrayValues.length;
        return IntStream
                .range(0, minCount)
                .boxed()
                .collect(Collectors.toMap(i -> arrayKey[i], i -> arrayValues[i]));
    }

    public void initConfig(){
        try {
            Configurations cfg = new Configurations();
            FileBasedConfigurationBuilder<XMLConfiguration> builder = cfg.xmlBuilder(new File(pathToConfig));
//            builder.setAutoSave(true);
            xmlConfig = builder.getConfiguration();
        } catch (ConfigurationException e) {
            logger.error("Config error", e);
            System.exit(-1);
        }
    }

    public String[] getDocKind() {
        String xmlNode = "tradinghouse.typeDocs.id";
        return xmlConfig.getStringArray(xmlNode);
    }

    public String getConnUrl(){
        String xmlNode = "server.url";
        return xmlConfig.getString(xmlNode);
    }

    public Table<String, String, String> getGoodsColumnParams() {
        String titleAtrName = "title";
        String xmlNodeAtrTitleColumn = "titles.goodsTable.columns.column[@" + titleAtrName +"]";

        String withAttrName = "prefWidth";
        String xmlNodeAtrWidthColumn = "titles.goodsTable.columns.column[@" + withAttrName + "]";

        String xmlNodeTagColumn = "titles.goodsTable.columns.column";

        String editableAtrName = "editable";
        String xmlNodeAtrEditColumn = "titles.goodsTable.columns.column[@" + editableAtrName + "]";

        String typeAtrName = "type";
        String xmlNodeAtrTypeColumn = "titles.goodsTable.columns.column[@" + typeAtrName + "]";

        String columnTitles[] = xmlConfig.getStringArray(xmlNodeAtrTitleColumn);
        String columnTags[] = xmlConfig.getStringArray(xmlNodeTagColumn);
        String columnWidths[] = xmlConfig.getStringArray(xmlNodeAtrWidthColumn);
        String columnEdits[] = xmlConfig.getStringArray(xmlNodeAtrEditColumn);
        String columnTypes[] = xmlConfig.getStringArray(xmlNodeAtrTypeColumn);

        Table<String, String, String> columnParams = HashBasedTable.create();
        for (int i = 0; i < columnTags.length; i++) {
            String columnTag = columnTags[i];
            String columnTitle = columnTitles[i];
            String columnWidth = columnWidths[i];
            String columnEdit = columnEdits[i];
            String columnType = columnTypes[i];
            columnParams.put(columnTag, titleAtrName, columnTitle);
            columnParams.put(columnTag, withAttrName, columnWidth);
            columnParams.put(columnTag, editableAtrName, columnEdit);
            columnParams.put(columnTag, typeAtrName, columnType);
        }
        return columnParams;
    }

    public Table<String, String, String> getDocColumnParams(){
        String titleAtrName = "title";
        String xmlNodeAtrTitleColumn = "titles.docTable.columns.column[@" + titleAtrName + "]";

        String widthAttrName = "prefWidth";
        String xmlNodeAtrWidthColumn = "titles.docTable.columns.column[@" + widthAttrName + "]";

        String xmlNodeTagColumn = "titles.docTable.columns.column";

        String columnTitles[] = xmlConfig.getStringArray(xmlNodeAtrTitleColumn);
        String columnTags[] = xmlConfig.getStringArray(xmlNodeTagColumn);
        String columnWidths[] = xmlConfig.getStringArray(xmlNodeAtrWidthColumn);

        Table<String, String, String> columnParams = HashBasedTable.create();
        for (int i = 0; i < columnTags.length; i++) {
            String columnTag = columnTags[i];
            String columnTitle = columnTitles[i];
            String columnWidth = columnWidths[i];

            columnParams.put(columnTag, titleAtrName, columnTitle);
            columnParams.put(columnTag, widthAttrName, columnWidth);
        }
        return columnParams;
    }

    public Float getMinWeightForShow() {
        String xmlNode = "per_kg[@min_weight]";
        return xmlConfig.getFloat(xmlNode);
    }

    public List<Integer> getHideWeightGroup() {
        String xmlNode = "per_kg.always_hide.groups.group";
        return Arrays.asList(((Integer[]) xmlConfig.getArray(Integer.class, xmlNode)));
    }

    public List<Integer> getAlwaysShowWeightGroup() {
        String xmlNode = "per_kg.always_show.groups.group";
        return Arrays.asList(((Integer[]) xmlConfig.getArray(Integer.class, xmlNode)));
    }

    public Table<String, String, String> getReportFilesParams() {
        String sourceReportAtrName = "source";
        String sourceParam = "jreport.formats.format[@"+ sourceReportAtrName +"]";

        String targetReportAtrName = "target";
        String targetParam = "jreport.formats.format[@" + targetReportAtrName +"]";

        String formatAtrName = "name";
        String nameParam = "jreport.formats.format[@" + formatAtrName + "]";

        String names[] = xmlConfig.getStringArray(nameParam);
        String sources[] = xmlConfig.getStringArray(sourceParam);
        String targets[] = xmlConfig.getStringArray(targetParam);

        Table<String, String, String> reportParams = HashBasedTable.create();
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            String sourceFile = sources[i];
            String targetFile = targets[i];
            reportParams.put(name, "sourceFile", sourceFile);
            reportParams.put(name, "targetFile", targetFile);
        }
        return reportParams;
    }

    public Map<String, String> getJReportParams(){
        String xmlNodeParamName = "jreport.reportTitles.titles[@name]";
        String xmlNodeParamValue = "jreport.reportTitles.titles[@value]";
        return getMapByArrays(xmlConfig.getStringArray(xmlNodeParamName), xmlConfig.getStringArray(xmlNodeParamValue));
    }

    public String getPriceRub(){
        String xmlNode = "jreport.price.rubl";
        return xmlConfig.getString(xmlNode);
    }

    public String getPriceKop(){
        String xmlNode = "jreport.price.kop";
        return xmlConfig.getString(xmlNode);
    }

    public String getDocDateFormat(){
        String xmlNode = "jreport.document.dateTmpl";
        return xmlConfig.getString(xmlNode);
    }

    public Integer getLenghtForName(){
        String xmlNode = "jreport.formats[@name_lenght]";
        return xmlConfig.getInt(xmlNode);
    }
}
