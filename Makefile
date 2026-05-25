# 1. Detecta o Sistema Operacional e define o comando do Gradle
ifeq ($(OS),Windows_NT)
	GRADLEW = gradlew.bat
else
	GRADLEW = ./gradlew
endif

# 2. Configuração de verbosidade (O Silenciador)
HIDE = @
ifdef VERBOSE
	HIDE = 
endif

# 3. As Tarefas
run:
	$(HIDE)cd Game && $(GRADLEW) lwjgl3:run

build:
	$(HIDE)cd Game && $(GRADLEW) build

dist:
	$(HIDE)cd Game && $(GRADLEW) lwjgl3:dist

clean:
	$(HIDE)cd Game && $(GRADLEW) clean