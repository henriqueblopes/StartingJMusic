close all
clear all
%convMean = load('convergence_mean_2016_12_5_15_39_29.dat');

%convMean = load('convergence_fuxFitness_33_p300_c0.9_m0.32016_12_7_5_58_39.dat');
convMean = load('convergence_fuxFitness_33_p300_c0.7_m0.32016_12_10_5_43_10.dat');
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
convMean2 = load('convergence_zipfFitnessErrorFit_33_p300_c0.7_m0.32016_12_10_9_28_13.dat')
plot(convMean2(:,1))
hold
plot(convMean2(:,2))
axis([0 1000 -12 0])
title('Convergência otimizada por ZipfEQM')
xlabel ('Gerações')
ylabel ('Fitness')
text(size(convMean2,1)/2, convMean2(size(convMean2,1),1)+0.1, 'Fux');
text(size(convMean2,1)/2, convMean2(size(convMean2,1),2)+0.1, 'ZipfEQM');
