// Página "Minhas trocas": solicitações recebidas (com aceite e recusa) e enviadas.

function chipDeStatus(status) {
    const chip = document.createElement('span');
    chip.className = 'status-chip status-' + status.toLowerCase();
    chip.textContent = status === 'PENDENTE' ? 'Pendente' : (status === 'ACEITA' ? 'Aceita' : 'Recusada');
    return chip;
}

function itemDeTroca(solicitacao, acoes) {
    const item = document.createElement('article');
    item.className = 'troca-item';

    const link = document.createElement('a');
    link.className = 'troca-quem';
    link.href = 'estudante.html?id=' + solicitacao.outroUsuarioId;
    link.innerHTML = '<span class="user-mini">' + solicitacao.outroNome.charAt(0).toUpperCase() + '</span><b>' +
        solicitacao.outroNome + '</b>';

    const lado = document.createElement('div');
    lado.className = 'troca-acoes';
    lado.appendChild(chipDeStatus(solicitacao.status));
    (acoes || []).forEach((acao) => lado.appendChild(acao));

    item.append(link, lado);
    return item;
}

function botaoAcao(texto, classe, aoClicar) {
    const botao = document.createElement('button');
    botao.type = 'button';
    botao.className = 'button ' + classe;
    botao.textContent = texto;
    botao.addEventListener('click', aoClicar);
    return botao;
}

async function carregarTrocas(usuarioId) {
    const dados = await chamarApi('/usuarios/' + usuarioId + '/solicitacoes');
    const recebidas = document.querySelector('#lista-recebidas');
    const enviadas = document.querySelector('#lista-enviadas');
    recebidas.innerHTML = '';
    enviadas.innerHTML = '';

    if (!dados.recebidas.length) {
        recebidas.innerHTML = '<p class="skill-vazia">Você ainda não recebeu solicitações de troca.</p>';
    } else {
        dados.recebidas.forEach((solicitacao) => {
            const acoes = [];
            if (solicitacao.status === 'PENDENTE') {
                acoes.push(botaoAcao('Aceitar', 'button-primary', async (event) => {
                    await responder(solicitacao.id, usuarioId, 'ACEITA', event.target);
                    await carregarTrocas(usuarioId);
                }));
                acoes.push(botaoAcao('Recusar', 'button-ghost', async (event) => {
                    await responder(solicitacao.id, usuarioId, 'RECUSADA', event.target);
                    await carregarTrocas(usuarioId);
                }));
            }
            recebidas.appendChild(itemDeTroca(solicitacao, acoes));
        });
    }

    if (!dados.enviadas.length) {
        enviadas.innerHTML = '<p class="skill-vazia">Você ainda não enviou solicitações. Encontre estudantes na busca!</p>';
    } else {
        dados.enviadas.forEach((solicitacao) => enviadas.appendChild(itemDeTroca(solicitacao, [])));
    }
}

async function responder(solicitacaoId, usuarioId, status, botao) {
    botao.disabled = true;
    try {
        await chamarApi('/solicitacoes/' + solicitacaoId, {
            method: 'PUT',
            body: JSON.stringify({ usuarioId, status })
        });
        mostrarToast(status === 'ACEITA' ? 'Solicitação aceita! Combine a troca.' : 'Solicitação recusada.');
    } catch (erro) {
        mostrarToast(erro.message);
        botao.disabled = false;
    }
}

function iniciarTrocas() {
    const usuario = usuarioLogado();
    if (!usuario) {
        window.location.href = 'login.html';
        return;
    }
    document.querySelector('#link-sair').addEventListener('click', (event) => {
        event.preventDefault();
        sair();
    });
    carregarTrocas(usuario.id).catch((erro) => mostrarToast(erro.message));
}

iniciarTrocas();
