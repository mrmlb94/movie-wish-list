const BASE_URL = "http://localhost:8080/api";

const listEl = document.getElementById('Movie-list');
const formEl = document.getElementById('Movie-form');
const refreshBtn = document.getElementById('refresh');

let movies = [];

function parseTags(input) {
    return input
        .split(',')
        .map(t => t.trim())
        .filter(Boolean);
}

async function fetchMovies() {
    try {
        const res = await fetch(`${BASE_URL}/wishlist`);
        movies = await res.json();
        renderMovies();
    } catch (e) {
        console.error('Error loading movies', e);
        alert('Error fetching task list. Make sure the backend is running.');
    }
}

function renderMovies() {
    listEl.innerHTML = '';
    movies.forEach(t => {
        const li = document.createElement('li');
        li.className = 'movie-item' + (t.done ? ' done' : '');

        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.className = 'checkbox';
        checkbox.checked = !!t.done;
        checkbox.addEventListener('change', () => toggleDone(t));

        const content = document.createElement('div');
        const title = document.createElement('div');
        title.className = 'movie-title';
        title.textContent = t.title || '(Untitled)';

        const desc = document.createElement('div');
        desc.className = 'movie-desc';
        desc.textContent = t.description || '';

        const tagsWrap = document.createElement('div');
        tagsWrap.className = 'tags';
        (t.tags || []).forEach(tag => {
            const chip = document.createElement('span');
            chip.className = 'tag';
            chip.textContent = tag;
            tagsWrap.appendChild(chip);
        });

        content.appendChild(title);
        content.appendChild(desc);
        content.appendChild(tagsWrap);

        const actions = document.createElement('div');
        actions.className = 'item-actions';

        const editBtn = document.createElement('button');
        editBtn.className = 'btn';
        editBtn.textContent = 'Edit';
        editBtn.addEventListener('click', () => editMovie(t));

        const delBtn = document.createElement('button');
        delBtn.className = 'btn';
        delBtn.textContent = 'Delete';
        delBtn.addEventListener('click', () => deleteMovie(t.id));

        actions.appendChild(editBtn);
        actions.appendChild(delBtn);

        li.appendChild(checkbox);
        li.appendChild(content);
        li.appendChild(actions);
        listEl.appendChild(li);
    });
}

async function addMovie(e) {
    e.preventDefault();
    const title = document.getElementById('title').value.trim();
    const description = document.getElementById('description').value.trim();
    const tags = parseTags(document.getElementById('tags').value);
    if (!title) { alert('Title is required'); return; }
    try {
        const res = await fetch(`${BASE_URL}/wishlist`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title, description, tags, done: false })
        });
        if (!res.ok) throw new Error('Create failed');
        const created = await res.json();
        movies.unshift(created);
        renderMovies();
        formEl.reset();
    } catch (e) {
        console.error(e);
        alert('Failed to create item');
    }
}

async function editMovie(t) {
    const newTitle = prompt('Title:', t.title || '');
    if (newTitle === null) return;
    const newDesc = prompt('Description:', t.description || '');
    if (newDesc === null) return;
    const newTagsStr = prompt('Tags (separate with commas):', (t.tags || []).join(', '));
    if (newTagsStr === null) return;
    const newTags = parseTags(newTagsStr);

    try {
        const res = await fetch(`${BASE_URL}/wishlist/${t.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title: newTitle, description: newDesc, tags: newTags, done: t.done })
        });
        if (!res.ok) throw new Error('Update failed');
        const updated = await res.json();
        movies = movies.map(x => x.id === t.id ? updated : x);
        renderMovies();
    } catch (e) {
        console.error(e);
        alert('Failed to update item');
    }
}

async function toggleDone(t) {
    try {
        const res = await fetch(`${BASE_URL}/wishlist/${t.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title: t.title, description: t.description, tags: t.tags || [], done: !t.done })
        });
        if (!res.ok) throw new Error('Toggle failed');
        const updated = await res.json();
        movies = movies.map(x => x.id === t.id ? updated : x);
        renderMovies();
    } catch (e) {
        console.error(e);
        alert('Error changing status');
    }
}

async function deleteMovie(id) {
    if (!confirm('Delete this item?')) return;
    try {
        const res = await fetch(`${BASE_URL}/wishlist/${id}`, { method: 'DELETE' });
        if (res.status !== 204) throw new Error('Delete failed');
        movies = movies.filter(t => t.id !== id);
        renderMovies();
    } catch (e) {
        console.error(e);
        alert('Failed to delete item');
    }
}

formEl.addEventListener('submit', addMovie);
refreshBtn.addEventListener('click', fetchMovies);

fetchMovies();
