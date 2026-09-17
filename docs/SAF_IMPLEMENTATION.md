# SAF Implementation

## Added

- Document tree listing through `SafFileOperations`.
- Create folder, rename and delete through SAF.
- Stream copy and move matrix foundations:
  - SAF → SAF;
  - SAF → path;
  - path → SAF;
  - path → path through shared `SafeFileOperations`.
- Move uses copy plus delete after successful copy.
- Simple MIME guessing for path-to-SAF writes.
- Phone operation controller now routes SAF/path copy and move combinations.
- SAF conflict resolver for keep-both/replace/skip/fail strategies.
- SAF copy targets now avoid name collisions by default.

## Remaining

- Progress callbacks for recursive directory copies.
- Directory recursive copy via SAF.
- More detailed SAF errors.
