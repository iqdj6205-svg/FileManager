# Remote Write API

Remote write endpoints are now present behind safety flags.

## Endpoints

- `POST /api/rename?path=/sdcard/a.txt&name=b.txt&pin=123456`
- `POST /api/delete?path=/sdcard/a.txt&pin=123456`
- `POST /api/mkdir?path=/sdcard/Download&name=NewFolder&pin=123456`
- `POST /api/upload?path=/sdcard/Download&name=file.bin&pin=123456`

## Safety defaults

- Rename/delete require `allowDelete=true`.
- Mkdir/upload require `allowUploads=true`.
- PIN is required when `requirePin=true`.
- Protected paths and traversal are blocked.

## Current limitation

Upload endpoint is reserved and does not parse multipart body yet.
