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
                System.load(oxtRoot + "/libCorujaJNI.so");
                instance = new CorujaJNI();
                instance.setRootDirectory("julian.jconf");

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
        System.out.println("java: new sentence - " + uterrance);
    }

    /**
     * To try the Java C++ bind.
     * 
     * @param args
     *          0 - The root execution path. Inside this path we should have
     *              the lib/libCorujaJNI.so file
     */
    public static void main(String args[]) {
        try {

	        CorujaJNI.init("/home/colen/wrk/coruja/JNITest");
            CorujaJNI c = getSingleton();    
            System.out.println("java: will start dictation for 30 seconds");
            c.enableDictation(true);
            Thread.sleep(30000);
            System.out.println("java: stop dictation for 30s");
            c.enableDictation(false);
            Thread.sleep(30000);
            System.out.println("java: will start dictation for 30 seconds");
            c.enableDictation(true);
            Thread.sleep(30000);
            System.out.println("java: stop dictation");
            c.enableDictation(false);
        } catch (Exception ie) {
            ie.printStackTrace();
        }

    }
}
