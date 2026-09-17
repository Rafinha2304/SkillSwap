// Telas de busca de estudantes e de perfil público de outro estudante.

function aplicarSessaoNaNavegacao() {
    const logado = usuarioLogado();
    const acaoSair = document.querySelector('[data-sair]');
    if (acaoSair && logado) {
        acaoSair.hidden = false;
        acaoSair.addEventListener('click', (event) => {
            event.preventDefault();
            sair();
        });
    }
    const acaoTrocas = document.querySelector('[data-trocas]');
    if (acaoTrocas && logado) {
        acaoTrocas.hidden = false;
    }
}

function chipDeHabilidade(nome) {
    const chip = document.createElement('span');
    chip.className = 'skill-chip';
    chip.textContent = nome;
    return chip;
}

function preencherChips(container, rotulo, nomes, limite) {
    const titulo = document.createElement('span');
    titulo.className = 'card-rotulo' + (rotulo === 'Quero aprender' ? ' rotulo-aprender' : '');
    titulo.textContent = rotulo;
    container.appendChild(titulo);

    const chips = document.createElement('div');
    chips.className = 'card-chips';
    if (!nomes.length) {
        const aviso = document.createElement('span');
        aviso.className = 'skill-vazia';
        aviso.textContent = 'Nada registrado ainda.';
        chips.appendChild(aviso);
    } else {
        nomes.slice(0, limite).forEach((nome) => chips.appendChild(chipDeHabilidade(nome)));
        if (nomes.length > limite) {
            const resto = document.createElement('span');
            resto.className = 'skill-vazia';
            resto.textContent = '+' + (nomes.length - limite) + ' outra(s)';
            chips.appendChild(resto);
        }
    }
    container.appendChild(chips);
}

function iniciarBusca() {
    aplicarSessaoNaNavegacao();

    const form = document.querySelector('#form-busca');
    const resultados = document.querySelector('#resultados');
    const mensagemVazia = document.querySelector('#mensagem-vazia');

    const params = new URLSearchParams(window.location.search);
    form.busca.value = params.get('busca') || '';
    form.categoria.value = params.get('categoria') || '';

    async function buscar() {
        const busca = form.busca.value.trim();
        const categoria = form.categoria.value;
        const query = new URLSearchParams();
        if (busca) query.set('busca', busca);
        if (categoria) query.set('categoria', categoria);

        const estudantes = await chamarApi('/estudantes' + (query.toString() ? '?' + query : ''));
        resultados.innerHTML = '';
        mensagemVazia.hidden = estudantes.length > 0;

        estudantes.forEach((estudante) => {
            const cartao = document.createElement('article');
            cartao.className = 'estudante-card';

            const topo = document.createElement('div');
            topo.className = 'estudante-top';
            const avatar = document.createElement('span');
            avatar.className = 'user-mini';
            avatar.textContent = estudante.nome.charAt(0).toUpperCase();
            const identificacao = document.createElement('div');
            identificacao.innerHTML = '<b>' + estudante.nome + '</b>';
            topo.append(avatar, identificacao);

            const bio = document.createElement('p');
            bio.className = 'bio-curta';
            bio.textContent = estudante.biografia || 'Este estudante ainda não escreveu uma biografia.';

            const acoes = document.createElement('a');
            acoes.className = 'text-link';
            acoes.href = 'estudante.html?id=' + estudante.id;
            acoes.innerHTML = 'Ver perfil <span aria-hidden="true">→</span>';

            cartao.append(topo, bio);
            preencherChips(cartao, 'Posso ensinar', estudante.oferece, 3);
            preencherChips(cartao, 'Quero aprender', estudante.desejaAprender, 3);
            cartao.appendChild(acoes);
            resultados.appendChild(cartao);
        });
    }

    form.addEventListener('submit', (event) => {
        event.preventDefault();
        buscar().catch((erro) => mostrarToast(erro.message));
    });

    buscar().catch((erro) => mostrarToast(erro.message));
}

function iniciarEstudante() {
    aplicarSessaoNaNavegacao();

    const params = new URLSearchParams(window.location.search);
    const id = Number(params.get('id'));
    if (!id) {
        window.location.href = 'busca.html';
        return;
    }

    chamarApi('/usuarios/' + id).then((perfil) => {
        document.title = perfil.nome + ' | SkillSwap';
        document.querySelector('#estudante-inicial').textContent = perfil.nome.charAt(0).toUpperCase();
        document.querySelector('#estudante-nome').textContent = perfil.nome;
        document.querySelector('#estudante-desde').textContent = 'Membro desde ' +
            new Date(perfil.criadoEm).toLocaleDateString('pt-BR', { day: 'numeric', month: 'long', year: 'numeric' });
        document.querySelector('#estudante-bio').textContent = perfil.biografia ||
            'Este estudante ainda não escreveu uma biografia.';

        const listaOferece = document.querySelector('#estudante-oferece');
        const listaDeseja = document.querySelector('#estudante-deseja');
        if (!perfil.oferece.length) {
            listaOferece.innerHTML = '<p class="skill-vazia">Nenhuma habilidade registrada ainda.</p>';
        } else {
            perfil.oferece.forEach((item) => listaOferece.appendChild(chipDeHabilidade(item.nome)));
        }
        if (!perfil.desejaAprender.length) {
            listaDeseja.innerHTML = '<p class="skill-vazia">Nenhuma habilidade registrada ainda.</p>';
        } else {
            perfil.desejaAprender.forEach((item) => listaDeseja.appendChild(chipDeHabilidade(item.nome)));
        }
    }).catch((erro) => mostrarToast(erro.message));

    const botaoSolicitar = document.querySelector('#botao-solicitar');
    const sessao = usuarioLogado();
    if (sessao && sessao.id === id) {
        botaoSolicitar.hidden = true;
    }
    botaoSolicitar.addEventListener('click', async () => {
        if (!usuarioLogado()) {
            mostrarToast('Entre na sua conta para solicitar uma troca.');
            window.location.href = 'login.html';
            return;
        }
        botaoSolicitar.disabled = true;
        botaoSolicitar.textContent = 'Enviando...';
        try {
            await chamarApi('/solicitacoes', {
                method: 'POST',
                body: JSON.stringify({ deUsuarioId: sessao.id, paraUsuarioId: id })
            });
            botaoSolicitar.textContent = 'Solicitação enviada';
            mostrarToast('Solicitação de troca enviada!');
        } catch (erro) {
            mostrarToast(erro.message);
            botaoSolicitar.disabled = false;
            botaoSolicitar.textContent = 'Solicitar troca';
        }
    });
}

const paginas = { busca: iniciarBusca, estudante: iniciarEstudante };
paginas[document.body.dataset.page]();
