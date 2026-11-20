// Gráficos do dashboard. Lê os dados das tabelas da própria página (data-*),
// então não depende de serialização JSON do servidor e a tabela serve de alternativa em texto.
// Todas as cores e a fonte vêm dos tokens de static/css/tema.css (--ink, --line, --chart-*...),
// por isso os gráficos acompanham a troca de paleta e de tema sem editar este arquivo.
(function () {
    var charts = [];

    function css(name) {
        return getComputedStyle(document.documentElement).getPropertyValue(name).trim();
    }

    function rows(selector) {
        return Array.prototype.slice.call(document.querySelectorAll(selector));
    }

    function brl(v) {
        return 'R$ ' + Number(v).toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    }

    // eixo: sem centavos, para os rótulos caberem
    function brlEixo(v) {
        return 'R$ ' + Number(v).toLocaleString('pt-BR', { maximumFractionDigits: 0 });
    }

    function destroyAll() {
        charts.forEach(function (c) { c.destroy(); });
        charts = [];
    }

    function draw() {
        if (typeof Chart === 'undefined') return; // CDN indisponível: as tabelas continuam legíveis
        destroyAll();

        var ink = css('--ink');
        var muted = css('--ink-muted');
        var line = css('--line');
        var surface = css('--surface');

        Chart.defaults.font.family = css('--fonte-texto');
        Chart.defaults.font.size = 14;
        Chart.defaults.color = muted;

        // 1) Sócios por categoria (participação)
        var cat = rows('#tabela-categorias tbody tr[data-nome]');
        var elCat = document.getElementById('grafico-categorias');
        if (elCat && cat.length) {
            var palette = [css('--chart-1'), css('--chart-2'), css('--chart-3'), css('--chart-4'), css('--chart-5')];
            charts.push(new Chart(elCat, {
                type: 'doughnut',
                data: {
                    labels: cat.map(function (r) { return r.dataset.nome; }),
                    datasets: [{
                        data: cat.map(function (r) { return Number(r.dataset.total); }),
                        backgroundColor: cat.map(function (_, i) { return palette[i % palette.length]; }),
                        borderColor: surface,
                        borderWidth: 2
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    cutout: '62%',
                    plugins: {
                        legend: { position: 'bottom', labels: { color: ink, usePointStyle: true, boxWidth: 8 } },
                        tooltip: {
                            callbacks: {
                                label: function (ctx) {
                                    var novos = cat[ctx.dataIndex].dataset.novos;
                                    return ctx.label + ': ' + ctx.parsed + ' sócios (' + novos + ' novos no mês)';
                                }
                            }
                        }
                    }
                }
            }));
        }

        // 2) Ocupação dos próximos jogos
        var jogos = rows('#tabela-jogos tbody tr[data-titulo]');
        var elOcup = document.getElementById('grafico-ocupacao');
        if (elOcup && jogos.length) {
            charts.push(new Chart(elOcup, {
                type: 'bar',
                data: {
                    labels: jogos.map(function (r) { return r.dataset.titulo; }),
                    datasets: [{
                        label: 'Ocupação (%)',
                        data: jogos.map(function (r) { return Number(r.dataset.ocupacao); }),
                        backgroundColor: css('--chart-1'),
                        borderRadius: 4,
                        maxBarThickness: 22
                    }]
                },
                options: {
                    indexAxis: 'y',
                    maintainAspectRatio: false,
                    scales: {
                        x: { min: 0, max: 100, grid: { color: line }, ticks: { stepSize: 25, maxRotation: 0, callback: function (v) { return v + '%'; } } },
                        y: { grid: { display: false }, ticks: { color: ink } }
                    },
                    plugins: {
                        legend: { display: false },
                        tooltip: {
                            callbacks: {
                                label: function (ctx) {
                                    var r = jogos[ctx.dataIndex].dataset;
                                    return r.ocupacao + '% ocupado, ' + r.vendidos + ' ingressos vendidos';
                                }
                            }
                        }
                    }
                }
            }));
        }

        // 3) Receita por jogo (barras horizontais: os nomes dos jogos não colidem no celular)
        var elRec = document.getElementById('grafico-receita');
        if (elRec && jogos.length) {
            charts.push(new Chart(elRec, {
                type: 'bar',
                data: {
                    labels: jogos.map(function (r) { return r.dataset.titulo; }),
                    datasets: [{
                        label: 'Receita',
                        data: jogos.map(function (r) { return Number(r.dataset.receita); }),
                        backgroundColor: css('--chart-2'),
                        borderRadius: 4,
                        maxBarThickness: 22
                    }]
                },
                options: {
                    indexAxis: 'y',
                    maintainAspectRatio: false,
                    scales: {
                        x: { beginAtZero: true, grid: { color: line }, ticks: { maxTicksLimit: 5, maxRotation: 0, callback: function (v) { return brlEixo(v); } } },
                        y: { grid: { display: false }, ticks: { color: ink } }
                    },
                    plugins: {
                        legend: { display: false },
                        tooltip: { callbacks: { label: function (ctx) { return brl(ctx.parsed.x); } } }
                    }
                }
            }));
        }
    }

    document.addEventListener('DOMContentLoaded', draw);
    document.addEventListener('themechange', draw);
})();
