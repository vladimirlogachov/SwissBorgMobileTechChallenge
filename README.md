# SwissBorgMobileTechChallenge 

### Notes:

1) Written with Compose Multiplatform and supports two targets `Android` and `iOS`, so you can check
   both if you want to.

2) I decided not to split `shared` module onto 3 separate ones just for the sake of simplicity, but
   actually they should be separated to have proper control over dependecies.

3) Connecivity state updates based on native network monitoring on Android and Apple devices.
   This means that it properly updates state only if both WiFi and Mobile Data disabled. If this
   feature is going to be tested on emulated devices, app won't react and no `Connection lost` banner
   will be displayed. Yeah probably I should've periodically ping some endpoints instead, as this way
   we can be 100% sure that connection lost even if both or one of WiFi and Mobile Data enabled, but
   decided not to, probably that was a mistake.

5) I decided to additionally add some Unit Tests for `data` layer at the very beginning, just to be sure
   that custom deserialization works as expected. I was really confused by response scheme, never faced
   such (imho weird) structure before.

6) Just in case you want to see some `presentation` layer multiplatform tests, here is my "portfolio"
   [MoviesPot](https://github.com/vladimirlogachov/MoviesPot) app.
   
