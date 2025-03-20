package main

import (
	"log"
	"net/http"
	"send_code_to_mail/handlers"
)

func main() {
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
