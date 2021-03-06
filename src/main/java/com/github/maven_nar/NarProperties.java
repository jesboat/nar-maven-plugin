package com.github.maven_nar;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.PropertyUtils;

public class NarProperties {
	
	private final static String AOL_PROPERTIES = "aol.properties";
	private Properties properties;
	private static NarProperties instance;
	
	private NarProperties(MavenProject project) throws MojoFailureException {
		
		Properties defaults = PropertyUtils.loadProperties( NarUtil.class.getResourceAsStream( AOL_PROPERTIES ) );
        if ( defaults == null )
        {
            throw new MojoFailureException( "NAR: Could not load default properties file: '"+AOL_PROPERTIES+"'." );
        }
        
        properties = new Properties(defaults);
        FileInputStream fis = null;
        try 
        {
        	if (project != null) {
        		fis = new FileInputStream(project.getBasedir()+File.separator+AOL_PROPERTIES);
        		properties.load( fis );
        	}
		} 
        catch (FileNotFoundException e) 
        {
			// ignore (FIXME)
		} 
        catch (IOException e) 
        {
			// ignore (FIXME)
		}
        finally
        {
            try
            {
                if ( fis != null )
                {
                    fis.close();
                }
            }
            catch ( IOException e )
            {
                // ignore
            }
        }

	}
	
	/**
	 * Retrieve the NarProperties
	 * @param project may be null
	 * @return
	 * @throws MojoFailureException
	 */
	public static NarProperties getInstance(MavenProject project) throws MojoFailureException {
		if (instance == null) {
			instance = new NarProperties(project);
		}
		return instance;
	}
	
	/**
	 * Programmatically inject properties (and possibly overwrite existing properties)
	 * @param project the current maven project
	 * @param properties the properties from input stream
	 */
	public static void inject(MavenProject project, InputStream properties) throws MojoFailureException {
		final Properties defaults = PropertyUtils.loadProperties( properties );
		final NarProperties nar = getInstance(project);
		nar.properties.putAll(defaults);
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
}
