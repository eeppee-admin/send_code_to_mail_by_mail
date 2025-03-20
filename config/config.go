package config

import (
	"os"

	"gopkg.in/yaml.v2"
)

// Config 配置文件结构体
type Config struct {
	Mail struct {
		SMTP     string `yaml:"smtp"`
		SMTPPort int    `yaml:"smtp-port"`
		User     string `yaml:"user"`
		Password string `yaml:"password"`
	} `yaml:"mail"`

	// 厂商标志，说明是谁发的邮件
	WhoAmI struct {
		Company string `yaml:"company"`
	}
}

// ReadConfig 读取配置文件
func ReadConfig(path string) (*Config, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, err
	}
	var config Config
	err = yaml.Unmarshal(data, &config)
	if err != nil {
		return nil, err
	}
	return &config, nil
}
