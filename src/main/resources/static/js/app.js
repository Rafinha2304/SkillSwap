// Página inicial: modal de categorias, sessão visível no topo e atalhos personalizados.

function iniciarModalCategorias() {
    const modalBackdrop = document.querySelector('.modal-backdrop');
    const modalTitle = document.querySelector('#modal-title');
    const modalIcon = document.querySelector('.modal-icon');
    const skillChips = document.querySelector('.skill-chips');
    const modalAction = document.querySelector('.modal-action');

    document.querySelectorAll('[data-category]').forEach((button) => {
        button.addEventListener('click', () => {
            modalTitle.textContent = button.dataset.category;
            modalIcon.textContent = button.dataset.icon;
            modalAction.href = 'busca.html?categoria=' + encodeURIComponent(button.dataset.category);
            skillChips.innerHTML = '';
            button.dataset.skills.split(', ').forEach((skill) => {
                const chip = document.createElement('span');
                chip.textContent = skill;
                skillChips.appendChild(chip);
            });
            modalBackdrop.hidden = false;
            document.body.classList.add('no-scroll');
            document.querySelector('.modal-close').focus();
        });
    });

    function fecharModal() {
        modalBackdrop.hidden = true;
        document.body.classList.remove('no-scroll');
    }

    document.querySelector('.modal-close').addEventListener('click', fecharModal);
    modalBackdrop.addEventListener('click', (event) => {
        if (event.target === modalBackdrop) fecharModal();
    });
    document.addEventListener('keydown', (event) => {
        if (event.key === 'Escape' && !modalBackdrop.hidden) fecharModal();
    });
}

function aplicarSessaoNaPaginaInicial() {
    const usuario = usuarioLogado();
    if (!usuario) return;

    const acoes = document.querySelector('.header-actions');
    const primeiroNome = usuario.nome.split(' ')[0];
    acoes.innerHTML = [
        '<span class="user-chip"><span class="user-mini">' + usuario.nome.charAt(0).toUpperCase() + '</span>' + primeiroNome + '</span>',
        '<a class="button button-ghost" href="perfil.html">Meu perfil</a>',
        '<a class="button button-ghost" href="solicitacoes.html">Trocas</a>',
        '<button class="button button-primary" type="button" data-sair>Sair</button>'
    ].join('');
    acoes.querySelector('[data-sair]').addEventListener('click', sair);

    const botaoHero = document.querySelector('.hero-actions .button-primary');
    botaoHero.href = 'busca.html';
    botaoHero.innerHTML = 'Encontrar estudantes <span aria-hidden="true">→</span>';

    const botaoCta = document.querySelector('.cta .button');
    botaoCta.href = 'busca.html';
    botaoCta.innerHTML = 'Explorar estudantes <span aria-hidden="true">→</span>';
}

iniciarModalCategorias();
aplicarSessaoNaPaginaInicial();
