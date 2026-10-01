// ============================================
// NAVIGATION — switching between sidebar sections
// ============================================
const navItems = document.querySelectorAll('.nav-item');
const pages = document.querySelectorAll('.page');

navItems.forEach(item => {
    item.addEventListener('click', () => {
        const target = item.getAttribute('data-section');

        navItems.forEach(n => n.classList.remove('active'));
        item.classList.add('active');

        pages.forEach(p => p.classList.remove('active'));
        document.getElementById('section-' + target).classList.add('active');
    });
});

// ============================================
// GAME CLOCK — live time display in topbar
// ============================================
function updateClock() {
    const now = new Date();
    const time = now.toLocaleTimeString('en-IN', { hour12: true });
    document.getElementById('gameClock').textContent = time;
}
updateClock();
setInterval(updateClock, 1000);

// ============================================
// THEME TOGGLE — dark (default) / light ops mode
// ============================================
const themeToggle = document.getElementById('themeToggle');
themeToggle.addEventListener('click', () => {
    document.body.classList.toggle('light-mode');
    const isLight = document.body.classList.contains('light-mode');
    themeToggle.textContent = isLight ? '☀ Dark Ops Mode' : '☾ Light Ops Mode';
});

// ============================================
// TOAST NOTIFICATIONS — user feedback for actions
// ============================================
function showToast(message, type = 'success') {
    const container = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = 'toast' + (type === 'error' ? ' error' : '');
    toast.textContent = message;
    container.appendChild(toast);
    setTimeout(() => toast.remove(), 3500);
}

// ============================================
// DASHBOARD STATS — pulls live counts from every API
// FIX: employees count was missing from the original implementation.
// All five modules are now fetched and displayed correctly.
// ============================================
async function loadDashboardStats() {
    try {
        const [employees, weapons, players, exercises, systems] = await Promise.all([
            fetch('/api/employees').then(r => r.json()),
            fetch('/api/weapons').then(r => r.json()),
            fetch('/api/players').then(r => r.json()),
            fetch('/api/exercises').then(r => r.json()),
            fetch('/api/systems').then(r => r.json())
        ]);

        document.getElementById('statEmployees').textContent = employees.length;
        document.getElementById('statWeapons').textContent = weapons.length;
        document.getElementById('statPlayers').textContent = players.length;
        document.getElementById('statExercises').textContent = exercises.length;
        document.getElementById('statSystems').textContent = systems.length;

        renderOverviewChart(weapons, systems);
    } catch (err) {
        console.error('Failed to load dashboard stats:', err);
        showToast('Could not load dashboard data — check the backend is running', 'error');
    }
}

// ============================================
// CHART — resource allocation overview (Chart.js)
// ============================================
let overviewChartInstance = null;

function renderOverviewChart(weapons, systems) {
    const ctx = document.getElementById('overviewChart');

    const labels = [
        ...weapons.map(w => w.weaponType),
        ...systems.map(s => s.resourceType)
    ];
    const totals = [
        ...weapons.map(w => w.total),
        ...systems.map(s => s.total)
    ];
    const currents = [
        ...weapons.map(w => w.current),
        ...systems.map(s => s.current)
    ];

    if (overviewChartInstance) overviewChartInstance.destroy();

    overviewChartInstance = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [
                {
                    label: 'Total',
                    data: totals,
                    backgroundColor: 'rgba(201, 162, 39, 0.25)',
                    borderColor: '#C9A227',
                    borderWidth: 1
                },
                {
                    label: 'Current',
                    data: currents,
                    backgroundColor: 'rgba(107, 143, 113, 0.35)',
                    borderColor: '#6B8F71',
                    borderWidth: 1
                }
            ]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { labels: { color: '#8B98A5', font: { family: 'Inter' } } }
            },
            scales: {
                x: { ticks: { color: '#8B98A5' }, grid: { color: 'rgba(139,152,165,0.1)' } },
                y: { ticks: { color: '#8B98A5' }, grid: { color: 'rgba(139,152,165,0.1)' } }
            }
        }
    });
}

