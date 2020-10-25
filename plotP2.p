set title 'Terminos'
set xlabel "Ranking"
set ylabel "Frecuencia"

plot 'data/AdministracionParaEmprendedores-ms.data' using 1:3 title 'Español log-log' with lines
plot 'data/ColomboDescobrimentos-ms.data' using 1:3 title 'Poprtugués log-log' with lines
plot 'data/DonJuanTenorio-ms.data' using 1:3 title 'Español log-log' with lines
plot 'data/dracula-ms.data' using 1:3 title 'Inglés log-log' with lines
plot 'data/Iliada-ms.data' using 1:3 title 'Español log-log' with lines

#ñljñlkñlk
plot 'data/AdministracionParaEmprendedores-ms.data' using (log($1)):(log($3)) title 'Español log-log' with lines
plot 'data/ColomboDescobrimentos-ms.data' using (log($1)):(log($3)) title 'Poprtugués log-log' with lines
plot 'data/DonJuanTenorio-ms.data' using (log($1)):(log($3)) title 'Español log-log' with lines
plot 'data/dracula-ms.data' using (log($1)):(log($3)) title 'Inglés log-log' with lines
plot 'data/Iliada-ms.data' using (log($1)):(log($3)) title 'Español log-log' with lines

f(x) = log (k) -m*x

fit f(x) 'data/AdministracionParaEmprendedores-ms.data' using (log($1)):(log($3)) via k,m
fit f(x) 'data/ColomboDescobrimentos-ms.data' using (log($1)):(log($3)) via k,m
fit f(x) 'data/DonJuanTenorio-ms.data' using (log($1)):(log($3)) via k,m
fit f(x) 'data/dracula-ms.data' using (log($1)):(log($3)) title via k,m
fit f(x) 'data/Iliada-ms.data' using (log($1)):(log($3)) title via k,m

plot 'data/GutenbergWeb-ms.data' using (log($1)):(log($3)) title 'Español log-log' with lines, \
 'data/ColomboDescobrimentos-ms.data' using (log($1)):(log($3)) title 'Poprtugués log-log' with lines, \
 'data/DonJuanTenorio-ms.data' using (log($1)):(log($3)) title 'Español log-log' with lines, \
 'data/dracula-ms.data' using (log($1)):(log($3)) title 'Inglés log-log' with lines, \
 'data/JapanTale-ms.data' using (log($1)):(log($3)) title 'Español log-log' with lines, f(x) title 'Ajuste lineal general'