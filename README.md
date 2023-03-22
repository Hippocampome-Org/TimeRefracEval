# TimeRefracEval

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
