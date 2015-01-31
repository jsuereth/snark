# snark
Twitter command line client example (ne scala 2015)


## Building

1. Get a twitter developer API key
2. Create the file `core/src/main/scala/com/jsuereth/snark/EvilSecrets.scala` with:

```scala
package com.jsuereth.snark

// TODO - Read from a file, or environment variable...
object EvilSecrets {
  val apiKey = "<your api key>"
  val apiSecret = "<your api secret>"
}
```

3. Run `sbt debian:packageBin` or `sbt stage`
4. Results will be under `native/target/<deb>` or `native/target/universal/stage`


## Using

Type `snark` on the command line to get a twitter terminal.  From here you can type a command:

* `mentions` shows your mention timeline
* `check` shows your timeline
* `tweet <text>` issues a raw tweet
* `search <text>` searches for tweets with the given string.


