clear all
close all
i = 1;
A = [0 25 50 75 100 300]
symMat = {'rx','gx','yx','bx'}


for j = 6:6
    %subplot(1,6,j);
    hold
    while  exist(strcat(num2str(A(j)),'F_', num2str(i), '.dat'), 'file') == 2
        f = load(strcat(num2str(A(j)),'F_', num2str(i), '.dat'))
        symMat(rem(i,4)+1)
        if i<= 4
            if rem(i,4) == 1
                plot(f(:,1),f(:,2),'gx')
            end
            if rem(i,4) == 2
                plot(f(:,1),f(:,2),'rx')
            end
            if rem(i,4) == 3
                plot(f(:,1),f(:,2),'yx')
            end
            if rem(i,4) == 0
                plot(f(:,1),f(:,2),'kx')
            end
        end
        i = i +1;
        
    end
    i = 1;
end

g = load('991F_1.dat')
 plot(g(:,1),g(:,2),'ro')
 g = load('992F_1.dat')
 plot(g(:,1),g(:,2),'r*')

