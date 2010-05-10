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

package org.speechoo.coruja;

import java.util.Observable;
import java.io.File;

public class CorujaJNI extends Observable {

    private volatile static CorujaJNI instance;

    private CorujaJNI() {
        // prevents instantiation
    }

    public static void init(String oxtRoot) {
        synchronized (CorujaJNI.class) {
            if (instance == null) {
                System.load(oxtRoot + "/lib/libCorujaJNI.so");
                instance = new CorujaJNI();
		instance.setRootDirectory(oxtRoot);

            } else {
                throw new RuntimeException("Already initialized");
            }
        }
    }

    public static CorujaJNI getSingleton() {
        if (instance == null) {
            throw new RuntimeException("Not initialized");
        }
        return instance;
    }

    private native void enableDictation(boolean enableDictation);

    private native void setRootDirectory(String rootDirectory);

    public void dictation(boolean enableDictation) {
        enableDictation(enableDictation);
    }

    private static void newSentence(String uterrance) {
        instance.setChanged();
        instance.notifyObservers(uterrance);
    }

    public static void main(String args[]) {
        try {
	    if(args != null && args.length > 0) {
		System.out.println(new File(args[0]).getCanonicalPath());
            	CorujaJNI.init(new File(args[0]).getCanonicalPath());
	    } else {
	        CorujaJNI.init("/home/colen/NetBeansProjects/SpeechOO/tmp/base/");
	    }
            CorujaJNI c = getSingleton();    
            System.out.println("vai chamar C++");
            c.enableDictation(true);
            System.out.println("retornou");
            Thread.sleep(3000);
            System.out.println("desliga");
            c.enableDictation(false);
            Thread.sleep(5000);
            c.enableDictation(true);
            Thread.sleep(5000);
        } catch (Exception ie) {
            ie.printStackTrace();
        }

    }
}
