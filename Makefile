# Директории
SRC_DIR = src/main/java
TEST_DIR = src/test/java
BUILD_DIR = build/main
TEST_BUILD_DIR = build/test
LIB_DIR = lib
MAIN_CLASS = org.example.Main
JUNIT_STANDALONE = $(LIB_DIR)/junit-platform-console-standalone-1.10.0.jar

.PHONY: run compile compile-tests test clean

run: compile
	@echo "Running program..."
	@java -cp $(BUILD_DIR) $(MAIN_CLASS)

compile:
	@echo "Compiling Java sources..."
	@mkdir -p $(BUILD_DIR)
	@find $(SRC_DIR) -name "*.java" > sources.txt
	@javac -d $(BUILD_DIR) @sources.txt
	@rm -f sources.txt
	@echo "Compilation completed!"

compile-tests: compile
	@echo "Compiling tests..."
	@mkdir -p $(TEST_BUILD_DIR)
	@find $(TEST_DIR) -name "*.java" > test_sources.txt
	@javac -d $(TEST_BUILD_DIR) -cp "$(BUILD_DIR):$(JUNIT_STANDALONE)" @test_sources.txt
	@rm -f test_sources.txt
	@echo "Test compilation completed!"

test: compile-tests
	@echo "Running JUnit tests..."
	@java -jar $(JUNIT_STANDALONE) --class-path "$(BUILD_DIR):$(TEST_BUILD_DIR)" --scan-class-path

clean:
	@echo "Cleaning build directory..."
	@rm -rf build
	@echo "Clean completed!"
