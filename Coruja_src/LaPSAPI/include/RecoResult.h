/**********************************************************
 * RecogResult.h
 *
 *  Created on: Aug 26, 2009 9:00:13 AM
 *      Author: Pedro Batista
 *      Federal University of Pará
 +********************************************************/

#ifndef RECOGRESULT_H_
#define RECOGRESULT_H_

#include <iostream>
#include <string>
using namespace std;
using std::string;

namespace LaPSAPI {

class RecoResult {
public:
	RecoResult();
	RecoResult(string uterrance, float confidence);
	string getUterrance();
	float getConfidence();
private:
	string uterrance;
	float confidence;
};
}

#endif /* RECOGRESULT_H_ */
