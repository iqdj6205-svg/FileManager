# Storage Analyzer

The storage analyzer should help users understand what consumes space on a watch.

## First version

- Analyze the current accessible directory
- Group by file type
- Show largest files
- Show total visible size

## Later version

- Recursive scan with cancellation
- Duplicate detection
- Cache cleanup suggestions
- App-specific storage view
- Phone companion dashboard

## Battery rules

Recursive scans should require explicit user action and must be cancellable. Avoid automatic background scans on watches.
