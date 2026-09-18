package com.sere.filemanager.core.remote

object WebManagerPage {
    fun render(status: com.sere.filemanager.core.model.RemoteSession, config: RemoteConfig = RemoteConfig(), currentPath: String = "/sdcard", message: String? = null): String {
        val pin = status.pin.orEmpty()
        val uploads = if (config.allowUploads) "enabled" else "disabled"
        val destructive = if (config.allowDelete) "enabled" else "disabled"
        val pinLabel = if (config.requirePin) "required" else "disabled"
        val network = if (config.localNetworkOnly) "local Wi‑Fi only" else "any active network"
        return """
            <!doctype html><html lang="en"><head><meta charset="utf-8"/><meta name="viewport" content="width=device-width, initial-scale=1"/><title>FileManager Remote</title>
            <style>:root{color-scheme:dark;font-family:system-ui,-apple-system,BlinkMacSystemFont,sans-serif}body{margin:0;background:#09090b;color:#fafafa}header{padding:18px;background:linear-gradient(135deg,#2563eb,#7c3aed)}main{padding:16px;max-width:960px;margin:0 auto}.card{border:1px solid #27272a;border-radius:18px;padding:16px;margin:12px 0;background:#18181b}.row{display:flex;gap:8px;flex-wrap:wrap;align-items:center}input,button{border-radius:12px;border:1px solid #3f3f46;padding:10px 12px;background:#09090b;color:#fafafa}button{background:#2563eb;border:none;cursor:pointer}button.danger{background:#dc2626}button.secondary{background:#3f3f46}code{background:#27272a;padding:2px 6px;border-radius:6px}a{color:#93c5fd}.entry{display:grid;grid-template-columns:1fr auto auto;gap:8px;align-items:center;padding:10px;border-bottom:1px solid #27272a}.muted{color:#a1a1aa;font-size:12px}pre{white-space:pre-wrap;background:#09090b;border-radius:12px;padding:12px;max-height:260px;overflow:auto}</style></head>
            <body><header><h1>FileManager Remote</h1><p>Watch storage browser · $network</p></header><main>
            <section class="card"><h2>Connection</h2><p>State: <b>${status.state}</b></p><p>URL: <code>${status.url.orEmpty()}</code></p><p>PIN: <code>$pin</code> ($pinLabel)</p>${message?.let { "<p>$it</p>" }.orEmpty()}</section>
            <section class="card"><h2>Server settings</h2><p>Uploads: <b>$uploads</b></p><p>Delete/rename: <b>$destructive</b></p><p>Auto stop: <b>${config.autoStopMinutes} min</b></p><p class="muted">Change these settings from the watch or phone companion before starting the server.</p></section>
            <section class="card"><h2>Browse</h2><div class="row"><input id="path" value="$currentPath" style="flex:1"/><button onclick="loadList()">Open</button><button class="secondary" onclick="goUp()">Up</button></div><div id="list"></div></section>
            <section class="card"><h2>Actions</h2><div class="row"><input id="mkdir" placeholder="New folder name"/><button onclick="mkdir()">Create folder</button></div><div class="row"><input id="upload" type="file"/><button onclick="upload()">Upload</button></div><p class="muted">Uploads/delete/rename depend on server settings.</p></section>
            <section class="card"><h2>Audit</h2><div class="row"><button class="secondary" onclick="loadAudit()">Refresh audit</button><button class="secondary" onclick="exportAudit()">Export text</button></div><pre id="audit">No audit loaded</pre></section>
            </main><script>
            const pin="$pin";function esc(s){return String(s).replace(/[&<>\"]/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c]))}
            async function api(url,options){const r=await fetch(url+(url.includes('?')?'&':'?')+'pin='+encodeURIComponent(pin),options);if(!r.ok)throw new Error(await r.text());return r}
            async function loadList(){const p=document.getElementById('path').value;const r=await api('/api/list?path='+encodeURIComponent(p));const data=await r.json();const list=document.getElementById('list');list.innerHTML='';(data.items||[]).forEach(i=>{const d=document.createElement('div');d.className='entry';const open=document.createElement('a');open.href='#';open.textContent=(i.directory?'📁 ':'📄 ')+i.name;open.onclick=()=>{if(i.directory){document.getElementById('path').value=i.path;loadList()}else window.location='/api/download?path='+encodeURIComponent(i.path)+'&pin='+encodeURIComponent(pin)};const rename=document.createElement('button');rename.className='secondary';rename.textContent='Rename';rename.onclick=()=>renameItem(i.path,i.name);const del=document.createElement('button');del.className='danger';del.textContent='Delete';del.onclick=()=>deleteItem(i.path,i.name);d.appendChild(open);d.appendChild(rename);d.appendChild(del);list.appendChild(d)})}
            function goUp(){const el=document.getElementById('path');const p=el.value.replace(/\/$/,'');el.value=p.substring(0,p.lastIndexOf('/'))||'/';loadList()}
            async function mkdir(){const name=document.getElementById('mkdir').value;const path=document.getElementById('path').value;await api('/api/mkdir?path='+encodeURIComponent(path)+'&name='+encodeURIComponent(name),{method:'POST'});loadList();loadAudit()}
            async function upload(){const f=document.getElementById('upload').files[0];if(!f)return;const path=document.getElementById('path').value;await api('/api/upload?path='+encodeURIComponent(path)+'&name='+encodeURIComponent(f.name),{method:'POST',body:f});loadList();loadAudit()}
            async function renameItem(path,oldName){const name=prompt('New name',oldName);if(!name)return;await api('/api/rename?path='+encodeURIComponent(path)+'&name='+encodeURIComponent(name),{method:'POST'});loadList();loadAudit()}
            async function deleteItem(path,name){if(!confirm('Delete '+name+'?'))return;await api('/api/delete?path='+encodeURIComponent(path),{method:'POST'});loadList();loadAudit()}
            async function loadAudit(){const r=await api('/api/audit');const data=await r.json();document.getElementById('audit').textContent=JSON.stringify(data,null,2)}
            async function exportAudit(){const r=await api('/api/audit/export');document.getElementById('audit').textContent=await r.text()}
            loadList().catch(e=>document.getElementById('list').innerText=e.message);loadAudit().catch(()=>{});
            </script></body></html>
        """.trimIndent()
    }
}
