close all
clear all
convMean = load('convergence_fuxFitness_20_p300_c0.9_m0.32016_12_10_23_42_17.dat');
subplot(1,2,1)
plot(convMean(:,1))
hold
plot(convMean(:,2))
axis([0 4000 -12 0])
title('Convergência otimizada por Fux')
xlabel ('Gerações')
ylabel ('Fitness')
text(size(convMean,1)/2, convMean(size(convMean,1),1)+0.1, 'Fux');
text(size(convMean,1)/2, convMean(size(convMean,1),2)+0.1, 'ZipfEQM');

subplot(1,2,2)
convMean2 = load('convergence_zipfFitnessErrorFit_20_p300_c0.9_m0.32016_12_11_7_43_36.dat')
plot(convMean2(:,1))
hold
plot(convMean2(:,2))
axis([0 4000 -12 0])
title('Convergência otimizada por ZipfEQM')
xlabel ('Gerações')
ylabel ('Fitness')
text(size(convMean2,1)/2, convMean2(size(convMean2,1),1)+0.1, 'Fux');
text(size(convMean2,1)/2, convMean2(size(convMean2,1),2)+0.1, 'ZipfEQM');
