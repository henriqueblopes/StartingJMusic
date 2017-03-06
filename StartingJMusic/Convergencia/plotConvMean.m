close all
clear all
%convMean = load('convergence_mean_2016_12_5_15_39_29.dat');

convMean = load('C+Copy_convergence_fuxFitness_33_p300_c0.7_m0.32017_2_21_18_22_11.dat');
%convMean = load('C+convergence_fuxFitness_33_p300_c0.7_m0.32017_2_20_2_28_58.dat');
subplot(1,2,1)
plot(convMean(:,1))
hold
plot(convMean(:,2))
axis([0 1000 -12 0])
title('Convergência otimizada por Fux')
xlabel ('Gerações')
ylabel ('Fitness')
text(size(convMean,1)/2, convMean(size(convMean,1),1)+0.1, 'Fux');
text(size(convMean,1)/2, convMean(size(convMean,1),2)+0.1, 'ZipfEQM');

subplot(1,2,2)
convMean2 = load('C+Copy_convergence_zipfFitnessErrorFit_33_p300_c0.7_m0.32017_2_21_21_9_40.dat')
%convMean2 = load('C+convergence_zipfFitnessErrorFit_33_p300_c0.7_m0.32017_2_20_13_13_56.dat')
plot(convMean2(:,1))
hold
plot(convMean2(:,2))
axis([0 1000 -12 0])
title('Convergência otimizada por ZipfEQM')
xlabel ('Gerações')
ylabel ('Fitness')
text(size(convMean2,1)/2, convMean2(size(convMean2,1),1)+0.1, 'Fux');
text(size(convMean2,1)/2, convMean2(size(convMean2,1),2)+0.1, 'ZipfEQM');
