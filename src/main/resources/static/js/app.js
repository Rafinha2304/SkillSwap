const toast = document.querySelector('.toast');
const modalBackdrop = document.querySelector('.modal-backdrop');
const modalTitle = document.querySelector('#modal-title');
const modalIcon = document.querySelector('.modal-icon');
const skillChips = document.querySelector('.skill-chips');
let toastTimer;

function showMessage(message) {
    clearTimeout(toastTimer);
    toast.textContent = message;
    toast.classList.add('show');
    toastTimer = setTimeout(() => toast.classList.remove('show'), 3500);
}

document.querySelectorAll('[data-demo-message]').forEach((button) => {
    button.addEventListener('click', () => showMessage(button.dataset.demoMessage));
});

document.querySelectorAll('[data-category]').forEach((button) => {
    button.addEventListener('click', () => {
        modalTitle.textContent = button.dataset.category;
        modalIcon.textContent = button.dataset.icon;
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

function closeModal() {
    modalBackdrop.hidden = true;
    document.body.classList.remove('no-scroll');
}

document.querySelector('.modal-close').addEventListener('click', closeModal);
document.querySelector('.modal-action').addEventListener('click', () => {
    closeModal();
    showMessage('A busca por habilidades será implementada no Ciclo 3.');
});
modalBackdrop.addEventListener('click', (event) => {
    if (event.target === modalBackdrop) closeModal();
});
document.addEventListener('keydown', (event) => {
    if (event.key === 'Escape' && !modalBackdrop.hidden) closeModal();
});
