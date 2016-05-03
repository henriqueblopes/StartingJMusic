function e = erroQuadradosMinimos(a,b,X,Y,tam)
    e = 0;
    for i=1:tam
        ep = (Y(i) - a -b*X(i))^2;
        e = e + ep;
    end
    e = e/(tam);
end