// ============================================
// ENTITY CONFIG — describes each module's API path,
// table columns, and form fields. Adding a new module
// only requires a new entry here — no other JS changes needed.
// ============================================
const entityConfigs = {
    weapons: {
        apiPath: '/api/weapons',
        title: 'Weapon',
        columns: [
            { key: 'weaponType', label: 'Weapon Type' },
            { key: 'range', label: 'Range' },
            { key: 'total', label: 'Total' },
            { key: 'current', label: 'Current' },
            { key: 'player', label: 'Assigned To', render: (r) => r.player ? r.player.playerName : '—' }
        ],
        fields: [
            { key: 'weaponType', label: 'Weapon Type', type: 'text' },
            { key: 'range', label: 'Range', type: 'number' },
            { key: 'total', label: 'Total', type: 'number' },
            { key: 'current', label: 'Current', type: 'number' },
            { key: 'player', label: 'Assign to Player', type: 'select', optionsFrom: 'players', optionLabel: 'playerName' }
        ]
    },
    players: {
        apiPath: '/api/players',
        title: 'Player',
        columns: [
            { key: 'playerName', label: 'Player Name' },
            { key: 'role', label: 'Role' },
            { key: 'unit', label: 'Unit' },
            { key: 'totalLoginTime', label: 'Login Time (hrs)' },
            { key: 'exercise', label: 'Exercise', render: (r) => r.exercise ? r.exercise.name : '—' }
        ],
        fields: [
            { key: 'playerName', label: 'Player Name', type: 'text' },
            { key: 'role', label: 'Role', type: 'text' },
            { key: 'unit', label: 'Unit', type: 'text' },
            { key: 'totalLoginTime', label: 'Login Time (hrs)', type: 'number' },
            { key: 'exercise', label: 'Assigned Exercise', type: 'select', optionsFrom: 'exercises', optionLabel: 'name' }
        ]
    },
    exercises: {
        apiPath: '/api/exercises',
        title: 'Exercise',
        columns: [
            { key: 'name', label: 'Name' },
            { key: 'location', label: 'Location' },
            { key: 'date', label: 'Date' },
            { key: 'commander', label: 'Commander' }
        ],
        fields: [
            { key: 'name', label: 'Name', type: 'text' },
            { key: 'location', label: 'Location', type: 'text' },
            { key: 'date', label: 'Date', type: 'date' },
            { key: 'commander', label: 'Commander', type: 'text' }
        ]
    },
    systems: {
        apiPath: '/api/systems',
        title: 'System',
        columns: [
            { key: 'resourceType', label: 'Resource Type' },
            { key: 'total', label: 'Total' },
            { key: 'current', label: 'Current' },
            { key: 'exercise', label: 'Exercise', render: (r) => r.exercise ? r.exercise.name : '—' }
        ],
        fields: [
            { key: 'resourceType', label: 'Resource Type', type: 'text' },
            { key: 'total', label: 'Total', type: 'number' },
            { key: 'current', label: 'Current', type: 'number' },
            { key: 'exercise', label: 'Assigned Exercise', type: 'select', optionsFrom: 'exercises', optionLabel: 'name' }
        ]
    },
    employees: {
        apiPath: '/api/employees',
        title: 'Employee',
        columns: [
            { key: 'firstName', label: 'First Name' },
            { key: 'lastName', label: 'Last Name' },
            { key: 'email', label: 'Email' }
        ],
        fields: [
            { key: 'firstName', label: 'First Name', type: 'text' },
            { key: 'lastName', label: 'Last Name', type: 'text' },
            { key: 'email', label: 'Email', type: 'email' }
        ]
    }
};

let currentEditId = null;
let currentEntityKey = null;

