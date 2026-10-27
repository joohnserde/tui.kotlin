# tui.kotlin Audit Report

## What this repo does
A Kotlin library for building Text User Interfaces (TUI) in the terminal. It provides components (Box, Column, Text, Border, Canvas, AsciiArt) and a builder DSL that emits ANSI escape sequences. Single developer tool, no server, no persistence, no multi-user load.

**Load assumed:** One person running locally. No concurrent processes, no production traffic, no data durability requirements.

---

## Must fix

### 1. Internal JDK API used (`WidgetBuilder.kt:1`, `Box.kt:4`, `Column.kt:4`)
- **What this is:** `import com.sun.tools.javac.tree.TreeInfo` appears in three files.
- **Problem:** This is Oracle-internal API. It does not exist on OpenJDK, GraalVM, Android, or any non-Oracle build. The code will fail to compile or crash at runtime.
- **Fix:** Delete the import. It is not used anywhere (grep shows zero references to `TreeInfo`).
- **If we skip it:** Build breaks on any non-Oracle JDK. CI will pass locally but fail elsewhere.

### 2. Terminal size detection Linux-only (`TermManager.kt:16-39`)
- **What this is:** `getTerminalDimension()` runs `stty size` with stdin redirected to `/dev/tty`.
- **Problem:** Fails on Windows, in Docker without TTY, in CI, over SSH without a pty. The `try/catch` swallows the exception and returns garbage dimensions.
- **Fix:** Use ANSI query `ESC[18t` (DECREQTPARM) or add `jline-terminal` (small, cross-platform). Keep `stty` as fallback.
- **If we skip it:** Users on Windows or CI get wrong layout or crashes.

### 3. AWT `Color` ties to desktop JVM (`CharStyle.kt:3`, `LiteralCharStyle.kt:3`, all components)
- **What this is:** Every color uses `java.awt.Color`.
- **Problem:** AWT is not available on GraalVM native-image, Android, Kotlin/JS, Kotlin/WASM. Adds ~2 MB to native image.
- **Fix:** Create `data class Color(val r: Int, val g: Int, val b: Int, val a: Int = 255)` in `model`. Replace all `java.awt.Color` usages. One-file change.
- **If we skip it:** Library cannot be used in native, mobile, or web targets.

### 4. `Text.buildMultiline()` returns empty (`Text.kt:56-58`)
- **What this is:** `buildMultiline()` returns `RawContent()` with no content added.
- **Problem:** Calling `text(singleLine=false)` produces zero output silently. No error, no warning.
- **Fix:** Either implement line-wrapping logic or throw `UnsupportedOperationException("multiline not implemented")`.
- **If we skip it:** Users waste hours debugging why long text disappears.

### 5. Two `TuiManager` instances in `main` (`Main.kt:84-86`)
- **What this is:** `TuiManager().write(...)` then `TuiManager().write2File(...)`.
- **Problem:** Second instance loses cursor state (hide/save/restore) from first. File gets raw content without cursor control codes.
- **Fix:** Use one instance: `val mgr = TuiManager(); mgr.write(...); mgr.write2File(...)`.
- **If we skip it:** File output corrupted; terminal cursor state leaks.

### 6. `Border` swallows exceptions (`Border.kt:163-166`, `182-185`, `201-204`)
- **What this is:** Three `try/catch` blocks catch `BorderException`, print empty string, rethrow.
- **Problem:** Stack trace loses original context. `println("")` does nothing useful.
- **Fix:** Remove the `try/catch` entirely. Let the exception bubble with full stack trace.
- **If we skip it:** Debugging border bugs is harder; no functional change.

---

## Should fix

### 7. No tests exist (entire repo)
- **What this is:** Zero test files. `build.gradle.kts` declares `kotlin("test")` but no `src/test` directory.
- **Problem:** Every refactor is a gamble. `Border` (262 lines), `Canvas`, `Text` logic unverified.
- **Fix:** Add `src/test/kotlin` with at least: `OffsetTest`, `RawContentTest`, `CanvasTest`, `BorderTest` (one arrangement), `TextTest` (single-line).
- **If we skip it:** Any change risks silent regressions.

### 8. `TuiBuilder` mutable state leaked (`WidgetBuilder.kt:22-25`, `Box.kt:26`, `Column.kt:26`)
- **What this is:** `getWidgets` returns the internal `StringBuilder`. Callers can mutate it.
- **Problem:** `Box.appendTui()` and `Column.appendTui()` append to `layer.stringLayer` but `getWidgets` stays mutable. External code can corrupt builder state.
- **Fix:** Make `getWidgets` return `String` (immutable snapshot). Or return `RenderResult` data class.
- **If we skip it:** Hard to debug when output gets corrupted by caller.

### 9. `Layer.stringLayer` mutable and exposed (`Layer.kt:7`, `10`)
- **What this is:** `internal val stringLayer: StringBuilder` with getter returning `String`.
- **Problem:** Any code in module can mutate the layer directly, bypassing `Layout` contract.
- **Fix:** Make `stringLayer` `private`. Provide `append(String)` and `prepend(String)` on `Layer`.
- **If we skip it:** Inconsistent state possible; harder to add validation later.

### 10. `Border` 262 lines of duplicated cursor logic (`Border.kt:97-261`)
- **What this is:** Each `Arrangement` variant repeats `moveTo` + char append patterns.
- **Problem:** Bug in one arrangement (e.g., off-by-one in `buildVerticalLine`) must be fixed in 7 places. `Arrangement.FULL` calls `buildBorderLine()` which calls both horizontal and vertical, duplicating corners.
- **Fix:** Extract `drawLine(start: Offset, end: Offset, char: Char)` and `drawCorners()`. One `when` on arrangement calling shared helpers.
- **If we skip it:** Next border feature (double-line, rounded) multiplies duplication.

