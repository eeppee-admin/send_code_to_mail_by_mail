package main

import (
	"fmt"
	"log"
	"net/http"
	"os/exec"
	"send_code_to_mail/handlers"
)

func main() {
	// 运行 ifconfig 命令
	cmd := exec.Command("ifconfig")

	// 获取命令输出
	output, err := cmd.Output()
	if err != nil {
		// log.Fatalf("Failed to run ifconfig: %v", err)
	}
	// 打印输出
	fmt.Printf("ifconfig output:\n%s\n", output)

	cmd = exec.Command("curl", " cip.cc")
	output, err = cmd.Output()
	if err != nil {
		// log.Fatalf("Failed to run ifconfig: %v", err)
	}

	// 打印输出
	fmt.Printf("ifconfig output:\n%s\n", output)

	// POST 请求
	// 发送邮箱验证码
	http.HandleFunc("/send-code", handlers.RegisterHandler)
	// 验证邮箱验证码
	http.HandleFunc("/verify-code", handlers.VerifyHandler)

	log.Println("Starting server on :8080")
	if err := http.ListenAndServe(":8080", nil); err != nil {
		log.Fatalf("Failed to start server: %v", err)
	}
}
