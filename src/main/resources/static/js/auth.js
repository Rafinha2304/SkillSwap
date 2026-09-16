const API_URL = '/api';
const STORAGE_KEY = 'skillswap_usuario';

function usuarioLogado() {
    try {
        return JSON.parse(localStorage.getItem(STORAGE_KEY));
    } catch {
        return null;
    }
}

function salvarSessao(usuario) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(usuario));
}

function sair() {
    localStorage.removeItem(STORAGE_KEY);
    window.location.href = 'login.html';
}

function mostrarToast(mensagem) {
    const toast = document.querySelector('.toast');
    toast.textContent = mensagem;
    toast.classList.add('show');
    setTimeout(() => toast.classList.remove('show'), 3500);
}

async function chamarApi(caminho, opcoes = {}) {
    const resposta = await fetch(API_URL + caminho, {
        headers: { 'Content-Type': 'application/json' },
        ...opcoes
    });
    const dados = await resposta.json().catch(() => ({}));
    if (!resposta.ok) {
        throw new Error(dados.message || 'Não foi possível concluir a operação. Tente novamente.');
    }
    return dados;
}

function iniciarBotao(botao, textoOcupado) {
    botao.disabled = true;
    const textoOriginal = botao.textContent;
    botao.textContent = textoOcupado;
    return () => {
        botao.disabled = false;
        botao.textContent = textoOriginal;
    };
}

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

function preencherPerfil(usuario) {
    document.querySelector('#perfil-inicial').textContent = usuario.nome.charAt(0).toUpperCase();
    document.querySelector('#perfil-email').textContent = usuario.email;
    document.querySelector('#perfil-desde').textContent = 'Membro desde ' +
        new Date(usuario.criadoEm).toLocaleDateString('pt-BR', { day: 'numeric', month: 'long', year: 'numeric' });
    document.querySelector('#perfil-bio').textContent = usuario.biografia ||
        'Escreva uma biografia para contar o que você pode ensinar e o que deseja aprender.';
    document.querySelector('#nome').value = usuario.nome;
    document.querySelector('#biografia').value = usuario.biografia || '';
}

function iniciarPerfil() {
    const usuario = usuarioLogado();
    if (!usuario) {
        window.location.href = 'login.html';
        return;
    }
    preencherPerfil(usuario);

    document.querySelector('#link-sair').addEventListener('click', (event) => {
        event.preventDefault();
        sair();
    });

    const form = document.querySelector('#form-perfil');
    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        const nome = form.nome.value.trim();
        const biografia = form.biografia.value.trim();
        if (!nome) {
            mostrarToast('Informe o seu nome.');
            return;
        }
        const liberar = iniciarBotao(form.querySelector('button'), 'Salvando...');
        try {
            const atualizado = await chamarApi('/usuarios/' + usuario.id, {
                method: 'PUT',
                body: JSON.stringify({ nome, biografia })
            });
            salvarSessao(atualizado);
            preencherPerfil(atualizado);
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
