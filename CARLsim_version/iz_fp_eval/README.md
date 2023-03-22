# CARLsim Version of Izhikevich Model Refractory Period Evaluation Code

This code is designed for evaluating CARLsim's implementation of Izhikevich model (IM) refractory period (RP) code. This code has been tested with CARLsim6.

Usage:<br>
In CARLsim, create a new project with the name "iz_fp_eval". Copy the files in this folder into that project's folder. In the build folder for the project copy the file rebuild_stellate.sh into that. That is a Linux script that rebuilds the project and runs it with Stellate cell parameters.

The compiled CARLsim code must be run with 9 parameters, which represent 9 IM params. They are in the order: C,k,vr,vt,a,b,vpeak,c,d. Scripts to run the software can be saved with different neurons' IM parameters, e.g., a Stellate cell. Example parameters can be found on hippocampome.org/php/Izhikevich_model.php.

This software was designed to be used with the Hippocampome feature branch of CARLsim which includes a IM RP. This is availible at hippocampome.org/CARLsim.

The RP time period can be set by the last parameter in the setNeuronParameters() function. The value is the number of milliseconds the RP should occur for.

Plotting:<br>
A matlab plotting script is provided in /scripts/plot_results.m. It can be run in Matlab by running the script run_plot.m. The directory "build_dir" should be in the base CARLsim directory for this project. This directory should be a softlink to the folder that is the build directory for the project. This can be done in linux by "ln -s <build_dir> build_dir" where <build_dir> is the folder path. This softlinked directory is used in the plotting script to find the simulation results for plotting. An alternative would be to include the full directory path to the build directory in the plotting script instead of the softlink.

Plotting uses functions from CARLsim's Offline Analysis Toolkit (OAT). Users should update addpath() in initOAT.m to direct to where the files for OAT are located on each of their computers.