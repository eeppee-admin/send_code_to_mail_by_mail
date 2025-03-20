package handlers

import (
	"encoding/json"
	"net/http"
	"send_code_to_mail/pkg"
	"send_code_to_mail/services"
	"send_code_to_mail/utils"
	"time"
)

// Response 响应结构体, for web
type Response struct {
	Code    string `json:"code"`
	Message string `json:"message"`
}

type ResponseForApp struct {
	Code    string `json:"code"`
	Message string `json:"message"`
	// Data
}

// RegisterHandler 发送邮箱验证码的函数
func RegisterHandler(w http.ResponseWriter, r *http.Request) {
	email := r.FormValue("email")
	// 根据我具体的业务，我需要生成4位随机数字验证码
	code := utils.GenerateSecureRandomCode(4)

	err := services.SendVerificationCode(email, code)
	if err != nil {
		http.Error(w, "Failed to send verification code", http.StatusInternalServerError)
		return
	}

	redisClient := pkg.NewRedisClient()
	// redis存储120秒
	err = redisClient.SetCode(email, code, 120*time.Second)
	if err != nil {
		http.Error(w, "Failed to store verification code", http.StatusInternalServerError)
		return
	}

	response := Response{
		Code:    "200",
		Message: "邮箱验证码发送成功",
	}

	json.NewEncoder(w).Encode(response)
}

// VerifyHandler 验证邮箱验证码的函数
func VerifyHandler(w http.ResponseWriter, r *http.Request) {
	email := r.FormValue("email")
	code := r.FormValue("code")

	redisClient := pkg.NewRedisClient()
	storedCode, err := redisClient.GetCode(email)
	if err != nil {
		http.Error(w, "Failed to retrieve verification code", http.StatusInternalServerError)
		return
	}

	if storedCode != code {
		http.Error(w, "邮箱验证码错误", http.StatusBadRequest)
		return
	}

	response := Response{
		Code:    "200",
		Message: "邮箱验证码验证成功",
	}
	json.NewEncoder(w).Encode(response)
}
