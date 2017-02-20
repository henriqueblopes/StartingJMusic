close all
clear all
%convMean = load('convergence_mean_2016_12_5_15_39_29.dat');

convMeanF1 = load('convergence_fuxFitness_33_p300_c0.9_m0.32016_12_7_5_58_39.dat');
convMeanF2 = load('convergence_fuxFitness_33_p300_c0.9_m0.12016_12_9_23_1_10.dat')
convMeanF3 = load('convergence_fuxFitness_33_p300_c0.7_m0.32016_12_10_5_43_10.dat');
convMeanF4 = load('convergence_fuxFitness_33_p300_c0.7_m0.12016_12_10_15_50_58.dat');
subplot(1,2,1)
plot(convMeanF1(:,1),'k')
hold
plot(convMeanF1(:,2),'k')
plot(convMeanF3(:,1),'g--')
plot(convMeanF3(:,2),'g--')
plot(convMeanF2(:,1),'r')
plot(convMeanF2(:,2),'r')
plot(convMeanF4(:,1),'b--')
plot(convMeanF4(:,2),'b--')
axis([0 1000 -12 0])
title('Convergence curve optimized by Fux')
xlabel ('Generations')
ylabel ('Fitness value')
legend('Method 1', 'Method 1', 'Method 2', 'Method 2','Method 3', 'Method 3','Method 4', 'Method 4')
legend('location','southwest')
text(size(convMeanF1,1)/2, convMeanF1(size(convMeanF1,1),1)+0.1, 'Fux');
text(size(convMeanF1,1)/2, convMeanF1(size(convMeanF1,1),2)+0.1, 'ZipfEQM');

subplot(1,2,2)
convMeanZ1 = load('convergence_zipfFitnessErrorFit_33_p300_c0.9_m0.32017_1_17_19_36_36.dat');
convMeanZ2 = load('convergence_zipfFitnessErrorFit_33_p300_c0.9_m0.12016_12_10_2_16_24.dat');
convMeanZ3 = load('convergence_zipfFitnessErrorFit_33_p300_c0.7_m0.32016_12_10_9_28_13.dat');
convMeanZ4 = load('convergence_zipfFitnessErrorFit_33_p300_c0.7_m0.12016_12_10_18_29_14.dat');

plot(convMeanZ1(:,1),'k')
hold
plot(convMeanZ1(:,2),'k')
plot(convMeanZ3(:,1),'g--')
plot(convMeanZ3(:,2),'g--')
plot(convMeanZ2(:,1),'r')
plot(convMeanZ2(:,2),'r')
plot(convMeanZ4(:,1),'b--')
plot(convMeanZ4(:,2),'b--')


axis([0 1000 -12 0])
title('Convergence curve optimizad by ZipfEQM')
xlabel ('Generations')
ylabel ('Fitness value')
legend('Method 1', 'Method 1', 'Method 2', 'Method 2','Method 3', 'Method 3','Method 4', 'Method 4')
legend('location','southwest')
text(size(convMeanZ1,1)/2, convMeanZ1(size(convMeanZ1,1),1)+0.1, 'Fux');
text(size(convMeanZ1,1)/2, convMeanZ1(size(convMeanZ1,1),2)+0.1, 'ZipfEQM');