// ============================================
// LOAD + RENDER TABLE for a given entity key
// ============================================
async function loadTable(entityKey) {
    const config = entityConfigs[entityKey];
    const table = document.getElementById('table-' + entityKey);
    if (!config || !table) return;

    const tbody = table.querySelector('tbody');
    tbody.innerHTML = `<tr><td colspan="${config.columns.length + 1}" class="empty-state">Loading...</td></tr>`;

    try {
        const res = await fetch(config.apiPath);

        if (res.status === 401) {
            showToast('Session expired — please log in again', 'error');
            setTimeout(() => window.location.href = '/login.html', 1500);
            return;
        }

        if (!res.ok) {
            throw new Error('Server returned ' + res.status);
        }

        const records = await res.json();

        const thead = table.querySelector('thead');
        thead.innerHTML = '<tr>' +
            config.columns.map(c => `<th>${c.label}</th>`).join('') +
            '<th>Actions</th></tr>';

        if (records.length === 0) {
            tbody.innerHTML = `<tr><td colspan="${config.columns.length + 1}" class="empty-state">No ${config.title.toLowerCase()} records yet — click "Add ${config.title}" to create one.</td></tr>`;
            return;
        }

        tbody.innerHTML = records.map(record => `
      <tr>
        ${config.columns.map(c => `<td>${c.render ? c.render(record) : (record[c.key] ?? '')}</td>`).join('')}
        <td class="actions-cell">
          <button class="btn-icon" onclick='openModal("${entityKey}", ${JSON.stringify(record)})'>✎ Edit</button>
          <button class="btn-icon danger" onclick="deleteRecord('${entityKey}', ${record.id})">✕ Delete</button>
        </td>
      </tr>
    `).join('');

    } catch (err) {
        console.error(`Failed to load ${entityKey}:`, err);
        tbody.innerHTML = `<tr><td colspan="${config.columns.length + 1}" class="empty-state" style="color: var(--danger);">Failed to load data. Is the backend running?</td></tr>`;
        showToast(`Could not load ${config.title.toLowerCase()} data`, 'error');
    }
}

// ============================================
// FILTER TABLE — client-side search/filter
// FIX: This function was called from HTML but never implemented.
// It now filters visible table rows without making new API calls.
// ============================================
function filterTable(entityKey, searchValue) {
    const table = document.getElementById('table-' + entityKey);
    if (!table) return;

    const rows = table.querySelectorAll('tbody tr');
    const query = searchValue.toLowerCase().trim();

    rows.forEach(row => {
        // Check all text cells in the row
        const rowText = row.textContent.toLowerCase();
        // Show the row if any cell contains the search term; hide it otherwise
        row.style.display = (query === '' || rowText.includes(query)) ? '' : 'none';
    });
}

// ============================================
// MODAL — open (for add or edit), close, submit
// ============================================
async function openModal(entityKey, record = null) {
    const config = entityConfigs[entityKey];
    currentEntityKey = entityKey;
    currentEditId = record ? record.id : null;

    document.getElementById('modalTitle').textContent =
        (record ? 'Edit ' : 'Add ') + config.title;

    const form = document.getElementById('modalForm');
    form.innerHTML = '<p class="empty-state">Loading form...</p>';
    document.getElementById('modalOverlay').classList.add('active');

    // Build each field; fetch dropdown options for 'select' type fields
    const fieldHtmlParts = await Promise.all(config.fields.map(async (f) => {
        if (f.type === 'select') {
            try {
                const options = await fetch(entityConfigs[f.optionsFrom].apiPath).then(r => r.json());
                const currentValue = record && record[f.key] ? record[f.key].id : '';
                const optionTags = options.map(opt =>
                    `<option value="${opt.id}" ${String(opt.id) === String(currentValue) ? 'selected' : ''}>${opt[f.optionLabel]}</option>`
                ).join('');
                return `
        <div class="form-group">
          <label>${f.label}</label>
          <select name="${f.key}">
            <option value="">-- None --</option>
            ${optionTags}
          </select>
        </div>
      `;
            } catch (err) {
                return `<div class="form-group"><label>${f.label}</label><p style="color:var(--danger)">Could not load options</p></div>`;
            }
        }
        return `
      <div class="form-group">
        <label>${f.label}</label>
        <input type="${f.type}" name="${f.key}" value="${record ? (record[f.key] ?? '') : ''}">
      </div>
    `;
    }));

    form.innerHTML = fieldHtmlParts.join('') + `
    <div class="modal-actions">
      <button type="button" class="btn-secondary" onclick="closeModal()">Cancel</button>
      <button type="submit" class="btn-primary">Save</button>
    </div>
  `;

    form.onsubmit = (e) => {
        e.preventDefault();
        submitForm(entityKey);
    };
}

