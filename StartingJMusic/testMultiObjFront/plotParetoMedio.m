close all
clear all

%convMean = load('0F_900ParetoCombined_3_p300_c0.9_m0.3_M_2016_12_6_1_56_31.mid.dat')
convMean = load('0F_900ParetoCombined_3_p100_c0.9_m0.3_M_2016_12_6_23_59_4.mid.dat')
plot(convMean(:,1), convMean(:,2), 'x')