### 11. `AsciiArt.insertAsciiArt()` dead code (`AsciiArt.kt:34-36`)
- **What this is:** Empty function, never called (grep confirms).
- **Problem:** Dead code confuses readers. Suggests unfinished feature.
- **Fix:** Delete it.
- **If we skip it:** Minor confusion only.

### 12. `Row.kt` entirely commented out (`Row.kt:3-26`)
- **What this is:** Full class commented out with `/* */`.
- **Problem:** Dead code in source tree. Not in git history (single commit?).
- **Fix:** Delete file or restore if needed.
- **If we skip it:** Noise for readers.

### 13. `TermManager.clearScreen()` duplicates escape code (`TermManager.kt:44`)
- **What this is:** Hardcodes `"\u001b[H\u001b[2J"`.
- **Problem:** `LiteralCursor.CLEAR` already defines `"${AnsiEscape.ESC}2J"`. Two sources of truth.
- **Fix:** Use `LiteralCursor.CLEAR` + `LiteralCursor.moveTo(Offset(1,1))`.
- **If we skip it:** Inconsistent if escape codes change.

### 14. Package structure inconsistent (`api` vs `core` vs root)
- **What this is:** `Arrangement`, `Offset`, `Layer`, `RawContent`, `AnsiEscape` in root `tui.kotlin`. `Cursor` in `api.navigation.cursor`, `LiteralCursor` in `core.navigation.cursor`. Same split for `CharStyle`/`LiteralCharStyle`.
- **Problem:** No clear public vs internal boundary. `internal` package exists but `TuiManager` (main API) sits in root.
- **Fix:** Flatten to `tui.kotlin.{model, component, style, navigation, terminal, render}`. Move `TuiManager` to `terminal` or `render`.
- **If we skip it:** New contributors cannot guess where to put things.

### 15. `Layout` interface anemic (`Layout.kt:5-7`)
- **What this is:** Only `val layer: Layer`.
- **Problem:** `Box` and `Column` implement `Layout` but also extend `TuiBuilder`. `TuiManager.write()` does `if (layout is Box)` — breaks abstraction.
- **Fix:** Either make `Layout` a sealed interface with `render(): RenderResult`, or delete it and use `TuiBuilder` directly.
- **If we skip it:** Polymorphism is fake; `instanceof` checks spread.

### 16. `RawContent.add()` overloads instead of operator (`RawContent.kt:10-24`)
- **What this is:** Four `add()` methods for `RawContent`, `StringBuilder`, `String`, `Char`.
- **Problem:** Verbose call sites: `rc.add(other.content)`. No `+` operator.
- **Fix:** Add `operator fun StringBuilder.plusAssign(rc: RawContent)` and `operator fun RawContent.plus(other: RawContent): RawContent`.
- **If we skip it:** Minor ergonomics only.

### 17. `BorderException` and `TermManagerException` empty (`BorderException.kt:3`, `TermManagerException.kt:3`)
- **What this is:** Only hold a message. No error codes, no recovery hints.
- **Problem:** Callers cannot distinguish "bad dimensions" from "bad arrangement" programmatically.
- **Fix:** Add `enum class BorderError { INVALID_DIMENSION, INVALID_ARRANGEMENT }` and `val error: BorderError`.
- **If we skip it:** All errors caught as generic `Exception`.

---

## Nice to have

### 18. `Canvas.buildCanvas()` uses `while` loop (`Canvas.kt:54-59`)
- **What this is:** `while (tempHeight <= dimension.first)` with manual increment.
- **Problem:** Idiomatic Kotlin: `repeat(dimension.first + 1) { row -> ... }`.
- **Fix:** Replace with `repeat`. Same logic, readable.
- **If we skip it:** Works fine; style only.

### 19. `Offset` componentN missing (`Offset.kt:3-7`)
- **What this is:** `data class Offset(val row: Int, val col: Int)` — destructuring works but no `component1()`/`component2()` explicit.
- **Problem:** None; data class generates them. But `var (row, col) = offset` in `AsciiArt.kt:44` works by accident of declaration order.
- **Fix:** None needed. Just note: order matters for destructuring.
- **If we skip it:** No issue.

### 20. `AnsiEscape.ESC` hardcoded (`AnsiEscape.kt:8`)
- **What this is:** `const val ESC = "\u001B["`.
- **Problem:** Some terminals use `ESC` (0x1B) vs `CSI` (0x9B). Rare but real.
- **Fix:** Keep as is. Document assumption. Not worth abstraction.
- **If we skip it:** Works on 99% of terminals.

---

## Verdict
Not production-ready. Critical: internal JDK import, Linux-only terminal detection, AWT dependency, silent multiline bug. Must fix 1–6 before any release. Should fix 7–17 for maintainability.

## Lean: -310 lines, -1 dependency possible.
- Delete `com.sun.tools.javac.tree.TreeInfo` imports (3 lines)
- Delete `Row.kt` (27 lines)
- Delete `AsciiArt.insertAsciiArt()` (3 lines)
- Delete `Border` try/catch blocks (30 lines)
- Replace `Border` duplication with helpers (~150 lines saved)
- Replace `java.awt.Color` with own `Color` (-1 implicit dependency on AWT)
- Remove `stty` dependency (external command)

## Not checked
- No performance benchmarks run
- No native-image / GraalVM test
- No Windows terminal test
- No ANSI capability detection (true color, 256-color, no color)
- No input/keyboard handling code (only output)
- No integration with existing TUI apps
- Build artifacts (`build/`, `*.jar`) not audited