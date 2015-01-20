package com.abien.xray.business.configuration.boundary;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.abien.xray.business.configuration.control.ConfigurationProvider;

/**
 * User: blog.adam-bien.com
 * Date: 25.02.11
 * Time: 13:27
 */
@RunWith(Arquillian.class)
public class ConfigurationWithCustomProviderIT {

    @Inject
    Configurable configurable;

    @Inject
    Configuration configuration;

    @org.jboss.arquillian.container.test.api.Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "xray.jar").
                addClasses(Configuration.class,Configurable.class,ConfigurationProvider.class,DummyConfigurationProvider.class,AnotherConfigurationProvider.class).
                addAsManifestResource(
                        new ByteArrayAsset("<beans/>".getBytes()),
                        ArchivePaths.create("beans.xml"));
    }

    
    
    @Test
    public void customConfigurationExist(){
        assertTrue(this.configuration.doesCustomConfigurationExist());
    }


    @Test
    public void versionInjection() {
        String expected = new DummyConfigurationProvider().getConfiguration().get("version");
        assertThat(this.configurable.getVersion(),is(expected));
    }

    @Test
    public void answerKeyInjection() {
        String expectedString = new AnotherConfigurationProvider().getConfiguration().get("answer");
        int expected = Integer.parseInt(expectedString);
        assertThat(this.configurable.getAnswer(),is(expected));
    }

}
