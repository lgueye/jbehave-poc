/**
 * 
 */
package fr.explorimmo.poc.stories;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.core.steps.ParameterConverters.ExamplesTableConverter;
import org.jbehave.core.steps.spring.SpringApplicationContextFactory;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author louis.gueye@gmail.com
 */
public class StoriesRunner extends JUnitStories {

    private final CrossReference xref = new CrossReference();

    @Override
    public Configuration configuration() {
        final Class<? extends Embeddable> embeddableClass = this.getClass();
        final Properties viewResources = new Properties();
        viewResources.put("decorateNonHtml", "true");
        // Start from default ParameterConverters instance
        final ParameterConverters parameterConverters = new ParameterConverters();
        // factory to allow parameter conversion and loading from external resources (used by StoryParser too)
        final ExamplesTableFactory examplesTableFactory = new ExamplesTableFactory(new LocalizedKeywords(),
                new LoadFromClasspath(embeddableClass), parameterConverters);
        // add custom coverters
        parameterConverters.addConverters(new DateConverter(new SimpleDateFormat("yyyy-MM-dd")),
            new ExamplesTableConverter(examplesTableFactory));
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(embeddableClass))
                .useStoryParser(new RegexStoryParser(examplesTableFactory))
                .useStoryReporterBuilder(
                    new StoryReporterBuilder().withCodeLocation(CodeLocations.codeLocationFromClass(embeddableClass))
                            .withDefaultFormats().withViewResources(viewResources).withFailureTrace(true)
                            .withFormats(Format.HTML_TEMPLATE).withFailureTraceCompression(true)
                            .withCrossReference(xref)).useParameterConverters(parameterConverters)
                .useStepMonitor(xref.getStepMonitor());
    }

    private ApplicationContext createContext() {
        return new SpringApplicationContextFactory(this.getClass().getClassLoader(), "jbehave-context.xml",
                "stories-context.xml").createApplicationContext();
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new SpringStepsFactory(configuration(), createContext());
    }

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder().findPaths(CodeLocations.codeLocationFromClass(this.getClass()).getFile(),
            Arrays.asList("**/*.story"), null);
    }
}
