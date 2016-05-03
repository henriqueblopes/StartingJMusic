zipfs = load('testeGraf2016_4_19_14_52_58.dat')
plot(log([1:129]),zipfs(1,:),'*')
i = 1;
while zipfs(1,i) ~= 0
    X(i) = log(i);
    Y(i) = zipfs(1,i);
end
siz = size(X);
[a,b] = quadradosMinimos(X,Y,siz(1,2));
plot(X,Y,'*');
hold
plot([1:siz(1,2)],a + b*[1:siz(1,2)])