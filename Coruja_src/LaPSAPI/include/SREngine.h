/*
 * SREngine.h
 *
 *  Created on: May 15, 2010
 *      Author: pedro
 */

#ifndef SRENGINE_H_
#define SRENGINE_H_

#include <iostream>
using namespace std;

extern "C"{
#include <julius/julius.h>
#include <pthread.h>
}

#include <RecoResult.h>

namespace LaPSAPI{
class SREngine{
	friend void onRecognized(Recog *recog, void *data);
	friend void *startRecognitionLoop(void *engine);
	friend void pauseRecognition(Recog *recog, void *engine);
public:
	SREngine();
	SREngine(char *jconfFilePath);
	~SREngine();
	void startRecognition();
	void stopRecognition();
	void setOnRecognizedAction(void (*action)(RecoResult *recogResult));
private:
	void onRecognized();
	void (*actionRecognized)(RecoResult *recogResult);
	Recog *recog;
	Jconf *jconf;

	void initThreadTypes();
	pthread_mutex_t recognitionPausedMutex;
	pthread_cond_t recognitionPausedCondition;
	void recognitionLoop();
	pthread_t recognitionThread;
	bool recognitionStarted;
};
}


#endif /* SRENGINE_H_ */
