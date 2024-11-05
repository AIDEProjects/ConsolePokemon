#!/bin/bash

# 定义配置变量

# 读取用户输入的版本号
read -p "请输入版本号 (例如 0.1.0): " version

# 清理之前的构建文件，创建输出目录
rm consolePokemon*.jar
rm consolePokemon*.zip
rm -r outputs
mkdir -p outputs

# 编译 Java 文件，添加 myJniLib.jar 到 classpath
find src -name "*.java" -exec javac -Xlint:-options -source 1.7 -target 1.7 -d outputs -cp "gson-2.8.5.jar" {} +

# 创建清单文件
echo "Manifest-Version: $version" > MANIFEST.MF
echo "Main-Class: Main" >> MANIFEST.MF
echo "Class-Path: gson-2.8.5.jar" >> MANIFEST.MF
echo "" >> MANIFEST.MF  # 确保文件以空行结束

# 创建 JAR 文件
cd outputs
jar cfm ../consolePokemon_v$version.jar ../MANIFEST.MF .
cd ..

# 将所需 JAR 输入 app 的库目录
cp *.jar ../app/libs
mv ../app/libs/consolePokemon_v$version.jar ../app/libs/consolePokemon_main.jar

# 压缩 JAR 为 ZIP
zip "consolePokemon_v$version.zip" "consolePokemon_v$version.jar" "gson-2.8.5.jar"

echo "JAR 文件创建完成: consolePokemon_v$version.jar"
echo "ZIP 文件创建完成: consolePokemon_v$version.zip"

# java -jar consolePokemon_v$version.jar
