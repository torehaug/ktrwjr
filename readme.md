SVN import of https://code.google.com/p/ktrwjr/ repository.

Changes from the original:
- Ant build.xml updated to use Apache Ivy for dependencies
- Ant build.xml updated with a war target to create the sample-app
- Maven pom.xml added for simple import as a maven project in Eclipse

Features:
- com.google.apphosting dependency removed. OverQuotaException is not handeled anymore.


ktrwjr
======
Kotori Web JUnit Runner - A junit runner for GAE/j Production Server.

Code license: Apache License 2.0

What Is Kotori Web JUnit Runner(ktrwjr)?
----------------------------------------
ktrwjr is a JUnit runner web application for GAE/j.

Haven't you ever wanted to execute your tests on the Production Server?
With ktrwjr, you can do it easily.
