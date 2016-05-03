function [b,a] = retaMenos1Minima(x,y,tam)
    sx = 0;
    sy = 0;
    sxquad = 0;
    sxy = 0;
    count = 0;
    for i=1:tam
        if y(i) ~= -Inf
            sx = sx + x(i);
            sy = sy+ y(i);
            sxy = sxy + x(i)*y(i);
            sxquad = sxquad + x(i)^2;
        else
            count = count +1;
        end
    end
    b = -1;
    a = sy/tam -b*sx/tam;
end