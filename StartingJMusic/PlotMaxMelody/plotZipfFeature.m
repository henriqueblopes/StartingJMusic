close all
clear all
figure(1)
panel1 = uipanel('Parent',1);
panel2 = uipanel('Parent',panel1);
set(panel1,'Position',[0 0 0.95 1]);
set(panel2,'Position',[0 0 1 2]);
%set(panel1,'Position',[100 200 1300 800]);
%set(panel2,'Position',[200 400 1300 800]);
    
set(gca,'Parent',panel2);
s = uicontrol('Style','Slider','Parent',1,...
      'Units','normalized','Position',[0.95 0 0.05 1],...
      'Value',1,'Callback',{@slider_callback1,panel2});


zipfs = load('zipfFitnessM_2016_4_26_14_52_2.mid.dat')
zipfs2 = load('zipfFitnessErrorFitM_2016_4_26_14_58_19.mid.dat')
zipfsHeyJude = load('heyJudePM2016_4_22_11_16_4.dat')

[z(1) z(2)] = plotZipfLines(zipfs, 1)
[z2(1) z2(2)] =plotZipfLines(zipfs2, 2)
[zhj(1) zhj(2)] =plotZipfLines(zipfsHeyJude, 3)
features = exp(zipfsHeyJude);