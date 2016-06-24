package com.matteo.cc.sip.service;

interface ISip{
	int makeCall(String callee);
	int sendDtmf(char c);
	int getSipState();
	int acceptCall();
	int rejectCall();
	int hangupCall();
}