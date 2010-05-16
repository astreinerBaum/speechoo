/*
 * main.cpp
 *
 *  Created on: May 16, 2010
 *      Author: pedro
 */

#include <SREngine.h>
using namespace LaPSAPI;

#include <iostream>
using namespace std;

void handleResults(RecoResult *recoResult) {
	cout << recoResult->getConfidence() << " | " << recoResult->getUterrance()
			<< endl;
}

int main() {
	SREngine *engine = new SREngine("./LaPSAM1.3/ppt_com.jconf");
	engine->setOnRecognizedAction(handleResults);
	engine->startRecognition();
	cout << "ok reconhecendo" << endl;
	char d;
	while (d = getchar()) {
		if (d == 'p') {
			engine->stopRecognition();
			cout << "ok pausando" << endl;
		} else if (d == 'q') {
			cout << "ok saindo" << endl;
			break;
		} else if (d == 's') {
			engine->startRecognition();
			cout << "ok reconhecendo" << endl;
		}
	}
}
