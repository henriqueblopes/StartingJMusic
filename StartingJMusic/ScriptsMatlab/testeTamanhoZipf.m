notas = 120
X = zeros(1,notas)+300
for i = 2:notas
    X(i) = floor(X(1)/i )
end
sum(X)
