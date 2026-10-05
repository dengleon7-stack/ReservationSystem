# Hotel Reservation System

A Java Swing desktop app for making a hotel reservation: guest details, room selection, booking dates, payment, and a final summary — with a JUnit 5 test suite covering each panel.

## What changed from the original IntelliJ project

This was originally a bare IntelliJ project (`.idea/`, `.iml`, with JUnit 5 added as an IntelliJ-managed library and all `.java` files sitting together in one `src/` folder). To make it open and run cleanly in **VS Code** with no manual setup, it's been converted to a standard **Maven** project:

- `src/` was split into `src/main/java` (the actual app: `Main`, `ReservationFrame`, and the five panel classes) and `src/test/java` (all `*Test.java` files, plus the `SwingTestUtils` test helper).
- A `pom.xml` was added declaring **JUnit 5.14.0** as a dependency (matching the version the original project used) — Maven/VS Code will download it automatically, so you don't need to install anything by hand.
- The old `.idea/`, `.iml`, and compiled `out/` folder (IntelliJ-specific, not needed) were dropped in favor of `.vscode/` config and Maven's own `target/` build output.

The actual source code is untouched — only its folder layout and build setup changed.

## Structure

```
ReservationSystem/
├── .vscode/
│   ├── launch.json      # Run configuration for Main
│   └── settings.json
├── pom.xml               # Maven build file — declares JUnit 5 as a dependency
├── src/
│   ├── main/java/
│   │   ├── Main.java
│   │   ├── ReservationFrame.java
│   │   ├── GuestInfoPanel.java
│   │   ├── RoomSelectionPanel.java
│   │   ├── BookingDatesPanel.java
│   │   ├── PaymentPanel.java
│   │   └── ReservationSummaryPanel.java
│   └── test/java/
│       ├── SwingTestUtils.java
│       ├── GuestInfoPanelTest.java
│       ├── RoomSelectionPanelTest.java
│       ├── BookingDatesPanelTest.java
│       ├── PaymentPanelTest.java
│       ├── ReservationSummaryPanelTest.java
│       └── ReservationFrameTest.java
└── .gitignore
```

## Opening in VS Code

1. Install the **Extension Pack for Java** (Microsoft) if you don't already have it.
2. **File → Open Folder...** and select this `ReservationSystem` folder.
3. Wait for the Java extension to detect `pom.xml` and download JUnit 5 in the background (needs an internet connection the first time — look for activity in the bottom status bar).
4. Once it's done indexing, you're ready to run or test.

## Running the app

- Open `src/main/java/Main.java` and click the **Run** codelens above `main`, **or**
- Use the **"Run Main (Hotel Reservation GUI)"** launch configuration in the Run and Debug panel, **or**
- From a terminal: `mvn exec:java`

## Running the tests

- Open any `*Test.java` file in `src/test/java` — you'll see **Run Test | Debug Test** links above each `@Test` method and above the class itself.
- Or run the whole suite from a terminal: `mvn test`

## Notes

- `maven.compiler.release` in `pom.xml` is set to **17** for broad compatibility. The original IntelliJ project targeted JDK 25 — if you have JDK 25 installed and want to match that exactly, change `<maven.compiler.release>17</maven.compiler.release>` to `25` in `pom.xml`.
