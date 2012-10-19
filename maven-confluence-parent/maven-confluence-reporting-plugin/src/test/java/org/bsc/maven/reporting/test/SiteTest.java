/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.maven.reporting.test;

import org.bsc.maven.reporting.model.Site;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author softphone
 */
public class SiteTest {
        
    @Test
    public void jaxbTest() throws Exception {

        JAXBContext jc = JAXBContext.newInstance(Site.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();

        Object result = unmarshaller.unmarshal( getClass().getClassLoader().getResourceAsStream("site.xml"));

        Assert.assertThat(result, IsNull.notNullValue());
        Assert.assertThat(result, IsInstanceOf.instanceOf(Site.class));
        
        Site site = (Site) result;
        
        Assert.assertThat(site.getHome().getUri(), IsNull.notNullValue());        
        Assert.assertThat(site.getHome().getName(), IsEqual.equalTo("home"));        
        Assert.assertThat(site.getHome().getSource().getName(), IsEqual.equalTo("home.confluence"));        
        Assert.assertThat(site.getHome(), IsNull.notNullValue());        
        Assert.assertThat(site.getHome().getAttachments(), IsNull.notNullValue());        
        Assert.assertThat(site.getHome().getAttachments().isEmpty(), Is.is(false));
        Assert.assertThat(site.getHome().getChildren(), IsNull.notNullValue());        
        Assert.assertThat(site.getHome().getChildren().isEmpty(), Is.is(false));
        Assert.assertThat(site.getLabels().isEmpty(), Is.is(false));
 
        for( String label : site.getLabels() ) {
            System.out.printf( "label=[%s]\n", label);
        }
    }

}
