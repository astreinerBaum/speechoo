SpeechOO is a voice recognition extension for OpenOffice.org, LibreOffice and BrOffice.
It is still alpha stage. Fell free to download the source code and build it to try.
Check our milestones for new alpha releases.

An initial prototype is available, but not connecting to the voice recognition libraries yet. It proves the concept of running mixed extension: Java plus C++ wrapped with JNI.

Are you able to develop OOo extensions or would like to learn how to do it? Join this group and become member of the development team!

## Tutorial ##
Check http://www/falabrasil/files/manual.pdf for a instalation tutorial for version 1.0.0

## Video tutorial (Deprecated) ##
Check http://www.youtube.com/watch?v=bcc9tvnHjr8 for a video tutorial for version 0.0.4. Portuguese only.

## Version available ##
SpeechOO is vailable in downloads section. This version works only under GNU/Linux systems. This can get utterances from Julius and append it to the current document.
To use the extension, install the .oxt using Extension Manager and configure SpeechOO using Tools > Options > OpenOffice.org Writer > SpeechOO Options.<a href='Hidden comment:  Set the path to a Julius configuration file (.jconf) which gives the models for the recognition process.'></a> We suggest the free models bellow.

**[VoxForge](http://www.voxforge.org/) English Model** http://www.voxforge.org/home/downloads

**[FalaBrasil](http://www.laps.ufpa.br/falabrasil) Brazilian Portuguese Model (dictation)** http://www.laps.ufpa.br/falabrasil/speechoo.php

**warning:** the extension is experimental and can damage your OOo profile folder.

**Requirements:** Java 1.6 (or similar). Ubuntu requires the installation of openoffice.org-java-common package.

The project is managed by members of [Fala Brasil/LaPS](http://www.laps.ufpa.br/falabrasil) and [CCSL-IME/USP](http://ccsl.ime.usp.br)