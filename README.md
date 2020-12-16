# Deprecation Notice

Maintaining a demo-app is not sustainable if there are no users.
Moreover, I recommend to avoid too much native testing and focus on cross-platform testing instead (e.g. via https://capacitorjs.com or Flutter).

____

# Ndef Demo Android
Simple Android NFC app to demonstrate Ndef messages with instrumented tests (UI tests).
Beside of testing, this app can be used for performance-profiling of NFC-related code.

The instrumented tests fire stubbed NFC Intents to trigger NFC actions.

When handling NFC Intents, we distinguish between cold starts and foreground dispatch.

