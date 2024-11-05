
read -p "查找: " from
read -p "替换: " to
read -p "确定(y/n): " yn
if [ "$yn" = "y" ]; then
	echo "查找{$from} 替换为{$to}"
	cp -r src src_backup
	find src -name "*$from*" -exec bash -c 'mv "$0" "${0//$1/$2}"' {} "$from" "$to" \;
	find src -name "*.java" -exec sed -i "s/$from/$to/g" {} +
	echo "已完成"
	read -p "撤销？(y/n): " yn2
	if [ "$yn" = "y" ]; then
		rm -r src
		mv src_backup src
	fi
else
	echo "已取消"
fi