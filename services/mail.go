package services

import (
	"send_code_to_mail/config"

	"gopkg.in/gomail.v2"
)

// SendVerificationCode 发送验证码
func SendVerificationCode(email, code string) error {
	// 读取配置文件，获取邮箱配置，然后发送邮件
	config, err := config.ReadConfig("config/config.yaml")
	if err != nil {
		return err
	}

	m := gomail.NewMessage()
	m.SetHeader("From", config.Mail.User)
	m.SetHeader("To", email)
	m.SetHeader("Subject", config.WhoAmI.Company+" - 验证邮箱地址")
	m.SetBody("text/html", "你的邮箱验证码是: "+code+"\n\n请在120秒内完成验证")

	d := gomail.NewDialer(config.Mail.SMTP, config.Mail.SMTPPort, config.Mail.User, config.Mail.Password)
	return d.DialAndSend(m)
}
