HIDE = @

ifdef VERBOSE
	HIDE =
endif

run:
	$(HIDE)cd Game && gradlew.bat lwjgl3:run

build:
	$(HIDE)cd Game && gradlew.bat build

clean:
	$(HIDE)cd Game && gradlew.bat clean 

dist:
	$(HIDE)cd Game && gradlew.bat lwjgl3:dist