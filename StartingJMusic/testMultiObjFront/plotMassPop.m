clear all
close all
massPop = load('901F_0.dat');

plot(massPop(:,1),massPop(:,2),'r.')
title('10000 Indivíduos Aleatórios')
xlabel ('Fux')
ylabel ('ZipfEQM')
axis([-17 0 -17 0])
