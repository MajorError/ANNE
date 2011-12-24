package uk.ac.ic.doc.neuralnets.gui.statistics;

/**
 * Output node firings to a matlab matrix
 *
 * @author Peter Coetzee
 */
public class MatlabMatrixConfig extends StatisticianFileConfig {

    protected String getExtension() {
        return "*.m";
    }

    public int getPriority() {
        return 2;
    }

    public String getName() {
        return "MatlabMatrix";
    }

}
