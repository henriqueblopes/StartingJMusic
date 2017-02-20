close all
clear all

convMeanIP = load('0F_900InitPopParetoCombined_33_p300_c0.7_m0.3_M_2016_12_10_9_39_17.mid.dat')
convMeanPC = load('0F_900ParetoCombined_33_p300_c0.7_m0.3_M_2016_12_9_3_36_4.mid.dat')
bestZipf = load('2F_1_zipf33_p300_c0.9_m0.1.dat')
bestFux = load('1F_1_fux33_p300_c0.9_m0.1.dat')
plot(convMeanIP(:,1), convMeanIP(:,2), 'rx')
hold
axis([-12 0 -7.5 0])
plot(convMeanPC(:,1), convMeanPC(:,2), 'b*')
plot(bestZipf(:,1), bestZipf(:,2), 'g+')
plot(bestFux(:,1), bestFux(:,2), 'k+')
xlabel ('Fux')
ylabel ('ZipfEQM')
legend('Method 4', 'Method 3', 'Method 2', 'Method 1')
legend('location','southwest')
title('Pareto Combined of Methods 3 and 4 from 33 runs')