function closeModal() {
    document.getElementById('modalOverlay').classList.remove('active');
    currentEditId = null;
    currentEntityKey = null;
}

// ============================================
// SUBMIT FORM — handles both create and update
// ============================================
async function submitForm(entityKey) {
    const config = entityConfigs[entityKey];
    const form = document.getElementById('modalForm');
    const formData = new FormData(form);

    const payload = {};
    config.fields.forEach(f => {
        const raw = formData.get(f.key);
        if (f.type === 'number') {
            payload[f.key] = Number(raw);
        } else if (f.type === 'select') {
            payload[f.key] = raw ? { id: Number(raw) } : null;
        } else {
            payload[f.key] = raw;
        }
    });

    const isEdit = currentEditId !== null;
    const url = isEdit ? `${config.apiPath}/${currentEditId}` : config.apiPath;
    const method = isEdit ? 'PUT' : 'POST';

    try {
        const res = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.status === 400) {
            const errorData = await res.json();
            const msg = errorData.message || 'Validation failed. Please check your input.';
            showToast(msg, 'error');
            return;
        }

        if (res.status === 401) {
            showToast('Session expired — please log in again', 'error');
            setTimeout(() => window.location.href = '/login.html', 1500);
            return;
        }

        if (!res.ok) {
            const errorData = await res.json().catch(() => ({}));
            throw new Error(errorData.message || 'Request failed with status ' + res.status);
        }

        showToast(`${config.title} ${isEdit ? 'updated' : 'created'} successfully`);
        closeModal();
        loadTable(entityKey);
        loadDashboardStats(); // keep dashboard counts in sync
    } catch (err) {
        console.error(`Failed to save ${entityKey}:`, err);
        showToast(err.message || `Could not save ${config.title.toLowerCase()}`, 'error');
    }
}

// ============================================
// DELETE RECORD — with confirmation dialog
// ============================================
async function deleteRecord(entityKey, id) {
    const config = entityConfigs[entityKey];
    if (!confirm(`Delete this ${config.title.toLowerCase()}? This cannot be undone.`)) return;

    try {
        const res = await fetch(`${config.apiPath}/${id}`, { method: 'DELETE' });

        if (res.status === 401) {
            showToast('Session expired — please log in again', 'error');
            setTimeout(() => window.location.href = '/login.html', 1500);
            return;
        }

        if (!res.ok) {
            const errorData = await res.json().catch(() => ({}));
            throw new Error(errorData.message || 'Delete failed with status ' + res.status);
        }

        showToast(`${config.title} deleted`);
        loadTable(entityKey);
        loadDashboardStats();
    } catch (err) {
        console.error(`Failed to delete from ${entityKey}:`, err);
        showToast(err.message || `Could not delete ${config.title.toLowerCase()}`, 'error');
    }
}

// ============================================
// Load the right table whenever a nav section is opened
// ============================================
navItems.forEach(item => {
    item.addEventListener('click', () => {
        const target = item.getAttribute('data-section');
        if (entityConfigs[target]) {
            loadTable(target);
        }
    });
});

// Load dashboard stats when the page first opens
loadDashboardStats();