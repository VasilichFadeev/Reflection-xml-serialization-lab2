# Директории
SRC_DIR = src/main/java
TEST_DIR = src/test/java
BUILD_DIR = build/main/java
TEST_BUILD_DIR = build/test/java
MAIN_CLASS = org.example.Main

# Компиляция проекта
compile:
	@echo "Compiling Java sources..."
	@mkdir -p $(BUILD_DIR)
	@find $(SRC_DIR) -name "*.java" > sources.txt
	@javac -d $(BUILD_DIR) @sources.txt
	@rm -f sources.txt
	@echo "Compilation completed!"

# Компиляция тестов
compile-tests: compile
	@echo "Compiling tests..."
	@mkdir -p $(TEST_BUILD_DIR)
	@find $(TEST_DIR) -name "*.java" > test_sources.txt
	@javac -d $(TEST_BUILD_DIR) -cp $(BUILD_DIR) @test_sources.txt
	@rm -f test_sources.txt
	@echo "Test compilation completed!"

# Запуск приложения
run: compile
	@echo "Running application..."
	@java -cp $(BUILD_DIR) $(MAIN_CLASS)

# Запуск тестов
test: compile-tests
	@echo "Running tests..."
	@java -cp "$(BUILD_DIR):$(TEST_BUILD_DIR)" org.example.Test

# Очистка скомпилированных файлов
clean:
	@echo "Cleaning build directory..."
	@rm -rf build *.xml
	@echo "Clean completed!"

# Помощь
help:
	@echo "Available targets:"
	@echo "  make compile - Compile Java sources"
	@echo "  make run     - Compile and run the application"
	@echo "  make clean   - Remove build and xml files"
	@echo "  make test    - Run tests"
	@echo "  make help    - Show this help"

.PHONY: compile run clean tree help