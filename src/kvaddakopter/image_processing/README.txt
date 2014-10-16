F�r att k�ra bildbehandlings modulen i Eclipse:

1. Importera befintligt projekt
	- File -> Import
	- General -> Existing Projects into Workspace 
 	- Select Root Directory ->  v�lj mappen "image_processing". -> Finish

2. Importera n�dv�ndiga bibliotek (OpenCV exluderat)
	- H�gerklicka p� projektet ImageProcessing -> Properties
	- Java Build Path -> V�lj fliken Libraries
	- Add External JARs -> v�lj sedan alla JAR-filer som ligger i mappen "kvaddakopter/decoder/lib":
	  commons-cli-1.2, lockback-classic-1.1.2,lockback-core-1.1.2, slf4-api-1.7.7 och xuggle-xuggler-5.4

3. Importera OpenCV
	http://docs.opencv.org/trunk/doc/tutorials/introduction/java_eclipse/java_eclipse.html

	  
	

OBS! JAVA SE 7 beh�ver vara installerat, kan laddas ner h�r:
http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html


