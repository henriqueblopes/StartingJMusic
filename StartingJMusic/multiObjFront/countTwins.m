count = 0;
antcount = 0;
b = size(a)
for i = 1:b(1,1)-1
    if a(i,1) == a(i+1,1)
       if a(i,2) == a(i+1,2)
           count = count + 1;
       else
           antcount = antcount + 1;
       end
    else
        antcount = antcount + 1;
    end
end
plot(a(:,1), a(:,2))
