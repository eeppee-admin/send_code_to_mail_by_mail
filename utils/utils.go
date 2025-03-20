package utils

import (
	"crypto/rand"
	"math/big"
)

// GenerateSecureRandomCode 生成指定长度的随机数字验证码（安全版本）
func GenerateSecureRandomCode(length int) string {
	var letters = []rune("0123456789")
	b := make([]rune, length)

	// 生成指定长度的随机数字验证码
	for i := range b {
		n, err := rand.Int(rand.Reader, big.NewInt(int64(len(letters))))
		if err != nil {
			panic(err) // 在实际应用中应处理错误
		}
		b[i] = letters[n.Int64()]
	}

	return string(b)
}
