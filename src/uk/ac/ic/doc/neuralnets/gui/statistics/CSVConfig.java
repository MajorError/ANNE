package uk.ac.ic.doc.neuralnets.gui.statistics;

/**
 * Output neurone firings as comma separated values
 * 
 * @author Peter Coetzee
 */
public class CSVConfig extends StatisticianFileConfig {

    protected String getExtension() {
        return "*.csv";
    }

    public int getPriority() {
        return 3;
    }

    public String getName() {
        return "CSV";
    }

}
