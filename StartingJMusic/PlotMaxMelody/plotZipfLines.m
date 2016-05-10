function [fitB, fitE] = plotZipfLines(zipfs, column)
    metrics = {'pitch', 'pitchDist', 'pitchDur', 'chroPitchDis', 'chroPitchDur', 'chroPitch', 'melodicInter', 'melodicBigram', 'melodicTrigram', 'duration', 'rhytym', 'rhytymInterval', 'rhytymBigram', 'rhytymTrigram'};
    %metrics = {'duration', 'rhytym', 'rhytymInterval', 'rhytymBigram', 'rhytymTrigram'};
    i = 1;
    
    fitB = 0;
    fitE = 0;
    for j=1:14
        
        if rem(j-1,3) == 0
            figure(j)
            
            
        end
        X = zeros(1,1);
        Y = zeros(1,1);
        while zipfs(j,i) ~= -1
            X(i) = log(i);
            Y(i) = zipfs(j,i);
            i = i+1;
        end
        i = 1;
        siz = size(X);
        if siz(1,2) >1
            %[b,a] = quadradosMinimos(X,Y,siz(1,2));
            %e = erroQuadradosMinimos(a,b,X,Y,siz(1,2));
            mdl = fitlm(X,Y);

            [b,a] = retaMenos1Minima(X,Y,siz(1,2));
            e = erroQuadradosMinimos(a,b,X,Y,siz(1,2));
            
            fitB = fitB + exp((-(-1.0-mdl.Coefficients.Estimate(2,1))^2)/0.5);
            fitE = fitE + e;

            subplot(3,3,(rem(j-1,3))*3 +column);
            plot(X,Y,'*');
            hold on
            plot(X,mdl.Coefficients.Estimate(1,1) + mdl.Coefficients.Estimate(2,1)*X, 'r')
            plot(X,a + b*X, 'g')
            title({strcat(metrics{1,j}, ':', 'b=',num2str(mdl.Coefficients.Estimate(2,1)),' a=', num2str(mdl.Coefficients.Estimate(1,1)), ' mse=', num2str(mdl.MSE), ' r=',num2str(mdl.Rsquared.Adjusted)); 
                strcat(' mse-1=', num2str(e), ' a-1=', num2str(a))})
            %title(strcat(metrics{1,j}, ':', 'b=',num2str(b),' a=', num2str(a), ' e=', num2str(e)));
            axis([0.0, 5.0, 0.0, 7]);
        end
    end
end