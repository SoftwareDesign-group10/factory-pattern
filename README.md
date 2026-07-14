# YorkU Conference Room Scheduler — D2 Implementation

## How to import into Eclipse (plain Java project, no Maven)

**If you imported an earlier version of this project, DELETE it from your
workspace first** (right-click the old project → Delete → check "Delete
project contents on disk" if it was copied into the workspace). A stale
old project sitting next to this one causes exactly the
"Could not find or load main class" error.

1. Unzip this folder somewhere (NOT inside your Eclipse workspace folder —
   pick Documents or Desktop; Eclipse will copy it in during import).
2. Copy your `javacsv.jar` into the `lib/` folder, named exactly
   `javacsv.jar` (same jar as the csv-example project). Delete the
   placeholder `.txt` file.
3. Eclipse: `File → Import → General → Existing Projects into Workspace`
4. **Select root directory** → Browse to the unzipped `D2-scheduler`
   folder → the project appears checked → tick **"Copy projects into
   workspace"** → Finish.
5. Wait for the build (bottom-right progress bar). Then open
   `src → com.group10.scheduler.gui → MainUI.java`, right-click →
   **Run As → Java Application**.

If you see "Could not find or load main class": the project didn't build.
Check `Window → Show View → Problems` — 99% of the time it's the missing
`javacsv.jar` (step 2). Fix that, then `Project → Clean...` and re-run.

## Project structure (one flat `src`, standard Eclipse style)

```
src/
  com.group10.scheduler.domain           → Room, Booking, Payment, managers, enums
  com.group10.scheduler.domain.state     → State pattern (Booking lifecycle)
  com.group10.scheduler.domain.strategy  → Strategy pattern (Payment methods)
  com.group10.scheduler.domain.account   → User hierarchy, Factory Method (Farid's, integrated)
  com.group10.scheduler.persistence      → Repository interfaces (Adapter Target)
  com.group10.scheduler.persistence.csv  → CSV Adapter implementations
  com.group10.scheduler.facade           → SchedulerFacade (Facade pattern)
  com.group10.scheduler.gui              → Swing GUI (MainUI is the entry point)
  com.group10.scheduler.test             → SmokeTest (optional end-to-end check, run like any main())
lib/        → put javacsv.jar here
data/       → CSV files get created here when the app runs
```

## Pattern status

| Package / files | Pattern | Owner | Status |
|---|---|---|---|
| `domain.account` (factory, user hierarchy, validator) | Factory Method | Farid | **Integrated** from the factory-pattern repo, with fixes — see `CHANGES.md` |
| `ChiefEventCoordinator` | Singleton | Farid | **Not built yet** (only an `Administrator` data class exists) |
| `facade.SchedulerFacade` | Facade | Farid | Placeholder — works, needs Farid's confirmation/ownership |
| `persistence` + `persistence.csv` | Adapter | Hiva | **Done** |
| `domain.state` | State | Hiva | Placeholder — transitions work, but Req4's 30-min window / deposit forfeiture rules not implemented |
| `domain.strategy` | Strategy | Hiva | Placeholder — `pay()`/`refund()` always succeed, no real validation |

Whoever replaces `ConcreteStates`/`ConcreteStrategies`: keep the helper
methods `ConcreteStates.fromStatus(BookingStatus)` and
`ConcreteStrategies.fromMethod(PaymentMethod)` (or equivalents) — the CSV
layer uses them to reconstruct state/strategy objects when loading.

## What works end-to-end right now

Register → login → search rooms → book (deposit charged) → check in
(sensor verified) → cancel/extend rules enforced by State pattern →
everything persisted to CSV and correctly reloaded on restart.

## Known gaps (see CHANGES.md "Still open" too)

- Req4's 30-minute check-in window + deposit forfeiture logic
- `verifyUniversityAccount()` (Req1 verification step)
- ChiefEventCoordinator Singleton + admin-generation flow in the GUI
- Real payment validation / collecting card details in the GUI
