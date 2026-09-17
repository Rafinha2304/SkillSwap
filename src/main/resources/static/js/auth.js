// Telas de login, cadastro e perfil (com habilidades).

function iniciarLogin() {
    const form = document.querySelector('#form-login');
    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        const email = form.email.value.trim();
        const senha = form.senha.value;
        if (!email || !senha) {
            mostrarToast('Preencha o e-mail e a senha.');
            return;
        }
        const liberar = iniciarBotao(form.querySelector('button'), 'Entrando...');
        try {
            const usuario = await chamarApi('/login', {
                method: 'POST',
                body: JSON.stringify({ email, senha })
            });
            salvarSessao(usuario);
            window.location.href = 'perfil.html';
        } catch (erro) {
            mostrarToast(erro.message);
            liberar();
        }
    });
}

function iniciarCadastro() {
    const form = document.querySelector('#form-cadastro');
    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        const nome = form.nome.value.trim();
        const email = form.email.value.trim();
        const senha = form.senha.value;
        const biografia = form.biografia.value.trim();
        if (!nome || !email || !senha) {
            mostrarToast('Preencha nome, e-mail e senha.');
            return;
        }
        if (senha.length < 6) {
            mostrarToast('A senha deve ter no mínimo 6 caracteres.');
            return;
        }
        const liberar = iniciarBotao(form.querySelector('button'), 'Criando conta...');
        try {
            const usuario = await chamarApi('/usuarios', {
                method: 'POST',
                body: JSON.stringify({ nome, email, senha, biografia })
            });
            salvarSessao(usuario);
            window.location.href = 'perfil.html';
        } catch (erro) {
            mostrarToast(erro.message);
            liberar();
        }
    });
}

function preencherCabecalhoDoPerfil(perfil) {
    document.querySelector('#perfil-inicial').textContent = perfil.nome.charAt(0).toUpperCase();
    document.querySelector('#perfil-email').textContent = perfil.email;
    document.querySelector('#perfil-desde').textContent = 'Membro desde ' +
        new Date(perfil.criadoEm).toLocaleDateString('pt-BR', { day: 'numeric', month: 'long', year: 'numeric' });
    document.querySelector('#perfil-bio').textContent = perfil.biografia ||
        'Escreva uma biografia para contar o que você pode ensinar e o que deseja aprender.';
}

function preencherListaDeHabilidades(perfil, seletor, itens) {
    const lista = document.querySelector(seletor);
    lista.innerHTML = '';
    if (!itens.length) {
        const aviso = document.createElement('p');
        aviso.className = 'skill-vazia';
        aviso.textContent = 'Nenhuma habilidade registrada ainda.';
        lista.appendChild(aviso);
        return;
    }
    itens.forEach((item) => {
        const chip = document.createElement('span');
        chip.className = 'skill-chip';
        chip.append(item.nome);
        const remover = document.createElement('button');
        remover.type = 'button';
        remover.className = 'chip-remove';
        remover.setAttribute('aria-label', 'Remover ' + item.nome);
        remover.dataset.vinculo = item.vinculoId;
        remover.textContent = '×';
        remover.addEventListener('click', async () => {
            try {
                await chamarApi('/usuarios/' + perfil.id + '/habilidades/' + item.vinculoId, { method: 'DELETE' });
                await recarregarPerfil(perfil.id);
                mostrarToast('Habilidade removida do perfil.');
            } catch (erro) {
                mostrarToast(erro.message);
            }
        });
        chip.appendChild(remover);
        lista.appendChild(chip);
    });
}

async function recarregarPerfil(id) {
    const perfil = await chamarApi('/usuarios/' + id);
    preencherCabecalhoDoPerfil(perfil);
    preencherListaDeHabilidades(perfil, '#lista-oferece', perfil.oferece);
    preencherListaDeHabilidades(perfil, '#lista-deseja', perfil.desejaAprender);
    document.querySelector('#nome').value = perfil.nome;
    document.querySelector('#biografia').value = perfil.biografia || '';
    const sessao = usuarioLogado();
    if (sessao) {
        salvarSessao({ ...sessao, nome: perfil.nome, email: perfil.email, biografia: perfil.biografia });
    }
    return perfil;
}

async function iniciarPerfil() {
    const usuario = usuarioLogado();
    if (!usuario) {
        window.location.href = 'login.html';
        return;
    }

    document.querySelector('#link-sair').addEventListener('click', (event) => {
        event.preventDefault();
        sair();
    });

    let perfil;
    try {
        perfil = await recarregarPerfil(usuario.id);
    } catch {
        sair();
        return;
    }

    const catalogo = await chamarApi('/habilidades');
    const datalist = document.querySelector('#habilidades-existentes');
    catalogo.forEach((habilidade) => {
        const opcao = document.createElement('option');
        opcao.value = habilidade.nome;
        opcao.label = habilidade.categoria;
        datalist.appendChild(opcao);
    });

    const formHabilidade = document.querySelector('#form-habilidade');
    formHabilidade.addEventListener('submit', async (event) => {
        event.preventDefault();
        const nome = formHabilidade.nome.value.trim();
        const categoria = formHabilidade.categoria.value;
        const tipo = formHabilidade.tipo.value;
        if (!nome || !categoria || !tipo) {
            mostrarToast('Preencha a habilidade, a categoria e o tipo.');
            return;
        }
        try {
            await chamarApi('/usuarios/' + perfil.id + '/habilidades', {
                method: 'POST',
                body: JSON.stringify({ nome, categoria, tipo })
            });
            formHabilidade.reset();
            await recarregarPerfil(perfil.id);
            mostrarToast('Habilidade adicionada ao perfil!');
        } catch (erro) {
            mostrarToast(erro.message);
        }
    });

    const formPerfil = document.querySelector('#form-perfil');
    formPerfil.addEventListener('submit', async (event) => {
        event.preventDefault();
        const nome = formPerfil.nome.value.trim();
        const biografia = formPerfil.biografia.value.trim();
        if (!nome) {
            mostrarToast('Informe o seu nome.');
            return;
        }
        const liberar = iniciarBotao(formPerfil.querySelector('button'), 'Salvando...');
        try {
            await chamarApi('/usuarios/' + perfil.id, {
                method: 'PUT',
                body: JSON.stringify({ nome, biografia })
            });
            await recarregarPerfil(perfil.id);
            mostrarToast('Perfil atualizado com sucesso!');
        } catch (erro) {
            mostrarToast(erro.message);
        } finally {
            liberar();
        }
    });
}

const paginas = { login: iniciarLogin, cadastro: iniciarCadastro, perfil: iniciarPerfil };
paginas[document.body.dataset.page]();
