/*
 * SREngine.cpp
 *
 *  Created on: May 15, 2010
 *      Author: pedro
 */

#include <SREngine.h>

namespace LaPSAPI {

void onRecognized(Recog *recog, void *data) {
	SREngine *sr;
	sr = (SREngine*) data;
	sr->onRecognized();
}

void *startRecognitionLoop(void *engine) {
	SREngine *en = (SREngine*) engine;
	en->recognitionLoop();
}

void pauseRecognition(Recog *recog, void *engine) {
	SREngine *en = (SREngine*) engine;
	pthread_mutex_lock(&en->recognitionPausedMutex);
	pthread_cond_wait(&en->recognitionPausedCondition,
			&en->recognitionPausedMutex);
	pthread_mutex_unlock(&en->recognitionPausedMutex);
}

SREngine::SREngine() {
	//Just allocate memory for them
	recog = j_recog_new();
	jconf = j_jconf_new();
	actionRecognized = NULL;
	recognitionStarted = false;
	initThreadTypes();
}

SREngine::SREngine(char *jconfFilePath) {
	jlog_set_output(fopen("Juliuslog", "w"));
	//Load configurations file
	jconf = j_config_load_file_new(jconfFilePath);
	if (jconf == NULL) {
		printf("Error cannot load jconf\n");
		//throw gcnew System::ApplicationException("Error cannot load jconf");
		return;
	}
	//Force to use mic (jconf file do not will care)
	jconf->input.type = INPUT_WAVEFORM;
	jconf->input.speech_input = SP_MIC;
	jconf->input.device = SP_INPUT_DEFAULT;
	jconf->decodeopt.realtime_flag = TRUE;

	recog = j_create_instance_from_jconf(jconf);
	if (recog == NULL) {
		cout << "Error can not create an instance" << endl;
	}
	/*
	 * Pass the function that will be executed when something is reconized
	 * and is passed one pointer to this class to know how recognizer is
	 * executing for more information look startOnRecnized function
	 */
	callback_add(recog, CALLBACK_RESULT, LaPSAPI::onRecognized, this);
	callback_add(recog, CALLBACK_PAUSE_FUNCTION, LaPSAPI::pauseRecognition,
			this);
	actionRecognized = NULL;
	recognitionStarted = false;
	initThreadTypes();
}

SREngine::~SREngine() {
	//Free memory of recog including jconf
	j_recog_free(recog);
}

void SREngine::startRecognition() {
	if (!recognitionStarted) {
		recognitionStarted = true;
		pthread_create(&recognitionThread, NULL, startRecognitionLoop, this);
	} else {
		j_request_resume(recog);
		pthread_mutex_lock(&recognitionPausedMutex);
		pthread_cond_signal(&recognitionPausedCondition);
		pthread_mutex_unlock(&recognitionPausedMutex);
	}
}

void SREngine::recognitionLoop() {
	int ret;
	if (j_adin_init(recog) == FALSE) { /* error */
		return;
	}
	/* output system information to log */
	j_recog_info(recog);

	/***********************************/
	/* Open input stream and recognize */
	/***********************************/

	/* raw speech input (microphone etc.) */

	switch (j_open_stream(recog, NULL)) {
	case 0: /* succeeded */
		break;
	case -1: /* error */
		fprintf(stderr, "error in input stream\n");
		return;
	case -2: /* end of recognition process */
		fprintf(stderr, "failed to begin input stream\n");
		return;
	}

	/**********************/
	/* Recognization Loop */
	/**********************/
	/* enter main loop to recognize the input stream */
	/* finish after whole input has been processed and input reaches end */
	ret = j_recognize_stream(recog);
	if (ret == -1)
		return; /* error */

	/*******/
	/* End */
	/*******/

	/* calling j_close_stream(recog) at any time will terminate
	 recognition and exit j_recognize_stream() */
	j_close_stream(recog);
}

void SREngine::stopRecognition() {
	j_request_pause(recog);
}

void SREngine::setOnRecognizedAction(void(*action)(RecoResult *recoResult)) {
	this->actionRecognized = action;
}

void SREngine::onRecognized() {
	WORD_INFO *winfo;
	WORD_ID *seq;
	int seqnum;
	int n;
	Sentence *s;
	RecogProcess *r;
	string uterrance;
	/* all recognition results are stored at each recognition process
	 instance */
	for (r = recog->process_list; r; r = r->next) {

		/* skip the process if the process is not alive */
		if (!r->live)
			continue;

		/* result are in r->result.  See recog.h for details */

		/* check result status */
		if (r->result.status < 0) { /* no results obtained */
			/* outout message according to the status code */
			switch (r->result.status) {
			case J_RESULT_STATUS_REJECT_POWER:
				printf("<input rejected by power>\n");
				break;
			case J_RESULT_STATUS_TERMINATE:
				printf("<input teminated by request>\n");
				break;
			case J_RESULT_STATUS_ONLY_SILENCE:
				printf("<input rejected by decoder (silence input result)>\n");
				break;
			case J_RESULT_STATUS_REJECT_GMM:
				printf("<input rejected by GMM>\n");
				break;
			case J_RESULT_STATUS_REJECT_SHORT:
				printf("<input rejected by short input>\n");
				break;
			case J_RESULT_STATUS_FAIL:
				printf("<search failed>\n");
				break;
			}
			/* continue to next process instance */
			continue;
		}

		/* output results for all the obtained sentences */
		winfo = r->lm->winfo;

		for (n = 0; n < r->result.sentnum; n++) { /* for all sentences */
			s = &(r->result.sent[n]);
			seq = s->word;
			seqnum = s->word_num;
			uterrance = "";

			/* output word sequence like Julius */
			for (int i = 0; i < seqnum; i++) {
				uterrance += " ";
				uterrance += winfo->woutput[seq[i]];
			}
			float confidence = 0;
			for (int i = 1; i < seqnum - 1; i++) {
				confidence += s->confidence[i];
			}
			confidence /= ((float) seqnum - 2);
			//printf(" %s", winfo->woutput[seq[i]]);
			if (actionRecognized != NULL)
				actionRecognized(new RecoResult(uterrance, confidence));
		}
	}
}

void SREngine::initThreadTypes() {
	pthread_mutex_init(&recognitionPausedMutex, NULL);
	pthread_cond_init(&recognitionPausedCondition, NULL);
}

}
