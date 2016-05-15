:: This file copies extended policy files to java home
:: This should allow user to use long keys.

@ECHO OFF
ECHO Enabling longer keys by copying files...
copy /Y "UnlimitedJCEPolicy\*.jar" /B "%JAVA_HOME%\jre\lib\security\" /B
ECHO Done