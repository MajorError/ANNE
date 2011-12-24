package uk.ac.ic.doc.neuralnets.util.configuration;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * @author Peter Coetzee
 */
public class Log4JConfig implements Configurator {

    public void configure() {
        DOMConfigurator.configure( "conf/log-conf.xml" );
        Logger.getLogger( Log4JConfig.class.getName() )
                .info( "Logging configuration loaded" );
    }

    public String getName() {
        return "Log4J";
    }

}
