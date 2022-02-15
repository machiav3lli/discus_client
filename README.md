# DISCUS Client

The JSON-encoder for DISCUS - the discussion promoting disc. This simple app is used to ease the process of creating and managing proejcts that meant to use [DISCUS](https://github.com/machiav3lli/discus_scripts).

- Wherever "the fair" is mentioned, ["Digital:Sovereignty Design Fair"](https://www.codingixd.org/design-fair-digitalsovereignty) is meant.

![](Banner.jpg)

## For whom is this app

This app is meant as a proof-of-concept for a client used by the adminstartions to mainstream deploying DISCUS.

## Usage

- The main page includes a list of the saved projects.
- New projects can be added, existing projects can be edited and deleted.
- The create button writes a JSON-file `/DISCUS/project_{id}.json` including the project's details.
- The JSON-file can be used easily in DISCUS by placing it in the `/flyer/` folder and renaming it to `project.json` without further ado.

The JSON scheme is:

- question: String: The project's main question

- webpage: String: The link to the project's web page (e.g. on mein.berlin)

- replyPositive: String: The general text users get when they choose the positive answer.

- replyNeutral: String: The general text users get when they choose the neutral answer.

- replyNegative: String: The general text users get when they choose the negative answer.

- argumentsPositive: StringList (with size 3): group of arguments that speak for the positive answer.

- argumentsNegative: StringList (with size 3): group of arguments that speak for the negative answer.

- invitation: String: The invitation text to the planned meeting.

- meetingDateTime: Calendar (Serializable as long): The planned date and time of the meeting.

## Build

Specify your Android SDK path either using the `ANDROID_HOME` environment variable, or by filling out the `sdk.dir` property in `local.properties`.

Run `./gradlew assembleRelease` to build the package, which can be installed using the Android package manager or run through Android Studio.

## License

**DISCUS Client** is licensed under the [GNU's GPL v3](LICENSE.md).
