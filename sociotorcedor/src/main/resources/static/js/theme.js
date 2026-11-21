// Tema claro/escuro. O botão do topo ([data-alternar-tema]) alterna e salva a escolha no navegador.
// Sem escolha salva, o tema segue o sistema e acompanha se ele mudar. O script do <head>
// (fragments/head.html) já aplicou o tema antes de a página pintar; aqui só ligamos o botão.
// Sem JavaScript, o botão continua escondido e as cores seguem o sistema (paleta.css usa light-dark()).
(function () {
    var raiz = document.documentElement;
    var sistema = window.matchMedia ? window.matchMedia('(prefers-color-scheme: dark)') : null;

    function salvo() {
        try { return localStorage.getItem('tema'); } catch (e) { return null; }
    }

    function atual() {
        return raiz.getAttribute('data-theme') === 'dark' ? 'dark' : 'light';
    }

    function aplicar(tema, guardar) {
        raiz.setAttribute('data-theme', tema);
        if (guardar) {
            try { localStorage.setItem('tema', tema); } catch (e) { /* sem armazenamento: vale só nesta página */ }
        }
        document.querySelectorAll('[data-alternar-tema]').forEach(function (botao) {
            botao.hidden = false;
            botao.setAttribute('aria-pressed', tema === 'dark' ? 'true' : 'false');
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        aplicar(atual(), false);
        document.querySelectorAll('[data-alternar-tema]').forEach(function (botao) {
            botao.addEventListener('click', function () {
                aplicar(atual() === 'dark' ? 'light' : 'dark', true);
            });
        });
        if (sistema && sistema.addEventListener) {
            sistema.addEventListener('change', function (e) {
                if (!salvo()) aplicar(e.matches ? 'dark' : 'light', false);
            });
        }
    });
})();
