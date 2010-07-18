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

/**
 * Post process the text
 *
 * @author William Colen
 */
public class PostProcessor {
    public String processUtterance(String utterance) {
        if(utterance != null && utterance.length() > 0) {
            char f = utterance.charAt(0);
            f = Character.toUpperCase(f);
            utterance = f + utterance.substring(1) + ". ";
        }

        return utterance;
    }
}
