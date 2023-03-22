# Izhikevich Model Refractory Period Evaluation Code

This is software used to evaluate code used to create a refractory period (RP) in the Izhikevich model (IM) in code from (Venkadesh, et al., 2019; EA). It contains a concise version of the same code used to simulate the IM in that software. Methods used in this software will be applied to create the RP in the EA software.

Known limitations:<br>
As a first version, this code is only designed to add the RP to single compartment neurons. It could be expanded on, e.g., with ArrayList of spike_detected, etc., to process the RP in each of multiple compartments.

Running software:<br>
Apache Commons Math libraries are required and found included in /lib/commons-math3-3.6/ in the EA software. Copying the files to the directory of this software is not needed, instead updateing the classpath in run.sh should be done. For example, in run.sh update the line:
```
$CLASSPATH:<path_to_library>/commons-math3-3.6/*
```
to where the library files are located, which can be the lib directory of the EA software. It appears the library version needed is 3.6.

JAVA is required to be installed. This should allow javac and java to be run in run.sh.

Note this software was tested to work on Ubuntu 21.10 with openjdk 11.0.15 (2022-04-19).

Use the command:
```
$ ./run.sh
```
to run the software in a command prompt.

Plotting:<br>
The file plot_voltage.m is included to plot the voltage in Matlab. After running the software the file neuron_voltage.csv is created. This is used in the plotting script to generate a voltage plot.

Setting parameters:<br>
In the .java file, set params listed under "Izhikevich parameters" and "Experiment parameters" to set parameters. The parameter "refrac_time" sets the amount of time the RP should occur.

Approach to implementing the refractory period:<br>
Apache Commons Math libraries process IM results by returning V and U derivatives through computeDerivatives(). Through testing it was found that setting V and U directly (reset during RP) in computeDerivatives() did not save those values for the next timestep because computeDerivatives() appears to only output the V and U derivatives (dy[0] and dy[1]) for use in the next timestep. Because of this, an RP is created for a specified time by following a spike reset event with enforcement of V and U derivatives being 0 for the specified RP time period. Given that the derivatives measure the rate of change of V and U, forcing the change to be 0 from the reset values for a specified time period allows V and U to be at the RP values for the time period.

Plotting the results with different parameters including RP time can help visually verify that the RP is causing the voltage changes that are expected.

Specific conditions are included to help with the derivative control occuring at the right times and not wrong times. "t<=timeMax" is included to allow the RP to end once current is no longer supplied. This causes V to be able to go below vMin after that time which matches expected voltage activity better. "V0<vPeak" prevents the derivatives being set to 0 when V is at vPeak instead of vMin. Without this, V could remain at vPeak for the RP time.

CARLsim version:<br>
The results from the Java-based IM code can be compared to those from the C++-based CARLsim code. The CARLsim code is in the directory CARLsim_version. It has been tested to work with CARLsim6. The software includes a Matlab plotting script that can be used to create comparison plot with the Java version. Navigate to the iz_fp_eval directory to find a readme file for running that version.