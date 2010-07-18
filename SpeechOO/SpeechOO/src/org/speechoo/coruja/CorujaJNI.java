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

/**
 * Java wrapper of Coruja.
 *
 * @author William Colen
 */
public class CorujaJNI extends Observable {

    private volatile static CorujaJNI instance;

    private static final String LIB_NAME = "CorujaJNI";

    private boolean started = false;

    private CorujaJNI() {
        // prevents instantiation
    }

    public static void init(String oxtRoot) {
        synchronized (CorujaJNI.class) {
            if (instance == null) {
                System.load(oxtRoot + "/" + libName());
                instance = new CorujaJNI();
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

    public native void enableDictation(boolean enableDictation);

    private native void startSREngine(String config);

    private native void stopSREngine();

    public void dictation(boolean enableDictation) {
        enableDictation(enableDictation);
    }

    public void startSpeechRecognitionEngine(String config) {
        synchronized(this) {
            if(started == false) {
                this.startSREngine(config);
                started = true;
            }
        }
    }

    public void stopSpeechRecognitionEngine() {
        synchronized(this) {
            if(started == true) {
                this.stopSREngine();
                started = false;
            }
        }
    }

    private static void newSentence(String uterrance) {
        instance.setChanged();
        instance.notifyObservers(uterrance);
    }

     /**
     * Calculate the filename of the native hunspell lib.
     * The files have completely different names to allow them to live
     * in the same directory and avoid confusion.
     */
    private static String libName() throws UnsupportedOperationException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("windows")) {
            return libNameBare() + ".dll";

        } else if (os.startsWith("mac os x")) {
            //return "lib"+libNameBare()+".dylib";
            throw new UnsupportedOperationException("mac os x is unsupported");

        } else {
            return "lib" + libNameBare() + ".so";
        }
    }

    public static String libNameBare() throws UnsupportedOperationException {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();

        // Annoying that Java doesn't have consistent names for the arch types:
        boolean x86 = arch.equals("x86") || arch.equals("i386") || arch.equals("i686");
        boolean amd64 = arch.equals("x86_64") || arch.equals("amd64") || arch.equals("ia64n");

        if (os.startsWith("windows")) {
            if (x86) {
                return "hunspell-win-x86-32";
            }
            //if (amd64) {
            // Note: No bindings exist for this yet (no JNA support).
            //	return "hunspell-win-x86-64";
            //}

        } else if (os.startsWith("mac os x")) {
            if (x86) {
                return LIB_NAME + "-darwin-x86-32";
            }
            if (arch.equals("ppc")) {
                return LIB_NAME + "-darwin-ppc-32";
            }

        } else if (os.startsWith("linux")) {
            if (x86) {
                return LIB_NAME + "-linux-x86-32";
            }
            if (amd64) {
                return LIB_NAME + "-linux-x86-64";
            }

        } else if (os.startsWith("sunos")) {
            //if (arch.equals("sparc")) {
            //	return LIB_NAME + "-sunos-sparc-64";
            //}
        }

        throw new UnsupportedOperationException("Unknown OS/arch: " + os + "/" + arch);
    }

    @Override
    protected void finalize() throws Throwable {
    	this.stopSpeechRecognitionEngine();
    	super.finalize();
    }

}
