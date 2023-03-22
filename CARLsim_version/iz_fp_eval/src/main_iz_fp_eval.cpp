/* * Copyright (c) 2016 Regents of the University of California. All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions
* are met:
*
* 1. Redistributions of source code must retain the above copyright
*    notice, this list of conditions and the following disclaimer.
*
* 2. Redistributions in binary form must reproduce the above copyright
*    notice, this list of conditions and the following disclaimer in the
*    documentation and/or other materials provided with the distribution.
*
* 3. The names of its contributors may not be used to endorse or promote
*    products derived from this software without specific prior written
*    permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
* "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
* LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
* A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
* CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
* EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
* PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
* LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
* NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
* *********************************************************************************************** *
* CARLsim
* created by: (MDR) Micah Richert, (JN) Jayram M. Nageswaran
* maintained by:
* (MA) Mike Avery <averym@uci.edu>
* (MB) Michael Beyeler <mbeyeler@uci.edu>,
* (KDC) Kristofor Carlson <kdcarlso@uci.edu>
* (TSC) Ting-Shuo Chou <tingshuc@uci.edu>
* (HK) Hirak J Kashyap <kashyaph@uci.edu>
*
* CARLsim v1.0: JM, MDR
* CARLsim v2.0/v2.1/v2.2: JM, MDR, MA, MB, KDC
* CARLsim3: MB, KDC, TSC
* CARLsim4: TSC, HK
* CARLsim5: HK, JX, KC
* CARLsim6: LN, JX, KC, KW
*
* CARLsim available from http://socsci.uci.edu/~jkrichma/CARLsim/
* Ver 12/31/2016
*/

// include CARLsim user interface
#include <carlsim.h>

int main(int argc, char *argv[]) {
	if (argc != 10) {
		printf("\nUsage: iz_fp_eval <9_IM_params>\n");
		printf("%d arguments found.\n",argc);
	}
	else {
		// ---------------- CONFIG STATE -------------------
		CARLsim sim("hello world", GPU_MODE, USER, 1, 42);
		int dummy = sim.createSpikeGeneratorGroup("dummy_connection", 1, EXCITATORY_NEURON, 0, GPU_CORES);
		int neuron=sim.createGroup("neuron", 1, EXCITATORY_NEURON, ANY, GPU_CORES);
		sim.setNeuronParameters(neuron, atof(argv[1]), 0.0f, atof(argv[2]), 
	    0.0f, atof(argv[3]), 0.0f, atof(argv[4]), 0.0f, atof(argv[5]), 0.0f, 
	    atof(argv[6]), 0.0f, atof(argv[7]), 0.0f, atof(argv[8]), 0.0f, 
	    atof(argv[9]), 0.0f, 0);
		sim.connect(dummy, neuron, "one-to-one", 0.0f, SYN_FIXED); // CARLsim seems to require at least one connection. This is a dummy connection with no signaling to the other neuron.
		sim.setNeuronMonitor(neuron,"DEFAULT");
		sim.setIntegrationMethod(RUNGE_KUTTA4, 1);//40);
		// ---------------- SETUP STATE -------------------
		sim.setupNetwork();
		SpikeMonitor* spk_mon = sim.setSpikeMonitor(neuron, "DEFAULT");
		// ---------------- RUN STATE -------------------
		spk_mon->startRecording();
		for (int i=0; i<100; i++) {
			if (i==1) {sim.setExternalCurrent(neuron, 200.0f);}
			if (i==10) {sim.setExternalCurrent(neuron, 0.0f);}
			sim.runNetwork(0,100, false);
		}
		spk_mon->stopRecording();
	}
	
	return 0;
}
