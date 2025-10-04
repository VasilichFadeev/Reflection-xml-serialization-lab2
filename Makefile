# Директории
SRC_DIR = src/main/java
BUILD_DIR = build/main/java
MAIN_CLASS = org.example.Main

# Компиляция проекта
compile:
	@echo "Compiling Java sources..."
	@mkdir -p $(BUILD_DIR)
	@find $(SRC_DIR) -name "*.java" > sources.txt
	@javac -d $(BUILD_DIR) @sources.txt
	@rm -f sources.txt
	@echo "Compilation completed!"

# Запуск приложения
run: compile
	@echo "Running application..."
	@java -cp $(BUILD_DIR) $(MAIN_CLASS)

# Очистка скомпилированных файлов
clean:
	@echo "Cleaning build directory..."
	@rm -rf build
	@echo "Clean completed!"

# Помощь
help:
	@echo "Available targets:"
	@echo "  make compile - Compile Java sources"
	@echo "  make run     - Compile and run the application"
	@echo "  make clean   - Remove build files"
	@echo "  make tree    - Show project structure"
	@echo "  make help    - Show this help"

.PHONY: compile run clean tree help