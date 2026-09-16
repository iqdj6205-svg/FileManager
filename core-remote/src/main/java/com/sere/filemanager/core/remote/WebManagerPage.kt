package com.sere.filemanager.core.remote

object WebManagerPage {
    fun html(appName: String = "FileManager"): String = """
        <!doctype html>
        <html lang="en">
        <head>
          <meta charset="utf-8" />
          <meta name="viewport" content="width=device-width, initial-scale=1" />
          <title>${'$'}appName Remote</title>
          <style>
            :root { color-scheme: dark; font-family: system-ui, sans-serif; background: #0b0f14; color: #e8eef6; }
            body { margin: 0; padding: 24px; }
            main { max-width: 900px; margin: 0 auto; }
            .card { background: #121a24; border: 1px solid #243244; border-radius: 18px; padding: 18px; margin: 14px 0; }
            button, input { border-radius: 12px; border: 1px solid #37506c; padding: 10px 12px; background: #172333; color: #e8eef6; }
            button { cursor: pointer; }
            .danger { border-color: #8b2f35; color: #ffb4b4; }
            code { color: #90caf9; }
          </style>
        </head>
        <body>
          <main>
            <h1>${'$'}appName Remote</h1>
            <section class="card">
              <h2>Connect</h2>
              <p>Enter the PIN shown on the watch. Keep this page on a trusted local network.</p>
              <input placeholder="PIN" />
              <button>Unlock</button>
            </section>
            <section class="card">
              <h2>Files</h2>
              <p>Remote browsing API endpoints are prepared in the app core.</p>
              <code>/api/list?path=/sdcard</code>
            </section>
            <section class="card">
              <h2>Safety</h2>
              <p>Upload and delete actions should remain disabled until explicitly enabled on the watch.</p>
              <button class="danger">Delete actions disabled</button>
            </section>
          </main>
        </body>
        </html>
    """.trimIndent()
}
