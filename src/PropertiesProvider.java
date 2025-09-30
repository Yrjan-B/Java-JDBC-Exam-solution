import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

//# Klasse for å koble opp properties filen, for å beskytte informasjon
public class PropertiesProvider {

    // Instance variabel som holder en Properties verdi
    public static final Properties PROPERTIES;


    static {
        PROPERTIES = new Properties();
        try {
            //Leser inn info i propertiesfilen og laster det opp i PROPERTIES.
            PROPERTIES.load(new FileInputStream("scrapyard.properties"));
        } catch (IOException e) {
            System.out.println("Failes to load");
            throw new RuntimeException(e);
        }
    }
}
