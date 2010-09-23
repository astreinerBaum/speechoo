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

#include <stdio.h>
#include <jni.h>
#include <org_speechoo_coruja_CorujaJNI.h>
#include <iostream>
#include <fstream>
#include <string>
#include <string.h>
#include <SREngine.h>
#include <RecoResult.h>
#include <cstring>
using std::string;

using namespace LaPSAPI;
using namespace std;

static JavaVM *g_jVM = NULL;
static jclass g_jClazz = NULL;
static jmethodID g_jMID = NULL;
//static bool KEEP_READING = false;
static const char *rootDir = NULL;
//static jobject j_class_loader = NULL;
static SREngine* en;

void recognized(RecoResult *result){

	std::cout<<"\n %%%\t recognized, will get JNI" <<std::endl;
	JNIEnv *jEnv = NULL;
	g_jVM->AttachCurrentThread((void **)(&jEnv), NULL);

	jstring str = jEnv->NewStringUTF (result->getUterrance().c_str());
	std::cout<<"\n %%%\t string =" << str <<std::endl;
	jEnv->CallStaticVoidMethod(g_jClazz, g_jMID, str);

	g_jVM->DetachCurrentThread();

}


JNIEXPORT void JNICALL JNICALL Java_org_speechoo_coruja_CorujaJNI_startSREngine
	(JNIEnv *env, jobject obj, jstring cfgFile)
	{
		jboolean iscopy;
		rootDir = env->GetStringUTFChars(cfgFile, &iscopy);

		char *config = new char[strlen(rootDir) + 1];
		std::strcpy ( config, rootDir );
		cout << ">>> will start SREngine " << config << endl;
		en = new SREngine(config);

		en->setOnRecognizedAction(&recognized);
	}

JNIEXPORT void JNICALL JNICALL Java_org_speechoo_coruja_CorujaJNI_stopSREngine
	(JNIEnv *env, jobject obj)
	{
		en->~SREngine();
		rootDir = NULL;
		en = NULL;
	}

JNIEXPORT void JNICALL Java_org_speechoo_coruja_CorujaJNI_enableDictation
	(JNIEnv *env, jobject obj, jboolean enable)
	{
		if (enable) {
			cout << ">>> Java_Callbacks_enableDictation" << endl; 

			env->GetJavaVM(&g_jVM);
			g_jClazz = env->GetObjectClass(obj);
			g_jMID = env->GetStaticMethodID(g_jClazz, "newSentence", "(Ljava/lang/String;)V");

			cout << "will start recognition" << endl;
			en->startRecognition();
			cout << "recognition started" << endl;

		} else {
			cout << "will stop recognition" << endl;
			en->stopRecognition();
			cout << "recognition stopped" << endl;
		}
		cout << "<<< Java_Callbacks_enableDictation" << endl;
		return;
	}


