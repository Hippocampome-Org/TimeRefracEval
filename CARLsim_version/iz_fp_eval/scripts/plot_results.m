% analyze voltage from a neuron using IZ 9-param model

function []=plot_results(volt_file, output_desc)
	initOAT;
	save_plot=1;
	neur_read = NeuronReader(volt_file);
	voltages = neur_read.readValues();
	t_start = 1; % start time
	t_end = 1100; % end time
	t_range = linspace(t_start,t_end,(t_end-t_start+1));
	selected_volt = voltages.v(1,t_start:t_end);
	% plot
	plot(t_range, selected_volt,'Color','#80B3FF','LineWidth',1.5);
	if save_plot
	    output_filename = sprintf("voltage_%s.png",output_desc);
	    exportgraphics(gcf,output_filename,'Resolution',300)
	end
end