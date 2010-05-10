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
#include <boost/thread.hpp>
#include <boost/thread/xtime.hpp>
#include <string.h>

using namespace std;

static JavaVM *g_jVM = NULL;
static jclass g_jClazz = NULL;
static jmethodID g_jMID = NULL;
static boost::thread *readerThread = NULL;
static bool KEEP_READING = false; 
static const char *rootDir = NULL;
static jobject j_class_loader = NULL;


void recognized(const char *string){

	JNIEnv *jEnv = NULL;
	g_jVM->AttachCurrentThread((void **)(&jEnv), NULL);

	/*if (jEnv == NULL) {
		std::cout <<"\n !!! Failed to attach current thread with JVM !!! \n";
		if (jEnv->ExceptionOccurred()) {
			jEnv->ExceptionDescribe();
		}
	} else 	{
		std::cout <<"\n %%%\t JNIEnv attached : " <<(void *)jEnv;

		if (jEnv->ExceptionOccurred()) {
			jEnv->ExceptionDescribe();
		}
		jclass jClazz = jEnv->FindClass("org/speechoo/coruja/CorujaJNI");
		if (jClazz == NULL)
		{
			if (jEnv->ExceptionOccurred()) {
				jEnv->ExceptionDescribe();
			}
			std::cout<<"\n %%%\t !!! FindClass returns NULL !!! \n";
			return; 
		} else {
			std::cout<<"\n %%%\t FindClass works well in new thread !!! ";
		}
 
		std::cout<<"\n %%%\t Try to get method id from class, clz =" <<jClazz;
 
		jmethodID jMID = jEnv->GetStaticMethodID(jClazz , "newSentence", "(Ljava/lang/String;)V");
		if (jMID == NULL)
		{
			if (jEnv->ExceptionOccurred()) {
				jEnv->ExceptionDescribe();
			}
			std::cout<<"\n %%%\t GetStaticMethodID returns NULL !!!\n";
			return;
		} else {
			std::cout<<"\n %%%\t Could get method !!! ";
		}
 
		std::cout<<"\n %%%\t Try to call back to Java, mid =" <<g_jMID <<std::endl;
		*/
		jstring str = jEnv->NewStringUTF(string);
		std::cout<<"\n %%%\t string =" << str <<std::endl;
		jEnv->CallStaticVoidMethod(g_jClazz, g_jMID, str);
	
		g_jVM->DetachCurrentThread();
 	
	//}
}

void recognitionThread() 
{
	//cout << ">>> readFile: sleep" << endl; 

	sleep(1);

	int count = 0;

	char line[128];
	char number[4];
	while(true && KEEP_READING) {
		sleep(1);
		strcpy(line, "Sentence number ");
		std::cout<<" %%%\t Line =" << line <<std::endl;
		sprintf(number,"%d",count++);
		std::cout<<" %%%\t number =" << number <<std::endl;
		strcat(line, number);
		std::cout<<" %%%\t line =" << line <<std::endl;
		recognized(line);
	}
}

JNIEXPORT void JNICALL Java_org_speechoo_coruja_CorujaJNI_setRootDirectory
	(JNIEnv *env, jobject obj, jstring rootDirectory)
	{
		jboolean iscopy;
		rootDir = env->GetStringUTFChars(rootDirectory, &iscopy);
	}

JNIEXPORT void JNICALL Java_org_speechoo_coruja_CorujaJNI_enableDictation
	(JNIEnv *env, jobject obj, jboolean enable)
	{
		if (enable) {
			cout << ">>> Java_Callbacks_enableDictation" << endl; 

			env->GetJavaVM(&g_jVM);
			g_jClazz = env->GetObjectClass(obj);
			g_jMID = env->GetStaticMethodID(g_jClazz, "newSentence", "(Ljava/lang/String;)V");

			boost::thread reader(&recognitionThread);
			readerThread = &reader;
			cout << "will start reader thread" << endl;
			//reader.join();
			boost::posix_time::time_duration very_short = boost::posix_time::milliseconds(1);
			KEEP_READING = true;
			readerThread->timed_join(very_short);
		
			cout << "reader thread started" << endl;
			cout << "<<< Java_Callbacks_enableDictation" << endl;
		} else {
		 	KEEP_READING = false;
		}
	
		return;
	}


