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
    const resposta = await fetch('/api' + caminho, {
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
