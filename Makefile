all:
	go mod tidy
	go run main.go

test:
	curl -X POST "http://localhost:8080/send-code?email=1876056356@qq.com"
	@echo "see file test.http for more infomation"

download-redis-windows:
	wget https://github.com/redis-windows/redis-windows/releases/download/7.4.2/Redis-7.4.2-Windows-x64-msys2.zip
	