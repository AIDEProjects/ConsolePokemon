#!/bin/bash

# 读取用户输入的版本号
read -p "请输入版本号 (例如 0.1.0): " version

# 创建输出目录
rm -r outputs
mkdir -p outputs

# 编译 Java 文件
# find src -name "*.java" -exec javac -Xlint:-options -source 10 -target 10 -d outputs {} +
find src -name "*.java" -exec javac -Xlint:-options -source 10 -target 10 -d outputs -cp gson-2.8.8.jar {} +


# 创建清单文件
echo "Manifest-Version: $version" > MANIFEST.MF
echo "Main-Class: Main" >> MANIFEST.MF
echo "Class-Path: gson-2.8.8.jar" >> MANIFEST.MF
echo "" >> MANIFEST.MF  # 确保文件以空行结束

# 创建 JAR 文件
cd outputs
jar cfm ../consolePokemon_v$version.jar ../MANIFEST.MF .

# 返回到原目录
cd ..

# 提示用户运行 JAR 文件
echo "JAR 文件创建完成: consolePokemon_v$version.jar"

zip "consolePokemon_v$version.zip" "consolePokemon_v$version.jar" "gson-2.8.8.jar"
echo "ZIP 文件创建完成: consolePokemon_v$version.zip"

java -jar consolePokemon_v$version.jar







