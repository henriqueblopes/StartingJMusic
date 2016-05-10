close all
clear all
figure(1)

%panel1 = uipanel('Parent',;
%panel2 = uipanel('Parent',panel1);
%set(panel1,'Position',[0 0 0.95 1]);
%set(panel2,'Position',[0 0 1 2]);
%set(gca,'Parent',panel2);
%s = uicontrol('Style','Slider','Parent',1,...
%  'Units','normalized','Position',[0.95 0 0.05 1],...
%  'Value',1,'Callback',{@slider_callback1,panel2});

zipfs = load('zipfFitnessErrorFitmutateMelodicAndRhythmTrigramM_2016_5_5_17_34_54.mid.dat');
zipfs2 = load('zipfFitnessErrorFitmutateNormalOrRankRankedMelodicAndRhythmTrigramM_2016_5_5_17_40_39.mid.dat');
zipfsHeyJude = load('heyJudePM2016_4_22_11_16_4.dat');

[z(1) z(2)] = plotZipfLines(zipfs, 1);
[z2(1) z2(2)] =plotZipfLines(zipfs2, 2);
[zhj(1) zhj(2)] =plotZipfLines(zipfsHeyJude, 3);
features = exp(zipfsHeyJude);