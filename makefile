SOURCE = src/
BUILD = build/
RESOURCE = resources/
MAIN_CLASS = game.Launcher

all:
		@echo "test"

compile: clean
		@mkdir -p $(BUILD)
		@javac -sourcepath $(SOURCE) -d $(BUILD) $(SOURCE)*/*.java
		@echo "Compilation completed"

run:
		@java -cp $(BUILD) $(MAIN_CLASS)

clean:
		@rm -rf $(BUILD)