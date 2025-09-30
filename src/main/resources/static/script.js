const BASE_URL = "http://localhost:8080/api";

const listEl = document.getElementById('Movie-list');
const formEl = document.getElementById('Movie-form');
const refreshBtn = document.getElementById('refresh');

let movies = [];

// Helper: parse tags string into array
function parseTags(input) {
  return input
      .split(',')
      .map(t => t.trim())
      .filter(Boolean);
}

// Helper: fetch JSON from a URL
async function fetchJson(url, options) {
  const res = await fetch(url, options);
  if (!res.ok) throw new Error(res.statusText || 'Request failed');
  if (res.status === 204) return null; // No content
  return await res.json();
}

// Helper: update movie array and re-render
function updateMovieList(updatedMovie) {
  movies = movies.map(x => x.id === updatedMovie.id ? updatedMovie : x);
  renderMovies();
}

// Helper: render all movies
function renderMovies() {
  listEl.innerHTML = '';

  for (const t of movies) {
    const li = document.createElement('li');
    li.className = 'movie-item' + (t.done ? ' done' : '');

    // Checkbox
    const checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.className = 'checkbox';
    checkbox.checked = !!t.done;
    checkbox.addEventListener('change', () => toggleDone(t));

    // Content
    const content = document.createElement('div');
    const title = document.createElement('div');
    title.className = 'movie-title';
    title.textContent = t.title || '(Untitled)';

    const desc = document.createElement('div');
    desc.className = 'movie-desc';
    desc.textContent = t.description || '';

    const tagsWrap = document.createElement('div');
    tagsWrap.className = 'tags';
    for (const tag of t.tags || []) {
      const chip = document.createElement('span');
      chip.className = 'tag';
      chip.textContent = tag;
      tagsWrap.appendChild(chip);
    }

    content.appendChild(title);
    content.appendChild(desc);
    content.appendChild(tagsWrap);

    // Actions
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
  }
}

// Fetch all movies
async function fetchMovies() {
  try {
    movies = await fetchJson(`${BASE_URL}/wishlist`);
    renderMovies();
  } catch (e) {
    console.error('Error loading movies', e);
    alert('Error fetching task list. Make sure the backend is running.');
  }
}

// Add new movie
async function addMovie(e) {
  e.preventDefault();
  const title = document.getElementById('title').value.trim();
  const description = document.getElementById('description').value.trim();
  const tags = parseTags(document.getElementById('tags').value);
  if (!title) { alert('Title is required'); return; }

  try {
    const created = await fetchJson(`${BASE_URL}/wishlist`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title, description, tags, done: false })
    });
    movies.unshift(created);
    renderMovies();
    formEl.reset();
  } catch (e) {
    console.error(e);
    alert('Failed to create item');
  }
}

// Edit movie
async function editMovie(t) {
  const newTitle = prompt('Title:', t.title || '');
  if (newTitle === null) return;
  const newDesc = prompt('Description:', t.description || '');
  if (newDesc === null) return;
  const newTagsStr = prompt('Tags (separate with commas):', (t.tags || []).join(', '));
  if (newTagsStr === null) return;
  const newTags = parseTags(newTagsStr);

  try {
    const updated = await fetchJson(`${BASE_URL}/wishlist/${t.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title: newTitle, description: newDesc, tags: newTags, done: t.done })
    });
    updateMovieList(updated);
  } catch (e) {
    console.error(e);
    alert('Failed to update item');
  }
}

// Toggle done status
async function toggleDone(t) {
  try {
    const updated = await fetchJson(`${BASE_URL}/wishlist/${t.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title: t.title, description: t.description, tags: t.tags || [], done: !t.done })
    });
    updateMovieList(updated);
  } catch (e) {
    console.error(e);
    alert('Error changing status');
  }
}

// Delete movie
async function deleteMovie(id) {
  if (!confirm('Delete this item?')) return;
  try {
    await fetchJson(`${BASE_URL}/wishlist/${id}`, { method: 'DELETE' });
    movies = movies.filter(t => t.id !== id);
    renderMovies();
  } catch (e) {
    console.error(e);
    alert('Failed to delete item');
  }
}

// Event listeners
formEl.addEventListener('submit', addMovie);
refreshBtn.addEventListener('click', fetchMovies);


// Initial load (top-level await)
await fetchMovies();
