close all;
voltage_data=readmatrix("neuron_voltage.csv");
plot(voltage_data(:,1),voltage_data(:,2));