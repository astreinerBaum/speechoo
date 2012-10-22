/**
 * Copyright (C) 2010 SpeechOO Team (speechoo-dev AT googlegroups DOT com)
 *
 * SpeechOO (speechoo-dev AT googlegroups DOT com)
 *
 * CCSL-IME/USP (FLOSS Competence Center at IME - University of São Paulo),
 * Rua do Matão, 1010
 * CEP 05508-090 - São Paulo - SP - BRAZIL
 *
 * LAPS-UFPA (Signal Processing Laboratory - Federal University of Pará),
 * Rua Augusto Correa, 1
 * CEP 660750-110 - Belém - PA - Brazil
 *
 * http://code.google.com/p/speechoo
 *
 * This file is part of SpeechOO.
 *
 * SpeechOO is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpeechOO is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with SpeechOO. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * CentralRegistrationClass.java
 *
 * Created on 2010.05.08 - 10:52:57
 *
 */
package org.speechoo;

import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import org.apache.log4j.Logger;

/**
 *
 * @author colen
 */
public class CentralRegistrationClass {
    
    public static XSingleComponentFactory __getComponentFactory( String sImplementationName ) {
        String regClassesList = getRegistrationClasses();
        StringTokenizer t = new StringTokenizer(regClassesList, " ");
        while (t.hasMoreTokens()) {
            String className = t.nextToken();
            if (className != null && className.length() != 0) {
                try {
                    Class regClass = Class.forName(className);
                    Method writeRegInfo = regClass.getDeclaredMethod("__getComponentFactory", new Class[]{String.class});
                    Object result = writeRegInfo.invoke(regClass, sImplementationName);
                    if (result != null) {
                       return (XSingleComponentFactory)result;
                    }
                }
                catch (ClassNotFoundException ex) {
                    SpeechOO.logger = Logger.getLogger(CentralRegistrationClass.class.getName());
                    SpeechOO.logger.error(ex);
                } catch (ClassCastException ex) {
                    SpeechOO.logger = Logger.getLogger(CentralRegistrationClass.class.getName());
                    SpeechOO.logger.error(ex);
                } catch (SecurityException ex) {
                    SpeechOO.logger = Logger.getLogger(CentralRegistrationClass.class.getName());
                    SpeechOO.logger.error(ex);
                } catch (NoSuchMethodException ex) {
                    SpeechOO.logger = Logger.getLogger(CentralRegistrationClass.class.getName());
                    SpeechOO.logger.error(ex);
                } catch (IllegalArgumentException ex) {
                    SpeechOO.logger = Logger.getLogger(CentralRegistrationClass.class.getName());
                    SpeechOO.logger.error(ex);
                } catch (InvocationTargetException ex) {
                    SpeechOO.logger = Logger.getLogger(CentralRegistrationClass.class.getName());
                    SpeechOO.logger.error(ex);
                } catch (IllegalAccessException ex) {
                    SpeechOO.logger = Logger.getLogger(CentralRegistrationClass.class.getName());
                    SpeechOO.logger.error(ex);
                }
            }
        }
        return null;
    }

    public static boolean __writeRegistryServiceInfo( XRegistryKey xRegistryKey ) {
        boolean bResult = true;
        String regClassesList = getRegistrationClasses();
        StringTokenizer t = new StringTokenizer(regClassesList, " ");
        while (t.hasMoreTokens()) {
            String className = t.nextToken();
            if (className != null && className.length() != 0) {
                try {
                    Class regClass = Class.forName(className);
                    Method writeRegInfo = regClass.getDeclaredMethod("__writeRegistryServiceInfo", new Class[]{XRegistryKey.class});
                    Object result = writeRegInfo.invoke(regClass, xRegistryKey);
                    bResult &= ((Boolean)result).booleanValue();
                }
                catch (ClassNotFoundException ex) {
                    SpeechOO.logger = Logger.getLogger(CentralRegistrationClass.class.getName());
                    SpeechOO.logger.error(ex);
                } catch (ClassCastException ex) {
                    SpeechOO.logger = Logger.getLogger(CentralRegistrationClass.class.getName());
                    SpeechOO.logger.error(ex);
                } catch (SecurityException ex) {
                    SpeechOO.logger = Logger.getLogger(CentralRegistrationClass.class.getName());
                    SpeechOO.logger.error(ex);
                } catch (NoSuchMethodException ex) {
                    SpeechOO.logger = Logger.getLogger(CentralRegistrationClass.class.getName());
                    SpeechOO.logger.error(ex);
                } catch (IllegalArgumentException ex) {
                    SpeechOO.logger = Logger.getLogger(CentralRegistrationClass.class.getName());
                    SpeechOO.logger.error(ex);
                } catch (InvocationTargetException ex) {
                    SpeechOO.logger = Logger.getLogger(CentralRegistrationClass.class.getName());
                    SpeechOO.logger.error(ex);
                } catch (IllegalAccessException ex) {
                    SpeechOO.logger = Logger.getLogger(CentralRegistrationClass.class.getName());
                    SpeechOO.logger.error(ex);
                }
            }
        }
        return bResult;
    }

    private static String getRegistrationClasses() {
        CentralRegistrationClass c = new CentralRegistrationClass();
        String name = c.getClass().getCanonicalName().replace('.', '/').concat(".class");
        try {
            Enumeration<URL> urlEnum = c.getClass().getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (urlEnum.hasMoreElements()) {
                URL url = urlEnum.nextElement();
                String file = url.getFile();
                JarURLConnection jarConnection =
                    (JarURLConnection) url.openConnection();
                Manifest mf = jarConnection.getManifest();

                Attributes attrs = (Attributes) mf.getAttributes(name);
                if ( attrs != null ) {
                    String classes = attrs.getValue( "RegistrationClasses" );
                    return classes;
                }
            }
        } catch (IOException ex) {
            SpeechOO.logger.error(ex);
        }
            
        return "";
    }
    
    /** Creates a new instance of CentralRegistrationClass */
    private CentralRegistrationClass() {
    }
}
