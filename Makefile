
DATE:=$(shell date)
VERSION:=1.2

.PHONY: all
all: jar src-jar

.PHONY: manifest
manifest:
	rm -f manifest.md
	echo "Name: ColorPicker" >> manifest.md
	echo "Implementation-Build-Date: $(DATE)" >> manifest.md
	echo "Implementation-Version: $(VERSION)" >> manifest.md
	echo "Implementation-Vendor: Kevin Walsh" >> manifest.md
	echo "Built-By: $(USER)" >> manifest.md

.PHONY: compile
compile:
	javac -cp ./src/ -d . ./src/com/bric/swing/ColorPicker.java

.PHONY: jar
jar: compile manifest
	jar cfm colorpicker.jar manifest.md com resources LICENSE

.PHONY: src-jar
src-jar: compile manifest
	jar cfm colorpicker-src.jar manifest.md com resources src LICENSE README.md Makefile

.PHONY: clean
clean:
	rm -rf com colorpicker.jar colorpicker-src.jar manifest.md
