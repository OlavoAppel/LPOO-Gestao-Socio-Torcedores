// Alternância de tema claro/escuro e menu do perfil (usado pela navbar).
(function () {
    var root = document.documentElement;

    function currentTheme() {
        return root.getAttribute('data-theme') === 'dark' ? 'dark' : 'light';
    }

    function applyTheme(t) {
        root.setAttribute('data-theme', t);
        try { localStorage.setItem('theme', t); } catch (e) { /* sem storage: vale só nesta página */ }
        var btn = document.getElementById('themeToggle');
        if (btn) {
            btn.setAttribute('aria-pressed', t === 'dark' ? 'true' : 'false');
            btn.setAttribute('aria-label', t === 'dark' ? 'Usar tema claro' : 'Usar tema escuro');
        }
        // avisa quem desenha com cores calculadas (ex.: gráficos do dashboard)
        document.dispatchEvent(new CustomEvent('themechange', { detail: t }));
    }

    document.addEventListener('DOMContentLoaded', function () {
        var toggle = document.getElementById('themeToggle');
        if (toggle) {
            applyTheme(currentTheme());
            toggle.addEventListener('click', function () {
                applyTheme(currentTheme() === 'dark' ? 'light' : 'dark');
            });
        }

        var menuBtn = document.getElementById('userMenuBtn');
        var menu = document.getElementById('profileDropdown');
        if (menuBtn && menu) {
            menuBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                var open = menu.classList.toggle('active');
                menuBtn.setAttribute('aria-expanded', open ? 'true' : 'false');
            });
            document.addEventListener('click', function (e) {
                if (!menu.contains(e.target)) {
                    menu.classList.remove('active');
                    menuBtn.setAttribute('aria-expanded', 'false');
                }
            });
            document.addEventListener('keydown', function (e) {
                if (e.key === 'Escape') {
                    menu.classList.remove('active');
                    menuBtn.setAttribute('aria-expanded', 'false');
                }
            });
        }
    });
})();
