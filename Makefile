.PHONY: all clean

all: compile run

compile:
	@echo "Compiling Java files..."
	javac *.java

run:
	@echo "Running Pong..."
	java Pong

