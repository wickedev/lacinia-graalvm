.PHONY: all test deps

PROJECT = lacinia
EXE = ./target/$(PROJECT)
JAR = ./target/simple-main.jar

all: test

test: $(EXE)
	lein run-native

deps: clean $(JAR)
	-java -jar -agentlib:native-image-agent=config-merge-dir=./graalvm-config $(JAR)
	@echo Configuration files written to $$(pwd)/graalvm-config

clean:
	-lein clean

$(JAR):
	@if [ ! -f $@ ]; then lein uberjar; fi

$(EXE): $(JAR)
	@if [ ! -f $@ ]; then lein native; fi
