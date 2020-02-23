# AndroidDataAnalyserSG

#### This Android Data Analyser is a mobile application which fetches data from the Sinporean Govt Data Open Source and display the mobile data usage for every annual year with a described format. This application is build based on MVVM Architecture in Kotlin.

###### You can check the data and other details [here](https://data.gov.sg/dataset/mobile-data-usage).


## Built With
- Kotlin Coroutine - for Thread handling and async task
- retrofit2 - networking library
- dagger2 - dependency injection framework
- robolectric, mockito, JUnit4 -for testing
- espresso - UI testing

## Installation
- Take a clone or download of the project.
- Wait for the project to download dependency. (This will take some time for the first time.)
- Run the app in emulators or connect an physical android device and click run. (Make sure the emulator or physical device Api level is between 23-28)
- Switch to retrofit_branch and Run again to see how retrofit handle the caching. (git checkout retrofit_cahing)
- Switch to koin_di branch to see koin implementation replacing dagger. (git checkout koin_di)
- Run at all instances offline and online mode.


