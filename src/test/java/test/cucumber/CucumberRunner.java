package cucumber;

/**
 * Created by mladen on 8/19/2017.
 */
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/cucumber",
        format = {"pretty", "html:target/cucumber"})
public class CucumberRunner {
}
