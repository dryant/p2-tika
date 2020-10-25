#0.- Establece el titulo de la tabla y el nombre de los ejes vertical y horizontal

set title 'Terminos'
set xlabel 'Ranking'
set ylabel 'Frecuencia'

#1.- Gráficos de cada libro sin suavizar. Copiar y pegar en GNUPLOT cada linea de forma independiente

plot 'data/PremiereLivre-ms.data' using 1:3 title 'fr' with lines
plot 'data/dracula-ms.data' using 1:3 title 'en' with lines
plot 'data/ColomboDescobrimentos-ms.data' using 1:3 title 'pt' with lines
plot 'data/jokaimor-ms.data' using 1:3 title 'hu' with lines
plot 'data/lucrezia_borgia-ms.data' using 1:3 title 'it' with lines
plot 'data/DonJuanTenorio-ms.data' using 1:3 title 'en' with lines
plot 'data/GutenbergWeb-ms.data' using 1:3 title 'hu' with lines
plot 'data/JapanTale-ms.data' using 1:3 title 'en' with lines

#2.- Dibuja la grafica suavizada por cada libro. 
#Copiar y pegar en GNUPLOT cada línea de forma individual

plot 'data/PremiereLivre-ms.data' using (log($1)):(log($3)) title 'fr' with lines
plot 'data/dracula-ms.data' using (log($1)):(log($3)) title 'en' with lines
plot 'data/ColomboDescobrimentos-ms.data' using (log($1)):(log($3)) title 'pt' with lines
plot 'data/jokaimor-ms.data' using (log($1)):(log($3)) title 'hu' with lines
plot 'data/lucrezia_borgia-ms.data' using (log($1)):(log($3)) title 'it' with lines
plot 'data/DonJuanTenorio-ms.data' using (log($1)):(log($3)) title 'en' with lines
plot 'data/GutenbergWeb-ms.data' using (log($1)):(log($3)) title 'hu' with lines
plot 'data/JapanTale-ms.data' using (log($1)):(log($3)) title 'en' with lines

#3.- Creamos la funcion f(x).

f(x) = log (k) -m*x

#4.- Creamos la tabla suavizada

fit f(x) 'data/PremiereLivre-ms.data' using (log($1)):(log($3)) via k,m
fit f(x) 'data/dracula-ms.data' using (log($1)):(log($3)) via k,m
fit f(x) 'data/ColomboDescobrimentos-ms.data' using (log($1)):(log($3)) via k,m
fit f(x) 'data/jokaimor-ms.data' using (log($1)):(log($3)) via k,m
fit f(x) 'data/lucrezia_borgia-ms.data' using (log($1)):(log($3)) via k,m
fit f(x) 'data/DonJuanTenorio-ms.data' using (log($1)):(log($3)) via k,m
fit f(x) 'data/GutenbergWeb-ms.data' using (log($1)):(log($3)) via k,m
fit f(x) 'data/JapanTale-ms.data' using (log($1)):(log($3)) via k,m

#5.- Dibujamos en un solo gráfico todas las gráficas y el ajuste lineal

plot 'data/PremiereLivre-ms.data' using (log($1)):(log($3)) title 'fr' with lines, \
 'data/dracula-ms.data' using (log($1)):(log($3)) title 'en' with lines, \
 'data/ColomboDescobrimentos-ms.data' using (log($1)):(log($3)) title 'pt' with lines, \
 'data/jokaimor-ms.data' using (log($1)):(log($3)) title 'hu' with lines, \
 'data/lucrezia_borgia-ms.data' using (log($1)):(log($3)) title 'it' with lines, \
 'data/DonJuanTenorio-ms.data' using (log($1)):(log($3)) title 'en' with lines, \
 'data/GutenbergWeb-ms.data' using (log($1)):(log($3)) title 'hu' with lines, \
 'data/JapanTale-ms.data' using (log($1)):(log($3)) title 'en' with lines, f(x) title 'Ajuste lineal general' 
