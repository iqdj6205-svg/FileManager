# Test Feedback Backlog

## Observed on first watch test

- Rotary input works on some screens, not all. Need one shared list component across every Wear screen.
- Permission buttons should be hidden once media access is granted.
- Permission actions are duplicated in Files and Media; create one Storage/Permissions hub and show contextual shortcut only if missing.
- ADB Advanced screen needs concrete commands.
- Remote screen should show server address and PIN directly, not only in notification.
- Remote screen should show network status and warn if no network is available.

## Product decision

These are valid polish/product issues. The immediate milestone was successful launch on watch; that is achieved. Continue building real application logic while gradually folding these fixes into the UI.
