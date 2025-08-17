# Makefile

# Variables
JAVAC = javac
JAVA = java
MAIN = Main

# Default target
all: $(MAIN).class

# Compile .java to .class
%.class: %.java
	$(JAVAC) $<

# Run the program
run: all
	$(JAVA) $(MAIN)

# Clean compiled files
clean:
	rm -f *.class
