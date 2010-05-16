/**********************************************************
 * RecogResult.cpp
 *
 *  Created on: Aug 26, 2009 9:07:33 AM
 *      Author: Pedro Batista
 *      Federal University of Pará
 +********************************************************/

#include <RecoResult.h>

namespace LaPSAPI {

RecoResult::RecoResult() {
}

RecoResult::RecoResult(string uterrance, float confidence) {
	this->uterrance = uterrance;
	this->confidence = confidence;
}

string RecoResult::getUterrance() {
	return this->uterrance;
}

float RecoResult::getConfidence() {
	return this->confidence;
}
}
